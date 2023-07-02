package com.example.sonder_dating_app.presentation.utils.hepler

import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.example.sonder_dating_app.R
import com.example.sonder_dating_app.presentation.utils.extensions.startShakeAnimation

class OTPEditTextHelper {

    private var editTextList = mutableListOf<EditText>()
    private var onOTPInputComplete: ((String) -> Unit)? = null

    fun bind(editTextOTP1: EditText, editTextOTP2: EditText, editTextOTP3: EditText,
             editTextOTP4: EditText, editTextOTP5: EditText, editTextOTP6: EditText
    ){
        editTextList.add(editTextOTP1)
        editTextList.add(editTextOTP2)
        editTextList.add(editTextOTP3)
        editTextList.add(editTextOTP4)
        editTextList.add(editTextOTP5)
        editTextList.add(editTextOTP6)
        setOTPEditTextHandler()
    }

    fun setVerificationCode(code: String){
        val codeCharList = getCharListFromString(code) // разбиваем строку на символы и
        editTextList.forEachIndexed { index, editText -> // устанавливаем в EditText
            editText.setText(codeCharList[index]) }
    }

    private fun getVerificationCode(): String {
        val builder = StringBuilder()
        editTextList.forEach { editText -> builder.append("${editText.text}")  }
        return builder.toString()
    }

    fun setOnInputOTPComplete(onInputOTPComplete: (String) -> Unit) {
        onOTPInputComplete = onInputOTPComplete
    }

    fun showVerificationException(){
        editTextList.forEach { editText ->
            editText.setTextColor(ContextCompat.getColor(editText.context, R.color.red))
            editText.startShakeAnimation()
        }
        Handler(Looper.getMainLooper()).postDelayed({ clearAllEditText() } , 500)
    }

    private fun setOTPEditTextHandler() {
        for (count in 0 until editTextList.count()) {
            editTextList[count].doOnTextChanged { _, _, _, _ ->
                if(count == 5 && editTextList[count].text.toString().isNotEmpty()) {
                    editTextList[count].requestFocus() // Оставляем фокус на последнем EditText
                    setOTPEnabled(false) // Блокируем ввод
                    onOTPInputComplete?.invoke(getVerificationCode()) // Отправляем результат
                }
                else if (editTextList[count].text.toString().isNotEmpty())
                    editTextList[count + 1].requestFocus() // Устанавливаем фокус на следующий EditText
            }

            editTextList[count].setOnKeyListener(object: View.OnKeyListener {
                override fun onKey(view: View?, key: Int, event: KeyEvent?): Boolean {
                    if (event?.action != KeyEvent.ACTION_DOWN) return false

                    // При пустом значении во время удаления устанавлием фокус на предыдущий EditText
                    if (key == KeyEvent.KEYCODE_DEL && count != 0 &&
                        editTextList[count].text.toString().isEmpty()){
                        editTextList[count - 1].text.clear()
                        editTextList[count - 1].requestFocus()
                    }

                    // Реализуем удаление символа
                    if (key == KeyEvent.KEYCODE_DEL &&
                        editTextList[count].text.toString().isNotEmpty())
                        editTextList[count].text.clear()

                    // Замена значение в EditText
                    if(editTextList[count].text.isNotEmpty() && key.isKeyNumber()
                        && key != KeyEvent.KEYCODE_DEL)
                        editTextList[count].setText(fromKeyEventToNumber(key))

                    return false
                }
            })
        }
    }

    private fun setOTPEnabled(enable: Boolean = true) {
        editTextList.forEach { editText -> editText.isEnabled = enable}
    }

    private fun clearAllEditText() {
        setOTPEnabled()
        editTextList.forEach { editText ->
            editText.text.clear()
            editText.setTextColor(ContextCompat.getColor(editText.context, R.color.primary_red))
        }
        editTextList[0].requestFocus()
    }

    private fun Int.isKeyNumber(): Boolean{
        if(fromKeyEventToNumber(key = this) == null) return false
        return true
    }

    private fun fromKeyEventToNumber(key: Int): String?{
        return when(key){
            KeyEvent.KEYCODE_1 -> "1"
            KeyEvent.KEYCODE_2 -> "2"
            KeyEvent.KEYCODE_3 -> "3"
            KeyEvent.KEYCODE_4 -> "4"
            KeyEvent.KEYCODE_5 -> "5"
            KeyEvent.KEYCODE_6 -> "6"
            KeyEvent.KEYCODE_7 -> "7"
            KeyEvent.KEYCODE_8 -> "8"
            KeyEvent.KEYCODE_9 -> "9"
            KeyEvent.KEYCODE_0 -> "0"
            else -> null
        }
    }

    private fun getCharListFromString(str: String): MutableList<String>{
        val charList = str.split("").toMutableList()
        charList.removeAt(0) // Удаление первого и последнего символа из массива
        charList.removeAt(charList.count() - 1) // они будут пустые
        if (charList.count() < 6) throw StringIndexOutOfBoundsException() // вызываем исключение, если код не соответствует размеру
        return charList
    }

}

package com.example.outdoorsport.presentation

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

internal class CustomEditText(context: Context, attrs: AttributeSet?) :
    AppCompatEditText(
        context, attrs
    ) {
    private var onBackPressListener: MyEditTextListener? = null

    fun setOnBackPressListener(onBackPressListener: MyEditTextListener?) {
        this.onBackPressListener = onBackPressListener
    }

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK &&
            event.action == KeyEvent.ACTION_UP) {
            //back button pressed
            if (ViewCompat.getRootWindowInsets(rootView)?.isVisible(WindowInsetsCompat.Type.ime()) == true) {
                //keyboard is open
                onBackPressListener?.callback()
            }
            return false
        }
        return super.dispatchKeyEvent(event)
    }

    interface MyEditTextListener {
        fun callback()
    }
}
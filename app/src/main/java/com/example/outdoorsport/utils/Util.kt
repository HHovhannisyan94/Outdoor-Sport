package com.example.outdoorsport.utils

import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.outdoorsport.R

object Util {
    fun loadFragment( activity: FragmentActivity, fragment: Fragment) {
        activity.supportFragmentManager.beginTransaction()
            .replace(R.id.container_main, fragment)
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }

     fun changeTextStyle(msg: String, color: Int): SpannableString {
        val spannableString = SpannableString(msg)
        val boldSpan = StyleSpan(Typeface.BOLD_ITALIC)
        spannableString.setSpan(
            boldSpan,
            0, msg.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            ForegroundColorSpan(color),
            0, msg.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannableString
    }

}
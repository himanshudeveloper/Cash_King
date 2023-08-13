package com.allrecipes.recipes5.helper

import android.widget.TextView

object Helper {

    const val FRAGMENT_TYPE : String = "fragment_type"
    const val FRAGMENT_SCRATCH : String = "fragment_scratch"
    const val FRAGMENT_SIGNUP : String = "signup"
    const val FRAGMENT_FORGOT_PASSWORD : String = "forgot_password"
    const val FRAGMENT_CHANGE_PASSWORD : String = "change_password"
    const val FRAGMENT_LOAD_WEB_VIEW : String = "load_webview"
    const val APPS_LIST : String = "apps_list"
    const val APP_DATE : String = "apps_date"
    const val TODAY_DATE : String = "today_date"
    const val VISITED_DATE : String = "VISITED_DATE"
    const val WEBSITE_LIST : String = "WEBSITE_LIST"
    const val ONE_THOUSAND_MILISECOND = 1000
    const val VISIT_REQUEST_CODE = 100

    @JvmStatic
    fun isValidName(name: String): String =
        when {
            name.isEmpty() -> "Please enter name"
            name.trim().length < 4 -> "Please enter at least 4 character name"
            else -> "ok"
        }

    @JvmStatic
    fun isValidPassword(password: String): String =
        when {
            password.isEmpty() -> "Please enter password"
            password.trim().length < 6 -> "Please enter at least 6 character password"
            else -> "ok"
        }

    @JvmStatic
    fun isValidEmail(email: String): String =
        when {
            email.isEmpty() -> "Please enter email"
            !email.contains("@") -> "Please enter valid email"
            else -> "ok"
        }

    @JvmStatic
    fun getTextFromTextView(textView: TextView): String =
        textView.text.toString().trim()
}
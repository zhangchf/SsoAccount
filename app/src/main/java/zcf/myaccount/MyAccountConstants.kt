package zcf.myaccount

import android.content.Context

/**
 * Created by zhangchf on 05/12/2017.
 */

val KEY_AUTH_TOKEN_TYPE = "authTokenType"
val KEY_ACCOUNT_TYPE = "accountType"
val KEY_IS_ADDING_NEW_ACCOUNT = "isAddingNewAccount"

val MY_AUTH_TOKEN_TYPE = "full_access"

fun getAccountType(context: Context): String {
    return context.getString(R.string.account_type)
}
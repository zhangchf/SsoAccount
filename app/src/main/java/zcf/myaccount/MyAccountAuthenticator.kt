package zcf.myaccount

import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils


/**
 * Created by zhangchf on 05/12/2017.
 */
class MyAccountAuthenticator(val context: Context) : AbstractAccountAuthenticator(context) {
    override fun getAuthTokenLabel(authTokenType: String?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun confirmCredentials(response: AccountAuthenticatorResponse?, account: Account?, options: Bundle?): Bundle {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateCredentials(response: AccountAuthenticatorResponse?, account: Account?, authTokenType: String?, options: Bundle?): Bundle {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAuthToken(response: AccountAuthenticatorResponse?, account: Account, authTokenType: String?, options: Bundle?): Bundle {
        val am = AccountManager.get(context)

        var authToken = am.peekAuthToken(account, authTokenType)

        if (TextUtils.isEmpty(authToken)) {
            authToken = account.name + ":" + am.getPassword(account)
        }

        if (!TextUtils.isEmpty(authToken)) {
            val result = Bundle()
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name)
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type)
            result.putString(AccountManager.KEY_AUTHTOKEN, authToken)
            return result
        }

        // If you reach here, person needs to login again. or sign up

        // If we get here, then we couldn't access the user's password - so we
        // need to re-prompt them for their credentials. We do that by creating
        // an intent to display our AuthenticatorActivity which is the AccountsActivity in my case.
        val intent = Intent(context, MyAccountAuthActivity::class.java)
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)
        intent.putExtra(KEY_ACCOUNT_TYPE, account.type)
        intent.putExtra(KEY_AUTH_TOKEN_TYPE, authTokenType)

        val retBundle = Bundle()
        retBundle.putParcelable(AccountManager.KEY_INTENT, intent)
        return retBundle
    }

    override fun hasFeatures(response: AccountAuthenticatorResponse?, account: Account?, features: Array<out String>?): Bundle {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun editProperties(response: AccountAuthenticatorResponse?, accountType: String?): Bundle {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addAccount(response: AccountAuthenticatorResponse?, accountType: String, authTokenType: String?, requiredFeatures: Array<out String>?, options: Bundle?): Bundle {
        val intent = Intent(context, MyAccountAuthActivity::class.java)

        // This key can be anything. Try to use your domain/package
        intent.putExtra(KEY_ACCOUNT_TYPE, accountType)

        // This key can be anything too. It's just a way of identifying the token's type (used when there are multiple permissions)
        intent.putExtra(KEY_AUTH_TOKEN_TYPE, authTokenType)

        // This key can be anything too. Used for your reference. Can skip it too.
        intent.putExtra(KEY_IS_ADDING_NEW_ACCOUNT, true)

        // Copy this exactly from the line below.
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)

        val bundle = Bundle()
        bundle.putParcelable(AccountManager.KEY_INTENT, intent)

        return bundle
    }

}
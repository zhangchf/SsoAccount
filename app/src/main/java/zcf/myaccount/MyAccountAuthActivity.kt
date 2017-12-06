package zcf.myaccount

import android.accounts.Account
import android.accounts.AccountManager
import android.accounts.AccountManagerCallback
import android.accounts.AccountManagerFuture
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import kotlinx.android.synthetic.main.activity_account_auth.*


/**
 * Created by zhangchf on 05/12/2017.
 */
class MyAccountAuthActivity : AppCompatActivity() {
    var accountType: String? = null
    var authTokenType: String? = null
    var isAddingNewAccount: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_auth)

        accountType = intent.getStringExtra(KEY_ACCOUNT_TYPE)
        authTokenType = intent.getStringExtra(KEY_AUTH_TOKEN_TYPE)
        isAddingNewAccount = intent.getBooleanExtra(KEY_IS_ADDING_NEW_ACCOUNT, true)

        if (accountType == null) {
            accountType = getAccountType(this)
        }
        if (authTokenType == null) {
            authTokenType = MY_AUTH_TOKEN_TYPE
        }

        btnLogin.setOnClickListener {
            createAccount()
        }
    }

    fun createAccount() {
        val am = AccountManager.get(this)
        val account = Account(edtUsername.text.toString(), accountType)
        am.getAuthToken(account, authTokenType, null, this, object: AccountManagerCallback<Bundle> {
            override fun run(future: AccountManagerFuture<Bundle>) {
                // Get the result of the operation from the AccountManagerFuture.
                val bundle = future.getResult()

                // The token is a named value in the bundle. The name of the value
                // is stored in the constant AccountManager.KEY_AUTHTOKEN.
                val token = bundle.getString(AccountManager.KEY_AUTHTOKEN)

                if (!TextUtils.isEmpty(token)) {
                    am.setAuthToken(account, KEY_AUTH_TOKEN_TYPE, token)
                    am.addAccountExplicitly(account, edtPassword.text.toString(), null)
                }
                finish()
            }
        }, null)


    }
}



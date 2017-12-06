package zcf.myaccount

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.accounts.Account
import android.accounts.AccountManager
import android.accounts.AccountManagerCallback
import android.accounts.AccountManagerFuture
import android.content.Intent
import android.text.TextUtils
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    var accounts: Array<Account>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnGetAuthToken.setOnClickListener {
            if (accounts == null || accounts?.size == 0) {
                AddNewAccount()
                return@setOnClickListener
            }

            val am = AccountManager.get(this)
            am.getAuthToken(accounts!![0], MY_AUTH_TOKEN_TYPE, null, this@MainActivity, object: AccountManagerCallback<Bundle> {
                override fun run(future: AccountManagerFuture<Bundle>) {
                    // Get the result of the operation from the AccountManagerFuture.
                    val bundle = future.getResult()

                    // The token is a named value in the bundle. The name of the value
                    // is stored in the constant AccountManager.KEY_AUTHTOKEN.
                    val token = bundle.getString(AccountManager.KEY_AUTHTOKEN)

                    if (!TextUtils.isEmpty(token)) {
                        val msg = accounts!![0].name + ":" + accounts!![0].type + "@" + token
                        Toast.makeText(this@MainActivity, msg, Toast.LENGTH_LONG).show()
                    }
                }
            }, null)
        }


        btnAddAccount.setOnClickListener {
            AddNewAccount()
        }


        btnLogin.setOnClickListener {
            startActivity(Intent(this, MyAccountAuthActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        fetchMyAccounts()
    }

    fun fetchMyAccounts() {
        val accountManager = AccountManager.get(this)
        accounts = accountManager.getAccountsByType(getString(R.string.account_type))

        var accountInfo = "accounts:\n"
        for (account in accounts!!) {
            accountInfo += account.name + ":" + account.type + "\n"
        }

        txtMyAccounts.setText(accountInfo)
    }

    fun AddNewAccount() {
        val am = AccountManager.get(this)
        am.addAccount(getAccountType(this), MY_AUTH_TOKEN_TYPE, null, null, this, object: AccountManagerCallback<Bundle> {
            override fun run(future: AccountManagerFuture<Bundle>?) {
                val bundle = future?.result
                if (bundle == null) {
                    return
                }
                val accountName = bundle.getString(AccountManager.KEY_ACCOUNT_NAME)
                val accountType = bundle.getString(AccountManager.KEY_ACCOUNT_TYPE)
                if (accountName == null || accountType == null) {
                    return
                }
                val account = Account(accountName, accountType)
                am.getAuthToken(account, KEY_AUTH_TOKEN_TYPE, null, this@MainActivity, object: AccountManagerCallback<Bundle> {
                    override fun run(future: AccountManagerFuture<Bundle>) {
                        // Get the result of the operation from the AccountManagerFuture.
                        val bundle = future.getResult()

                        // The token is a named value in the bundle. The name of the value
                        // is stored in the constant AccountManager.KEY_AUTHTOKEN.
                        val token = bundle.getString(AccountManager.KEY_AUTHTOKEN)

                        if (!TextUtils.isEmpty(token)) {
                            val msg = accountName + ":" + accountType + "@" + token
                            Toast.makeText(this@MainActivity, msg, Toast.LENGTH_LONG).show()
                        }
                    }
                }, null)
            }

        }, null)
    }
}

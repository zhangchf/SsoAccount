package zcf.myaccount

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * Created by zhangchf on 05/12/2017.
 */

class MyAuthenticatorService : Service() {
    // Instance field that stores the authenticator object
    // Notice, this is the same Authenticator class we defined earlier
    private var mAuthenticator: MyAccountAuthenticator? = null

    override fun onCreate() {
        // Create a new authenticator object
        mAuthenticator = MyAccountAuthenticator(this)
    }

    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    override fun onBind(intent: Intent): IBinder? {
        return mAuthenticator!!.getIBinder()
    }

}
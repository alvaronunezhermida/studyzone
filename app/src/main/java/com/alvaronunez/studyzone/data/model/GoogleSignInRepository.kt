package com.alvaronunez.studyzone.data.model
import android.app.Activity
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class GoogleSignInRepository {

    companion object{
        private const val LOG_TAG = "GOOGLE_SIGN_REPO::"
    }

    fun getGoogleSignInIntent(activity: Activity, requestToken: String): Intent {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(requestToken)
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(activity, gso)
        return googleSignInClient.signInIntent
    }

    fun getSignedAccountTokenFromIntent(data: Intent?): String? {
        return try {
            GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException::class.java)?.idToken
        } catch (e: ApiException) {
            Log.e(LOG_TAG, e.message?: "Exception lost")
            null
        }
    }


}
package com.example.techsample

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar


class LoginActivity : AppCompatActivity() {

    lateinit var loginButton: LoginButton
    lateinit var callbackManager: CallbackManager
    lateinit var loginManager: LoginManager
    private val EMAIL = "email"

    lateinit var sharedPreferences: SharedPreferences

    lateinit var signInButton: SignInButton

    var RC_SIGN_IN = 123

    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var constraintLayout: ConstraintLayout



    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginButton = findViewById<LoginButton>(R.id.login_button)

        signInButton = findViewById(R.id.sign_in_button)




        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        signInButton.setOnClickListener {
            val intent = mGoogleSignInClient.signInIntent
            startActivityForResult(intent, RC_SIGN_IN)
        }

        loginButton.setOnClickListener {

            callbackManager = CallbackManager.Factory.create()

            sharedPreferences = getApplicationContext().getSharedPreferences("SHARED_PREF", MODE_PRIVATE)
            LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {

                    // App code
                    val request = GraphRequest.newMeRequest(
                            loginResult.accessToken) { me, response ->
                        if (response.error != null) {
                            // handle error
                            Snackbar.make(constraintLayout, "Something Went Wrong", Snackbar.LENGTH_LONG).show()
                        } else {
                            // get email and id of the user
                            val email = me.optString("email")
                            val id = me.optString("id")
                            val firstName = me.optString("first_name")
                            val lastName = me.optString("last_name")
                            val ss = "https://graph.facebook.com/$id/picture?width=512&height=512"
                            sharedPreferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE)
                            val editor: SharedPreferences.Editor = sharedPreferences.edit()
                            editor.putString("login", "yes")
                            editor.putString("email",email)
                            editor.putString("first_name",firstName)
                            editor.putString("last_name",lastName)
                            editor.putString("id",id)
                            editor.putString("https://graph.facebook.com/$id/picture?width=512&height=512",ss)
                            editor.apply()
                            editor.commit()
                            Log.e("Profile","$firstName $email $lastName $ss")

                        }
                    }
                    val parameters = Bundle()
                    parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location,picture")
                    request.parameters = parameters
                    request.executeAsync()
                    goIndexScreen()


                }
                override fun onCancel() {
                }

                override fun onError(exception: FacebookException) {
                    Log.e("err", "" + exception.localizedMessage)
                }
            })

        }
    }

    fun goIndexScreen() {

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }else{
            callbackManager.onActivityResult(requestCode, resultCode, data)

        }

    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try
        {
            if (completedTask.isSuccessful) {

                val account = completedTask.getResult(ApiException::class.java)
                sharedPreferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE)
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString("login", "yes")
                editor.putString("name", account?.displayName.toString())
                editor.putString("email", account?.email.toString())
                editor.putString("url", account?.photoUrl.toString())
                editor.apply()
                editor.commit()
                if (account != null) {
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                }
            }

        }
        catch (e: ApiException) {
            Log.w("error", "signInResult:failed code=" + e.getStatusCode())

        }
    }





}



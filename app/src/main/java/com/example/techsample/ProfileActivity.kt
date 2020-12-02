package com.example.techsample

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class ProfileActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    lateinit var mGoogleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        val acct = GoogleSignIn.getLastSignedInAccount(this)

        val person_name = findViewById<TextView>(R.id.person_name)
        val person_email = findViewById<TextView>(R.id.person_email)
        val person_Photo= findViewById<ImageView>(R.id.profile_image)
        val person_last= findViewById<TextView>(R.id.person_last)

        sharedPreferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE)
        sharedPreferences.getString("login", "yes")

        var firstName = sharedPreferences.getString("first_name","")
        var lastName = sharedPreferences.getString("last_name","")
        var emailf = sharedPreferences.getString("email","")
        var id = sharedPreferences.getString("id","")
        var ss = sharedPreferences.getString("https://graph.facebook.com/$id/picture?width=512&height=512","")
        try {
            person_name.setText("Name : $firstName")
            person_email.setText("Email : $emailf")
            person_last.setText("last_name : $lastName")

            if (!ss.equals("null : $ss")) {
                Glide .with(this).load(ss).into(person_Photo)
            } else {
                person_Photo.visibility = View.GONE
            }
        }
        catch (e: Exception) {

        }


    /*    var name = sharedPreferences.getString("name", "")
        var email =   sharedPreferences.getString("email", "")
        var imageURL =   sharedPreferences.getString("url","")
        Log.e("error","$name $email $imageURL" )



        try {
            person_name.setText("Name : $name")
            person_email.setText("Email : $email")

            if (!imageURL.equals("null : $imageURL")) {
                Glide.with(this).load(imageURL).into(person_Photo)
            } else {
                person_Photo.visibility = View.GONE
            }
        }
        catch (e: Exception) {

        } */

    }
}
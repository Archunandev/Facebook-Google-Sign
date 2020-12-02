package com.example.techsample

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import androidx.annotation.NonNull
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import android.net.Uri
import android.view.MenuItem
import android.widget.*
import com.bumptech.glide.Glide
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.navigation.NavigationView
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    lateinit var drawerLayout:DrawerLayout
    lateinit var actionBarDrawerToggle:ActionBarDrawerToggle
    private lateinit var navigationView:NavigationView

    companion object{
        lateinit var dbHelper: MyDbHelper
        lateinit var adapter: WorkersAdapter
    }
    lateinit var nodata : TextView
    lateinit var btndelete : ImageView
    lateinit var rv : RecyclerView

    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    lateinit var mGoogleSignInClient: GoogleSignInClient




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        dbHelper = MyDbHelper(this,null,null,this,1)

        viewWorkers()


        floatingActionButton.setOnClickListener {
            val i = Intent (this,AddWorkers::class.java)
            startActivity(i)
        }

        val rv : RecyclerView = findViewById(R.id.recyclerView)


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        val acct = GoogleSignIn.getLastSignedInAccount(this)

        sharedPreferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE)
        sharedPreferences.getString("login", "yes")

        setUpToolbar()
        navigationView = findViewById(R.id.navigation_menu) as NavigationView
        navigationView.setNavigationItemSelectedListener(object: NavigationView.OnNavigationItemSelectedListener {

            override fun onNavigationItemSelected(@NonNull menuItem:MenuItem):Boolean {
                when (menuItem.getItemId()) {
                    R.id.nav_home -> {
                        val intent = Intent(this@MainActivity, MainActivity::class.java)
                        startActivity(intent)
                    }
                    //Paste your privacy policy link
                    // case R.id.nav_Policy:{
                    //
                    // Intent browserIntent = new Intent(Intent.ACTION_VIEW , Uri.parse(""));
                    // startActivity(browserIntent);
                    //
                    // }
                    // break;
                    R.id.nav_share -> {
                        val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
                        sharingIntent.setType("text/plain")
                        val shareBody = "http://play.google.com/store/apps/detail?id=" + getPackageName()
                        val shareSub = "Try now"
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub)
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody)
                        startActivity(Intent.createChooser(sharingIntent, "Share using"))
                    }

                    R.id.log_out -> {
                        signOut()
                    }

                    R.id.profile -> {
                        val intent = Intent(this@MainActivity, ProfileActivity::class.java)
                        startActivity(intent)
                    }

                }
                return false
            }
        })

    }

    fun signOut(){

        mGoogleSignInClient.signOut().addOnCompleteListener(
            this,
            object : OnCompleteListener<Void> {
                override fun onComplete(p0: Task<Void>) {
                    Toast.makeText(this@MainActivity, "Signed Out", Toast.LENGTH_LONG).show()
                }
            })

        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }




    fun setUpToolbar() {
        drawerLayout = findViewById(R.id.drawerLayout)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
    }


    private fun viewWorkers(){
        val workersList = dbHelper.getWorkers(this)
        val adapter = WorkersAdapter(this,workersList,this)
        rv = findViewById<RecyclerView>(R.id.recyclerView)
        nodata = findViewById<TextView>(R.id.empty_view)
        rv.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager
        rv.adapter = adapter


         if (workersList.isEmpty()){

           nodata.visibility= View.VISIBLE
           rv.visibility= View.GONE
        }
       else
        {
            nodata.visibility= View.GONE
            rv.visibility= View.VISIBLE
         }

    }

    override fun onResume() {

        viewWorkers()
        super.onResume()
    }
    override fun onBackPressed() {
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
        builder.setIcon(R.drawable.ic_baseline_loupe_24).setTitle("Exit")
        builder.setMessage("Are you sure you want to Exit?")
        builder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
            finishAffinity()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        })
        builder.setNegativeButton("No", null)
        //builder.show();
        val dialog: android.app.AlertDialog? = builder.create()
        dialog?.show() //Only after .show() was called

    }
}
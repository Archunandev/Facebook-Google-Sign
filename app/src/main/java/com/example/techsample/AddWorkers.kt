package com.example.techsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_workers.*

class AddWorkers : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_workers)

        btnSave.setOnClickListener {

            if (editWorkername.text.isEmpty()){
                Toast.makeText(this,"Enter Name",Toast.LENGTH_SHORT).show()
                editWorkername.requestFocus()
            }else{
                val workers = Workers()
                workers.workersName = editWorkername.text.toString()
                if (editMaxcredit.text.isEmpty())
                    workers.maxCredit = 0.0 else
                    workers.maxCredit = editMaxcredit.text.toString().toDouble()
                MainActivity.dbHelper.addWorkers(this,workers)
                clearEdit()
                finish()
               // editWorkername.requestFocus()

            }
        }

    }

    private fun clearEdit(){
        editWorkername.text.clear(
            
        )
        editMaxcredit.text.clear()

    }


}
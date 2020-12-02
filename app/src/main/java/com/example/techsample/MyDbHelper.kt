package com.example.techsample

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast

class MyDbHelper(
    context: Context, name: String?, factory:SQLiteDatabase.CursorFactory?,var activity: MainActivity, version: Int ) :

    SQLiteOpenHelper(context, DATABASE_NAME,factory, DATABSE_VERSION){



    companion object{
        private val DATABASE_NAME = "Workers.db"
        private val DATABSE_VERSION = 1

        val WORKERSTABLE_NAME = "WorkersList"
        val COLUMN_WORKERID = "Workersid"
        val COLUMN_WORKERNAME = "Workersname"
        val COLUMN_WORKERAGE = "Workersage"


    }

    override fun onCreate(db: SQLiteDatabase?) {

        val CREATE_WORKER_TABLE = ("CREATE TABLE $WORKERSTABLE_NAME ("+
                "$COLUMN_WORKERID INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "$COLUMN_WORKERNAME TEXT,"+
                "$COLUMN_WORKERAGE DOUBLE DEFAULT 0)")
        db?.execSQL(CREATE_WORKER_TABLE)


    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {



    }

    fun getWorkers(mCtx : Context ) : ArrayList<Workers> {


        val qry = "Select * From $WORKERSTABLE_NAME"
        val db = this.readableDatabase
        val cursor = db.rawQuery(qry, null)
        val workerss = ArrayList<Workers>()

        if (cursor.count == 0)

            Toast.makeText(mCtx, "No records", Toast.LENGTH_SHORT).show()


        else {

            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val workers = Workers()
                workers.workersID = cursor.getInt(cursor.getColumnIndex(COLUMN_WORKERID))
                workers.workersName = cursor.getString(cursor.getColumnIndex(COLUMN_WORKERNAME))
                workers.maxCredit = cursor.getDouble(cursor.getColumnIndex(COLUMN_WORKERAGE))
                workerss.add(workers)
                cursor.moveToNext()

            }

        }
        cursor.close()
        db.close()
        return workerss
    }

    fun addWorkers(mCtx: Context, workers: Workers){
        val values = ContentValues()
        values.put(COLUMN_WORKERNAME,workers.workersName)
        values.put(COLUMN_WORKERAGE,workers.maxCredit)
        val db = this.writableDatabase
        try {
            db.insert(WORKERSTABLE_NAME ,null,values)
           // db.rawQuery("Insert Into $WORKERSTABLE_NAME ($COLUMN_WORKERNAME, $COLUMN_WORKERAGE) Values(?,?)")
            Toast.makeText(mCtx,"Workers Added",Toast.LENGTH_SHORT).show()

        }catch (e: Exception){
            Toast.makeText(mCtx,e.message,Toast.LENGTH_SHORT).show()
        }
        db.close()
    }
    fun deleteWorker(workersID:Int) : Boolean{
        val qry = "Delete From $WORKERSTABLE_NAME where $COLUMN_WORKERID = $workersID"
        val db = this.writableDatabase
        var result : Boolean = false
        try {
           // val cursor = db.delete(WORKERSTABLE_NAME, "$COLUMN_WORKERID = ?", arrayOf(workersID.toString()))
            val cursor = db.execSQL(qry)
            result = true

        }catch (e : Exception){
            Log.e(ContentValues.TAG, "Error Deleting")
        }
        db.close()
        return result
    }

    fun updateWorker(id: String, workerName: String, maxCredit: String) : Boolean{

        val db = this.writableDatabase
        val contentValues = ContentValues()
        var result : Boolean = false
        contentValues.put(COLUMN_WORKERNAME,workerName)
        contentValues.put(COLUMN_WORKERAGE,maxCredit.toDouble())
        try {
            db.update(WORKERSTABLE_NAME,contentValues,"$COLUMN_WORKERID = ?", arrayOf(id))
            result = true
        }catch (e : Exception){
            Log.e(ContentValues.TAG, "Error Updating")
            result = false

        }

        return result


    }



}
package com.example.techsample

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.io_workers_update.view.*
import kotlinx.android.synthetic.main.lo_workers.view.*
import java.security.AccessController.getContext


class WorkersAdapter(mCtx : Context,  val workers : ArrayList<Workers>,var activity: MainActivity) : RecyclerView.Adapter<WorkersAdapter.ViewHolder>(){



    val mCtx = mCtx




    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val txtworkersname = itemView.workersname
        val txtworkersage = itemView.maxage
        val btnupdate = itemView.update
        val btndelete = itemView.delete
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkersAdapter.ViewHolder {
       val v = LayoutInflater.from(parent.context).inflate(R.layout.lo_workers,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return workers.size

    }



    override fun onBindViewHolder(holder: WorkersAdapter.ViewHolder, position: Int) {
        val worker : Workers = workers[position]

        holder.txtworkersname.text = worker.workersName
        holder.txtworkersage.text = worker.maxCredit.toString()




        holder.btndelete.setOnClickListener {

            val workerName = worker.workersName



            var alertDialoge = AlertDialog.Builder(mCtx)
                .setTitle("Warning")
                .setMessage("Are you sure to delete :$workerName ?")
                .setPositiveButton("yes", DialogInterface.OnClickListener { dialog, which ->
            if(MainActivity.dbHelper.deleteWorker(worker.workersID)){
                workers.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position,workers.size)
                Toast.makeText(mCtx,"List $workerName Deleted",Toast.LENGTH_SHORT).show()
                if (workers.isEmpty()){

                    activity.nodata.visibility= View.VISIBLE
                    activity.rv.visibility = View.GONE

                } else {

                    activity.nodata.visibility= View.GONE
                    activity.rv.visibility= View.VISIBLE

                }
            }else

                Toast.makeText(mCtx,"Error Deleting",Toast.LENGTH_SHORT).show()
                })
                .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->  })
                .setIcon(R.drawable.ic_baseline_warning_24)
                .show()




        }
        holder.btnupdate.setOnClickListener {
            val inflater = LayoutInflater.from(mCtx)
            val view = inflater.inflate(R.layout.io_workers_update,null)
            val txtworName : TextView = view.findViewById(R.id.editUpName)
            val txtmaxcredit : TextView = view.findViewById(R.id.editUpmaxCredit)

            txtworName.text = worker.workersName
            txtmaxcredit.text = worker.maxCredit.toString()

            val builder = AlertDialog.Builder(mCtx)
                .setTitle("Update Workers Info")
                .setView(view)
                .setPositiveButton("Update", DialogInterface.OnClickListener { dialog, which ->

                    val isUpdate = MainActivity.dbHelper.updateWorker(
                        worker.workersID.toString(),
                        view.editUpName.text.toString(),
                        view.editUpmaxCredit.text.toString())
                    if (isUpdate==true){
                        workers[position].workersName = view.editUpName.text.toString()
                        workers[position].maxCredit = view.editUpmaxCredit.text.toString().toDouble()
                        notifyDataSetChanged()
                        Toast.makeText(mCtx,"Updated Successfully",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(mCtx,"Error Updating",Toast.LENGTH_SHORT).show()

                    }
                }).setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->


                })
            val alert = builder.create()
            alert.show()
        }


    }

}


package com.example.roomdb

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdb.databinding.ActivityMainBinding
import com.example.roomdb.databinding.UpdateDialogBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(),View.OnClickListener,
    MyAdapter.OnItmClick{

    private lateinit var binding: ActivityMainBinding
    private lateinit var dao: UserDAO
    private var userList= ArrayList<User>()
    private lateinit var adapter: MyAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dao=UserDB.getInstance(this).myDao
        binding.insertBtn.setOnClickListener(this)
        binding.recycleId.layoutManager=LinearLayoutManager(this)
        binding.recycleId.setHasFixedSize(true)

        loadData()

        binding.swipeLayout.setOnRefreshListener {
            userList.clear()
            userList = dao.getUser() as ArrayList<User>
            adapter= MyAdapter(userList,this)
            binding.recycleId.adapter=adapter
            binding.swipeLayout.isRefreshing=false
        }
    }

    private fun loadData() {
        userList = dao.getUser() as ArrayList<User>
        adapter= MyAdapter(userList,this)
        binding.recycleId.adapter=adapter
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
           R.id.insertBtn ->{
               startActivity(Intent(this,InsertActivity::class.java))
           }
       }
    }

    override fun onItemClick(position: Int) {
        Snackbar.make(binding.mainLayout,"Name is "+userList[position].name + "\nEmail is "+userList[position].email,
            Snackbar.LENGTH_SHORT).show()

    }

    override fun onLongItemClick(position: Int) {

        val dialog = AlertDialog.Builder(this)
        val option = arrayOf("Update", "Delete")
        dialog.setTitle("Choose an Option")

        dialog.setItems(option){dialogInterface, i: Int ->
            val select = option[i]

            if (select=="Update"){
                updateData(position)
            }
            else{
                val id= userList[position].id
                val value= dao.delete(id)
                if(value>0){
                    userList.removeAt(position)
                    adapter.notifyItemRemoved(position)
                    Snackbar.make(binding.mainLayout,"Delete Successfully",Snackbar.LENGTH_SHORT).show()

                }
                else{
                    Snackbar.make(binding.mainLayout,"Delete Failed",Snackbar.LENGTH_SHORT).show()

                }
            }

        }
        dialog.create().show()
    }

    private fun updateData(position: Int) {

        val dialog= AlertDialog.Builder(this)
        val view= UpdateDialogBinding.inflate(LayoutInflater.from(this))
        dialog.setView(view.root).setTitle("Update").setCancelable(true)
            .setPositiveButton("Update"){ dialogInterface: DialogInterface, i: Int ->

                val id= userList[position].id
                val name= view.updateName.text.toString()
                val email= view.updateEmail.text.toString()
                val value= dao.update(id, name, email)
                if(value>0){
                    Snackbar.make(binding.mainLayout,"Update Successfully",Snackbar.LENGTH_SHORT).show()

                }
                else{
                    Snackbar.make(binding.mainLayout,"Update Failed",Snackbar.LENGTH_SHORT).show()

                }

            }.setNegativeButton("Close"){ dialogInterface: DialogInterface, i: Int ->

            }

        view.updateId.text= "Updating index no #${userList[position].id}"
        view.updateName.setText(userList[position].name)
        view.updateEmail.setText(userList[position].email)
        dialog.create().show()
    }
}
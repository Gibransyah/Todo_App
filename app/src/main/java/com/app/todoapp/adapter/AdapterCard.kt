package com.app.todoapp.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.app.todoapp.R
import com.app.todoapp.Ui.MainActivity
import com.app.todoapp.model.Data
import com.app.todoapp.model.Item
import com.app.todoapp.model.Todo
import com.app.todoapp.network.ApiConfig
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdapterCard(val activity: Activity, var data: List<Data>) :
    RecyclerView.Adapter<AdapterCard.Holder>() {
    class Holder(view: View) : RecyclerView.ViewHolder(view) {

        val tvTittleTodo = view.findViewById<TextView>(R.id.tv_tittle_todo)
        val btnAddItemCard = view.findViewById<MaterialButton>(R.id.btn_add_itemtodo)
        val btnEdit = view.findViewById<ImageView>(R.id.btn_edit_todo)
        val rvItemTodo = view.findViewById<RecyclerView>(R.id.rv_todo)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(
                R.layout.cardtodo_item,
                parent, false
            )
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.tvTittleTodo.text = data[position].name

        val adapterItemCard = data[position].items.let { AdapterItemCard(activity, it) }
        holder.rvItemTodo.adapter = adapterItemCard

        val name = data[position].name
        val id = data[position].id

        holder.btnAddItemCard.setOnClickListener {
            val mDialog =
                LayoutInflater.from(activity).inflate(R.layout.dialog_newitemtodo, null)

            val edtDialog = mDialog.findViewById<EditText>(R.id.edt_dialog_newitem_todo)
            val btnSimpan = mDialog.findViewById<Button>(R.id.btn_simpan_newitem_todo)
            val btnBatal = mDialog.findViewById<Button>(R.id.btn_batal_dialog_edit_todos)

            val mBuild = AlertDialog.Builder(activity)
                .setView(mDialog)
                .setTitle("Create new todo")

            val mAlertDialog = mBuild.show()


            btnBatal.setOnClickListener {
                mAlertDialog.dismiss()
            }

            btnSimpan.setOnClickListener {
                val namenew = edtDialog.text.toString()
                ApiConfig.instanceRetrofit.postTodoItem(namenew,id).enqueue(object : Callback<Item>{
                    override fun onResponse(call: Call<Item>, response: Response<Item>) {
                        if (response.isSuccessful){
                            Toast.makeText(activity, "Item Data Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
                            (activity as MainActivity).getAllTodos()
                        } else {
                            Toast.makeText(activity, "Item Data Gagal Ditambahkan", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Item>, t: Throwable) {
                        (activity as MainActivity).getAllTodos()
                    }

                })
                mAlertDialog.dismiss()
            }

        }

        holder.btnEdit.setOnClickListener {
            val mDialog =
                LayoutInflater.from(activity).inflate(R.layout.dialog_edit, null)

            val edtDialog = mDialog.findViewById<EditText>(R.id.edt_dialog_edit_todo)
            val btnUpate = mDialog.findViewById<Button>(R.id.btn_update_dialog_item_todos)
            val btnBatal = mDialog.findViewById<Button>(R.id.btn_batal_dialog_edit_todos)
            val btnDelet = mDialog.findViewById<Button>(R.id.btn_hapus_dialog_edit_todos)

            val mBuild = AlertDialog.Builder(activity)
                .setView(mDialog)
                .setTitle("Create new todo")

            val mAlertDialog = mBuild.show()

            edtDialog.setText(name)

            btnBatal.setOnClickListener {
                mAlertDialog.dismiss()
            }

            btnUpate.setOnClickListener {
                val edtname = edtDialog.text.toString()
                ApiConfig.instanceRetrofit.updateTodo(id,edtname).enqueue(object : Callback<Todo>{
                    override fun onResponse(call: Call<Todo>, response: Response<Todo>) {
                        if (response.isSuccessful){
                            Toast.makeText(activity, "Todo Data Berhasil Diupdate", Toast.LENGTH_SHORT).show()
                            (activity as MainActivity).getAllTodos()
                        } else{
                            Toast.makeText(activity, "Todo Data Gagal Diupdate", Toast.LENGTH_SHORT).show()

                        }
                    }

                    override fun onFailure(call: Call<Todo>, t: Throwable) {
                        (activity as MainActivity).getAllTodos()
                    }

                })
                mAlertDialog.dismiss()
            }

            btnDelet.setOnClickListener {
                ApiConfig.instanceRetrofit.deleteTodo(id).enqueue(object : Callback<Todo>{
                    override fun onResponse(call: Call<Todo>, response: Response<Todo>) {
                        if (response.isSuccessful){
                            Toast.makeText(activity, "Todo Data Berhasil DiHapus", Toast.LENGTH_SHORT).show()
                            (activity as MainActivity).getAllTodos()
                        } else{
                            Toast.makeText(activity, "Todo Data Gagal DiHapus", Toast.LENGTH_SHORT).show()

                        }
                    }

                    override fun onFailure(call: Call<Todo>, t: Throwable) {
                        (activity as MainActivity).getAllTodos()
                    }

                })
                mAlertDialog.dismiss()
            }
        }
    }

    override fun getItemCount(): Int = data.size

    }



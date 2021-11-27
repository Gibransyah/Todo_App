package com.app.todoapp.Ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.app.todoapp.R
import com.app.todoapp.adapter.AdapterCard
import com.app.todoapp.adapter.AdapterItemCard
import com.app.todoapp.model.Todo
import com.app.todoapp.network.ApiConfig
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private var adapter: AdapterCard? = null
    private lateinit var rvParent: RecyclerView
    private lateinit var swPlayout: SwipeRefreshLayout
    private lateinit var btnAdd: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        swPlayout = findViewById(R.id.swpref)
        swPlayout.setOnRefreshListener(this)

        getAllTodos()

    }

    fun display(){
        rvParent = findViewById(R.id.rv_parent)
        btnAdd = findViewById(R.id.btn_fload_add)

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL

        rvParent.adapter = adapter
        rvParent.layoutManager = layoutManager
    }

    fun getAllTodos() {
        ApiConfig.instanceRetrofit.getAllTodo().enqueue(object : retrofit2.Callback<Todo> {
            override fun onResponse(call: Call<Todo>, response: Response<Todo>) {
                if (response.isSuccessful) {
                    adapter = AdapterCard(this@MainActivity, response.body()?.data.orEmpty())

                    display()
                    postTodo()

                    swPlayout.isRefreshing = false
                }
            }

            override fun onFailure(call: Call<Todo>, t: Throwable) {
                Log.d("TAG","Gagal get data Todo")
            }

        })

        }

    fun postTodo(){
        btnAdd.setOnClickListener {
            val mDialog =
                LayoutInflater.from(this).inflate(R.layout.dialog_newtodo, null)

            val edtDialog = mDialog.findViewById<EditText>(R.id.edt_simpan_todo)
            val btnSimpan = mDialog.findViewById<Button>(R.id.btn_simpantodo)

            val mBuild = AlertDialog.Builder(this)
                .setView(mDialog)
                .setTitle("Create new todo")

            val mAlertDialog = mBuild.show()

            btnSimpan.setOnClickListener {
                val name = edtDialog.text.toString()

                ApiConfig.instanceRetrofit.postTodo(name).enqueue(object : retrofit2.Callback<Todo>{
                    override fun onResponse(call: Call<Todo>, response: Response<Todo>) {
                        if (response.isSuccessful){
                            Toast.makeText(this@MainActivity, "Data Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
                            getAllTodos()
                        } else {
                            Toast.makeText(this@MainActivity, "Data Gagal Ditambahkan", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Todo>, t: Throwable) {
                        getAllTodos()
                    }

                })
                mAlertDialog.dismiss()

            }

        }
    }

    override fun onRefresh() {
        getAllTodos()
    }
    }

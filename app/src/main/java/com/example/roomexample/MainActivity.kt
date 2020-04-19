package com.example.roomexample

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roomexample.adapters.UsersListAdapter
import com.example.roomexample.data.User
import com.example.roomexample.databinding.ActivityMainBinding
import com.example.roomexample.viewmodels.UserViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel
    private val newUserActivityRequestCode = 1
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val adapter = UsersListAdapter(this)

        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.allUsers.observe(this, Observer { users ->
            users?.let { adapter.setWords(it) }
        })

        binding.fab.setOnClickListener {
            val intent = Intent(this@MainActivity,NewUserActivity::class.java)
            startActivityForResult(intent,newUserActivityRequestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == newUserActivityRequestCode && resultCode == Activity.RESULT_OK) {
            data?.getStringExtra(NewUserActivity.EXTRA_REPLY)?.let {
                val user = User(it, it)
                userViewModel.insert(user)
            }
        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG).show()
        }
    }
}

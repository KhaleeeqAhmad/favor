package com.fyp.favorproject.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fyp.favorproject.databinding.ActivityUserAthenticationBinding

class UserAuthenticationActivity : AppCompatActivity() {

    private lateinit var view: ActivityUserAthenticationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = ActivityUserAthenticationBinding.inflate(layoutInflater)
        setContentView(view.root)
    }
}
package com.fyp.favorproject.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.fyp.favorproject.R
import com.fyp.favorproject.databinding.ActivityMainBinding
import com.fyp.favorproject.mainFragment.ChatFragment
import com.fyp.favorproject.mainFragment.HomeFragment
import com.fyp.favorproject.mainFragment.NotificationFragment
import com.fyp.favorproject.mainFragment.StoreFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        /** Fragments */
        val homeFragment = HomeFragment()
        val storeFragment = StoreFragment()
        val notificationFragment = NotificationFragment()
        val chatFragment = ChatFragment()

        setCurrentFragment(homeFragment)
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    setCurrentFragment(homeFragment)
                    binding.fragmentTitle.visibility = View.GONE
                    binding.searchView.visibility = View.VISIBLE
                }
                R.id.storeFragment -> {
                    setCurrentFragment(storeFragment)
                    binding.searchView.visibility = View.GONE
                    binding.fragmentTitle.visibility = View.VISIBLE
                    binding.fragmentTitle.setText(R.string.store)
                }
                R.id.askFavorFragment -> {
                    binding.bottomNavigation.visibility = View.GONE
                    binding.tbMain.visibility = View.GONE
                }
                R.id.notificationFragment -> {
                    setCurrentFragment(notificationFragment)
                    binding.searchView.visibility = View.GONE
                    binding.fragmentTitle.visibility = View.VISIBLE
                    binding.fragmentTitle.setText(R.string.notifications)
                }
                R.id.chatFragment -> {
                    setCurrentFragment(chatFragment)
                    binding.fragmentTitle.visibility = View.GONE
                    binding.searchView.visibility = View.VISIBLE
                }
            }
            true
        }

    }
    //test



    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentHolder, fragment)
            commit()
        }

}


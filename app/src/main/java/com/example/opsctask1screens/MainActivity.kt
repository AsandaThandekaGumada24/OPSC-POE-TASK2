package com.example.opsctask1screens
import FirstPage
import SecondPage
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity :AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.NavBar)

        bottomNavigation.setOnNavigationItemReselectedListener {  menuItem ->
            when(menuItem.itemId)
            {
                R.id.id_hotspot ->{
                    // load the hotspotFragment
                    supportFragmentManager.beginTransaction().replace(R.id.frag_container, FirstPage())
                        .commit()
                    true
                }
                R.id.id_birds ->{
                    // load the hotspotFragment
                    supportFragmentManager.beginTransaction().replace(R.id.frag_container, SecondPage())
                        .commit()
                    true
                }

                R.id.id_setting ->{
                    // load the hotspotFragment
                    supportFragmentManager.beginTransaction().replace(R.id.frag_container, FourthPage())
                        .commit()
                    true
                }
                else -> false
            }
        }



    }


}
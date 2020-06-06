package itmo.notes

import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import android.content.Intent
import android.widget.TextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import itmo.notes.ui.login.LoginActivity

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val leftNavView = findViewById<NavigationView>(R.id.left_nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        
//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)



        leftNavView.setNavigationItemSelectedListener(this)


        val loginText = leftNavView.getHeaderView(0).findViewById<TextView>(R.id.login_info_menu)


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_login -> {
//                navController.navigate(R.id.loginActivity)

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
                super.onBackPressed()
        }

            //Log.e("Nav", navController.currentDestination.toString())
            //navController.navigateUp()
    }
}

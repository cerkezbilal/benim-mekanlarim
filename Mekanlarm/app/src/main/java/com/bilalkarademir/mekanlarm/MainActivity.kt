package com.bilalkarademir.mekanlarm

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_main.*

var namesArray = ArrayList<String>()
var locationArray = ArrayList<LatLng>()

class MainActivity : AppCompatActivity() {





    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflate = menuInflater
        menuInflate.inflate(R.menu.add_place,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item!!.itemId==R.id.add_place){

            val intent = Intent(applicationContext,MapsActivity::class.java)
            intent.putExtra("info","new")
            startActivity(intent)

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {


        try {

            val database = openOrCreateDatabase("Places", Context.MODE_PRIVATE,null)
            val cursor = database.rawQuery("SELECT * FROM places", null)

            val nameIndex = cursor.getColumnIndex("name")
            val latitudeIndex = cursor.getColumnIndex("latitude")
            val longitudeIndex = cursor.getColumnIndex("longitude")

            cursor.moveToFirst()
            namesArray.clear()
            locationArray.clear()
            while (cursor!= null){

                val namedata = cursor.getString(nameIndex)
                val latitudeFromDatabase = cursor.getString(latitudeIndex)
                val longitudeFromDatabase = cursor.getString(longitudeIndex)



                namesArray.add(namedata)
                val latitudeCoordinate = latitudeFromDatabase.toDouble()
                val longitudeCoordinate = longitudeFromDatabase.toDouble()
                val location = LatLng(latitudeCoordinate,longitudeCoordinate)

                locationArray.add(location)
                cursor.moveToNext()
            }

        }catch (e:Exception){
            e.printStackTrace()
        }

        val arrayAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,namesArray)

        liste.adapter = arrayAdapter

 liste.setOnItemClickListener { parent, view, position, id ->


     val intent = Intent(applicationContext,MapsActivity::class.java)
     intent.putExtra("info","old")
     intent.putExtra("name",namesArray[position])
     intent.putExtra("latitude",locationArray[position].latitude)
     intent.putExtra("longitude",locationArray[position].longitude)
     startActivity(intent)
 }



        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)





    }
}

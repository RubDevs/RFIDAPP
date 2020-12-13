package com.example.rubdevsrfid

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.nfc.NfcAdapter
import android.nfc.NfcManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior.getTag
import kotlin.IllegalStateException
import android.nfc.tech.MifareClassic
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.example.rubdevsrfid.interfaces.SellerAPI
import com.example.rubdevsrfid.models.Seller
import com.example.rubdevsrfid.models.SellerRoom
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class MainActivity : AppCompatActivity() {
    private var adapter: NfcAdapter? = null
    lateinit var vendedor_nombre: EditText
    lateinit var NoHabitacion: EditText
    lateinit var enviar: Button
    lateinit var retrofit: Retrofit
    lateinit var id_seller: String
    lateinit var urlToAPI: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        PreferenceManager.setDefaultValues(this,R.xml.preferences,false)
        val SP: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val preferences = Preferences(this)
        val api_url = preferences.get_api_url(SP)
        val api_port = preferences.get_api_puerto(SP)
        vendedor_nombre = findViewById(R.id.vendedor_nombre)
        NoHabitacion = findViewById(R.id.seller_room)
        enviar = findViewById(R.id.enviar)
        vendedor_nombre.isEnabled = false
        id_seller = ""
        enviar.setOnClickListener(View.OnClickListener {
            if (!vendedor_nombre.text.equals("") && !NoHabitacion.text.equals("") ) {
                val service = retrofit.create<SellerAPI>(SellerAPI::class.java)
                val registro : SellerRoom = SellerRoom()
                registro.room = NoHabitacion.text.toString()
                registro.seller = id_seller
                registro.fecha = Date()
                service.save(registro).enqueue(object : Callback<SellerRoom> {
                    override fun onResponse(call: Call<SellerRoom>, response: Response<SellerRoom>) {
                        TODO("Not yet implemented")
                    }

                    override fun onFailure(call: Call<SellerRoom>, t: Throwable) {
                        t?.printStackTrace()
                        Toast.makeText(applicationContext,"Error al guardar el registro", Toast.LENGTH_SHORT).show()
                    }

                })
            } else {
                Toast.makeText(applicationContext,"Comlete el campo Habitacion", Toast.LENGTH_SHORT).show()
            }
        })
        if (api_url == ""  && api_port == "")
        {
            val intent = Intent(applicationContext,SettingsActivity::class.java)
            startActivity(intent)
            finish()
        }
        urlToAPI = "$api_url:$api_port"
        retrofit = Retrofit.Builder()
            .baseUrl(urlToAPI)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        initNfcAdapter()
    }

    override fun onResume() {
        super.onResume()
        enableNfcForegroundDispatch()
    }

    override fun onPause() {
        disableNfcForegroundDispatch()
        super.onPause()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.action.equals(NfcAdapter.ACTION_TAG_DISCOVERED)){
            id_seller = ByteArrayToHexString(intent?.getByteArrayExtra(NfcAdapter.EXTRA_ID))
            val service = retrofit.create<SellerAPI>(SellerAPI::class.java)
            service.find(id_seller).enqueue(object : Callback<Seller>{
                override fun onResponse(call: Call<Seller>, response: Response<Seller>) {
                   val seller = response?.body()

                }

                override fun onFailure(call: Call<Seller>, t: Throwable) {
                    t?.printStackTrace()
                    Toast.makeText(applicationContext,"Error al obtener datos", Toast.LENGTH_SHORT).show()
                }

            })

            if (id_seller == "0DA1C75F") {
                vendedor_nombre.setText(
                        "Ruben Hernandez"
                )
            }

        }
    }

    private fun ByteArrayToHexString(inarray: ByteArray?): String{
        var i = 0
        var on = 0
        val j = Int
        val hex = arrayOf("0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F")
        var out = ""
        if (inarray != null) {
            for (index in inarray.indices){
                on = inarray.get(index).toInt() and 0xff
                i = (on shr  4) and 0x0f
                out += hex[i]
                i = on and 0x0f
                out += hex[i]
            }
        }
        return out
    }

    private fun initNfcAdapter(){
        val nfcManager = getSystemService(Context.NFC_SERVICE) as NfcManager
        adapter = nfcManager.defaultAdapter
    }

    private fun enableNfcForegroundDispatch() {
        try {
            val intent = Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            val nfcPendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
            val filter = IntentFilter()
            filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED)
            filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED)
            adapter?.enableForegroundDispatch(this, nfcPendingIntent, arrayOf(filter), null)
        } catch (ex: IllegalStateException) {
            //Log.e("Error enabling NFC foreground dispatch")
        }
    }

    private fun disableNfcForegroundDispatch(){
        try {
            adapter?.disableForegroundDispatch(this)
        } catch (ex: IllegalStateException) {
            //Log.e(getTag(),"Error disabling NFC foreground dispatch", ex)
        }
    }
}
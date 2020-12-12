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
import android.widget.EditText
import androidx.preference.PreferenceManager

class MainActivity : AppCompatActivity() {
    private var adapter: NfcAdapter? = null
    lateinit var vendedor_nombre: EditText
    lateinit var NoHabitacion: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        PreferenceManager.setDefaultValues(this,R.xml.preferences,false)
        val SP: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val preferences = Preferences(this)
        val api_url = preferences.get_api_url(SP)
        val api_port = preferences.get_api_puerto(SP)
        if (api_url == ""  && api_port == "")
        {
            val intent = Intent(applicationContext,SettingsActivity::class.java)
            startActivity(intent)
            finish()
        }
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
            vendedor_nombre = findViewById(R.id.vendedor_nombre)
            if (ByteArrayToHexString(intent?.getByteArrayExtra(NfcAdapter.EXTRA_ID)) == "0DA1C75F") {
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
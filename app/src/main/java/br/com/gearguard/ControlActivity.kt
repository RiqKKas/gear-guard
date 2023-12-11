package br.com.gearguard

import android.Manifest
import android.os.Build
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import br.com.gearguard.controller.LogCommandController
import br.com.gearguard.databinding.ActivityControlBinding
import android.provider.Settings


class ControlActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityControlBinding
    private lateinit var logCommandController: LogCommandController

    private lateinit var bluetoothAdapter: BluetoothAdapter
    private val MY_PERMISSIONS_REQUEST_LOCATION = 99

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityControlBinding.inflate(layoutInflater)
        setContentView(binding.root)

        logCommandController = LogCommandController(baseContext)
        registerEvent()

        val bluetoothManager: BluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        requestBluetoothPermissions()
    }

    private fun requestBluetoothPermissions() {
        val permissionsToRequest = mutableListOf<String>()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.BLUETOOTH_CONNECT)
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), MY_PERMISSIONS_REQUEST_LOCATION)
        } else {
            requestEnableBluetooth()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                    requestEnableBluetooth()
                } else {
                    // Alguma permissão foi negada, desabilitar funcionalidade que depende dessa permissão.
                    executeMenu()
                }
            }
        }
    }

    private fun requestEnableBluetooth(): Boolean {
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled) {
            // O Bluetooth não está ativado ou não é suportado
            return false
        }

        var isConnected = false
        val bluetoothA2dp = BluetoothProfile.A2DP
        val bluetoothProfile: Boolean = bluetoothAdapter.getProfileProxy(this, object : BluetoothProfile.ServiceListener {
            override fun onServiceDisconnected(profile: Int) {}
            override fun onServiceConnected(profile: Int, proxy: BluetoothProfile?) {
                if (profile == bluetoothA2dp) {
                    val connectedDevices = proxy?.connectedDevices
                    if (connectedDevices.isNullOrEmpty()) {
                        val intentOpenBluetoothSettings = Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
                        startActivity(intentOpenBluetoothSettings)

                        isConnected = false
                    }

                    isConnected = true
                }
            }
        }, bluetoothA2dp)

        return isConnected
    }

    private fun registerEvent() {
        binding.btnBack.setOnClickListener(this)
        binding.touchRight.setOnClickListener(this)
        binding.touchLeft.setOnClickListener(this)
        binding.touchRight.setOnClickListener(this)
        binding.touchBottom.setOnClickListener(this)
        binding.touchStop.setOnClickListener(this)
        binding.touchTop.setOnClickListener(this)
    }

    override fun onClick(button: View) {
        when (button.id) {
            binding.btnBack.id -> executeMenu()
            binding.touchLeft.id -> callCommandExecution("Mover para Esquerda")
            binding.touchRight.id -> callCommandExecution("Mover para Direita")
            binding.touchBottom.id -> callCommandExecution("Mover para Trás")
            binding.touchStop.id -> callCommandExecution("Parar")
            binding.touchTop.id -> callCommandExecution("Mover para Frente")
        }
    }

    private fun executeMenu() {
        val menuActivity = Intent(baseContext, MenuActivity::class.java)
        startActivity(menuActivity)
    }

    private fun callCommandExecution(command: String) {
        if (requestEnableBluetooth()) {
            logCommandController.insert(command)
        }
    }

}
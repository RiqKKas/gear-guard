package br.com.gearguard

import android.Manifest
import android.os.Build
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.widget.Toast
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
import java.io.IOException
import java.util.UUID

class ControlActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityControlBinding
    private lateinit var logCommandController: LogCommandController
    private var isBluetoothConnected = false

    private lateinit var bluetoothDevice: BluetoothDevice
    private lateinit var bluetoothSocket: BluetoothSocket
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
        bluetoothSocket.close()
        val menuActivity = Intent(baseContext, MenuActivity::class.java)
        startActivity(menuActivity)
    }

    private fun establishBluetoothConnection() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S || ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
            try {
                val deviceAddress = this.getBluetoothAddress()
                if (deviceAddress != "") {
                    bluetoothDevice = bluetoothAdapter.getRemoteDevice(deviceAddress)
                    // UUID para o serviço SPP (Serial Port Profile)
                    val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
                    bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid)
                    bluetoothSocket.connect()

                    isBluetoothConnected = true
                    logToast(baseContext, "Módulo Bluetooth Conectado")
                }
            } catch (e: IOException) {
                logToast(baseContext, "Erro ao estabelecer conexão com o dispositivo Bluetooth")
            }
        }
    }

    private fun getBluetoothAddress() : String {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S || ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
            val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter.bondedDevices
            if (pairedDevices.isNullOrEmpty()) {
                logToast(baseContext, "Nenhum dispositivo Bluetooth pareado")
                return ""
            }

            for (device in pairedDevices) {
                val deviceName = device.name
                val deviceAddress = device.address // Este é o endereço Bluetooth

                if (deviceName == "HC-06") {
                    return deviceAddress
                }
            }
        }

        return ""
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
            establishBluetoothConnection()
            requestEnableBluetooth()
        }
    }

    private fun requestEnableBluetooth() {
        if (!this.isBluetoothConnected && (Build.VERSION.SDK_INT < Build.VERSION_CODES.S || ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED)) {
            val intentOpenBluetoothSettings = Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
            startActivity(intentOpenBluetoothSettings)
            logToast(baseContext, "Pareie o dispositivo Bluetooth")
        }
    }

    private fun sendBluetoothCommand(command: String) {
        try {
            var msg = "parar"
            when (command) {
                "Mover para Frente" -> {
                    msg = "frente"
                }
                "Mover para Trás" -> {
                    msg = "tras"
                }
                "Mover para Esquerda" -> {
                    msg = "esquerda"
                }
                "Mover para Direita" -> {
                    msg = "direita"
                }
                "Parar" -> {
                    msg = "parar"
                }
            }

            val outputStream = bluetoothSocket.outputStream
            outputStream.write(msg.toByteArray())
        } catch (e: IOException) {
            logToast(baseContext, "Erro ao enviar comando para o dispositivo Bluetooth")
        }
    }

    private fun callCommandExecution(command: String) {
        if (this.isBluetoothConnected) {
            this.logCommandController.insert(command)
            sendBluetoothCommand(command)
        } else {
            establishBluetoothConnection()
            requestEnableBluetooth()
        }
    }

    private fun logToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}
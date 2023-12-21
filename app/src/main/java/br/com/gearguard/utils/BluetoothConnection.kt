package br.com.gearguard.utils

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.IOException
import java.util.UUID

class BluetoothConnection(
    private var activity: AppCompatActivity,
    private var bluetoothAdapter: BluetoothAdapter) {

    private var isBluetoothConnected = false
    private lateinit var bluetoothDevice: BluetoothDevice
    private lateinit var bluetoothSocket: BluetoothSocket

    fun establishBluetoothConnection() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S || ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
            try {
                val deviceAddress = this.getBluetoothAddress()
                if (deviceAddress != "") {
                    bluetoothDevice = bluetoothAdapter.getRemoteDevice(deviceAddress)
                    // UUID para o serviço SPP (Serial Port Profile)
                    val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
                    bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid)
                    bluetoothSocket.connect()

                    this.isBluetoothConnected = true
                    LogToast.logToast(activity.baseContext, "Módulo Bluetooth Conectado")

                    return
                }
            } catch (e: IOException) {
                LogToast.logToast(activity.baseContext, "Erro ao estabelecer conexão com o dispositivo Bluetooth")
            }
        }

        this.isBluetoothConnected = false
        return
    }

    private fun getBluetoothAddress() : String {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S || ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
            val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter.bondedDevices
            if (pairedDevices.isNullOrEmpty()) {
                LogToast.logToast(activity.baseContext, "Nenhum dispositivo Bluetooth pareado")
                return ""
            }

            for (device in pairedDevices) {
                val deviceName = device.name
                val deviceAddress = device.address // Este é o endereço Bluetooth

                if (deviceName == "HC-06") { // nome generico do módulo Bluetooth
                    return deviceAddress
                }
            }
        }

        return ""
    }

    fun requestBluetoothPermissions() {
        val permissionsToRequest = mutableListOf<String>()

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.BLUETOOTH_CONNECT)
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(activity, permissionsToRequest.toTypedArray(), 99)
        } else {
            establishBluetoothConnection()
            requestEnableBluetooth()
        }
    }

    fun requestEnableBluetooth() {
        if (!this.isBluetoothConnected && (Build.VERSION.SDK_INT < Build.VERSION_CODES.S || ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED)) {
            val intentOpenBluetoothSettings = Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
            activity.startActivity(intentOpenBluetoothSettings)
            LogToast.logToast(activity.baseContext, "Pareie o dispositivo Bluetooth")
        }
    }

     fun sendBluetoothCommand(msg: String) {
        try {
            val outputStream = bluetoothSocket.outputStream
            outputStream.write(msg.toByteArray())
        } catch (e: IOException) {
            LogToast.logToast(activity.baseContext, "Erro ao enviar comando para o dispositivo Bluetooth")
        }
    }

    fun getIsBluetoothConnected() : Boolean {
        return this.isBluetoothConnected
    }

    fun bluetoothSocketClose() {
        bluetoothSocket.close()
    }

}
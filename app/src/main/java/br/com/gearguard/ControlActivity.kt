package br.com.gearguard

import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import br.com.gearguard.controller.LogCommandController
import br.com.gearguard.databinding.ActivityControlBinding
import br.com.gearguard.utils.BluetoothConnection

class ControlActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityControlBinding
    private lateinit var logCommandController: LogCommandController
    private lateinit var bluetoothConnection : BluetoothConnection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityControlBinding.inflate(layoutInflater)
        setContentView(binding.root)

        logCommandController = LogCommandController(baseContext)
        registerEvent()

        val bluetoothManager: BluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothConnection = BluetoothConnection(this, bluetoothManager.adapter)
        bluetoothConnection.requestBluetoothPermissions()
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

    private fun callCommandExecution(command: String) {
        if (bluetoothConnection.getIsBluetoothConnected()) {
            this.logCommandController.insert(command)

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

            bluetoothConnection.sendBluetoothCommand(msg)
        } else {
            bluetoothConnection.establishBluetoothConnection()
            bluetoothConnection.requestEnableBluetooth()
        }
    }

    private fun executeMenu() {
        bluetoothConnection.bluetoothSocketClose()
        val menuActivity = Intent(baseContext, MenuActivity::class.java)
        startActivity(menuActivity)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            99 -> {
                if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                    bluetoothConnection.requestEnableBluetooth()
                } else {
                    // Alguma permissão foi negada, voltar ao menu.
                    executeMenu()
                }
            }
        }
    }

}
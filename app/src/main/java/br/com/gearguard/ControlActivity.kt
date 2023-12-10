package br.com.gearguard

import android.os.Bundle
import android.content.Intent
import android.view.View.OnClickListener
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import br.com.gearguard.controller.LogCommandController

import br.com.gearguard.databinding.ActivityControlBinding

class ControlActivity : AppCompatActivity(), OnClickListener {

    lateinit var binding: ActivityControlBinding
    lateinit var logCommandController: LogCommandController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityControlBinding.inflate(layoutInflater)
        setContentView(binding.root)

        logCommandController = LogCommandController(baseContext)
        registerEvent()
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

    private fun  executeMenu() {
        val menuActivity = Intent(baseContext, MenuActivity::class.java)
        startActivity(menuActivity)
    }

    private fun callCommandExecution(command: String) {
        logCommandController.insert(command)
    }

}
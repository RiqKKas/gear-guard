package br.com.gearguard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.gearguard.controller.LogCommandController
import br.com.gearguard.databinding.ActivityLogsBinding
import br.com.gearguard.viewmodel.LogsCommandsViewModel
import androidx.lifecycle.ViewModelProvider

class LogsActivity : AppCompatActivity() {

    lateinit var binding: ActivityLogsBinding
    lateinit var logCommandController: LogCommandController
    lateinit var viewModel: LogsCommandsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        logCommandController = LogCommandController(baseContext)
        viewModel = ViewModelProvider(this)[LogsCommandsViewModel::class.java]
    }

}
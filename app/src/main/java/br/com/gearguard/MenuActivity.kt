package br.com.gearguard

import android.os.Bundle
import android.content.Intent
import android.view.View.OnClickListener
import android.view.View
import androidx.appcompat.app.AppCompatActivity

import br.com.gearguard.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity(), OnClickListener {

    lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerEvent()
    }

    private fun registerEvent() {
        binding.btnControle.setOnClickListener(this)
        binding.btnLogs.setOnClickListener(this)
        binding.btnDocumentacao.setOnClickListener(this)
    }

    override fun onClick(button: View) {
        when (button.id) {
            binding.btnControle.id -> executeControl()
            binding.btnLogs.id -> executeLogs()
            binding.btnDocumentacao.id -> executeDocs()
        }
    }

    private fun executeControl() {
        val controlActivity = Intent(baseContext, ControlActivity::class.java)
        startActivity(controlActivity)
    }

    private fun executeLogs() {
        val logsActivity = Intent(baseContext, LogsActivity::class.java)
        startActivity(logsActivity)
    }

    private fun executeDocs() {
        val docsActivity = Intent(baseContext, DocsActivity::class.java)
        startActivity(docsActivity)
    }

}
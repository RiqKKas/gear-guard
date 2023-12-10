package br.com.gearguard

import android.content.Intent
import android.os.Bundle
import android.view.View.OnClickListener
import android.view.View
import androidx.appcompat.app.AppCompatActivity

import br.com.gearguard.databinding.ActivityDocsBinding

class DocsActivity : AppCompatActivity(), OnClickListener {

    lateinit var binding: ActivityDocsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDocsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerEvent()
    }

    private fun registerEvent() {
        binding.btnBack.setOnClickListener(this)
    }

    override fun onClick(button: View) {
        when (button.id) {
            binding.btnBack.id -> executeMenu()
        }
    }

    private fun executeMenu() {
        val menuActivity = Intent(baseContext, MenuActivity::class.java)
        startActivity(menuActivity)
    }

}
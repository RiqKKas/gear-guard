package br.com.gearguard

import android.os.Bundle
import android.content.Intent
import android.view.View.OnClickListener
import android.view.View
import androidx.appcompat.app.AppCompatActivity

import br.com.gearguard.databinding.ActivityControlBinding

class ControlActivity : AppCompatActivity(), OnClickListener {

    lateinit var binding: ActivityControlBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityControlBinding.inflate(layoutInflater)
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
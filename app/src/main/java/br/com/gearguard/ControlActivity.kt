package br.com.gearguard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import br.com.gearguard.databinding.ActivityControlBinding

class ControlActivity : AppCompatActivity() {

    lateinit var binding: ActivityControlBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityControlBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}
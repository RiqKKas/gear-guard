package br.com.gearguard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import br.com.gearguard.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {

    lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}
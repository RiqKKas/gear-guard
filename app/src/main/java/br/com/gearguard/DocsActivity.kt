package br.com.gearguard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import br.com.gearguard.databinding.ActivityDocsBinding

class DocsActivity : AppCompatActivity() {

    lateinit var binding: ActivityDocsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDocsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}
package br.com.gearguard.view

import androidx.recyclerview.widget.RecyclerView
import br.com.gearguard.databinding.LogItemBinding
import br.com.gearguard.model.LogCommandEntity

class LogCommandHold(var binding: LogItemBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(logCommand: LogCommandEntity){
        binding.commandName.text = logCommand.commandName
        binding.timesUsed.text = 0.toString()
        binding.dateCommandUsed.text = logCommand.date
    }

}
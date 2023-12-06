package br.com.gearguard.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.gearguard.model.LogCommandEntity

class LogsCommandsViewModel : ViewModel(){
    private var logsCommand: MutableLiveData<MutableList<LogCommandEntity>> = MutableLiveData()

    fun loadLogsCommand(logsCommandList: MutableList<LogCommandEntity>) {
        logsCommand.value = logsCommandList
    }

    fun getLogsCommand(): MutableList<LogCommandEntity>? {
        return logsCommand.value
    }

}
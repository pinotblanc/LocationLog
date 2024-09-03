package noncom.pino.locationlog.ui.timeline

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TimelineViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "the timeline goes here"
    }
    val text: LiveData<String> = _text
}
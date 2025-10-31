package pt.isel.cher.ui.about

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import pt.isel.cher.domain.Author
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor() : ViewModel() {
    val authors =
        listOf(
            Author("47207", "Diogo", "Ribeiro", "a47207@alunos.isel.pt"),
            Author("49423", "Rafael", "Pegacho", "a49423@alunos.isel.pt"),
            Author("47236", "António", "Coelho", "a47236@alunos.isel.pt"),
        )

    private val _sendEmailEvent = Channel<List<String>>()
    val sendEmailEvent = _sendEmailEvent.receiveAsFlow()

    fun onSendEmailClicked() {
        viewModelScope.launch { _sendEmailEvent.send(authors.map { it.email }) }
    }

    companion object {
        const val EMAIL_SUBJECT = "Congratulations!"
        const val EMAIL_BODY = "Good work, let's go!!!"
    }
}

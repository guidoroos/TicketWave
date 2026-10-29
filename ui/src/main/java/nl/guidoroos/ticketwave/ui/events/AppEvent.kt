package nl.guidoroos.ticketwave.ui.events

sealed interface AppEvent {
    data class ShowSnackbar(val message: String) : AppEvent
    data class ShowDialog(
        val title: String,
        val message: String,
        val onConfirm: () -> Unit = {}
    ) : AppEvent
}

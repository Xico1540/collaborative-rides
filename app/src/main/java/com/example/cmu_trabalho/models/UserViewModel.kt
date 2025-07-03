import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf

class UserViewModel : ViewModel() {
    // Usando uma variável para armazenar o ID do usuário
    var userId = mutableStateOf<Long?>(null)
}

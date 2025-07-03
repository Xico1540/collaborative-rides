package com.example.cmu_trabalho

import PreferencesManager
import UserViewModel
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cmu_trabalho.database.AppDatabase
import com.example.cmu_trabalho.models.Car
import com.example.cmu_trabalho.models.User
import com.example.cmu_trabalho.repository.TripRepository
import com.example.cmu_trabalho.repository.UserRepository
import com.example.cmu_trabalho.ui.screens.*
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import kotlinx.coroutines.launch
import pt.ipp.estg.androidintentscompose.ui.screens.AccountScreen
import pt.ipp.estg.androidintentscompose.ui.screens.NotificationsScreen
import pt.ipp.estg.androidintentscompose.ui.theme.AndroidIntentsComposeTheme
import java.util.*

class MainActivity : ComponentActivity(), PermissionsListener {
    private lateinit var permissionsManager: PermissionsManager
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var preferencesManager: PreferencesManager



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferencesManager = PreferencesManager(this)


        // Estados globais para tema e idioma
        val isDarkTheme = preferencesManager.loadDarkModePreference()
        val currentLanguage = preferencesManager.loadLanguagePreference()

        // Configuração inicial
        handleLocationPermission()
        enableEdgeToEdge()

        // Inicializações de banco de dados e repositórios
        val database = AppDatabase.getDatabase(this)
        val userRepository = UserRepository(database.userDao())
        val tripRepository = TripRepository(database.tripDao())

        // Conteúdo principal da aplicação
        setContent {
            val isDarkThemeState = remember { mutableStateOf(isDarkTheme) }
            AndroidIntentsComposeTheme(darkTheme = isDarkThemeState.value) {
                val navController = rememberNavController()
                MainScreen(
                    navController = navController,
                    userRepository = userRepository,
                    tripRepository = tripRepository,
                    userViewModel = userViewModel,
                    isDarkTheme = isDarkThemeState.value,
                    onThemeChange = { newTheme ->
                        isDarkThemeState.value = newTheme
                        preferencesManager.saveDarkModePreference(newTheme)
                    },
                    currentLanguage = currentLanguage,
                    onLanguageChange = { language ->
                        preferencesManager.saveLanguagePreference(language)
                        updateLocale(this, language)
                    }
                )
            }
        }
    }

    // Atualiza o idioma do aplicativo
    private fun updateLocale(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale)
        } else {
            config.locale = locale
        }
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }


    // Gerenciamento de permissões de localização
    override fun onExplanationNeeded(permissionsToExplain: List<String>) {
        Toast.makeText(
            this,
            "Este aplicativo precisa de permissão de localização para mostrar sua posição no mapa.",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            activateLocationFeatures()
        } else {
            Toast.makeText(this, "Permissão de localização negada.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleLocationPermission() {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            activateLocationFeatures()
        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager.requestLocationPermissions(this)
        }
    }

    private fun activateLocationFeatures() {
        Toast.makeText(this, "Localização ativa!", Toast.LENGTH_SHORT).show()
    }
}

// Tela principal com navegação
@Composable
fun MainScreen(
    navController: NavHostController,
    userRepository: UserRepository,
    tripRepository: TripRepository,
    userViewModel: UserViewModel,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    currentLanguage: String,
    onLanguageChange: (String) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route
    val userId = userViewModel.userId.value

    if (currentRoute == "register") {
        RegisterScreen(navController, userRepository)
    } else if (currentRoute == "login") {
        LoginScreen(navController, userRepository)
    } else {
        val isDrawerEnabled = currentRoute != "map_selection/{isPartida}"


        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                if (isDrawerEnabled) {
                    ModalDrawerSheet {
                        DrawerContent(navController, drawerState)
                    }
                }
            },
            gesturesEnabled = isDrawerEnabled
        ) {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding()
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = "login",
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    composable("home") {
                        HomeScreen(
                            navController = navController,
                            isDarkTheme = isDarkTheme,  // Passando o estado do tema
                            onThemeChange = onThemeChange  // Passando a função de alteração de tema
                        )
                    }
                    composable("my_trips") { MyTripsScreen(navController = navController) }
                    composable("settings") {
                        val user = User(
                            userId = 1L,
                            name = "Bruno Pontvianne",
                            email = "bruno.pontvianne2004@gmail.com",
                            phone = "912345678",
                            password = "password123",
                            pais = "Portugal",
                            birthDate = "2004-01-01",
                            car = Car(
                                Marca = "Toyota",
                                Modelo = "Corolla",
                                lugares = 5,
                                Matricula = "12-AB-34"
                            )
                        )
                        SettingsScreen(navController, user)
                    }

                    composable("account") {
                        AccountScreen(
                            navController,
                            userRepository,
                            userId!!
                        )
                    }
                    composable("notifications") { NotificationsScreen() }
                    composable("preferences") {
                        PreferencesScreen(
                            currentTheme = isDarkTheme,
                            onThemeChange = onThemeChange,
                            onLanguageChange = onLanguageChange
                        )
                    }
                    composable("login") { LoginScreen(navController, userRepository) }
                    composable("register") { RegisterScreen(navController, userRepository) }
                    composable("trips") { TripsScreen(navController) }
                    composable("view_reviews") { ViewReviewsScreen() }
                    composable("edit_trip") {
                        EditTripScreen(navController = navController)
                    }
                    composable("create_trip") {
                        CreateTripScreen(
                            navController,
                            tripRepository,
                            userRepository,
                            userId!!
                        )
                    }
                    composable("details_trip") {
                        DetailsTripScreen(
                            navController,
                            isDarkTheme = isDarkTheme
                        )
                    }
                    composable(
                        "map_selection/{isPartida}",
                        arguments = listOf(navArgument("isPartida") { type = NavType.BoolType })
                    ) { backStackEntry ->
                        val isPartida = backStackEntry.arguments?.getBoolean("isPartida") ?: true
                        MapSelectionScreen(
                            onLocationSelected = { latitude, longitude, isPartida ->
                                navController.previousBackStackEntry?.savedStateHandle?.set(
                                    if (isPartida) "origemLatLng" else "destinoLatLng",
                                    Pair(latitude, longitude)
                                )
                                navController.popBackStack()
                            },
                            isPartida = isPartida
                        )
                    }
                    composable("cancel_trip") {
                        CancelTripScreen(
                            onCancelConfirm = { /* lógica de confirmação de cancelamento */ },
                            onCancelDismiss = { navController.popBackStack() }
                        )
                    }
                    composable("rate_trip") {
                        RatingPopup(
                            onDismiss = { navController.popBackStack() },
                            onRatingSubmitted = { rating, comment->
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}

    @Composable
    fun DrawerContent(navController: NavHostController, drawerState: DrawerState) {
        val items = listOf(
            Triple(Icons.Filled.Home, "Home", "home"),
            Triple(Icons.Filled.DateRange, "Minhas viagens", "my_trips"),
            Triple(Icons.Filled.Notifications, "Notificações", "notifications"),
            Triple(Icons.Filled.Person, "Conta", "account"),
            Triple(Icons.Filled.Settings, "Preferências", "preferences")
        )

        val scope = rememberCoroutineScope()

        Column {
            items.forEach { (icon, label, route) ->
                Row(
                    modifier = Modifier
                        .clickable {
                            scope.launch {
                                navController.navigate(route) {
                                    popUpTo(route) { inclusive = true }
                                }
                                drawerState.close()
                            }
                        }
                        .padding(16.dp)
                ) {
                    Icon(imageVector = icon, contentDescription = label)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = label)
                }
            }
        }
    }



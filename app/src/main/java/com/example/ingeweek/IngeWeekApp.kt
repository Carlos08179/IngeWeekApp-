package com.example.ingeweek

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import androidx.compose.ui.unit.sp
import androidx.compose.material3.ScrollableTabRow


// Modelos de datos
data class Event(
    val id: Int,
    val title: String,
    val time: String,
    val location: String,
    val speaker: String = "",
    val description: String = "",
    val day: String,
    val school: String = "",
    val type: EventType = EventType.CONFERENCE
)

enum class EventType {
    CONFERENCE, CEREMONY, COMPETITION, SOCIAL, RELIGIOUS
}

data class Location(
    val name: String,
    val description: String,
    val campus: String = "Campus I"
)

data class Competition(
    val id: Int,
    val name: String,
    val time: String,
    val location: String,
    val participants: String,
    val day: String
)

// Repositorio de datos
object DataRepository {
    val events = listOf(
        Event(1, "Ceremonia de Inauguración", "09:00", "Plazuela de la UNS", "", "Ceremonia de inauguración de la semana del ingeniero", "Lunes 02", type = EventType.CEREMONY),

        // Martes 03 - Ingeniería Agroindustrial
        Event(2, "Hidrolisis marinos – vía enzimática", "08:00", "Auditorio EPIE", "Ing. Gabriel Sifuentes Penagos", "", "Martes 03", "Ingeniería Agroindustrial"),
        Event(3, "NutriAvo: Agroindustria con propósito", "08:45", "Auditorio EPIE", "Ing. Mirian Vásquez Chuquizuta", "Innovar, emprender y transformar desde la agricultura familiar", "Martes 03", "Ingeniería Agroindustrial"),
        Event(4, "Innovación, emprendimiento y gestión agroindustrial", "09:30", "Auditorio EPIE", "Ing. Nadia Esther Gamarra Abanto", "", "Martes 03", "Ingeniería Agroindustrial"),
        Event(5, "Gestión de Calidad en la producción de Aceite de Pescado", "10:15", "Auditorio EPIE", "Ing. Carmen Pajuelo Carbajal", "Para Consumo Humano", "Martes 03", "Ingeniería Agroindustrial"),

        // Martes 03 - Ingeniería Agrónoma
        Event(6, "Manejo agronómico del cultivo de palto var. Hass", "15:00", "Auditorio Ing. Agrónoma – Campus II", "Ing. Paco Chicoma Rivera", "", "Martes 03", "Ingeniería Agrónoma"),
        Event(7, "Estrés fisiológico de los cultivos", "15:45", "Auditorio Ing. Agrónoma – Campus II", "Ing. Daleska Longobardi Méndez", "", "Martes 03", "Ingeniería Agrónoma"),
        Event(8, "Agroexportación de frutales", "16:30", "Auditorio Ing. Agrónoma – Campus II", "Ing. Jesús Jaque Chauca", "", "Martes 03", "Ingeniería Agrónoma"),
        Event(9, "Experiencias fitosanitarias en el cultivo de palto", "17:15", "Auditorio Ing. Agrónoma – Campus II", "Ms. Teófilo Arias Miranda", "Agro Chimu", "Martes 03", "Ingeniería Agrónoma"),
        Event(10, "Nuevas alternativas de nutrición en el cultivo de palta", "18:00", "Auditorio Ing. Agrónoma – Campus II", "Ing. Shimy Zapata Gutiérrez", "Yara Perú", "Martes 03", "Ingeniería Agrónoma"),

        // Martes 03 - Sistemas e Informática
        Event(11, "La inteligencia artificial y su influencia en la programación", "15:00", "Auditorio EPIE", "Dr. Carlos Eugenio Vega Moreno", "", "Martes 03", "Ingeniería de Sistemas"),
        Event(12, "Estrategias competitivas en los negocios empresariales", "15:45", "Auditorio EPIE", "Dra. Lisbeth Dora Briones Pereyra", "", "Martes 03", "Ingeniería de Sistemas"),
        Event(13, "Del dato a la decisión: Fundamentos y aplicaciones de Power BI", "16:30", "Auditorio EPIE", "Ms. Johan López Heredia", "", "Martes 03", "Ingeniería de Sistemas"),
        Event(14, "Stacking ensemble approach to diagnosing diabetes", "17:15", "Auditorio EPIE", "Dr. Alfredo Daza Vergaray", "", "Martes 03", "Ingeniería de Sistemas"),
        Event(15, "Modelo multiclasificador para predicción de carga térmica", "18:00", "Auditorio EPIE", "Mg. Luis Enrique Ramírez Milla", "En edificios residenciales", "Martes 03", "Ingeniería de Sistemas"),

        // Miércoles 04 - Ingeniería en Energía
        Event(16, "Agrovoltaica, una alternativa sostenible", "09:00", "Auditorio EPIE", "Dr. Denis Arangurí Cayetano", "Para la transición energética", "Miércoles 04", "Ingeniería en Energía"),
        Event(17, "Técnicas nucleares para detección de gas Radón", "10:00", "Auditorio EPIE", "Msc. Carlos Montañez Montenegro", "En hidrocarburos y el medio ambiente", "Miércoles 04", "Ingeniería en Energía"),
        Event(18, "Implementación de energía Eólica residencial en el Perú", "11:00", "Auditorio EPIE", "M.Sc. Ricardo Antonio Cedrón Maguiña", "Análisis comparativo de regulaciones Europeas", "Miércoles 04", "Ingeniería en Energía"),

        // Miércoles 04 - Ingeniería Mecánica
        Event(19, "Aplicación del TPM a equipos de tratamiento", "15:00", "Auditorio EPIE", "M.Sc. Arquímedes Iparraguirre Lozano", "De caldos del proceso de harina de pescado", "Miércoles 04", "Ingeniería Mecánica"),
        Event(20, "Bloqueo y etiquetado de energía residual en la industria", "15:45", "Auditorio EPIE", "M.Sc. Luis Carlos Calderón Rodríguez", "", "Miércoles 04", "Ingeniería Mecánica"),
        Event(21, "Análisis de un sistema automático de transferencia ATS", "16:15", "Auditorio EPIE", "Ms. Fredesbildo Fidel Ríos Noriega", "", "Miércoles 04", "Ingeniería Mecánica"),
        Event(22, "Aplicación de la hidrocinética en la ingeniería", "17:00", "Auditorio EPIE", "M.Sc. Nelver Javier Escalante Espinoza", "", "Miércoles 04", "Ingeniería Mecánica"),
        Event(23, "Los retos en la fabricación de puentes metálicos", "17:45", "Auditorio EPIE", "Ing. Diego Alonso Blondet Belaunde", "En Chimbote bajo el enfoque BIM", "Miércoles 04", "Ingeniería Mecánica"),

        // Miércoles 04 - Ingeniería Civil
        Event(24, "El ingeniero como agente de cambio", "10:00", "Auditorio Biblioteca Central", "Ing. Jorge Castañeda Centurión", "", "Miércoles 04", "Ingeniería Civil"),
        Event(25, "Desempeño sísmico de un edificio esencial", "10:45", "Auditorio Biblioteca Central", "Ing. Iván León Malo", "Con fines de reforzamiento estructural", "Miércoles 04", "Ingeniería Civil"),
        Event(26, "Análisis de fallas en estructuras metálicas", "11:30", "Auditorio Biblioteca Central", "Ing. Gumercindo Flores Reyes", "", "Miércoles 04", "Ingeniería Civil"),
        Event(27, "Cimentaciones para edificaciones en Chimbote", "11:30", "Auditorio Biblioteca Central", "Ing. Jorge Morillo Trujillo", "Aplicando la norma E.050", "Miércoles 04", "Ingeniería Civil"),

        // Jueves 05
        Event(28, "Misa de Celebración", "09:00", "Capilla de la UNS", "", "Por la Semana de Ingeniería", "Jueves 05", type = EventType.RELIGIOUS),
        Event(29, "Ceremonia Central", "10:00", "Auditorio de Ingeniería en Energía", "", "Celebración de la Semana de Ingeniería", "Jueves 05", type = EventType.CEREMONY),
        Event(30, "Corso Inter escuelas", "15:00", "Complejo deportivo UNS", "", "Desde Puerta 1 Campus Universitario", "Jueves 05", type = EventType.SOCIAL),
        Event(31, "Partido de Fútbol de Confraternidad", "16:00", "Complejo deportivo UNS", "", "Partidos Inaugurales", "Jueves 05", type = EventType.COMPETITION),

        // Viernes 06
        Event(32, "Campeonato Interescuelas", "09:00", "Complejo Deportivo – UNS", "", "Eliminatorias de Fútbol, Vóley y Básquet", "Viernes 06", type = EventType.COMPETITION),
        Event(33, "Almuerzo de Confraternidad", "13:00", "Complejo deportivo – UNS", "", "Compartir de la Facultad de Ingeniería", "Viernes 06", type = EventType.SOCIAL),
        Event(34, "Campeonato Fulbito Docentes", "14:00", "Complejo deportivo - UNS", "", "Inter escuelas de la Facultad", "Viernes 06", type = EventType.COMPETITION),
        Event(35, "Final de Campeonatos", "15:00", "Complejo Deportivo – UNS", "", "Fútbol, Vóley y Básquet", "Viernes 06", type = EventType.COMPETITION),
        Event(36, "Premiación", "18:00", "Complejo deportivo UNS", "", "Equipos ganadores del Campeonato", "Viernes 06", type = EventType.CEREMONY),
        Event(37, "Clausura", "18:15", "Complejo deportivo UNS", "", "Cierre de la Semana de Ingeniería", "Viernes 06", type = EventType.CEREMONY)
    )

    val competitions = listOf(
        Competition(1, "Fútbol - Ing. Agrónoma vs Ing. Sistema", "09:00", "Complejo Deportivo UNS", "Estudiantes", "Viernes 06"),
        Competition(2, "Fútbol - Ing. Mecánica vs Ing. Agroindustrial", "09:30", "Complejo Deportivo UNS", "Estudiantes", "Viernes 06"),
        Competition(3, "Fútbol - Ing. Energía vs Ing. Civil", "10:00", "Complejo Deportivo UNS", "Estudiantes", "Viernes 06"),
        Competition(4, "Vóley - Ing. Agrónoma vs Ing. Sistema", "14:00", "Complejo Deportivo UNS", "Estudiantes", "Viernes 06"),
        Competition(5, "Vóley - Ing. Mecánica vs Ing. Agroindustrial", "14:30", "Complejo Deportivo UNS", "Estudiantes", "Viernes 06"),
        Competition(6, "Vóley - Ing. Energía vs Ing. Civil", "15:00", "Complejo Deportivo UNS", "Estudiantes", "Viernes 06"),
        Competition(7, "Básquet - Ing. Energía vs Ing. Civil", "09:00", "Complejo Deportivo UNS", "Estudiantes", "Viernes 06"),
        Competition(8, "Básquet - Ing. Mecánica vs Ing. Agroindustrial", "09:30", "Complejo Deportivo UNS", "Estudiantes", "Viernes 06"),
        Competition(9, "Básquet - Ing. Agrónoma vs Ing. Sistema", "10:00", "Complejo Deportivo UNS", "Estudiantes", "Viernes 06"),
        Competition(10, "Fulbito Docentes", "14:00", "Complejo Deportivo UNS", "Docentes", "Viernes 06"),
        Competition(11, "Escuela Campeón 2024 vs Club UNS", "16:00", "Complejo Deportivo UNS", "Inaugural", "Jueves 05"),
        Competition(12, "Egresados Master 1 vs Master 2", "16:30", "Complejo Deportivo UNS", "Egresados", "Jueves 05")
    )

    val locations = listOf(
        Location("Auditorio EPIE", "Auditorio principal de la Escuela de Ingeniería en Energía", "Campus I"),
        Location("Auditorio Biblioteca Central", "Auditorio de la Biblioteca Central UNS", "Campus I"),
        Location("Auditorio Ing. Agrónoma", "Auditorio de la Escuela de Ingeniería Agrónoma", "Campus II"),
        Location("Auditorio de Ingeniería en Energía", "Auditorio específico de la escuela", "Campus I"),
        Location("Complejo Deportivo UNS", "Instalaciones deportivas principales", "Campus I"),
        Location("Plazuela de la UNS", "Plaza central de la universidad", "Campus I"),
        Location("Capilla de la UNS", "Capilla universitaria", "Campus I")
    )
}

// Enums para navegación
enum class Screen(val route: String, val title: String, val icon: ImageVector) {
    AGENDA("agenda", "Agenda", Icons.Default.Schedule),
    SEMINARS("seminars", "Seminarios", Icons.Default.School),
    COMPETITIONS("competitions", "Competencias", Icons.Default.EmojiEvents),
    LOCATIONS("locations", "Ubicaciones", Icons.Default.LocationOn)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngeWeekApp() {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Semana de Ingeniería UNS 2025",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Screen.entries.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Acción del FAB */ },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Agregar",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.AGENDA.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.AGENDA.route) { AgendaScreen() }
            composable(Screen.SEMINARS.route) { SeminarsScreen() }
            composable(Screen.COMPETITIONS.route) { CompetitionsScreen() }
            composable(Screen.LOCATIONS.route) { LocationsScreen() }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaScreen() {
    val days = listOf("Lunes 02", "Martes 03", "Miércoles 04", "Jueves 05", "Viernes 06")
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Column {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            days.forEachIndexed { index, day ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(day) }
                )
            }
        }

        val dayEvents = DataRepository.events.filter { it.day == days[selectedTabIndex] }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(dayEvents) { event ->
                EventCard(event = event)
            }
        }
    }
}

@Composable
fun SeminarsScreen() {
    val schools = listOf("Todos", "Ingeniería Civil", "Ingeniería Agrónoma", "Ingeniería de Sistemas",
        "Ingeniería en Energía", "Ingeniería Mecánica", "Ingeniería Agroindustrial")
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Column {
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = MaterialTheme.colorScheme.surface,

        ) {
            schools.forEachIndexed { index, school ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(school, fontSize = 12.sp) }
                )
            }
        }

        val seminars = if (selectedTabIndex == 0) {
            DataRepository.events.filter { it.type == EventType.CONFERENCE }
        } else {
            DataRepository.events.filter {
                it.type == EventType.CONFERENCE && it.school == schools[selectedTabIndex]
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(seminars) { event ->
                SeminarCard(event = event)
            }
        }
    }
}

@Composable
fun CompetitionsScreen() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(DataRepository.competitions) { competition ->
            CompetitionCard(competition = competition)
        }
    }
}

@Composable
fun LocationsScreen() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(DataRepository.locations) { location ->
            LocationCard(location = location)
        }
    }
}

@Composable
fun EventCard(event: Event) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = when (event.type) {
                    EventType.CONFERENCE -> Icons.Default.School
                    EventType.CEREMONY -> Icons.Default.Event
                    EventType.COMPETITION -> Icons.Default.EmojiEvents
                    EventType.SOCIAL -> Icons.Default.Group
                    EventType.RELIGIOUS -> Icons.Default.Church
                },
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${event.time} • ${event.location}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (event.speaker.isNotEmpty()) {
                    Text(
                        text = "Ponente: ${event.speaker}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                if (event.description.isNotEmpty()) {
                    Text(
                        text = event.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun SeminarCard(event: Event) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = event.school.ifEmpty { "Conferencia General" },
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = event.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${event.day} - ${event.time}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = event.location,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (event.speaker.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = event.speaker,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            if (event.description.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = event.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
fun CompetitionCard(competition: Competition) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = competition.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${competition.day} - ${competition.time}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = competition.location,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Participantes: ${competition.participants}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun LocationCard(location: Location) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = location.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = location.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = location.campus,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
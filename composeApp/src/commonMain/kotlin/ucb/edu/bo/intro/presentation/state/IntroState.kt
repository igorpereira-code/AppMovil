package ucb.edu.bo.intro.presentation.state

import ucb.edu.bo.intro.domain.model.IntroPage

data class IntroState(
    val pages: List<IntroPage> = listOf(
        IntroPage(
            title = "Gestiona tus tareas",
            description = "Puedes gestionar fácilmente todas tus tareas diarias de forma gratuita",
            imageRes = "task_image"
        ),
        IntroPage(
            title = "Crea tu rutina diaria",
            description = "En nuestra app puedes crear tu rutina personalizada para mantenerte productivo",
            imageRes = "routine_image"
        ),
        IntroPage(
            title = "Organiza tus tareas",
            description = "Puedes organizar tus tareas diarias añadiéndolas en categorías separadas",
            imageRes = "organize_image"
        )
    ),
    val currentPage: Int = 0
)
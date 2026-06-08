package ucb.edu.bo.todoApp.intro.presentation.state

import appmovil.composeapp.generated.resources.*
import ucb.edu.bo.todoApp.intro.domain.model.IntroPage

data class IntroState(
    val pages: List<IntroPage> = listOf(
        IntroPage(
            title = Res.string.intro_title_page1,
            description = Res.string.intro_desc_page1,
            imageRes = "task_image"
        ),
        IntroPage(
            title = Res.string.intro_title_page2,
            description = Res.string.intro_desc_page2,
            imageRes = "routine_image"
        ),
        IntroPage(
            title = Res.string.intro_title_page3,
            description = Res.string.intro_desc_page3,
            imageRes = "organize_image"
        )
    ),
    val currentPage: Int = 0
)
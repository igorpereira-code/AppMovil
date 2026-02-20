package ucb.edu.bo

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
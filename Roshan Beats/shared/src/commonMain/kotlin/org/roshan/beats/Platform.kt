package org.roshan.beats

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
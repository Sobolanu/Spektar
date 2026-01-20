package com.example.spektar.ui.navigation.utils

import androidx.navigation.NavController

// this extension exists because if i use .navigate normally, then pressing another item that navigates
// mid-transition would break my navigation and disable both objects from navigation completely.
// this says "if you're moving to a different route, you can't move anywhere else mid-transition"

var lastNavigateTime = 0L
inline fun <reified T : Any> NavController.safeNavigate(
    destination: T,
    debounceMillis: Long = 500
) {
    val now = System.currentTimeMillis()
    val currentRoute = currentBackStackEntry?.destination?.route
    val targetRoute = destination::class.qualifiedName

    if (currentRoute != targetRoute && now - lastNavigateTime > debounceMillis) {
        lastNavigateTime = now
        navigate(destination)
    }

}

/*
implement this later to use type-safe objects/data classes and not a string
something something https://github.com/raamcosta/compose-destinations and instead of T : Any you do Directions[Something] i can't remember

inline fun <reified T : Any> NavController.safeNavigate(
    destination: T,
    debounceMillis: Long = 500,
    popUpToRoute: String = "home",
    inclusive: Boolean = false
) {
    val now = System.currentTimeMillis()
    val currentRoute = currentBackStackEntry?.destination?.route
    val targetRoute = destination::class.qualifiedName

    if (currentRoute != targetRoute && now - lastNavigateTime > debounceMillis) {
        lastNavigateTime = now
        navigate(destination) {
            popUpTo(popUpToRoute) {
                this.inclusive = inclusive
            }
        }
    }
}
*/
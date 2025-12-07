package com.example.spektar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

private val books : Hobby = Hobby("Books")
private val movies : Hobby = Hobby("Movies")
private val anime : Hobby = Hobby("Anime")
private val games : Hobby = Hobby("Games")
internal var globalHobbyList = mutableListOf(books, movies, anime, games) // placeholder until i figure out firebase

internal var globalHobbyImagesList : MutableList<Any> = mutableListOf(
    "https://static.jojowiki.com/images/e/e2/latest/20200423212900/Joseph_Joestar_Infobox_Manga.png",
    "https://static.jojowiki.com/images/b/bd/latest/20221006234855/Jonathan_Infobox_Manga.png",
    "https://www.pngmart.com/files/13/Giorno-Giovanna-PNG-Clipart.png",
    "https://static.jojowiki.com/images/thumb/0/0a/latest/20210424101455/DIO_Normal_SC_Infobox_Anime.png/800px-DIO_Normal_SC_Infobox_Anime.png",
    "https://img.favpng.com/18/24/23/jotaro-kujo-josuke-higashikata-jojo-s-bizarre-adventure-all-star-battle-jolyne-cujoh-yoshikage-kira-png-favpng-D8MncfnMA6pURtugyD2pDLyCW.jpg",
    "https://static.jojowiki.com/images/a/a1/Josuke_DU_Infobox_Manga.png",
)

internal data class NavigationItem(
    val title : String,
    val selectedIcon : ImageVector,
    val unselectedIcon : ImageVector
)

internal val topIcon = NavigationItem (
    title = "Profile",
    selectedIcon = Icons.Filled.AccountCircle,
    unselectedIcon = Icons.Outlined.AccountCircle
)

internal val bottomIcons = listOf(
    NavigationItem(
        title = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    ),

    NavigationItem(
        title = "Repeat Questionnaire",
        selectedIcon = Icons.Filled.AddBox,
        unselectedIcon = Icons.Outlined.AddBox
    ),

    NavigationItem(
        title = "Settings",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings
    )
)

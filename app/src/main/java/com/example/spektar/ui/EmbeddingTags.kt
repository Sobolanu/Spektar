package com.example.spektar.ui

enum class Tags {
    // by age:
    KIDS_UNUSED,
    TEENS,
    ADULTS,
    FAMILY_FRIENDLY,

    // general:
    ACTION,
    FANTASY,
    SCI_FI,
    COMEDY,
    THRILLER,
    CRIME,
    PHILOSOPHY,
    PSYCHOLOGY,
    DRAMA,
    HORROR,
    ROMANCE,
    INDIE,
    SPORTS,
    ANIMATED,
    HISTORICAL,
    WAR,
    DOCUMENTARY,
    MYSTERY,
    MUSICAL,
    BIOGRAPHY,
    FAMILY,
    ADVENTURE,

    // anime-specific
    ANIME,
    SHONEN,
    SHOJO,
    SEINEN,
    JOSEI,
    ISEKAI,
    SLICE_OF_LIFE,
    MECHA,
    SPORTS_ANIME,

    // game specific

    MULTIPLAYER,
    RPG,
    FPS,
    OPEN_WORLD,
    RTS,
    STORY_DRIVEN,
    SIMULATION,
    TURN_BASED,
    ROGUELIKE,
    SURVIVAL,
    SANDBOX,
    MMO
}

fun removeWhiteSpace(menuName: String): String {
    return menuName.replace(" ", "_")
}

// parse string name to enum index
fun stringToEnumParser(tagName: String): Int {
    var parsedTag = tagName.uppercase()
    parsedTag.replace('-', '_')
    parsedTag = removeWhiteSpace(parsedTag)

    val a : Tags = enumValueOf(parsedTag)
    return a.ordinal
}
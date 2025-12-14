package com.example.spektar.models

/*
Repository of placeholder pieces of media.
*/

class MediaRepository {
    private val Jojo = SpecificMedia(
        "Anime",
        "Jojo's Bizarre Adventure",
        "https://static.jojowiki.com/images/a/a1/Josuke_DU_Infobox_Manga.png",
        "slice of life thingamajig",
        "Hirohiko Araki",
        "1/1/2012 or some shit",
    )

    private val Jojo2 = SpecificMedia(
        "Anime",
        "Jojo's Bizarre Adventure",
        "https://static.jojowiki.com/images/e/e2/latest/20200423212900/Joseph_Joestar_Infobox_Manga.png",
        "haha joseph joestar said your next line is",
        "Hirohiko Araki",
        "1/1/2012 or some shit",
    )

    private val Jojo3 = SpecificMedia(
        "Anime",
        "Jojo's Bizarre Adventure",
        "https://www.pngmart.com/files/13/Giorno-Giovanna-PNG-Clipart.png",
        "bro is NOT martin luther king",
        "Hirohiko Araki",
        "1/1/2012 or some shit",
    )

    private val Jojo4 = SpecificMedia(
        "Anime",
        "Jojo's Bizarre Adventure",
        "https://img.favpng.com/18/24/23/jotaro-kujo-josuke-higashikata-jojo-s-bizarre-adventure-all-star-battle-jolyne-cujoh-yoshikage-kira-png-favpng-D8MncfnMA6pURtugyD2pDLyCW.jpg",
        "my glorious queen",
        "Hirohiko Araki",
        "1/1/2012 or some shit",
    )

    internal val globalMediaList = mutableListOf(Jojo, Jojo2, Jojo3, Jojo4)
    fun getMedia(): List<SpecificMedia> = globalMediaList
}
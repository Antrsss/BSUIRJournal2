package com.example.bsuirjournal2.data

object DataSource {
    val groupNumberOptions = listOf (
        "353501",
        "353502",
        "353503",
        "353504",
        "353505",
    )

    fun search(text: String): List<String> {
        return groupNumberOptions.filter {
            it.startsWith(text)
        }
    }
}
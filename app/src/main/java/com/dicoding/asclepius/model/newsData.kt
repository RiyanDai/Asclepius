package com.dicoding.asclepius.model

data class newsData(
    var author: String,
    var title: String,
    var description: String,
    var url: String,
    var urlToImage: String,
    var publishedAt: String,
    var content: String,
    var source: source
)
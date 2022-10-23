package com.dldmswo1209.chuncheonconquest.model

data class Post(
    var uid: String, // 작성자 uid
    var title: String,
    var imageUrl: String? = null,
    var content: String,
    var date: String
){
    constructor(): this("","","","","")
}

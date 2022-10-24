package com.dldmswo1209.chuncheonconquest.model

import java.io.Serializable

data class User(
    val uid : String,
    val email: String,
    val pw: String,
    val name: String,
    var imageUrl: String? = null, // 스토리지에 저장할 이미지 경로
    var imageUri: String? = null // 스토리지에서 가져올 이미지 uri
): Serializable{
    constructor() : this("","","","")
}

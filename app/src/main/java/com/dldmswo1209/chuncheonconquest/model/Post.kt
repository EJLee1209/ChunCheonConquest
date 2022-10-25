package com.dldmswo1209.chuncheonconquest.model

import java.io.Serializable

data class Post(
    var key: String,
    var title: String,
    var imageUrl: String? = null, // 스토리지에 저장할 이미지 경로
    var imageUri: String? = null, // 스토리지에서 가져올 이미지 uri
    var content: String,
    var date: String,
    var user: User,
): Serializable{
    constructor(): this("","","","","","",User())
}

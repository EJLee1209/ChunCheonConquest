package com.dldmswo1209.chuncheonconquest.model

import java.io.Serializable

data class TourSpot(
    val id: Int,
    val title: String,
    val img_url: String,
    val addr: String,
    val tel: String,
    val description: String,
    val naverUrl: String,
    val menu: List<Menu>,
    val latitude: Double,
    val longitude: Double

) : Serializable{
    constructor() : this(0,"","","","","","", listOf<Menu>() ,0.0,0.0)
}

data class Menu(
    val menu: String,
    val price: String
): Serializable{
    constructor() : this("","")
}
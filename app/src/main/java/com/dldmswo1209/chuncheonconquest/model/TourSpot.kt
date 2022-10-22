package com.dldmswo1209.chuncheonconquest.model

data class TourSpot(
    val id: Int,
    val title: String,
    val img_url: String,
    val addr: String,
    val tel: String,
    val latitude: Double,
    val longitude: Double
){
    constructor() : this(0,"","","","",0.0,0.0)
}
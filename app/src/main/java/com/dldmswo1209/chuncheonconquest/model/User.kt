package com.dldmswo1209.chuncheonconquest.model

import java.io.Serializable

data class User(
    val uid : String,
    val email: String,
    val pw: String,
    val name: String
): Serializable{
    constructor() : this("","","","")
}

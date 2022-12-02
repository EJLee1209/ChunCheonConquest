package com.dldmswo1209.chuncheonconquest.model

data class UserData(
    val conquerCount: Int,
    val information: UserInfo
){
    constructor() : this(0, UserInfo())
}
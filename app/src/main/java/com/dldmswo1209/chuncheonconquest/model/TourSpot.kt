package com.dldmswo1209.chuncheonconquest.model

data class TourSpot(
    val name: String,
    val imageUrl: String,
    val description: String,
    val latitude: Double,
    val longitude: Double
){
    constructor() : this("","","",0.0,0.0)
}
//
//val cafeList = mutableListOf<TourSpot>(
//    TourSpot(
//        "어스17",
//        "https://firebasestorage.googleapis.com/v0/b/chuncheonconquest.appspot.com/o/images%2Fcafe_earth17.png?alt=media&token=f5fba3a1-5d19-4256-a79b-6d7dcfce0351",
//        "earth17",
//        37.9326956,
//        127.793774
//    ),
//    TourSpot(
//        "카르페",
//        "https://firebasestorage.googleapis.com/v0/b/chuncheonconquest.appspot.com/o/images%2Fcafe_carpe.jpg?alt=media&token=22502a60-b211-411f-93c1-2e0810092c08",
//        "carpe",
//        37.8848897,
//        127.6839449
//    ),
//    TourSpot(
//        "GC 아뜰리에",
//        "https://firebasestorage.googleapis.com/v0/b/chuncheonconquest.appspot.com/o/images%2Fcafe_gcAtelier.jpg?alt=media&token=9e25de78-ff88-4e53-b797-bb05d083d58b",
//        "gcAtelier",
//        37.8929022,
//        127.7761023
//    ),
//    TourSpot(
//        "감자밭",
//        "https://firebasestorage.googleapis.com/v0/b/chuncheonconquest.appspot.com/o/images%2Fcafe_potatoField.jpg?alt=media&token=372d5646-aee0-4d64-a8a0-374129af4682",
//        "potatoField",
//        37.9295747,
//        127.784477
//    ),
//    TourSpot(
//        "산토리니",
//        "https://firebasestorage.googleapis.com/v0/b/chuncheonconquest.appspot.com/o/images%2Fcafe_santorini.png?alt=media&token=d7382f2a-7ae0-42bf-ad46-f110313243ea",
//        "santorini",
//        37.8914143,
//        127.7764909
//    ),
//    TourSpot(
//        "스타벅스(구봉산)",
//        "https://firebasestorage.googleapis.com/v0/b/chuncheonconquest.appspot.com/o/images%2Fcafe_startbucks.png?alt=media&token=81a2303e-bed8-4dad-865d-4e003648da66",
//        "starbucks",
//        37.8884158,
//        127.7759379
//    ),
//    TourSpot(
//        "카페 드 200볼트",
//        "https://firebasestorage.googleapis.com/v0/b/chuncheonconquest.appspot.com/o/images%2Fcafe_220volt.jpg?alt=media&token=74d788be-e1ee-43b1-ad8a-e06ffcad0880",
//        "220volt",
//        37.8569836,
//        127.7838962
//    ),
//    TourSpot(
//        "소울 로스터리",
//        "https://firebasestorage.googleapis.com/v0/b/chuncheonconquest.appspot.com/o/images%2FsoulRoastery.jpg?alt=media&token=d2935c59-3f2a-4e64-bf89-51035eb7687b",
//        "soulRoastery",
//        37.923845,
//        127.7651195
//    )
//)
//
//val TourList = mutableListOf<TourSpot>(
//    TourSpot(
//        "한림대학교",
//        "https://firebasestorage.googleapis.com/v0/b/chuncheonconquest.appspot.com/o/images%2Fhallym.jpg?alt=media&token=b8ce7c13-cc87-4cc7-9544-7fe3d56cc6c7",
//        "hallym",
//        37.8866303,
//        127.7353948
//    ),
//    TourSpot(
//        "제이드 가든",
//        "https://firebasestorage.googleapis.com/v0/b/chuncheonconquest.appspot.com/o/images%2FjadeGarden.jpg?alt=media&token=e8d2748b-32d9-477b-ac61-0fee80cfcce7",
//        "jadeGarden",
//        37.8325325,
//        127.5422982
//    ),
//    TourSpot(
//        "김유정역 레일 바이크",
//        "https://firebasestorage.googleapis.com/v0/b/chuncheonconquest.appspot.com/o/images%2FkimRailBike.jpg?alt=media&token=14e0a43d-3c28-4bbc-b087-0418c0bb2876",
//        "railBike",
//        37.8169376,
//        127.7133608
//    ),
//    TourSpot(
//        "레고랜드 코리아",
//        "https://firebasestorage.googleapis.com/v0/b/chuncheonconquest.appspot.com/o/images%2FlegoLand.jpg?alt=media&token=255fc091-0b51-4b08-8b67-5610fa32364a",
//        "legoLand",
//        37.8839056,
//        127.6969079
//    ),
//    TourSpot(
//        "남이섬",
//        "https://firebasestorage.googleapis.com/v0/b/chuncheonconquest.appspot.com/o/images%2Fnamisum.jpeg?alt=media&token=e5376941-701e-45e1-8757-dc45c26880dd",
//        "namisum",
//        37.7917117,
//        127.5255207
//    ),
//    TourSpot(
//        "쁘띠 프랑스",
//        "https://firebasestorage.googleapis.com/v0/b/chuncheonconquest.appspot.com/o/images%2FpetitFrance.jpg?alt=media&token=3e6fb087-bb90-4642-a6f9-9f4fb1bb1d05",
//        "petitFrance",
//        37.7148921,
//        127.4903236
//    ),
//    TourSpot(
//        "소양강, 소양댐",
//        "https://firebasestorage.googleapis.com/v0/b/chuncheonconquest.appspot.com/o/images%2Friver.jpg?alt=media&token=8b4a1c86-32f6-4e3d-ae06-aa79918d86d2",
//        "river",
//        37.945556,
//        127.814444
//    ),
//    TourSpot(
//        "엘리시안 강촌",
//        "https://firebasestorage.googleapis.com/v0/b/chuncheonconquest.appspot.com/o/images%2Fskiresort.jpg?alt=media&token=b8622ca1-09f6-4c47-82cd-e928a2c66daf",
//        "skiResort",
//        37.8163989,
//        127.587019
//    ),
//    TourSpot(
//        "소양강 스카이워크",
//        "https://firebasestorage.googleapis.com/v0/b/chuncheonconquest.appspot.com/o/images%2Fskywork.jpg?alt=media&token=280a29d3-2d0b-464d-95fe-a5b8e8d40db9",
//        "skywork",
//        37.8933383,
//        127.7234127
//    ),
//    TourSpot(
//        "의암호 스카이워크",
//        "https://firebasestorage.googleapis.com/v0/b/chuncheonconquest.appspot.com/o/images%2Fskywork2.jpg?alt=media&token=334205ea-70cb-4aad-a4c8-66bf4d43c11d",
//        "skywork2",
//        37.8509617,
//        127.6856533
//    ),
//    TourSpot(
//        "청평사",
//        "https://firebasestorage.googleapis.com/v0/b/chuncheonconquest.appspot.com/o/images%2FcheongPyeongSaTemple.jpg?alt=media&token=08e32a64-fed9-4dd2-8418-14861e7e33d8",
//        "cheongPyeongSaTemple",
//        37.9857683,
//        127.8085904
//    )
//)
//
//val restaurantList = mutableListOf<TourSpot>(
//    TourSpot(
//        "1.5 닭갈비",
//        "https://firebasestorage.googleapis.com/v0/b/chuncheonconquest.appspot.com/o/images%2Ffood_1.5.jpg?alt=media&token=dbf03837-0521-4589-9ec4-d1df17faff2e",
//        "food_15",
//        37.8763358,
//        127.7530769
//    ),
//    TourSpot(
//        "어쩌다 농부",
//        "https://firebasestorage.googleapis.com/v0/b/chuncheonconquest.appspot.com/o/images%2Ffood_farmer.jpg?alt=media&token=e66f0f3a-75cc-4579-8c15-017bb1032b97",
//        "food_farmer",
//        37.8773987,
//        127.7288471
//    ),
//    TourSpot(
//        "통나무집 숯불 닭갈비",
//        "https://firebasestorage.googleapis.com/v0/b/chuncheonconquest.appspot.com/o/images%2Ffood_logCabin.jpg?alt=media&token=3a39c8bc-52b4-4847-9357-4e4d8b05df8d",
//        "food_logCabin",
//        37.9302505,
//        127.7825219
//    ),
//    TourSpot(
//        "라운지05",
//        "https://firebasestorage.googleapis.com/v0/b/chuncheonconquest.appspot.com/o/images%2Ffood_lounge05.jpg?alt=media&token=f87263d3-58e7-4432-96bf-b4930e79e330",
//        "food_lounge05",
//        37.9679432,
//        127.5790956
//    ),
//    TourSpot(
//        "퇴계 막국수",
//        "https://firebasestorage.googleapis.com/v0/b/chuncheonconquest.appspot.com/o/images%2Ffood_toegyeMakguksu.jpg?alt=media&token=1550468e-baa5-4a15-af96-1c5e09e49215",
//        "food_toegyeMakguksu",
//        37.8622805,
//        127.7257079
//    ),
//    TourSpot(
//        "우성 닭갈비",
//        "https://firebasestorage.googleapis.com/v0/b/chuncheonconquest.appspot.com/o/images%2Ffood_useongChickenRibs.jpg?alt=media&token=4681d687-7976-4b53-b957-d92dfa2997e8",
//        "food_useongChickenRibs",
//        37.887306,
//        127.7634732
//    ),
//
//)
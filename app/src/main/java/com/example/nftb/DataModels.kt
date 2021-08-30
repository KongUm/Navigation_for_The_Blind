package com.example.nftb

data class PostModel(
    var startX : String? =null ,
    var startY : String?=null,
    var endX : String? =null,
    var endY : String? = null
)

data class Coordinate(
    var x : String? = null,
    var y : String? = null
)
data class HTTP_GET_Model(
    var x : String? =null ,
    var y : String? =null
)

data class PostResult(
    var result:String? = null
)
data class PostResult_2(
    var result:String? = null
)
package com.example.demo.api.request

data class Phone(
        var id: Long,
        var name: String,
        var detailPhone: DetailPhone
){
    data class DetailPhone (
            var color: String,
            var os: String
    )
}

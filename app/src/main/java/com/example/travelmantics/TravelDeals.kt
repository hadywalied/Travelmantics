package com.example.travelmantics

import java.io.Serializable

data class TravelDeals(
    var title: String,
    var description: String,
    var price: String,
    var imageUrl: String,
    var imageName: String
) : Serializable {
    var id: String = ""

    constructor() : this("", "", "", "","")

}
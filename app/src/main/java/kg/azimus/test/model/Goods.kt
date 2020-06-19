package kg.azimus.test.model

import com.google.gson.annotations.SerializedName

data class Goods(
    @SerializedName("name") val name : String,
    @SerializedName("price") val price : Int,
    @SerializedName("desc") val desc : String,
    @SerializedName("company") val company : String,
    @SerializedName("category") val category : String,
    @SerializedName("id") val id : String,
    @SerializedName("img") val img : String
)

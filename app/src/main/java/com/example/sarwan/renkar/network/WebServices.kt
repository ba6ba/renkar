package com.example.sarwan.renkar.network

import com.example.sarwan.renkar.model.HERE.Suggesstion
import com.example.sarwan.renkar.model.MAPBOX.Addresses
import com.example.sarwan.renkar.model.TPL.Locations
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface WebServices {

    @GET(NetworkConstants.HERE_API_VERSION+ApiEndPoints.SUGGEST)
    fun getSuggesstions(@QueryMap suggest : Suggesstion): Call<ArrayList<String>>

    @GET(NetworkConstants.MAP_BOX_API_VERSION+ApiEndPoints.PLACES)
    fun getLocations(@Path("query") query: String , @Query("countryCode") countryCode: String = "PK",
                     @Query("limit") limit: Int = 6, @Query("access_token") access_token: String = NetworkConstants.MAP_BOX_ACCESS_TOKEN)
            : Call<Addresses>


    @GET(ApiEndPoints.SEARCH)
    fun getAddresses(@Query("name") name: String,
                     @Query("apikey") apikey: String = NetworkConstants.TPL_API_KEY,
                     @Query("limit") limit: Int = 6,
                     @Query("city") city: String = "Karachi", @Query("output") output: String = NetworkConstants.OUTPUT_KEYS)
            : Call<ArrayList<Locations>>
}

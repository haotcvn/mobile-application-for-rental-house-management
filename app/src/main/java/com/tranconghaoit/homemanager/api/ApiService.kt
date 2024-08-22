package com.tranconghaoit.homemanager.api


import com.tranconghaoit.homemanager.models.BillModel
import com.tranconghaoit.homemanager.models.BuildingModel
import com.tranconghaoit.homemanager.models.ContractDetailedModel
import com.tranconghaoit.homemanager.models.ContractModel
import com.tranconghaoit.homemanager.models.CounterModel
import com.tranconghaoit.homemanager.models.ProvinceModel
import com.tranconghaoit.homemanager.models.RenterModel
import com.tranconghaoit.homemanager.models.RevenueModel
import com.tranconghaoit.homemanager.models.RoomModel
import com.tranconghaoit.homemanager.models.RoomRentedModel
import com.tranconghaoit.homemanager.models.RoomRenterModel
import com.tranconghaoit.homemanager.models.UserModel
import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("register.php")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("numberPhone") numberPhone: String,
        @Field("password") password: String
    ): Observable<ResponseBody>

    @FormUrlEncoded
    @POST("login.php")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Observable<UserModel>

    @FormUrlEncoded
    @POST("changePassword.php")
    fun changePassword(
        @Field("userID") userID: Int,
        @Field("passwordOld") passwordOld: String,
        @Field("passwordNew") passwordNew: String
    ): Observable<ResponseBody>

    @GET("getDistrict.php")
    fun getDistrict(@Query("provinceID") provinceID: String): Observable<List<ProvinceModel>>

    @GET("getProvince.php")
    fun getProvince(): Observable<List<ProvinceModel>>

    @GET("getUser.php")
    fun getUser(@Query("userID") userID: Int): Observable<UserModel>

    @GET("counter.php")
    fun counter(@Query("userID") userID: Int): Observable<CounterModel>

    @GET("getRevenue.php")
    fun getRevenue(@Query("userID") userID: Int): Observable<List<RevenueModel>>

    @GET("getHome.php")
    fun getHome(@Query("userID") userID: Int): Observable<List<BuildingModel>>

    @GET("getHomeDetailed.php")
    fun getHomeDetailed(@Query("homeID") homeID: Int): Observable<BuildingModel>

    @POST("addHome.php")
    fun addHome(@Body home: BuildingModel): Observable<ResponseBody>

    @POST("updateHome.php")
    fun updateHome(@Body home: BuildingModel): Observable<ResponseBody>

    @FormUrlEncoded
    @POST("deleteHome.php")
    fun deleteHome(@Field("homeID") homeID: Int): Observable<ResponseBody>

    @GET("getRoom.php")
    fun getRoom(@Query("homeID") homeID: Int): Observable<List<RoomModel>>

    @GET("getRoomDetailed.php")
    fun getRoomDetailed(@Query("roomID") roomID: Int): Observable<RoomModel>

    @GET("getRoomRented.php")
    fun getRoomRented(): Observable<List<RoomRentedModel>>

    @POST("addRoom.php")
    fun addRoom(@Body room: RoomModel): Observable<ResponseBody>

    @POST("updateRoom.php")
    fun updateRoom(@Body room: RoomModel): Observable<ResponseBody>

    @FormUrlEncoded
    @POST("deleteRoom.php")
    fun deleteRoom(@Field("roomID") roomID: Int): Observable<ResponseBody>

    @GET("getRenterInRoom.php")
    fun getRenterInRoom(@Query("roomID") roomID: Int): Observable<List<RoomRenterModel>>

    @GET("getRenterInContract.php")
    fun getRenterInContract(@Query("renterID") renterID: Int): Observable<RenterModel>

    @GET("getRenterHasRoom.php")
    fun getRenterHasRoom(@Query("userID") userID: Int): Observable<List<RoomRenterModel>>

    @GET("getRenterNoRoom.php")
    fun getRenterNoRoom(@Query("userID") userID: Int): Observable<List<RoomRenterModel>>

    @GET("getRenterDetailed.php")
    fun getRenterDetailed(@Query("renterID") renterID: Int): Observable<RenterModel>

    @POST("addRenter.php")
    fun addRenter(@Body renter: RenterModel): Observable<ResponseBody>

    @POST("updateRenter.php")
    fun updateRenter(@Body renter: RenterModel): Observable<ResponseBody>

    @FormUrlEncoded
    @POST("deleteRenter.php")
    fun deleteRenter(@Field("renterID") renterID: Int): Observable<ResponseBody>

    @POST("addContract.php")
    fun addContract(@Body contract: ContractModel): Observable<ResponseBody>

    @GET("getContract.php")
    fun getContract(@Query("userID") userID: Int): Observable<List<ContractDetailedModel>>

    @GET("getContractDetailed.php")
    fun getContractDetailed(@Query("contractID") contractID: Int, @Query("roomID") roomID: Int): Observable<ContractDetailedModel>

    @FormUrlEncoded
    @POST("deleteContract.php")
    fun deleteContract(@Field("roomID") roomID: Int): Observable<ResponseBody>

    @POST("updateContract.php")
    fun updateContract(@Body contract: ContractModel): Observable<ResponseBody>

    @POST("addBill.php")
    fun addBill(@Body bill: BillModel): Observable<ResponseBody>

    @GET("getBill.php")
    fun getBill(@Query("userID") userID: Int): Observable<List<BillModel>>

    @GET("getBillUnPaid.php")
    fun getBillUnPaid(@Query("userID") userID: Int): Observable<List<BillModel>>

    @GET("getBillPaid.php")
    fun getBillPaid(@Query("userID") userID: Int): Observable<List<BillModel>>

    @FormUrlEncoded
    @POST("deleteBill.php")
    fun deleteBill(@Field("billID") billID: Int): Observable<ResponseBody>

    @GET("getBillDetailed.php")
    fun getBillDetailed(@Query("billID") billID: Int): Observable<BillModel>

    @FormUrlEncoded
    @POST("updateBillStatus.php")
    fun updateBillStatus(@Field ("billID") billID: Int): Observable<ResponseBody>
}
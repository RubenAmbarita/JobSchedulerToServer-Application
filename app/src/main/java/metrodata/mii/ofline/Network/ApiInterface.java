package metrodata.mii.ofline.Network;

import metrodata.mii.ofline.Model.ResponseName;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("send.php")
    Call<ResponseName> insertName(@Field("name") String nama);

    //interface ofline
    @GET("get.php")
    Call<ResponseName> getdata();
}

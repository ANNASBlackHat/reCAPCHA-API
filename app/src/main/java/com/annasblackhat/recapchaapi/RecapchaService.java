package com.annasblackhat.recapchaapi;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Mia Khalifa on 13/07/2017.
 */

public interface RecapchaService {

    @FormUrlEncoded
    @POST("recaptcha/api/siteverify")
    Call<Recapcha> verify(@Field("secret") String secretKey, @Field("response") String token);

}

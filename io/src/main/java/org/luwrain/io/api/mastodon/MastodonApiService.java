/*
   Copyright 2024-2025 Michael Pozhidaev <msp@luwrain.org>
   Copyright 2024 Stepan Bylkov <scroogemcfawk@gmail.com>
   This file is part of LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.io.api.mastodon;

import org.luwrain.io.api.mastodon.entity.*;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface MastodonApiService
{
    @POST("/api/v1/apps")
    Call<Application> registerApp(@Body ApplicationRegisterRequestBody body);

    @POST("/oauth/token")
    Call<Token> getToken(@Body ApplicationGetTokenRequestBody body);

    @GET("/api/v1/apps/verify_credentials")
    Call<Application> verifyApp(@Header("Authorization") String token);

    @POST("/api/v1/accounts")
    Call<Token> registerAccount(@Header("Authorization") String token, @Body AccountRegisterRequestBody body);

    @GET("/api/v1/accounts/verify_credentials")
    Call<ResponseBody> verifyAccount(@Header("Authorization") String token);
}

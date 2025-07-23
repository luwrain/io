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

package org.luwrain.io.api.mastodon.entity;

import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.*;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationGetTokenRequestBody
{
    @SerializedName("client_id")
    String clientId;

    @SerializedName("client_secret")
    String clientSecret;

    @SerializedName("redirect_uri")
    String redirectUri;

    @SerializedName("grant_type")
    String grantType;

    @SerializedName("scope")
    String scope;

    public ApplicationGetTokenRequestBody(String clientId, String clientSecret)
    {
        this(clientId, clientSecret, "urn:ietf:wg:oauth:2.0:oob", "client_credentials", "read write push");
    }
}

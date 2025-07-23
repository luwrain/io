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
import lombok.AllArgsConstructor;
import lombok.Data;

import java.security.Timestamp;

@Data
@AllArgsConstructor
public class Token
{
    @SerializedName("access_token")
    String accessToken;

    @SerializedName("token_type")
    String tokenType;

    @SerializedName("scope")
    String scope;

    @SerializedName("created_at")
    Integer createdAt;
}

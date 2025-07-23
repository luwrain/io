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

import com.google.gson.annotations.*;
import lombok.*;

@Data
@AllArgsConstructor()
public class AccountRegisterRequestBody
{
    @SerializedName("username")
    private String username;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("agreement")
    private Boolean agreement;

    @SerializedName("locale")
    private String locale;

    @SerializedName("reason")
    private String reason;
}

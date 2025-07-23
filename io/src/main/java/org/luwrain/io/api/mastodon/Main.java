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

import java.awt.image.PackedColorModel;
import java.io.IOException;


public class Main
{
    public static void main(String[] args) throws IOException
    {
        var config = new Configuration();
        var app = new ApplicationClient(config);
	//        app.writeApp();

        try {

//            app.print();

//            var accountToken = app.registerAccount("hehehaha", "havasege@pelagius.net", "pass1234", true, "", "");
            var accountToken = app.accountToken;
            if (accountToken != null) {
                System.out.println(app.verifyAccount(null));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        config.close();
    }
}

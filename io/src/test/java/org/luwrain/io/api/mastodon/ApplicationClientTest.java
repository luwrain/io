
package org.luwrain.io.api.mastodon;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Scanner;



class ApplicationClientTest
{
    public static ApplicationClient client;

    @BeforeAll
    public static void setupClient() throws IOException
    {
        // проверка корректности регистрации клиента и получения токена для авторизации
        client = new ApplicationClient(new Configuration());
        assertTrue(client.isValid());
    }

    @Disabled @Test
    public void registerAccount() throws IOException
    {
        var token = client.registerAccount("supercoolusername", "your@email.com", "password1234", true, "en", "");
        assertNotNull(token);
        System.out.println("Check your email for account confirmation letter.");
    }

    @Disabled @Test
    public void verifyAccount() throws IOException
    {
        assertTrue(client.verifyAccount(null));
    }


}

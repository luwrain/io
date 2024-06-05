package org.luwrain.io.api.yandex_disk.exceptions;

public class UnauthorizedException extends Exception{
    public UnauthorizedException(){
        super("Unauthorized");
    }

    public UnauthorizedException(String message) {
        super(message);
    }
}

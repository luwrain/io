package org.luwrain.io.api.yandex_disk.exceptions;

public class UnavailableException extends Exception {
    public UnavailableException() {
        super("The service is temporarily unavailable");
    }

    public UnavailableException(String message) {
        super(message);
    }
}

package org.luwrain.io.api.yandex_disk.exceptions;

public class TooManyRequestsException extends Exception {
    public TooManyRequestsException() {
        super("There are too many requests");
    }

    public TooManyRequestsException(String message) {
        super(message);
    }
}

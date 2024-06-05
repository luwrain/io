package org.luwrain.io.api.yandex_disk.exceptions;

public class FileIsTooBigException extends Exception {
    public FileIsTooBigException() {
        super("File is too big");
    }

    public FileIsTooBigException(String message) {
        super(message);
    }
}

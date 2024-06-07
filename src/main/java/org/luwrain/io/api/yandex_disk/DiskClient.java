package org.luwrain.io.api.yandex_disk;

import org.luwrain.io.api.yandex_disk.exceptions.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface DiskClient {
    void authorize(String token);

    InputStream download(String link) throws FileNotFoundException, IOException, FileIsTooBigException, TooManyRequestsException, UnavailableException;

    void cancelDownload(InputStream in);

    String upload(InputStream in, String filepath) throws IOException, UnauthorizedException, FileIsTooBigException, TooManyRequestsException, UnavailableException;

    void cancelUpload(InputStream in);
}

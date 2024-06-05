package org.luwrain.io.api.yandex_disk;

import ru.vorotov.yandex_disk_api_client_lib.exceptions.FileIsTooBigException;
import ru.vorotov.yandex_disk_api_client_lib.exceptions.TooManyRequestsException;
import ru.vorotov.yandex_disk_api_client_lib.exceptions.UnauthorizedException;
import ru.vorotov.yandex_disk_api_client_lib.exceptions.UnavailableException;

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

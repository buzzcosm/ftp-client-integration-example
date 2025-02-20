package com.buzzcosm.ftpclient.integration.example.ftp.new_architecture;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public interface FtpClientService {
    void connect() throws IOException;
    void disconnect() throws IOException;
    ByteArrayOutputStream downloadFile(String remotePath) throws IOException;
}

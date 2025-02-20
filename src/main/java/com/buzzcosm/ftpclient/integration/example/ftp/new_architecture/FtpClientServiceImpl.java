package com.buzzcosm.ftpclient.integration.example.ftp.new_architecture;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Log4j2
public class FtpClientServiceImpl implements FtpClientService {

    private final FTPClient ftpClient;
    private final String host;
    private final int port;
    private final String username;
    private final String password;

    public FtpClientServiceImpl(FTPClient ftpClient, String host, int port, String username, String password) {
        this.ftpClient = ftpClient;
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    @Override
    public void connect() throws IOException {
        ftpClient.setBufferSize(16 * 1024);
        ftpClient.setControlEncoding("UTF-8");

        ftpClient.connect(host, port);
        int reply = ftpClient.getReplyCode();

        if (!FTPReply.isPositiveCompletion(reply)) {
            ftpClient.disconnect();
            throw new IOException("FTP server refused connection");
        }

        if (!ftpClient.login(username, password)) {
            ftpClient.disconnect();
            throw new IOException("FTP login failed");
        }

        ftpClient.enterLocalPassiveMode();
        log.info("Connected to FTP server: {}", host);
    }

    @Override
    public ByteArrayOutputStream downloadFile(String remotePath) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if (!ftpClient.retrieveFile(remotePath, outputStream)) {
            throw new IOException("Failed to download file: " + remotePath);
        }
        log.info("File {} downloaded successfully", remotePath);
        return outputStream;
    }

    @Override
    public void disconnect() throws IOException {
        if (ftpClient.isConnected()) {
            ftpClient.logout();
            ftpClient.disconnect();
            log.info("Disconnected from FTP server");
        }
    }
}

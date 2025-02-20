package com.buzzcosm.ftpclient.integration.example.ftp.new_architecture;

import org.apache.commons.net.ftp.FTPClient;

public class FtpClientBuilder {
    private final FTPClient ftpClient;
    private String host;
    private int port = 21; // Default
    private String username;
    private String password;

    private FtpClientBuilder() {
        this.ftpClient = new FTPClient();
    }

    public static FtpClientBuilder builder() {
        return new FtpClientBuilder();
    }

    public FtpClientBuilder host(String host) {
        this.host = host;
        return this;
    }

    public FtpClientBuilder port(int port) {
        this.port = port;
        return this;
    }

    public FtpClientBuilder credentials(String username, String password) {
        this.username = username;
        this.password = password;
        return this;
    }

    public FtpClientServiceImpl build() {
        return new FtpClientServiceImpl(ftpClient, host, port, username, password);
    }
}

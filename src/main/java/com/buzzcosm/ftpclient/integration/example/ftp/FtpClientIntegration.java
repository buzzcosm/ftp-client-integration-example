package com.buzzcosm.ftpclient.integration.example.ftp;

import com.buzzcosm.ftpclient.integration.example.model.FtpClientConnectionProperties;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.io.PrintWriter;

public final class FtpClientIntegration {

    private FTPClient ftp;
    private FtpClientConnectionProperties props;

    public FtpClientIntegration(FtpClientConnectionProperties ftpConnectionProperties) {
        this.props = ftpConnectionProperties;
        this.ftp = new FTPClient();
    }

    public FTPClient getFTPClient() {
        return ftp;
    }

    void open() throws IOException {
        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

        ftp.connect(props.getHost(), props.getPort());
        int reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            throw new IOException("Exception in connecting to FTP Server");
        }

        ftp.login(props.getUsername(), props.getPassword());
    }

    void close() throws IOException {
        ftp.disconnect();
    }
}

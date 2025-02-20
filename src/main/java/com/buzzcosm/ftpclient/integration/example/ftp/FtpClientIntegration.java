package com.buzzcosm.ftpclient.integration.example.ftp;

import com.buzzcosm.ftpclient.integration.example.exception.FtpConnectionException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.net.ProtocolCommandEvent;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;

@Log4j2
public final class FtpClientIntegration {

    private static final String FTP_CONNECTION_FAILED = "Error connecting to the FTP server";
    private static final String FTP_LOGIN_FAILED = "Error logging on to the FTP server";
    private static final String FTP_DISCONNECTION_FAILED = "Error when closing the FTP connection";

    private FTPClient ftpClient;
    private FtpClientConnectionProperties props;

    public FtpClientIntegration(FtpClientConnectionProperties ftpConnectionProperties) {
        this.props = ftpConnectionProperties;
        this.ftpClient = new FTPClient();
    }

    public FTPClient open() throws FtpConnectionException {
        if (props == null) {
            throw new FtpConnectionException("Properties cannot be null");
        }

        String host = props.getHost();
        if (host == null || host.isEmpty()) {
            throw new FtpConnectionException("Host Address cannot be null or empty");
        }

        int port = props.getPort();
        if (port <= 0) {
            port = 21; // set default port
        }

        String username = props.getUsername();
        if (username == null || username.isEmpty()) {
            throw new FtpConnectionException("Username cannot be null or empty");
        }

        String password = props.getPassword();
        if (password == null || password.isEmpty()) {
            throw new FtpConnectionException("Password cannot be null or empty");
        }

        try {
            ftpClient.setBufferSize(16 * 1024);
            ftpClient.setControlEncoding("UTF-8");

            ftpClient.connect(host, port);

            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                throw new FtpConnectionException(FTP_CONNECTION_FAILED);
            }

            if (!ftpClient.login(username, password)) {
                throw new FtpConnectionException(FTP_LOGIN_FAILED);
            }

            ftpClient.addProtocolCommandListener(new ProtocolCommandListener() {
                @Override
                public void protocolCommandSent(ProtocolCommandEvent event) {
                    log.debug(getFTPCommandMessage(event));
                }

                @Override
                public void protocolReplyReceived(ProtocolCommandEvent event) {
                    log.debug(getFTPReplyMessage(event));
                }
            });

        } catch (IOException e) {
            throw new FtpConnectionException(FTP_CONNECTION_FAILED, e);
        }

        return ftpClient;
    }

    public void close() throws FtpConnectionException {
        try {
            if (ftpClient != null && ftpClient.isConnected()) {
                ftpClient.logout();
                int replyCode = ftpClient.getReplyCode();
                if (!FTPReply.isPositiveCompletion(replyCode)) {
                    throw new FtpConnectionException(FTP_DISCONNECTION_FAILED, replyCode, ftpClient.getReplyString());
                }
                ftpClient.disconnect();
            }
        } catch (IOException e) {
            throw new FtpConnectionException(FTP_DISCONNECTION_FAILED, e);
        }
    }

    private String getFTPCommandMessage(ProtocolCommandEvent event) {
        String command = event.getCommand();
        String message = event.getMessage();

        if ("PASS".equalsIgnoreCase(command)) {
            message = "PASS ********";
        }

        if ("USER".equalsIgnoreCase(command)) {
            message = message.replaceAll("USER\\s+\\S+", "USER [REDACTED]");
        }

        String commandMessage = String.format("[{}][{}] Command sent: [{}] - {}", Thread.currentThread().getName(), System.currentTimeMillis(), command, message.trim());

        return commandMessage;
    }

    private String getFTPReplyMessage(ProtocolCommandEvent event) {
        String reply = event.getMessage();
        reply = reply.replaceAll("User\\s+\\S+", "User [REDACTED]");

        String replyMessage = String.format("[{}][{}] Reply received: {}", Thread.currentThread().getName(), System.currentTimeMillis(), reply.trim());

        return replyMessage;
    }
}

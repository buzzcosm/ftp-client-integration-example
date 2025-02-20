package com.buzzcosm.ftpclient.integration.example.exception;

public class FtpConnectionException extends Exception {
    private static final long serialVersionUID = 1L;
    private int ftpStatusCode;
    private String ftpReply;

    public FtpConnectionException(String message) {
        super(message);
    }

    public FtpConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public FtpConnectionException(Throwable cause) {
        super(cause);
    }

    public FtpConnectionException(String message, int ftpStatusCode, String ftpReply) {
        super(message);
        this.ftpStatusCode = ftpStatusCode;
        this.ftpReply = ftpReply;
    }

    public int getFtpStatusCode() {
        return ftpStatusCode;
    }

    public String getFtpReply() {
        return ftpReply;
    }
}

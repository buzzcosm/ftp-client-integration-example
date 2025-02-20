package com.buzzcosm.ftpclient.integration.example.util;

import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Utility class for processing files on FTP Server
 */
public final class FtpFileProcess {

    /**
     * Uploads a file from the local file system to the FTP server.
     *
     * @param ftpClient The FTP client instance used for the connection to the FTP server.
     * @param file The file to be uploaded to the FTP server.
     * @param path The path where the file will be saved on the FTP server.
     * @throws IOException If an error occurs during the upload of the file.
     */
    public static void putFileToPath(FTPClient ftpClient, File file, String path) throws IOException {
        ftpClient.storeFile(path, new FileInputStream(file));
    }

    /**
     * Downloads a file from the FTP server and saves it to the local file system.
     *
     * @param ftpClient The FTP client instance used for the connection to the FTP server.
     * @param source The path of the file on the FTP server.
     * @param destination The path where the file will be saved on the local file system.
     * @throws IOException If an error occurs during the download or saving of the file.
     */
    public static void downloadFile(FTPClient ftpClient, String source, String destination) throws IOException {
        FileOutputStream out = new FileOutputStream(destination);
        ftpClient.retrieveFile(source, out);
    }
}

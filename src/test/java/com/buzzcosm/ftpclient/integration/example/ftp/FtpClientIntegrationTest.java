package com.buzzcosm.ftpclient.integration.example.ftp;

import com.buzzcosm.ftpclient.integration.example.model.FtpClientConnectionProperties;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class FtpClientIntegrationTest {

    private FakeFtpServer fakeFtpServer;
    FtpClientConnectionProperties props;
    private FtpClientIntegration ftpClientIntegration;
    private FTPClient ftpClient;

    @BeforeEach
    void setUp() throws IOException {
        fakeFtpServer = new FakeFtpServer();
        fakeFtpServer.addUserAccount(new UserAccount("user", "password", "/data"));

        FileSystem fileSystem = new UnixFakeFileSystem();
        fileSystem.add(new DirectoryEntry("/data"));
        fileSystem.add(new FileEntry("/data/foobar.txt", "Lorem ipsum dolor sit amet"));
        fakeFtpServer.setFileSystem(fileSystem);
        fakeFtpServer.setServerControlPort(0);

        fakeFtpServer.start();

        props = new FtpClientConnectionProperties();
        props.setHost("localhost");
        props.setPort(fakeFtpServer.getServerControlPort());
        props.setUsername("user");
        props.setPassword("password");

        ftpClientIntegration = new FtpClientIntegration(props);
        ftpClient = ftpClientIntegration.getFTPClient();
        ftpClientIntegration.open();
    }

    @AfterEach
    void tearDown() throws IOException {
        ftpClientIntegration.close();
        fakeFtpServer.stop();
    }

    @DisplayName("Given remote file when listing remote files then it is contained in list")
    @Test
    void givenRemoteFile_whenListingRemoteFiles_thenItIsContainedInList() throws IOException {
        FTPFile[] filesArray = ftpClient.listFiles("");
        Collection<String> files = Arrays.stream(filesArray)
                .map(FTPFile::getName)
                .collect(Collectors.toList());
        assertThat(files).contains("foobar.txt");
    }

    @DisplayName("Given remote file when downloading it then it is on the local filesystem")
    @Test
    public void givenRemoteFile_whenDownloading_thenItIsOnTheLocalFilesystem() throws IOException {
        downloadFile("/buz.txt", "downloaded_buz.txt");
        assertThat(new File("downloaded_buz.txt")).exists();
        new File("downloaded_buz.txt").delete(); // cleanup
    }

    @DisplayName("Given local file when uploading it then it exists on remote location")
    @Test
    void givenLocalFile_whenUploadingIt_thenItExistsOnRemoteLocation() throws URISyntaxException, IOException {
        File file = new File(getClass().getClassLoader().getResource("test-data/ftp-tests/baz.txt").toURI());
        putFileToPath(file, "/buz.txt");
        assertThat(fakeFtpServer.getFileSystem().exists("/buz.txt")).isTrue();
    }

    /**
     * Helper method to download file
     * @param source
     * @param destination
     * @throws IOException
     */
    void downloadFile(String source, String destination) throws IOException {
        FileOutputStream out = new FileOutputStream(destination);
        ftpClient.retrieveFile(source, out);
    }

    /**
     * Helper method to store file
     * @param file
     * @param path
     * @throws IOException
     */
    void putFileToPath(File file, String path) throws IOException {
        ftpClient.storeFile(path, new FileInputStream(file));
    }
}
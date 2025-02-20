package com.buzzcosm.ftpclient.integration.example.ftp;

import com.buzzcosm.ftpclient.integration.example.util.FtpFileProcess;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.junit.jupiter.api.*;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FtpClientIntegrationTest {

    private static FakeFtpServer fakeFtpServer;
    private static FtpClientConnectionProperties props;
    private static FtpClientIntegration ftpClientIntegration;
    private static FTPClient ftpClient;

    @BeforeAll
    static void setUp() throws Exception {
        if (fakeFtpServer == null) {
            fakeFtpServer = new FakeFtpServer();
            fakeFtpServer.addUserAccount(new UserAccount("user", "password", "/data"));

            FileSystem fileSystem = new UnixFakeFileSystem();
            fileSystem.add(new DirectoryEntry("/data"));
            fileSystem.add(new FileEntry("/data/foobar.txt", "Lorem ipsum dolor sit amet"));
            fakeFtpServer.setFileSystem(fileSystem);
            fakeFtpServer.setServerControlPort(0);
            fakeFtpServer.start();
        }

        props = new FtpClientConnectionProperties();
        props.setHost("localhost");
        props.setPort(fakeFtpServer.getServerControlPort());
        props.setUsername("user");
        props.setPassword("password");

        ftpClientIntegration = new FtpClientIntegration(props);
        ftpClient = ftpClientIntegration.open();
    }

    @AfterAll
    static void tearDown() throws Exception {
        ftpClientIntegration.close();
        fakeFtpServer.stop();
    }

    @DisplayName("FILES: Given remote file when listing remote files then it is contained in list")
    @Test
    @Order(1)
    void givenRemoteFile_whenListingRemoteFiles_thenItIsContainedInList() throws IOException {
        FTPFile[] filesArray = ftpClient.listFiles("");
        Collection<String> files = Arrays.stream(filesArray)
                .map(FTPFile::getName)
                .collect(Collectors.toList());
        assertThat(files).contains("foobar.txt");
    }

    @DisplayName("UPLOAD: Given local file when uploading it then it exists on remote location")
    @Test
    @Order(2)
    void givenLocalFile_whenUploadingIt_thenItExistsOnRemoteLocation() throws URISyntaxException, IOException {
        File file = new File(getClass().getClassLoader().getResource("test-data/baz.txt").toURI());
        FtpFileProcess.putFileToPath(ftpClient, file, "/buz.txt");
        assertThat(fakeFtpServer.getFileSystem().exists("/buz.txt")).isTrue();
    }

    @DisplayName("DOWNLOAD: Given remote file when downloading it then it is on the local filesystem")
    @Test
    @Order(3)
    public void givenRemoteFile_whenDownloading_thenItIsOnTheLocalFilesystem() throws IOException {
        FtpFileProcess.downloadFile(ftpClient, "/buz.txt", "downloaded_buz.txt");
        assertThat(new File("downloaded_buz.txt")).exists();
        new File("downloaded_buz.txt").delete(); // cleanup
    }
}
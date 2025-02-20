package com.buzzcosm.ftpclient.integration.example.ftp.new_architecture;

import org.junit.jupiter.api.*;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;

class FtpClientServiceImplTest {

    private static FakeFtpServer fakeFtpServer;
    private static FtpClientService ftpClient;

    @BeforeAll
    static void setUp() throws Exception {
        if (fakeFtpServer == null) {
            fakeFtpServer = new FakeFtpServer();
            fakeFtpServer.addUserAccount(new UserAccount("user", "password", "/data"));

            // test file setup
            File foobarFile = new File(FtpClientServiceImplTest.class.getClassLoader().getResource("test-data/foobar.txt").toURI());
            //String fileContent = new String(Files.readAllBytes(foobarFile.toPath()));
            String fileContent = Files.readString(foobarFile.toPath());

            FileSystem fileSystem = new UnixFakeFileSystem();
            fileSystem.add(new DirectoryEntry("/data"));
            fileSystem.add(new FileEntry("/data/foobar.txt", fileContent));
            fakeFtpServer.setFileSystem(fileSystem);
            fakeFtpServer.setServerControlPort(0);
            fakeFtpServer.start();
        }

        int port = fakeFtpServer.getServerControlPort();
        ftpClient = FtpClientBuilder.builder()
                .host("localhost")
                .port(port)
                .credentials("user", "password")
                .build();

        ftpClient.connect();
    }

    @AfterAll
    static void tearDown() throws Exception {
        ftpClient.disconnect();
        fakeFtpServer.stop();
    }

    @DisplayName("DOWNLOAD: Given remote file when downloading it then it is on the local filesystem")
    @Test
    public void givenRemoteFile_whenDownloading_thenItIsAsByteArrayOutputStreamIsGreaterThanZero() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = ftpClient.downloadFile("/data/foobar.txt");
        assertThat(byteArrayOutputStream.size()).isGreaterThan(0);
    }
}
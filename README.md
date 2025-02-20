# Apache FTPClient Integration Example

Examples:
- [Baeldung: Implementing a FTP-Client in Java](https://www.baeldung.com/java-ftp-client)

### Unit-Tests

FTP-Client: `src/test/java/com/buzzcosm/ftpclient/integration/example/ftp/*`

### Straightforward Implementation

FTP-Client: `FtpClientIntegration.java`

### Interface Implementation

> better architecture

FTP-Client: `FtpClientServiceImpl.java`

Use of the FTP client in a process scenario:
```java
public class Main {
    public static void main(String[] args) {
        FtpClientService ftpClient = FtpClientBuilder.builder()
                .host("ftp.example.com")
                .port(21)
                .credentials("user", "password")
                .build();

        try {
            ftpClient.connect();

            // download files
            ByteArrayOutputStream file1 = ftpClient.downloadFile("/file1.txt");
            ByteArrayOutputStream file2 = ftpClient.downloadFile("/file2.txt");

            // processing
            processFile(new ByteArrayInputStream(file1.toByteArray()));
            processFile(new ByteArrayInputStream(file2.toByteArray()));

            ftpClient.disconnect();
        } catch (IOException e) {
            log.error("FTP Error", e);
        }
    }

    public static void processFile(InputStream isFile) throws IOException {
        // ...
    }
}
```


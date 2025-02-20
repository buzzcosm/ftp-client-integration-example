package com.buzzcosm.ftpclient.integration.example.ftp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public final class FtpClientConnectionProperties {
    private String host;        // connection host
    private int port;           // connection port (standard: 21)
    private String username;    // credentials
    private String password;    // credentials
}

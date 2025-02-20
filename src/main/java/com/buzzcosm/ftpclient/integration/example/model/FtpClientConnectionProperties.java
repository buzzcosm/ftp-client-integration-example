package com.buzzcosm.ftpclient.integration.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FtpClientConnectionProperties {
    private String host;
    private int port;
    private String username;
    private String password;
}

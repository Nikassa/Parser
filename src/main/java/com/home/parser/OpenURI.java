package com.home.parser;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class OpenURI {

    public OpenURI() throws URISyntaxException {
    }

    public static void open(String uri) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(URI.create(uri));
            } catch (IOException e) {
                System.out.println(e);
            }
        } else {
            System.out.println("!Desktop.isDesktopSupported()");
        }
    }
}


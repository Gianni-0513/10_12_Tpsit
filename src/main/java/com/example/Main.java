package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(3000);

        while (true) {
            Socket s = ss.accept();

            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            DataOutputStream out = new DataOutputStream(s.getOutputStream());

            String firstLine = in.readLine();
            System.out.println(firstLine);
            String[] request = firstLine.split(" ");

            String method = request[0];
            String resourse = request[1];
            String version = request[2];

            String header;
            do {
                header = in.readLine();
                System.out.println(header);
            } while (!header.isEmpty());
            System.out.println("Richiesta terminata");

            File file = new File("htdocs" + resourse);
            InputStream input = new FileInputStream(file);

            if (file.exists()) {
                /*
                 * File file = new File("htdocs/index.html");
                 * InputStream input = new FileInputStream(file);
                 */

                out.writeBytes("HTTP/1.1 200 OK\n");
                out.writeBytes("Content-Length: " + file.length() + "\n");
                out.writeBytes("Content-Type: " + getContentType(file) + "\n");
                out.writeBytes("\n");
                byte[] buf = new byte[8192];
                int n;
                while ((n = input.read(buf)) != -1) {
                    out.write(buf, 0, n);
                }

                input.close();

            } else {
                out.writeBytes("HTTP/1.1 404 Not found\n");
                out.writeBytes("Content-Length: 0\n");
                out.writeBytes("\n");
            }

            s.close();
        }
    }

    private static String getContentType(File f) {
        String[] s = f.getName().split("\\.");
        String ext = s[s.length - 1];

        switch (ext) {
            case "htm":
            case "html":
                return "txt/html";
        }

        return null;
    }
}
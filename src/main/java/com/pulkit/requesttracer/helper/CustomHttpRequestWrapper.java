package com.pulkit.requesttracer.helper;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class CustomHttpRequestWrapper extends HttpServletRequestWrapper {

    private String requestBody;

    public CustomHttpRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);

        StringBuilder requestBodyBuilder = new StringBuilder();
        BufferedReader reader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (null != inputStream) {
                reader = new BufferedReader(new InputStreamReader(inputStream));
                char [] buffer = new char[128];
                int bytesRead = -1;
                while((bytesRead = reader.read(buffer)) != -1) {
                    requestBodyBuilder.append(buffer, 0, bytesRead);
                }
            } else {
                requestBodyBuilder.append("");
            }

        } catch (IOException ex) {
            throw ex;
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }
        this.requestBody = requestBodyBuilder.toString();
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(requestBody.getBytes(StandardCharsets.UTF_8));
        ServletInputStream inputStream = new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
        return inputStream;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }
    public String getBody() {
        return this.requestBody;
    }
}

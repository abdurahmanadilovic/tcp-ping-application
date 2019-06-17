package io;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamWrapperImplementation implements InputStreamWrapper {
    private InputStream inputStream;

    public InputStreamWrapperImplementation(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public int read(byte[] buffer) throws IOException {
        return this.inputStream.read(buffer);
    }

    @Override
    public void close() throws IOException {
        this.inputStream.close();
    }
}

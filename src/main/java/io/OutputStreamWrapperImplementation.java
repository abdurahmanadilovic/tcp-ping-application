package io;

import java.io.IOException;
import java.io.OutputStream;

public class OutputStreamWrapperImplementation implements OutputStreamWrapper {
    private OutputStream outputStream;

    public OutputStreamWrapperImplementation(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        outputStream.write(bytes);
    }

    @Override
    public void close() throws IOException {
        outputStream.close();
    }
}

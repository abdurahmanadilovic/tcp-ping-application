package io;

import java.io.IOException;

public interface OutputStreamWrapper {
    void write(byte[] bytes) throws IOException;

    void close() throws IOException;
}

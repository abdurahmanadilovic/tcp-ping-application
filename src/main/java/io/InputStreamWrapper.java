package io;

import java.io.IOException;

public interface InputStreamWrapper {
    int read(byte[] buffer) throws IOException;
    void close() throws IOException;
}

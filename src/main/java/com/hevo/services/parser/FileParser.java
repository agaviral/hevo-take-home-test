package com.hevo.services.parser;

import java.io.IOException;
import java.io.InputStream;

/**
 * Interface for parsing file content and returning any text found in the file.
 */
public interface FileParser {
    String getContent(InputStream is) throws IOException;
}

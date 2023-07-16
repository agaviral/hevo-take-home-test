package com.hevo.services.parser;

import java.io.IOException;
import java.io.InputStream;

public interface FileParser {
    String getContent(InputStream is) throws IOException;
}

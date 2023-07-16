package com.hevo.services.parser;

import java.nio.charset.StandardCharsets;

@FileParserRegistry(extension = "txt")
public class TextFileParser implements FileParser {

    @Override
    public String getContent(byte[] data) {
        return new String(data, StandardCharsets.UTF_8);
    }
}

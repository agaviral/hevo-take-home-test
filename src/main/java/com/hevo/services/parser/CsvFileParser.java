package com.hevo.services.parser;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@FileParserRegistry(extension = "csv")
public class CsvFileParser implements FileParser {

    @Override
    public String getContent(InputStream is) throws IOException {
        return IOUtils.toString(is, StandardCharsets.UTF_8);
    }
}

package com.hevo.services.parser;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.IOException;
import java.io.InputStream;

@FileParserRegistry(extension = "docx")
public class DocXFileParser implements FileParser {

    @Override
    public String getContent(InputStream is) throws IOException {
        XWPFDocument document = new XWPFDocument(is);
        XWPFWordExtractor extractor = new XWPFWordExtractor(document);
        return extractor.getText();
    }
}

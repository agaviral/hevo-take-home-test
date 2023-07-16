package com.hevo.services.parser;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@FileParserRegistry(extension = "pdf")
public class PdfParser implements FileParser {

    @Override
    public String getContent(InputStream is) throws IOException {
        BodyContentHandler handler = new BodyContentHandler();
        PDFParser parser = new PDFParser();
        try {
            parser.parse(is, handler, new Metadata(), new ParseContext());
            return handler.toString().trim();
        } catch (SAXException | TikaException e) {
            log.error("Error while parsing pdf", e);
            throw new IOException("Error while parsing pdf");
        }
    }
}

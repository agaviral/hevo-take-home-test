package com.hevo.services.parser;

import com.google.common.io.Files;
import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Helper class which selects the right parser according to the extension of the file.
 * Maintains a registry of parsers.
 */
@Slf4j
@Singleton
public class FileParserSelector {
    private final Map<String, FileParser> fileParserRegistry;

    @Inject
    public FileParserSelector(Injector injector) {
        fileParserRegistry = new HashMap<>();
        Reflections reflections = new Reflections(FileParserSelector.class.getPackageName());
        Set<Class<?>> parserClasses = reflections.getTypesAnnotatedWith(FileParserRegistry.class);

        for (Class<?> parserClass : parserClasses) {
            if (FileParser.class.isAssignableFrom(parserClass)) {
                Class<? extends FileParser> fileParserClass = (Class<? extends FileParser>) parserClass;
                String extension = parserClass.getAnnotation(FileParserRegistry.class).extension();
                fileParserRegistry.put(extension, injector.getInstance(fileParserClass));
            }
        }
    }

    public FileParser getFileParser(String fileName) throws ParserNotFoundException {
        String extension = Files.getFileExtension(fileName);
        log.debug("Got file with name: " + fileName + " and extension: " + extension);
        if (fileParserRegistry.containsKey(extension)) {
            return fileParserRegistry.get(extension);
        }
        throw new ParserNotFoundException();
    }
}

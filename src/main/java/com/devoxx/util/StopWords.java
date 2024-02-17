package com.devoxx.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class StopWords {

    private static final Logger LOGGER = Logger.getLogger(StopWords.class.getName());

    protected static final Set<String> ENGLISH = load(Language.ENGLISH);
    protected static final Set<String> FRENCH = load(Language.FRENCH);
    protected static final Set<String> GERMAN = load(Language.GERMAN);
    protected static final Set<String> ITALIAN = load(Language.ITALIAN);
    protected static final Set<String> SPANISH = load(Language.SPANISH);
    protected static final Set<String> DUTCH = load(Language.DUTCH);

    private StopWords() {
    }

    private static Set<String> load(Language language) {
        String filename = "stopwords-" + language.getCode() + ".txt";
        try (InputStream inputStream = StopWords.class.getClassLoader().getResourceAsStream(filename)) {
            if (inputStream == null) {
                LOGGER.severe("File not found: " + filename);
                return Set.of();
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                // Joining each line of the file content
                return reader.lines().collect(Collectors.toSet());
            }
        } catch (IOException e) {
            LOGGER.severe("Error reading file: " + filename);
            return Set.of();
        }
    }


}

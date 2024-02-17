package com.devoxx.util.stemmer;

import com.devoxx.util.Language;

/**
 * A language stemmer.
 */
public interface Stemmer {
    String stem(String word);
    Language getSupportedLanguage();
}

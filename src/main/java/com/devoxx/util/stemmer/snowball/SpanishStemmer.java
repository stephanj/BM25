package com.devoxx.util.stemmer.snowball;

import com.devoxx.util.Language;
import com.devoxx.util.stemmer.Stemmer;
import org.tartarus.snowball.ext.spanishStemmer;

public class SpanishStemmer implements Stemmer {
    private final spanishStemmer instance = new spanishStemmer();

    @Override
    public String stem(String word) {
        instance.setCurrent(word);
        if (instance.stem()) {
            return instance.getCurrent();
        } else {
            return word;
        }
    }

    @Override
    public Language getSupportedLanguage() {
        return Language.SPANISH;
    }
}

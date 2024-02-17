package com.devoxx.util.stemmer.snowball;

import com.devoxx.util.Language;
import com.devoxx.util.stemmer.Stemmer;
import org.tartarus.snowball.ext.germanStemmer;

public class GermanStemmer implements Stemmer {
    private final germanStemmer instance = new germanStemmer();

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
        return Language.GERMAN;
    }
}

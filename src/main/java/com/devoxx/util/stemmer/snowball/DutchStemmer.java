package com.devoxx.util.stemmer.snowball;

import com.devoxx.util.Language;
import com.devoxx.util.stemmer.Stemmer;
import org.tartarus.snowball.ext.dutchStemmer;

public class DutchStemmer implements Stemmer {
    private final dutchStemmer instance = new dutchStemmer();

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
        return Language.DUTCH;
    }
}

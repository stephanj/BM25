package com.devoxx.util.stopwords;

import com.devoxx.util.stopwords.StopWords;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class StopWordsTest {

    @Test
    void testEnglishStopWords() {
        Set<String> englishStopWords = StopWords.ENGLISH;
        assertTrue(englishStopWords.contains("the"));
        assertTrue(englishStopWords.contains("above"));
    }

    @Test
    void testDutchStopWords() {
        Set<String> englishStopWords = StopWords.DUTCH;
        assertTrue(englishStopWords.contains("de"));
        assertTrue(englishStopWords.contains("het"));
    }

    @Test
    void testItalianStopWords() {
        Set<String> englishStopWords = StopWords.ITALIAN;
        assertTrue(englishStopWords.contains("alla"));
        assertTrue(englishStopWords.contains("anno"));
    }

    @Test
    void testSpanishStopWords() {
        Set<String> englishStopWords = StopWords.SPANISH;
        assertTrue(englishStopWords.contains("aún"));
        assertTrue(englishStopWords.contains("cosas"));
    }

    @Test
    void testFrenchStopWords() {
        Set<String> englishStopWords = StopWords.FRENCH;
        assertTrue(englishStopWords.contains("autres"));
        assertTrue(englishStopWords.contains("la"));
    }

    @Test
    void testGermanStopWords() {
        Set<String> englishStopWords = StopWords.GERMAN;
        assertTrue(englishStopWords.contains("anderen"));
        assertTrue(englishStopWords.contains("dann"));
    }
}

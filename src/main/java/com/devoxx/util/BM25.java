/**
 * MIT License
 * Copyright (c) 2024 Stephan Janssen
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.devoxx.util;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * BM25 (Best Matching 25) is a ranking function used by search engines to rank matching documents
 * according to their relevance to a given search query.
 * @link <a href="https://en.wikipedia.org/wiki/Okapi_BM25">Wikipedia</a>
 */
public class BM25 {

    private static final Pattern SPACE_PATTERN = Pattern.compile("\\s+");
    private final List<String> corpus; // List of documents
    private final double avgDocLength;
    private Map<Integer, Map<String, Integer>> tf; // Term Frequency
    private Map<String, Double> idf; // Inverse Document Frequency
    private final double termFrequencyScalingFactor;
    private final double documentLengthNormalizationFactor;

    /**
     * Constructor to initialize BM25
     * @param corpus list of documents
     */
    public BM25(final List<String> corpus) {
        this(corpus, 1.5, 0.75);
    }

    /**
     * Constructor to initialize BM25
     * @param corpus list of documents
     * @param termFrequencyScalingFactor scaling factor for term frequency
     * @param documentLengthNormalizationFactor normalization factor for document length
     */
    public BM25(final List<String> corpus,
                final double termFrequencyScalingFactor,
                final double documentLengthNormalizationFactor) {
        if (corpus == null || corpus.isEmpty()) {
            throw new IllegalArgumentException("Corpus must not be null and must contain at least one document.");
        }
        if (termFrequencyScalingFactor <= 0 || documentLengthNormalizationFactor < 0) {
            throw new IllegalArgumentException("termFrequencyScalingFactor and documentLengthNormalizationFactor must be positive.");
        }
        this.corpus = corpus;
        this.avgDocLength = calculateAverageDocumentLength(corpus);
        this.tf = new HashMap<>();
        this.idf = new HashMap<>();
        this.termFrequencyScalingFactor = termFrequencyScalingFactor;
        this.documentLengthNormalizationFactor = documentLengthNormalizationFactor;
        initialize();
    }

    /**
     * Calculate the average length of documents in the corpus
     * @param corpus list of documents
     * @return average length of documents
     */
    private double calculateAverageDocumentLength(List<String> corpus) {
        long totalLength = corpus.stream()
            .mapToInt(doc -> SPACE_PATTERN.split(doc).length)
            .sum();
        return corpus.isEmpty() ? 0 : (double) totalLength / corpus.size();
    }

    /**
     * Initialize term frequency and inverse document frequency
     */
    private void initialize() {
        Map<String, Set<Integer>> docFreq = docFrequencyCalculation();
        termFrequencyCalculation();
        idfCalculation(docFreq);
    }

    /**
     * Calculate inverse document frequency (idf)
     * @param docFreq document frequency
     */
    private void idfCalculation(Map<String, Set<Integer>> docFreq) {
        int corpusSize = corpus.size();
        idf = docFreq.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> Math.log((corpusSize - entry.getValue().size() + 0.5) / (entry.getValue().size() + 0.5) + 1)
            ));
    }

    /**
     * The term frequency (tf) calculation.
     */
    private void termFrequencyCalculation() {
        tf = IntStream.range(0, corpus.size())
            .boxed()
            .collect(Collectors.toMap(
                Function.identity(),
                docIndex -> {
                    String[] terms = SPACE_PATTERN.split(corpus.get(docIndex).toLowerCase());
                    return Arrays.stream(terms)
                        .collect(Collectors.groupingBy(Function.identity(), Collectors.summingInt(term -> 1)));
                }
            ));
    }

    /**
     * Calculate document frequency (docFreq)
     * @return document frequency
     */
    private Map<String, Set<Integer>> docFrequencyCalculation() {
        // Document Frequency (docFreq) calculation
        return IntStream.range(0, corpus.size())
            .boxed()
            .flatMap(docIndex -> {
                String[] terms = SPACE_PATTERN.split(corpus.get(docIndex).toLowerCase());
                return Arrays.stream(terms)
                    .distinct()
                    .map(term -> new AbstractMap.SimpleEntry<>(term, docIndex));
            })
            .collect(Collectors.groupingBy(
                Map.Entry::getKey,
                Collectors.mapping(Map.Entry::getValue, Collectors.toSet())
            ));
    }

    /**
     * Compute BM25 score for a document
     * @param docIndex index of the document
     * @param query list of query terms
     * @return BM25 score for the document
     */
    public double calculateDocumentScore(Integer docIndex, List<String> query) {
        String document = corpus.get(docIndex);
        double docLength = SPACE_PATTERN.split(document).length;
        return query.stream()
            .distinct()
            .mapToDouble(term -> calculateTermScore(docIndex, term, docLength))
            .sum();
    }

    /**
     * Calculate BM25 score for a term in a document
     * @param docIndex index of the document
     * @param term term to calculate score for
     * @param docLength length of the document
     * @return BM25 score for the term in the document
     */
    private double calculateTermScore(Integer docIndex, String term, double docLength) {
        double termFrequency = tf.get(docIndex).getOrDefault(term, 0);
        double idfValue = idf.getOrDefault(term, 0.0);
        if (idfValue == 0.0) {
            return 0.0; // Skipping term or handle differently
        }
        double numerator = idfValue * (termFrequency * (termFrequencyScalingFactor + 1));
        double denominator = termFrequency +
            termFrequencyScalingFactor *
            (1 - documentLengthNormalizationFactor + documentLengthNormalizationFactor * (docLength / avgDocLength));
        return numerator / denominator;
    }

    /**
     * Search for documents that match the query
     * @param query list of query terms
     * @return list of documents with their BM25 scores
     */
    public List<Map.Entry<Integer, Double>> search(String query) {
        if (query == null || query.isEmpty()) {
            throw new IllegalArgumentException("Query must not be null and must contain at least one term.");
        }
        List<String> queryTerms = List.of(SPACE_PATTERN.split(query.toLowerCase()));
        return IntStream.range(0, corpus.size())
            .boxed()
            .map(docIndex -> Map.entry(docIndex, calculateDocumentScore(docIndex, queryTerms)))
            .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
            .toList();
    }

    /**
     * Main method to test the BM25 class
     * @param args command line arguments
     */
    public static void main(String[] args) {

        Long start = System.currentTimeMillis();

        List<String> corpus = List.of(
            "I love programming",
            "Java is my favorite programming language",
            "I enjoy writing code in Java",
            "Java is another popular programming language",
            "I find programming fascinating",
            "I love Java",
            "I prefer Java over Python"
        );

        double termFrequencyScalingFactor = 1.5;
        double documentLengthNormalizationFactor = 0.75;

        BM25 bm25 = new BM25(corpus, termFrequencyScalingFactor, documentLengthNormalizationFactor);

        String query = "Love Java";

        try {
            List<Map.Entry<Integer, Double>> results = bm25.search(query);

            System.out.println("Search results for : " + query);

            for (Map.Entry<Integer, Double> entry : results) {
                System.out.println("Sentence " + entry.getKey() + " : Score = " + entry.getValue() + " - [" + corpus.get(entry.getKey()) + "]");
            }
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }

        Long end = System.currentTimeMillis();
        System.out.println("Time taken: " + (end - start) + "ms");
    }
}

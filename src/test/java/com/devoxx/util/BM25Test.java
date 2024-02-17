/**
 * MIT License
 *
 * Copyright (c) 2024 Stephan Janssen
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.devoxx.util;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class BM25Test {

    @Test
    void testBM25_ILoveJava() {
        List<String> corpus = List.of(
            "I love programming",
            "Java is my favorite programming language",
            "I enjoy writing code in Java",
            "Java is another popular programming language",
            "I find programming fascinating",
            "I love Java",
            "I prefer Java over Python"
        );

        BM25 bm25 = new BM25(corpus);

        List<Map.Entry<Integer, Double>> results = bm25.search("I love java");

        for (Map.Entry<Integer, Double> entry : results) {
            System.out.println("Sentence " + entry.getKey() + " : Score = " + entry.getValue() + " - [" + corpus.get(entry.getKey()) + "]");
        }

        assertThat(results).isNotNull();
        assertThat(results.size()).isEqualTo(corpus.size());
        assertThat(results.getFirst().getKey()).isEqualTo(5);
        assertThat(results.getLast().getKey()).isEqualTo(4);

        assertThat(results.getFirst().getValue()).isGreaterThan(1.8);
        assertThat(results.getLast().getValue()).isEqualTo(0.0);
    }

    @Test
    void testBM25_PythonProgramming() {
        List<String> corpus = List.of(
            "I love programming",
            "Java is my favorite programming language",
            "I enjoy writing code in Java",
            "Java is another popular programming language",
            "I find programming fascinating",
            "I love Java",
            "I prefer Java over Python"
        );

        BM25 bm25 = new BM25(corpus);

        List<Map.Entry<Integer, Double>> results = bm25.search("Python programming");
        for (Map.Entry<Integer, Double> entry : results) {
            System.out.println("Sentence " + entry.getKey() + " : Score = " + entry.getValue() + " - [" + corpus.get(entry.getKey()) + "]");
        }

        assertThat(results).isNotNull();
        assertThat(results.size()).isEqualTo(corpus.size());

        assertThat(results.getFirst().getKey()).isEqualTo(6);
        assertThat(results.getLast().getKey()).isEqualTo(5);

        assertThat(results.getFirst().getValue()).isGreaterThan(1.5);
        assertThat(results.getLast().getValue()).isEqualTo(0.0);
    }

    @Test
    void testBM25_withEnglishStopWords() {
        List<String> corpus = List.of(
            "I love programming",
            "Java is my favorite programming language",
            "I enjoy writing code in Java",
            "Java is another popular programming language",
            "I find programming fascinating",
            "I love Java",
            "I prefer Java over Python"
        );

        BM25 bm25 = new BM25(corpus, StopWords.ENGLISH);

        List<Map.Entry<Integer, Double>> results = bm25.search("Python programming");
        for (Map.Entry<Integer, Double> entry : results) {
            System.out.println("Sentence " + entry.getKey() + " : Score = " + entry.getValue() + " - [" + corpus.get(entry.getKey()) + "]");
        }

        assertThat(results).isNotNull();
        assertThat(results.size()).isEqualTo(corpus.size());

        assertThat(results.getFirst().getKey()).isEqualTo(6);
        assertThat(results.getLast().getKey()).isEqualTo(5);

        assertThat(results.getFirst().getValue()).isGreaterThan(1.5);
        assertThat(results.getLast().getValue()).isEqualTo(0.0);
    }

}

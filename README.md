# BM25 Java Implementation

BM25 (Best Matching 25) is a ranking function used by search engines to rank matching documents according to their relevance to a given search query.

See also https://en.wikipedia.org/wiki/Okapi_BM25

# Simple usage

```java
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
```

```
Sentence 5 : Score = 2.286729869084079 - [I love Java]
Sentence 0 : Score = 1.8387268317084793 - [I love programming]
Sentence 6 : Score = 0.7294916714788526 - [I prefer Java over Python]
Sentence 2 : Score = 0.6674701123652661 - [I enjoy writing code in Java]
Sentence 4 : Score = 0.40211004330297734 - [I find programming fascinating]
Sentence 1 : Score = 0.33373505618263305 - [Java is my favorite programming language]
Sentence 3 : Score = 0.33373505618263305 - [Java is another popular programming language]
```

```Java
bm25.search("programming");
```

```
Sentence 0 : Score = 0.687935390645563 - [I love programming]
Sentence 4 : Score = 0.6174639603843102 - [I find programming fascinating]
Sentence 1 : Score = 0.5124700885780712 - [Java is my favorite programming language]
Sentence 3 : Score = 0.5124700885780712 - [Java is another popular programming language]
Sentence 2 : Score = 0.0 - [I enjoy writing code in Java]
Sentence 5 : Score = 0.0 - [I love Java]
Sentence 6 : Score = 0.0 - [I prefer Java over Python]
```

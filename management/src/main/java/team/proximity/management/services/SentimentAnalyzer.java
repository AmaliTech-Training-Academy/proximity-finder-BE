package team.proximity.management.services;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Properties;

// SentimentAnalyzer.java
@Service
@Slf4j
public class SentimentAnalyzer {
    private final StanfordCoreNLP pipeline;

    public SentimentAnalyzer() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        this.pipeline = new StanfordCoreNLP(props);
    }

    public String analyzeSentiment(String text) {
        int mainSentiment = 0;
        if (text != null && text.length() > 0) {
            try {
                Annotation annotation = pipeline.process(text);
                for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                    Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
                    int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
                    mainSentiment += sentiment;
                }
            } catch (Exception e) {
                log.error("Error analyzing sentiment: ", e);
                return "NEUTRAL";
            }
        }

        // Convert numerical sentiment to text
        if (mainSentiment <= 1) return "NEGATIVE";
        else if (mainSentiment == 2) return "NEUTRAL";
        else return "POSITIVE";
    }
}

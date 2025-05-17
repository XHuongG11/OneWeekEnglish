package com.example.oneweekenglish.util;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.util.ArrayList;
import java.util.Locale;

/**
 * PronunciationChecker - A single class to handle pronunciation checking
 * by comparing a target text with user's spoken input
 */
public class PronunciationChecker {
    private static final String TAG = "PronunciationChecker";
    private static final int MIN_GOOD_SCORE = 70;
    private static final int EXCELLENT_SCORE = 90;

    private Context context;
    private SpeechRecognizer speechRecognizer;
    private String targetText;
    private OnPronunciationResultListener resultListener;

    /**
     * Interface for callback when pronunciation results are ready
     */
    public interface OnPronunciationResultListener {
        void onPronunciationResult(String targetText, String spokenText, int accuracyPercentage, String formattedFeedback);
        void onError(String errorMessage);
        void onListeningStarted();
        void onListeningFinished();
    }

    /**
     * Constructor
     * @param context The context
     * @param targetText The text to be pronounced by user
     */
    public PronunciationChecker(Context context, String targetText) {
        this.context = context;
        this.targetText = targetText;
        initializeSpeechRecognizer();
    }

    /**
     * Set target text
     * @param targetText The text to be pronounced
     */
    public void setTargetText(String targetText) {
        this.targetText = targetText;
    }

    /**
     * Set the result listener
     * @param listener Callback listener for pronunciation results
     */
    public void setOnPronunciationResultListener(OnPronunciationResultListener listener) {
        this.resultListener = listener;
    }

    /**
     * Initialize the speech recognizer
     */
    private void initializeSpeechRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(context)) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
            speechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onResults(Bundle results) {
                    ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    if (matches != null && !matches.isEmpty()) {
                        String spokenText = matches.get(0);
                        processPronunciationResult(spokenText);
                    }
                    if (resultListener != null) {
                        resultListener.onListeningFinished();
                    }
                }

                @Override
                public void onReadyForSpeech(Bundle params) {
                    if (resultListener != null) {
                        resultListener.onListeningStarted();
                    }
                }

                @Override
                public void onBeginningOfSpeech() {}

                @Override
                public void onRmsChanged(float rmsdB) {}

                @Override
                public void onBufferReceived(byte[] buffer) {}

                @Override
                public void onEndOfSpeech() {}

                @Override
                public void onError(int error) {
                    if (resultListener != null) {
                        resultListener.onError(getErrorText(error));
                        resultListener.onListeningFinished();
                    }
                }

                @Override
                public void onPartialResults(Bundle partialResults) {}

                @Override
                public void onEvent(int eventType, Bundle params) {}
            });
        } else {
            Log.e(TAG, "Speech recognition not available on this device");
        }
    }

    /**
     * Start listening for speech input
     * @return true if successfully started, false otherwise
     */
    public boolean startListening() {
        if (speechRecognizer == null) {
            return false;
        }

        if (targetText == null || targetText.isEmpty()) {
            if (resultListener != null) {
                resultListener.onError("Target text is empty");
            }
            return false;
        }

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

        speechRecognizer.startListening(intent);
        return true;
    }

    /**
     * Process the speech recognition result
     * @param spokenText The text spoken by the user
     */
    private void processPronunciationResult(String spokenText) {
        // Normalize texts for comparison
        String normalizedTarget = targetText.toLowerCase().replaceAll("[.,?!]", "");
        String normalizedSpoken = spokenText.toLowerCase().replaceAll("[.,?!]", "");

        // Calculate overall accuracy
        int accuracy = calculateAccuracy(normalizedTarget, normalizedSpoken);

        // Generate detailed word-by-word feedback
        String wordByWordFeedback = generateWordByWordFeedback(normalizedTarget, normalizedSpoken);

        if (resultListener != null) {
            resultListener.onPronunciationResult(targetText,spokenText, accuracy, wordByWordFeedback);
        }
    }

    /**
     * Calculate accuracy percentage between target and spoken text
     * @param target The target text
     * @param spoken The spoken text
     * @return Accuracy percentage (0-100)
     */
    private int calculateAccuracy(String target, String spoken) {
        int distance = levenshteinDistance(target, spoken);
        float similarity = 1 - (float)distance / Math.max(target.length(), spoken.length());
        return (int)(similarity * 100);
    }

    /**
     * Generate HTML-formatted feedback for word-by-word comparison
     * @param target The target text
     * @param spoken The spoken text
     * @return HTML-formatted string with color-coded feedback
     */
    private String generateWordByWordFeedback(String target, String spoken) {
        String[] targetWords = target.split("\\s+");
        String[] spokenWords = spoken.split("\\s+");

        StringBuilder feedback = new StringBuilder();

        // Compare words
        int minLength = Math.min(targetWords.length, spokenWords.length);

        for (int i = 0; i < targetWords.length; i++) {
            if (i < minLength) {
                float similarity = wordSimilarity(targetWords[i], spokenWords[i]);
                if (similarity >= 0.8) {
                    feedback.append("<font color='green'>").append(targetWords[i]).append("</font> ");
                } else if (similarity >= 0.5) {
                    feedback.append("<font color='orange'>").append(targetWords[i]).append("</font> ");
                } else {
                    feedback.append("<font color='red'>").append(targetWords[i]).append("</font> ");
                }
            } else {
                // Words missing from spoken text
                feedback.append("<font color='red'>").append(targetWords[i]).append("</font> ");
            }
        }

        // Check for extra words in spoken text
        if (spokenWords.length > targetWords.length) {
            feedback.append("<br><br>Extra words: <font color='blue'>");
            for (int i = targetWords.length; i < spokenWords.length; i++) {
                feedback.append(spokenWords[i]).append(" ");
            }
            feedback.append("</font>");
        }

        return feedback.toString();
    }

    /**
     * Calculate similarity between two words
     * @param word1 First word
     * @param word2 Second word
     * @return Similarity score between 0.0 and 1.0
     */
    private float wordSimilarity(String word1, String word2) {
        int distance = levenshteinDistance(word1, word2);
        return 1 - (float)distance / Math.max(word1.length(), word2.length());
    }

    /**
     * Calculate Levenshtein distance between two strings
     * @param s1 First string
     * @param s2 Second string
     * @return Levenshtein distance
     */
    private int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= s2.length(); j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);
            }
        }

        return dp[s1.length()][s2.length()];
    }

    /**
     * Get text description for speech recognition error code
     * @param errorCode The error code
     * @return Human-readable error description
     */
    private String getErrorText(int errorCode) {
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO: return "Audio recording error";
            case SpeechRecognizer.ERROR_CLIENT: return "Client side error";
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS: return "Insufficient permissions";
            case SpeechRecognizer.ERROR_NETWORK: return "Network error";
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT: return "Network timeout";
            case SpeechRecognizer.ERROR_NO_MATCH: return "No speech recognized";
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY: return "Recognition service busy";
            case SpeechRecognizer.ERROR_SERVER: return "Server error";
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT: return "No speech detected";
            default: return "Unknown error";
        }
    }

    /**
     * Check if permission for audio recording is granted
     * @return true if permission is granted, false otherwise
     */
    public boolean hasRecordAudioPermission() {
        return context.checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Release resources
     */
    public void release() {
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
            speechRecognizer = null;
        }
    }
}

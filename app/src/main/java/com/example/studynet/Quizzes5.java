package com.example.studynet;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;
import android.content.res.ColorStateList;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.Collections;

public class Quizzes5 extends AppCompatActivity {

    // --- UI Elements ---
    private TextView timerText, progressText, questionText, finalScoreText, resultMessageText;
    private Button optionA, optionB, optionC, optionD, nextButton, finishButton;
    private ArrayList<Button> optionButtons;
    private ScrollView quizContentScrollView;
    private LinearLayout resultLayout;

    // --- Quiz State ---
    private ArrayList<Question> quizQuestions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private CountDownTimer countDownTimer;
    // 10 minutes total time (600 seconds)
    private final long TOTAL_TIME_MS = 600000;

    // Color definitions
    private final int DEFAULT_COLOR = Color.parseColor("#4F46E5"); // Indigo/Blue
    private final int CORRECT_COLOR = Color.parseColor("#10B981"); // Green for correct
    private final int INCORRECT_COLOR = Color.parseColor("#EF4444"); // Red for incorrect choice
    private final int NEUTRAL_HIGHLIGHT_COLOR = Color.parseColor("#F59E0B"); // Orange to highlight correct answer

    // --- Data Model (Nested Class) ---
    private static class Question {
        String questionText;
        String[] options;
        int correctAnswerIndex; // 0 for A, 1 for B, 2 for C, 3 for D

        public Question(String questionText, String[] options, int correctAnswerIndex) {
            this.questionText = questionText;
            this.options = options;
            this.correctAnswerIndex = correctAnswerIndex;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizzes5);

        // 1. Initialize Questions (Module 5: Network Topologies, Standards, and Types)
        initializeQuestions();

        // 2. Map UI Elements
        mapUIElements();

        // 3. Set up Listeners and Start Quiz
        setupListeners();
        startTimer();
        loadQuestion();
    }

    /**
     * Defines the 10 quiz questions based on Module 5: Network Topologies, Standards, and Types.
     */
    private void initializeQuestions() {
        quizQuestions = new ArrayList<>();

        // Q1 (Topology: Star)
        quizQuestions.add(new Question(
                "Which network **topology** connects every node to a central device (like a switch or hub), making it easy to troubleshoot but vulnerable if the central device fails?",
                new String[]{"Bus Topology", "Ring Topology", "Star Topology", "Mesh Topology"},
                2));

        // Q2 (Topology: Bus)
        quizQuestions.add(new Question(
                "Which topology is characterized by a single, continuous cable that connects all nodes, with terminators at each end to prevent signal reflection?",
                new String[]{"Ring Topology", "Bus Topology", "Star Topology", "Tree Topology"},
                1));

        // Q3 (Topology: Mesh)
        quizQuestions.add(new Question(
                "In which topology is every device connected directly to every other device, providing the highest level of fault tolerance and redundancy?",
                new String[]{"Star Topology", "Bus Topology", "Ring Topology", "Mesh Topology"},
                3));

        // Q4 (Network Type: WAN)
        quizQuestions.add(new Question(
                "A network that spans a large geographical area, often connecting multiple smaller networks (LANs) across cities or countries, is classified as a:",
                new String[]{"MAN (Metropolitan Area Network)", "PAN (Personal Area Network)", "LAN (Local Area Network)", "WAN (Wide Area Network)"},
                3));

        // Q5 (Network Type: LAN)
        quizQuestions.add(new Question(
                "A network that covers a small, localized area, such as a single office floor, home, or small campus, is known as a:",
                new String[]{"WAN (Wide Area Network)", "MAN (Metropolitan Area Network)", "LAN (Local Area Network)", "SAN (Storage Area Network)"},
                2));

        // Q6 (Standard/Model: OSI Layers)
        quizQuestions.add(new Question(
                "How many distinct layers are typically defined in the OSI (Open Systems Interconnection) reference model?",
                new String[]{"4 layers", "5 layers", "7 layers", "9 layers"},
                2));

        // Q7 (Standard/Model: OSI Session Layer)
        quizQuestions.add(new Question(
                "Which layer of the OSI model is responsible for establishing, managing, and terminating communication sessions between applications?",
                new String[]{"Transport Layer", "Presentation Layer", "Session Layer", "Application Layer"},
                2));

        // Q8 (Standard/Model: TCP/IP Layers)
        quizQuestions.add(new Question(
                "The TCP/IP model, which underpins the Internet, primarily consists of how many layers?",
                new String[]{"3 layers", "4 layers", "6 layers", "7 layers"},
                1));

        // Q9 (Standard/Model: TCP/IP Network Access)
        quizQuestions.add(new Question(
                "In the TCP/IP model, which layer is equivalent to the combination of the Physical and Data Link layers of the OSI model, handling physical transmission?",
                new String[]{"Internet Layer", "Application Layer", "Transport Layer", "Network Access Layer"},
                3));

        // Q10 (Network Type: MAN)
        quizQuestions.add(new Question(
                "A network designed to cover a city or large campus, often linking several LANs together within a limited metropolitan region, is classified as a:",
                new String[]{"GAN (Global Area Network)", "PAN (Personal Area Network)", "MAN (Metropolitan Area Network)", "LAN (Local Area Network)"},
                2));


        // Shuffle the questions for variety
        Collections.shuffle(quizQuestions);
    }

    /**
     * Maps the XML views to the Java variables.
     */
    private void mapUIElements() {
        // Quiz elements
        quizContentScrollView = findViewById(R.id.quiz_content_scrollview);
        timerText = findViewById(R.id.timer_text);
        progressText = findViewById(R.id.progress_text);
        questionText = findViewById(R.id.question_text);
        optionA = findViewById(R.id.option_a);
        optionB = findViewById(R.id.option_b);
        optionC = findViewById(R.id.option_c);
        optionD = findViewById(R.id.option_d);
        nextButton = findViewById(R.id.next_button);

        optionButtons = new ArrayList<>();
        optionButtons.add(optionA);
        optionButtons.add(optionB);
        optionButtons.add(optionC);
        optionButtons.add(optionD);

        // Result elements
        resultLayout = findViewById(R.id.result_layout);
        finalScoreText = findViewById(R.id.final_score_text);
        resultMessageText = findViewById(R.id.result_message_text);
        finishButton = findViewById(R.id.finish_button);
    }

    /**
     * Sets up click listeners for all option buttons and the next button.
     */
    private void setupListeners() {
        // Set listeners for each option button
        for (int i = 0; i < optionButtons.size(); i++) {
            final int optionIndex = i;
            optionButtons.get(i).setOnClickListener(v -> handleAnswerSelection(optionIndex));
        }

        // Set listener for Next/Finish button
        nextButton.setOnClickListener(v -> nextQuestion());

        // Set listener for the button on the Result screen
        finishButton.setOnClickListener(v -> {
            // Simply close the activity and return to the previous screen
            finish();
        });
    }

    /**
     * Handles the user's selection of an answer option and provides feedback.
     * @param selectedIndex The index of the selected option (0-3).
     */
    private void handleAnswerSelection(int selectedIndex) {
        // Disable all buttons to prevent double-clicking or changing the answer
        setOptionsEnabled(false);
        nextButton.setVisibility(View.VISIBLE);

        Question currentQ = quizQuestions.get(currentQuestionIndex);
        int correctIndex = currentQ.correctAnswerIndex;

        // Check if the selected answer is correct
        if (selectedIndex == correctIndex) {
            score++;
            // Apply correct color
            optionButtons.get(selectedIndex).setBackgroundTintList(ColorStateList.valueOf(CORRECT_COLOR));
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
        } else {
            // Incorrect answer
            // Apply incorrect color to the selected choice
            optionButtons.get(selectedIndex).setBackgroundTintList(ColorStateList.valueOf(INCORRECT_COLOR));
            // Highlight the correct answer with the neutral highlight color
            optionButtons.get(correctIndex).setBackgroundTintList(ColorStateList.valueOf(NEUTRAL_HIGHLIGHT_COLOR));
            Toast.makeText(this, "Incorrect. Correct answer is highlighted in Orange.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Enables or disables the option buttons and resets their color.
     * @param enabled True to enable, false to disable.
     */
    private void setOptionsEnabled(boolean enabled) {
        for (Button button : optionButtons) {
            button.setEnabled(enabled);

            if(enabled) {
                // Reset to default color when enabling
                button.setBackgroundTintList(ColorStateList.valueOf(DEFAULT_COLOR));
            }
        }
    }

    /**
     * Loads the next question or finishes the quiz.
     */
    private void nextQuestion() {
        currentQuestionIndex++;
        nextButton.setVisibility(View.GONE);

        if (currentQuestionIndex < quizQuestions.size()) {
            loadQuestion();
        } else {
            finishQuiz();
        }
    }

    /**
     * Resets button styling and loads the current question content.
     */
    private void loadQuestion() {
        Question currentQ = quizQuestions.get(currentQuestionIndex);

        // Reset button colors and enable options
        setOptionsEnabled(true);

        // Update UI Text
        questionText.setText(currentQ.questionText);

        // Populate options, prepending A., B., C., D.
        optionA.setText("A. " + currentQ.options[0]);
        optionB.setText("B. " + currentQ.options[1]);
        optionC.setText("C. " + currentQ.options[2]);
        optionD.setText("D. " + currentQ.options[3]);

        // Update progress counter
        progressText.setText((currentQuestionIndex + 1) + " / " + quizQuestions.size());

        // Update button text if it's the last question
        if (currentQuestionIndex == quizQuestions.size() - 1) {
            nextButton.setText("Finish Quiz");
        } else {
            nextButton.setText("Next Question");
        }
    }

    /**
     * Starts the countdown timer for the quiz (10 minutes).
     */
    private void startTimer() {
        countDownTimer = new CountDownTimer(TOTAL_TIME_MS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                        TimeUnit.MINUTES.toSeconds(minutes);

                String timeString = String.format("%02d:%02d", minutes, seconds);
                timerText.setText(timeString);

                // Highlight timer in red when under 1 minute
                if (millisUntilFinished < 60000) {
                    timerText.setTextColor(Color.parseColor("#EF4444")); // Red
                }
            }

            @Override
            public void onFinish() {
                timerText.setText("00:00");
                Toast.makeText(Quizzes5.this, "Time's up! Submitting answers.", Toast.LENGTH_LONG).show();
                finishQuiz();
            }
        }.start();
    }

    /**
     * Ends the quiz, displays the final score, and hides the quiz content.
     */
    private void finishQuiz() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        // 1. Hide the quiz view
        quizContentScrollView.setVisibility(View.GONE);

        // 2. Show the result view
        resultLayout.setVisibility(View.VISIBLE);

        // 3. Update the final score text
        String finalScore = score + "/" + quizQuestions.size();
        finalScoreText.setText("Your Score: " + finalScore);

        String encouragement;
        if (score == quizQuestions.size()) {
            encouragement = "Perfect Score! You've mastered network topologies and the OSI/TCP-IP models!";
        } else if (score >= quizQuestions.size() * 0.7) {
            encouragement = "Excellent score! You have a strong grasp of network structures and standards.";
        } else {
            encouragement = "Good attempt! Review the differences between network topologies (Star, Bus, Ring) and the layers of the OSI model.";
        }

        resultMessageText.setText(encouragement);

        // Provide an immediate summary via Toast
        Toast.makeText(this, "Quiz finished. " + encouragement, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Crucial: Stop the timer when the activity is destroyed
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
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
import android.content.res.ColorStateList; // NEW IMPORT
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Quizzes1 extends AppCompatActivity {

    // --- UI Elements ---
    private TextView timerText, progressText, questionText, finalScoreText;
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
        setContentView(R.layout.activity_quizzes1);

        // 1. Initialize Questions (based on strings.xml content)
        initializeQuestions();

        // 2. Map UI Elements
        mapUIElements();

        // 3. Set up Listeners and Start Quiz
        setupListeners();
        startTimer();
        loadQuestion();
    }

    /**
     * Defines the 10 quiz questions related to Networking Modules 1-5.
     */
    private void initializeQuestions() {
        quizQuestions = new ArrayList<>();

        // Q1: Module 1 (LAN) - from networking_vocabulary
        quizQuestions.add(new Question(
                "What does the acronym **LAN** stand for?",
                new String[]{"Local Access Network", "Local Area Network", "Large Area Node", "Limited Access Notation"},
                1));

        // Q2: Module 1 (Up/Down) - from networking_vocabulary
        quizQuestions.add(new Question(
                "If a computer on a network is described as **'Down'**, what state is it in?",
                new String[]{"Connected to the network", "Computer is turned off", "Resources are outside the computer", "Not connected to the network"},
                1));

        // Q3: Module 2 (WAN) - from network_type_wan
        quizQuestions.add(new Question(
                "Which network type exists over a **large-scale geographical area** and can connect multiple smaller networks (LANs and MANs)?",
                new String[]{"LAN", "CAN", "WAN", "WLAN"},
                2));

        // Q4: Module 3 (Sharing Resources) - from kind_sharing_resources_desc
        quizQuestions.add(new Question(
                "Which of these is NOT an example of a **shared resource** on a computer network?",
                new String[]{"A Laser Printer", "A File Server", "A hard drive set as shared", "A personal computer's local operating system"},
                3));

        // Q5: Module 4 (Server Computer) - from def_server_computer
        quizQuestions.add(new Question(
                "A **Server Computer** is dedicated to the sole task of providing shared resources to be accessed by what?",
                new String[]{"Intermediary devices", "Transmission media", "Network client computers", "Other servers"},
                2));

        // Q6: Module 4 (Network Interface) - from network_tick_1_interface
        quizQuestions.add(new Question(
                "What is the special electronic circuit **inside any computer** attached to a network, often referred to as a NIC?",
                new String[]{"Network switch", "Network cable", "Network interface", "Application server"},
                2));

        // Q7: Module 4 (Web Server) - from server_type_12_web_desc
        quizQuestions.add(new Question(
                "What is the core function of a **Web Server**?",
                new String[]{"To move and store mail", "To serve static content to a Web browser by loading a file from a disk", "To manage interactive mailing lists", "To enable remote logon to a host computer"},
                1));

        // Q8: Module 5 (Communication Elements) - from text_network_structure_desc
        quizQuestions.add(new Question(
                "In network communication, the **'channel'** refers to the:",
                new String[]{"Message source or sender", "Destination or receiver", "Media that provides the pathway over which the message travels", "Communication protocol used"},
                2));

        // Q9: Module 5 (Multiplexing) - from text_message_communication_methods
        quizQuestions.add(new Question(
                "What is the process used to **interleave the pieces of separate conversations** together on the network?",
                new String[]{"Segmentation", "Encapsulation", "Protocol layering", "Multiplexing"},
                3));

        // Q10: Module 5 (Protocol) - from text_different_protocols_desc
        quizQuestions.add(new Question(
                "Which protocol is the transport protocol that **manages the individual conversations** between web servers and web clients?",
                new String[]{"Hypertext Transfer Protocol (HTTP)", "Transmission Control Protocol (TCP)", "Internet Protocol (IP)", "Ethernet"},
                1));
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

        // Color definitions for feedback
        int correctColor = Color.parseColor("#10B981"); // Green for correct
        int incorrectColor = Color.parseColor("#EF4444"); // Red for incorrect choice
        int neutralHighlight = Color.parseColor("#F59E0B"); // Orange to highlight correct answer if wrong choice was made

        // Check if the selected answer is correct
        if (selectedIndex == correctIndex) {
            score++;
            // FIX: Use setBackgroundTintList to properly change the color of the Material Button
            optionButtons.get(selectedIndex).setBackgroundTintList(ColorStateList.valueOf(correctColor));
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
        } else {
            // Incorrect answer
            // FIX: Use setBackgroundTintList to properly change the color of the Material Button
            optionButtons.get(selectedIndex).setBackgroundTintList(ColorStateList.valueOf(incorrectColor));
            // Highlight the correct answer with a neutral highlight color
            optionButtons.get(correctIndex).setBackgroundTintList(ColorStateList.valueOf(neutralHighlight));
            Toast.makeText(this, "Incorrect. Correct answer is highlighted in Orange.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Enables or disables the option buttons.
     * @param enabled True to enable, false to disable.
     */
    private void setOptionsEnabled(boolean enabled) {
        // Default color for options (Indigo/Blue)
        int defaultColor = Color.parseColor("#4F46E5");

        for (Button button : optionButtons) {
            button.setEnabled(enabled);
            // FIX: Use setBackgroundTintList to reset the color to default when enabling
            if(enabled) {
                button.setBackgroundTintList(ColorStateList.valueOf(defaultColor));
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

        // Reset button colors and enable options (handled within setOptionsEnabled)
        setOptionsEnabled(true);

        // Update UI Text
        questionText.setText(currentQ.questionText);

        // Populate options
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
     * Starts the countdown timer for the quiz.
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
                Toast.makeText(Quizzes1.this, "Time's up! Submitting answers.", Toast.LENGTH_LONG).show();
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
            encouragement = "Perfect Score! You're a networking pro!";
        } else if (score >= quizQuestions.size() * 0.7) {
            encouragement = "Great Job! Solid understanding of the material.";
        } else {
            encouragement = "Keep studying! Review Modules 1-5 for better results.";
        }

        // You can use a Toast to give an immediate summary before the screen loads
        Toast.makeText(this, encouragement, Toast.LENGTH_LONG).show();
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
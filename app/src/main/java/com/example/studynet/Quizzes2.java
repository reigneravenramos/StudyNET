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

public class Quizzes2 extends AppCompatActivity {

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

    // Default color for options (Indigo/Blue)
    private final int DEFAULT_COLOR = Color.parseColor("#4F46E5");
    // Color definitions for feedback
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
        setContentView(R.layout.activity_quizzes2);

        // 1. Initialize Questions for Modules 6-10
        initializeQuestions();

        // 2. Map UI Elements
        mapUIElements();

        // 3. Set up Listeners and Start Quiz
        setupListeners();
        startTimer();
        loadQuestion();
    }

    /**
     * Defines the 10 quiz questions related to Networking Modules 6-10.
     */
    private void initializeQuestions() {
        quizQuestions = new ArrayList<>();

        // Q1: Module 6 (OSI Model)
        quizQuestions.add(new Question(
                "Which layer of the **OSI model** is responsible for providing communication services to the application process?",
                new String[]{"Data Link Layer", "Transport Layer", "Application Layer", "Network Layer"},
                2));

        // Q2: Module 6 (TCP/IP vs OSI)
        quizQuestions.add(new Question(
                "In the **TCP/IP model**, which layer combines the functions of the OSI model's Session, Presentation, and Application layers?",
                new String[]{"Internet Layer", "Access Layer", "Transport Layer", "Application Layer"},
                3));

        // Q3: Module 7 (Data Encapsulation)
        quizQuestions.add(new Question(
                "What is the process of adding protocol control information to data, moving from the Application Layer down to the Physical Layer, called?",
                new String[]{"De-encapsulation", "Segmentation", "Encapsulation", "Multiplexing"},
                2));

        // Q4: Module 7 (PDU at Transport Layer)
        quizQuestions.add(new Question(
                "The Protocol Data Unit (**PDU**) at the **Transport Layer** is typically referred to as a:",
                new String[]{"Frame", "Packet", "Segment", "Bit"},
                2));

        // Q5: Module 8 (TCP Role)
        quizQuestions.add(new Question(
                "What is the main function of the **Transmission Control Protocol (TCP)**?",
                new String[]{"Best-effort, connectionless delivery", "Handling physical addressing of devices", "Ensuring reliable, connection-oriented delivery", "Translating domain names to IP addresses"},
                2));

        // Q6: Module 8 (IP Addressing)
        quizQuestions.add(new Question(
                "Which of the following is responsible for providing unique, logical addresses for devices on an internetwork?",
                new String[]{"Ethernet", "Internet Protocol (IP)", "Address Resolution Protocol (ARP)", "User Datagram Protocol (UDP)"},
                1));

        // Q7: Module 9 (Hubs)
        quizQuestions.add(new Question(
                "A network **Hub** operates primarily at which layer of the OSI model, simply regenerating signals to all connected devices?",
                new String[]{"Data Link Layer", "Physical Layer", "Network Layer", "Transport Layer"},
                1));

        // Q8: Module 9 (Switches)
        quizQuestions.add(new Question(
                "What is the key functionality of a network **Switch** that distinguishes it from a hub?",
                new String[]{"It converts analog signals to digital", "It forwards traffic based on IP addresses", "It makes forwarding decisions based on MAC addresses", "It provides a wireless access point"},
                2));

        // Q9: Module 10 (Router)
        quizQuestions.add(new Question(
                "A **Router** operates at which layer of the OSI model and is responsible for determining the best path for data across multiple networks?",
                new String[]{"Data Link Layer", "Physical Layer", "Network Layer", "Application Layer"},
                2));

        // Q10: Module 10 (Firewall Purpose)
        quizQuestions.add(new Question(
                "The primary purpose of a **Firewall** in a network is to:",
                new String[]{"Increase network bandwidth", "Translate private IP addresses to public ones", "Enforce security policies and control access to resources", "Prioritize voice and video traffic"},
                2));
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
     * **FIX:** Uses setBackgroundTintList to properly apply colors to Material buttons.
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
            optionButtons.get(selectedIndex).setBackgroundTintList(ColorStateList.valueOf(CORRECT_COLOR));
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
        } else {
            // Incorrect answer
            optionButtons.get(selectedIndex).setBackgroundTintList(ColorStateList.valueOf(INCORRECT_COLOR));
            // Highlight the correct answer with a neutral highlight color
            optionButtons.get(correctIndex).setBackgroundTintList(ColorStateList.valueOf(NEUTRAL_HIGHLIGHT_COLOR));
            Toast.makeText(this, "Incorrect. Correct answer is highlighted in Orange.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Enables or disables the option buttons.
     * **FIX:** Uses setBackgroundTintList to reset the color to default when enabling.
     * @param enabled True to enable, false to disable.
     */
    private void setOptionsEnabled(boolean enabled) {
        for (Button button : optionButtons) {
            button.setEnabled(enabled);

            if(enabled) {
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
                Toast.makeText(Quizzes2.this, "Time's up! Submitting answers.", Toast.LENGTH_LONG).show();
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
            encouragement = "Perfect Score! You've mastered all the networking fundamentals!";
        } else if (score >= quizQuestions.size() * 0.7) {
            encouragement = "Excellent Work! You have a strong grasp of Modules 6-10.";
        } else {
            encouragement = "Good effort! Take some time to review the OSI model and network devices.";
        }

        // Provide an immediate summary via Toast
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
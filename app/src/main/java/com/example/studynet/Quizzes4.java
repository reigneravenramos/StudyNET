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

public class Quizzes4 extends AppCompatActivity {

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
        // Correctly referencing the layout activity_quizzes4
        setContentView(R.layout.activity_quizzes4);

        // 1. Initialize Questions (Module 4: Servers and Clients)
        initializeQuestions();

        // 2. Map UI Elements
        mapUIElements();

        // 3. Set up Listeners and Start Quiz
        setupListeners();
        startTimer();
        loadQuestion();
    }

    /**
     * Defines the 10 quiz questions based on Module 4: Servers and Clients.
     */
    private void initializeQuestions() {
        quizQuestions = new ArrayList<>();

        // Original 5 Questions
        // Q1: Server Definition
        quizQuestions.add(new Question(
                "What is the **sole task** of a dedicated Server Computer?",
                new String[]{"To run desktop versions of Windows efficiently", "To provide shared resources like hard drives and printers", "To act primarily as a client", "To manage user application installations"},
                1));

        // Q2: Network Components (What makes a Network Tick)
        quizQuestions.add(new Question(
                "Which device is **required** for networks built specifically with twisted-pair cabling?",
                new String[]{"Network Interface Card (NIC)", "Network cable", "Network switch", "Dedicated server"},
                2));

        // Q3: Server Types (Collaboration)
        quizQuestions.add(new Question(
                "Which server type, sometimes called 'groupware,' is designed to enable users to collaborate regardless of location via the Internet or intranet?",
                new String[]{"Application Server", "List Server", "Collaboration Server", "Real-Time Communication Server"},
                2));

        // Q4: Server Types (Proxy)
        quizQuestions.add(new Question(
                "Which server sits between a client program (like a Web browser) and an external server to filter requests, improve performance, and share connections?",
                new String[]{"Web Server", "FTP Server", "Mail Server", "Proxy Server"},
                3));

        // Q5: Network Components (Interface)
        quizQuestions.add(new Question(
                "The special electronic circuit inside any computer attached to a network, which the network cable plugs into, is called the:",
                new String[]{"Network cable", "Network switch", "Network interface", "Client Server"},
                2));

        // --- 5 New Questions for a total of 10 ---

        // Q6: Client Definition
        quizQuestions.add(new Question(
                "In a client/server network architecture, what is the primary role of a **client**?",
                new String[]{"To centrally manage all user accounts and data.", "To request resources or services from a server.", "To ensure network security and firewall management.", "To route data packets between different networks."},
                1));

        // Q7: File Server
        quizQuestions.add(new Question(
                "Which type of server is primarily responsible for storing, retrieving, and managing user files and folders in a centralized location?",
                new String[]{"Print Server", "Database Server", "File Server", "Communication Server"},
                2));

        // Q8: Network Cable Role
        quizQuestions.add(new Question(
                "What is the fundamental function of network media (like Ethernet cables or Wi-Fi radio waves) in a network?",
                new String[]{"To translate IP addresses to URLs.", "To manage user permissions.", "To provide the physical medium for data transmission.", "To compress large files."},
                2));

        // Q9: Web Server Protocol
        quizQuestions.add(new Question(
                "What protocol is used by a Web Server to transmit hypermedia documents (like HTML pages) to a client's web browser?",
                new String[]{"FTP", "SMTP", "HTTP/HTTPS", "POP3"},
                2));

        // Q10: NIC Unique Identifier
        quizQuestions.add(new Question(
                "The Network Interface Card (NIC) embeds a unique, burned-in hardware address that identifies the specific device on a network. This address is known as the:",
                new String[]{"Domain Name Service (DNS)", "Internet Protocol (IP) Address", "Subnet Mask", "Media Access Control (MAC) Address"},
                3));


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
                Toast.makeText(Quizzes4.this, "Time's up! Submitting answers.", Toast.LENGTH_LONG).show();
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
            encouragement = "Perfect Score! You're a master of servers and networking components!";
        } else if (score >= quizQuestions.size() * 0.7) {
            encouragement = "Excellent score! You clearly understand the role of servers and network infrastructure.";
        } else {
            encouragement = "Good attempt! Review the different server types and core network components for the next round.";
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
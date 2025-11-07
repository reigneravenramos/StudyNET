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

public class Quizzes3 extends AppCompatActivity {

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
        setContentView(R.layout.activity_quizzes3);

        // 1. Initialize Questions for Modules 11-15
        initializeQuestions();

        // 2. Map UI Elements
        mapUIElements();

        // 3. Set up Listeners and Start Quiz
        setupListeners();
        startTimer();
        loadQuestion();
    }

    /**
     * Defines the 10 quiz questions related to Networking Modules 11-15 (IP Addressing, Protocols, Security).
     */
    private void initializeQuestions() {
        quizQuestions = new ArrayList<>();

        // Q1: Subnetting/IP Addressing
        quizQuestions.add(new Question(
                "Which IP address range is reserved for **Class C Private Networks** (RFC 1918)?",
                new String[]{"10.0.0.0 to 10.255.255.255", "172.16.0.0 to 172.31.255.255", "192.168.0.0 to 192.168.255.255", "127.0.0.0 to 127.255.255.255"},
                2));

        // Q2: DHCP (Dynamic Host Configuration Protocol)
        quizQuestions.add(new Question(
                "What is the main function of **DHCP**?",
                new String[]{"To resolve domain names to IP addresses", "To assign IP addresses dynamically to client devices", "To provide secure communication over the internet", "To translate private IP addresses to public IP addresses"},
                1));

        // Q3: DNS (Domain Name System)
        quizQuestions.add(new Question(
                "What is the primary record type used by **DNS** to map a domain name (like example.com) to an IPv4 address?",
                new String[]{"MX Record", "PTR Record", "A Record", "CNAME Record"},
                2));

        // Q4: NAT (Network Address Translation)
        quizQuestions.add(new Question(
                "Where is **NAT (Network Address Translation)** typically performed in a home or small business network?",
                new String[]{"On the web server", "On the client computer", "On the local switch", "On the boundary router/gateway"},
                3));

        // Q5: Wireless Networks (Standard)
        quizQuestions.add(new Question(
                "What IEEE standard defines the specifications for Wireless Local Area Networks (**WLANs**)?",
                new String[]{"802.3", "802.11", "802.1Q", "802.1X"},
                1));

        // Q6: Security (Encryption)
        quizQuestions.add(new Question(
                "Which is the strongest and most current encryption protocol recommended for securing wireless networks?",
                new String[]{"WEP", "WPA", "WPA2/WPA3", "WPA-TKIP"},
                2));

        // Q7: Troubleshooting (Utility)
        quizQuestions.add(new Question(
                "Which command-line utility is used to display the path and measure transit delays of packets across an IP network?",
                new String[]{"ipconfig", "ping", "netstat", "traceroute (or tracert)"},
                3));

        // Q8: Cloud Networking
        quizQuestions.add(new Question(
                "In cloud networking, which service model provides the customer with access to computing resources (VMs, storage) but manages the operating system and above?",
                new String[]{"Software as a Service (SaaS)", "Platform as a Service (PaaS)", "Infrastructure as a Service (IaaS)", "Everything as a Service (XaaS)"},
                1));

        // Q9: IoT Networking
        quizQuestions.add(new Question(
                "A key challenge for **IoT networks** compared to traditional enterprise networks is the need for protocols that support:",
                new String[]{"High bandwidth, low latency", "High encryption, large packet size", "Low power, small memory footprint", "High complexity, centralized control"},
                2));

        // Q10: Transmission Media
        quizQuestions.add(new Question(
                "What is the primary advantage of using **Fiber Optic Cable** over Copper Ethernet Cable for long-distance data transmission?",
                new String[]{"Lower cost and easier installation", "Resistance to electromagnetic interference (EMI) and longer distance support", "Better physical durability", "Ability to carry electrical power"},
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
     * Uses setBackgroundTintList to properly apply colors to Material buttons.
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
     * Uses setBackgroundTintList to reset the color to default when enabling.
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
                Toast.makeText(Quizzes3.this, "Time's up! Submitting answers.", Toast.LENGTH_LONG).show();
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
            encouragement = "Perfect Score! You're a true networking master!";
        } else if (score >= quizQuestions.size() * 0.7) {
            encouragement = "Excellent score! You have a solid grasp of IP addressing and protocols.";
        } else {
            encouragement = "Good attempt! Focus on IP subnets and security concepts for the next round.";
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
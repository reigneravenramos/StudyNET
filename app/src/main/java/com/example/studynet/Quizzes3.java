package com.example.studynet;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;
import android.content.res.ColorStateList;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Quizzes3 extends AppCompatActivity {

    private TextView timerText, progressText, questionText, finalScoreText, resultMessageText;
    private Button optionA, optionB, optionC, optionD, nextButton, finishButton;
    private ArrayList<Button> optionButtons;
    private ScrollView quizContentScrollView;
    private LinearLayout resultLayout;

    private ArrayList<Question> quizQuestions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private CountDownTimer countDownTimer;
    private final long TOTAL_TIME_MS = 600000;

    private final int DEFAULT_COLOR = Color.parseColor("#6397D0");
    private final int CORRECT_COLOR = Color.parseColor("#10B981");
    private final int INCORRECT_COLOR = Color.parseColor("#EF4444");
    private final int NEUTRAL_HIGHLIGHT_COLOR = Color.parseColor("#F59E0B");

    private static class Question {
        String questionText;
        String[] options;
        int correctAnswerIndex;

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

        initializeQuestions();
        mapUIElements();
        setupListeners();
        startTimer();
        loadQuestion();

        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
    }

    private void initializeQuestions() {
        quizQuestions = new ArrayList<>();
        quizQuestions.add(new Question("Which IP range is reserved for Class C Private Networks?", new String[]{"10.0.0.0/8", "172.16.0.0/12", "192.168.0.0/16", "127.0.0.0/8"}, 2));
        quizQuestions.add(new Question("What is the main function of DHCP?", new String[]{"DNS resolution", "Dynamic IP assignment", "Secure communication", "NAT translation"}, 1));
        quizQuestions.add(new Question("What DNS record maps a domain to an IPv4 address?", new String[]{"MX", "PTR", "A", "CNAME"}, 2));
        quizQuestions.add(new Question("Where is NAT typically performed?", new String[]{"Web server", "Client PC", "Local switch", "Boundary router"}, 3));
        quizQuestions.add(new Question("What IEEE standard defines WLANs?", new String[]{"802.3", "802.11", "802.1Q", "802.1X"}, 1));
        quizQuestions.add(new Question("Strongest current wireless encryption protocol?", new String[]{"WEP", "WPA", "WPA2/WPA3", "WPA-TKIP"}, 2));
        quizQuestions.add(new Question("Utility to display path across an IP network?", new String[]{"ipconfig", "ping", "netstat", "traceroute"}, 3));
        quizQuestions.add(new Question("Cloud model providing resources like VMs?", new String[]{"SaaS", "PaaS", "IaaS", "XaaS"}, 2));
        quizQuestions.add(new Question("Key challenge for IoT networking?", new String[]{"High bandwidth", "High encryption", "Low power footprint", "Centralized control"}, 2));
        quizQuestions.add(new Question("Advantage of Fiber Optic over Copper?", new String[]{"Lower cost", "EMI resistance", "Physical durability", "Power delivery"}, 1));
    }

    private void mapUIElements() {
        quizContentScrollView = findViewById(R.id.quiz_content_scrollview);
        timerText = findViewById(R.id.timer_text);
        progressText = findViewById(R.id.progress_text);
        questionText = findViewById(R.id.question_text);
        optionA = findViewById(R.id.option_a); optionB = findViewById(R.id.option_b);
        optionC = findViewById(R.id.option_c); optionD = findViewById(R.id.option_d);
        nextButton = findViewById(R.id.next_button);
        optionButtons = new ArrayList<>();
        optionButtons.add(optionA); optionButtons.add(optionB); optionButtons.add(optionC); optionButtons.add(optionD);
        resultLayout = findViewById(R.id.result_layout);
        finalScoreText = findViewById(R.id.final_score_text);
        resultMessageText = findViewById(R.id.result_message_text);
        finishButton = findViewById(R.id.finish_button);
    }

    private void setupListeners() {
        for (int i = 0; i < optionButtons.size(); i++) {
            final int idx = i;
            optionButtons.get(i).setOnClickListener(v -> handleAnswerSelection(idx));
        }
        nextButton.setOnClickListener(v -> nextQuestion());
        finishButton.setOnClickListener(v -> finish());
    }

    private void handleAnswerSelection(int idx) {
        setOptionsEnabled(false);
        nextButton.setVisibility(View.VISIBLE);
        int correct = quizQuestions.get(currentQuestionIndex).correctAnswerIndex;
        if (idx == correct) {
            score++;
            optionButtons.get(idx).setBackgroundTintList(ColorStateList.valueOf(CORRECT_COLOR));
        } else {
            optionButtons.get(idx).setBackgroundTintList(ColorStateList.valueOf(INCORRECT_COLOR));
            optionButtons.get(correct).setBackgroundTintList(ColorStateList.valueOf(NEUTRAL_HIGHLIGHT_COLOR));
        }
    }

    private void setOptionsEnabled(boolean enabled) {
        for (Button b : optionButtons) {
            b.setEnabled(enabled);
            if(enabled) b.setBackgroundTintList(ColorStateList.valueOf(DEFAULT_COLOR));
        }
    }

    private void nextQuestion() {
        currentQuestionIndex++;
        nextButton.setVisibility(View.GONE);
        if (currentQuestionIndex < quizQuestions.size()) loadQuestion();
        else finishQuiz();
    }

    private void loadQuestion() {
        Question q = quizQuestions.get(currentQuestionIndex);
        setOptionsEnabled(true);
        questionText.setText(q.questionText);
        optionA.setText("A. " + q.options[0]); optionB.setText("B. " + q.options[1]);
        optionC.setText("C. " + q.options[2]); optionD.setText("D. " + q.options[3]);
        progressText.setText((currentQuestionIndex + 1) + " / " + quizQuestions.size());
        nextButton.setText(currentQuestionIndex == quizQuestions.size() - 1 ? "Finish Quiz" : "Next Question");
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(TOTAL_TIME_MS, 1000) {
            @Override
            public void onTick(long ms) {
                timerText.setText(String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(ms), TimeUnit.MILLISECONDS.toSeconds(ms) % 60));
                if (ms < 60000) timerText.setTextColor(Color.parseColor("#EF4444"));
            }
            @Override
            public void onFinish() { finishQuiz(); }
        }.start();
    }

    private void finishQuiz() {
        if (countDownTimer != null) countDownTimer.cancel();
        quizContentScrollView.setVisibility(View.GONE);
        resultLayout.setVisibility(View.VISIBLE);
        finalScoreText.setText("Your Score: " + score + "/" + quizQuestions.size());
        if (resultMessageText != null) {
            resultMessageText.setText("No matter what the result is, I know you did your best! Numbers don’t define your skills, what matters is what you’ve learned and how you use it. Keep swimming through knowledge, and remember… Sealbert says: “Smack your belly, give a happy squeak, and keep being seal-tastic!”");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) countDownTimer.cancel();
    }
}
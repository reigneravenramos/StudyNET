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

public class Quizzes2 extends AppCompatActivity {

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
        setContentView(R.layout.activity_quizzes2);

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
        quizQuestions.add(new Question("Which layer of the OSI model provides communication services to the application process?", new String[]{"Data Link", "Transport", "Application", "Network"}, 2));
        quizQuestions.add(new Question("In the TCP/IP model, which layer combines OSI Session, Presentation, and Application layers?", new String[]{"Internet", "Access", "Transport", "Application"}, 3));
        quizQuestions.add(new Question("What is the process of adding protocol control info to data called?", new String[]{"De-encapsulation", "Segmentation", "Encapsulation", "Multiplexing"}, 2));
        quizQuestions.add(new Question("The PDU at the Transport Layer is referred to as a:", new String[]{"Frame", "Packet", "Segment", "Bit"}, 2));
        quizQuestions.add(new Question("What is the main function of TCP?", new String[]{"Best-effort delivery", "Physical addressing", "Reliable delivery", "Domain translation"}, 2));
        quizQuestions.add(new Question("Which provides unique, logical addresses for devices?", new String[]{"Ethernet", "IP", "ARP", "UDP"}, 1));
        quizQuestions.add(new Question("A Hub operates at which layer of the OSI model?", new String[]{"Data Link", "Physical", "Network", "Transport"}, 1));
        quizQuestions.add(new Question("What distinguishes a Switch from a Hub?", new String[]{"Analog conversion", "IP forwarding", "MAC forwarding", "Wireless access"}, 2));
        quizQuestions.add(new Question("A Router operates at which layer of the OSI model?", new String[]{"Data Link", "Physical", "Network", "Application"}, 2));
        quizQuestions.add(new Question("The primary purpose of a Firewall is to:", new String[]{"Increase bandwidth", "Translate IP", "Enforce security", "Prioritize traffic"}, 2));
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
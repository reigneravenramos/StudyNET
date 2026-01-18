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

public class Quizzes1 extends AppCompatActivity {

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
        setContentView(R.layout.activity_quizzes1);

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
        quizQuestions.add(new Question("What does the acronym **LAN** stand for?", new String[]{"Local Access Network", "Local Area Network", "Large Area Node", "Limited Access Notation"}, 1));
        quizQuestions.add(new Question("If a computer on a network is described as **'Down'**, what state is it in?", new String[]{"Connected to the network", "Computer is turned off", "Resources are outside the computer", "Not connected to the network"}, 1));
        quizQuestions.add(new Question("Which network type exists over a **large-scale geographical area**?", new String[]{"LAN", "CAN", "WAN", "WLAN"}, 2));
        quizQuestions.add(new Question("Which of these is NOT an example of a **shared resource**?", new String[]{"A Laser Printer", "A File Server", "A hard drive set as shared", "A personal computer's local OS"}, 3));
        quizQuestions.add(new Question("A **Server Computer** provides shared resources to be accessed by what?", new String[]{"Intermediary devices", "Transmission media", "Network client computers", "Other servers"}, 2));
        quizQuestions.add(new Question("What is the special electronic circuit **inside any computer** attached to a network?", new String[]{"Network switch", "Network cable", "Network interface", "Application server"}, 2));
        quizQuestions.add(new Question("What is the core function of a **Web Server**?", new String[]{"To move and store mail", "To serve static content to a Web browser", "To manage interactive mailing lists", "To enable remote logon"}, 1));
        quizQuestions.add(new Question("In network communication, the **'channel'** refers to the:", new String[]{"Message source", "Destination", "Media that provides the pathway", "Communication protocol"}, 2));
        quizQuestions.add(new Question("What is the process used to **interleave pieces of separate conversations**?", new String[]{"Segmentation", "Encapsulation", "Protocol layering", "Multiplexing"}, 3));
        quizQuestions.add(new Question("Which protocol manages individual conversations between web servers and clients?", new String[]{"HTTP", "TCP", "IP", "Ethernet"}, 1));
    }

    private void mapUIElements() {
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

        resultLayout = findViewById(R.id.result_layout);
        finalScoreText = findViewById(R.id.final_score_text);
        resultMessageText = findViewById(R.id.result_message_text);
        finishButton = findViewById(R.id.finish_button);
    }

    private void setupListeners() {
        for (int i = 0; i < optionButtons.size(); i++) {
            final int optionIndex = i;
            optionButtons.get(i).setOnClickListener(v -> handleAnswerSelection(optionIndex));
        }
        nextButton.setOnClickListener(v -> nextQuestion());
        finishButton.setOnClickListener(v -> finish());
    }

    private void handleAnswerSelection(int selectedIndex) {
        setOptionsEnabled(false);
        nextButton.setVisibility(View.VISIBLE);
        Question currentQ = quizQuestions.get(currentQuestionIndex);
        int correctIndex = currentQ.correctAnswerIndex;

        if (selectedIndex == correctIndex) {
            score++;
            optionButtons.get(selectedIndex).setBackgroundTintList(ColorStateList.valueOf(CORRECT_COLOR));
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
        } else {
            optionButtons.get(selectedIndex).setBackgroundTintList(ColorStateList.valueOf(INCORRECT_COLOR));
            optionButtons.get(correctIndex).setBackgroundTintList(ColorStateList.valueOf(NEUTRAL_HIGHLIGHT_COLOR));
            Toast.makeText(this, "Incorrect.", Toast.LENGTH_LONG).show();
        }
    }

    private void setOptionsEnabled(boolean enabled) {
        for (Button button : optionButtons) {
            button.setEnabled(enabled);
            if(enabled) {
                button.setBackgroundTintList(ColorStateList.valueOf(DEFAULT_COLOR));
            }
        }
    }

    private void nextQuestion() {
        currentQuestionIndex++;
        nextButton.setVisibility(View.GONE);
        if (currentQuestionIndex < quizQuestions.size()) {
            loadQuestion();
        } else {
            finishQuiz();
        }
    }

    private void loadQuestion() {
        Question currentQ = quizQuestions.get(currentQuestionIndex);
        setOptionsEnabled(true);
        questionText.setText(currentQ.questionText);
        optionA.setText("A. " + currentQ.options[0]);
        optionB.setText("B. " + currentQ.options[1]);
        optionC.setText("C. " + currentQ.options[2]);
        optionD.setText("D. " + currentQ.options[3]);
        progressText.setText((currentQuestionIndex + 1) + " / " + quizQuestions.size());
        if (currentQuestionIndex == quizQuestions.size() - 1) {
            nextButton.setText("Finish Quiz");
        } else {
            nextButton.setText("Next Question");
        }
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
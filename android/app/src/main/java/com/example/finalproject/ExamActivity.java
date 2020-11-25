package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.finalproject.api.ToeicAPI;
import com.example.finalproject.databinding.ActivityExamBinding;
import com.example.finalproject.fragment.*;
import com.example.finalproject.models.Course;
import com.example.finalproject.models.MyDatabase;
import com.example.finalproject.models.Progress;
import com.example.finalproject.models.Question;
import com.example.finalproject.models.Record;
import com.example.finalproject.models.User;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ExamActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener,
        View.OnClickListener {
    private ActivityExamBinding binding;
    private MediaPlayer mediaPlayer;
    private Map<Integer, String> answer;
    private List<Question> questions;
    private List<String> tripleSelected;
    private String selected = "";
    private Timer timer;
    private int time = 2 * 60 * 60 * 1000;
    private int currentQuest = 0;
    private long courseID = -1;
    private int currentPart = 1;
    private int currentAudio;
    private int score = 0;
    private boolean isCounting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExamBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initMediaPlayer();

        Course course = getIntent().getParcelableExtra("course");
        courseID = course.getId();

        binding.btnClick.setOnClickListener(this);
        binding.back.setOnClickListener(this);
        binding.next.setOnClickListener(this);
        binding.btnClick.setEnabled(false);

        binding.txtTitle.setText(course.getName());
        binding.questionId.setText(getString(R.string.number_question, 1));

        answer = new HashMap<>();
        questions = new ArrayList<>();
        tripleSelected = new ArrayList<>();

        MyDatabase.getInstance(this).courseDAO().getCourseAndQuestion(courseID).
                forEach(c -> this.questions.addAll(c.questions));

        if (questions.size() != 0) {
            if (!checkProgress())
                showSelectMode();
        } else {
            Toast.makeText(this, getString(R.string.course_notfound), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        new Thread(() -> {
            List<Record> records = MyDatabase.getInstance(this).recordDAO().getAll();
            records.forEach(r -> ToeicAPI.updateRecord(this, r));
        }).start();

        super.onStart();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null && mediaPlayer.isPlaying())
            mediaPlayer.stop();
        saveProgress(false);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        Class fragmentClass = null;

        switch (v.getId()) {
            case R.id.btnClick:
                displayQuestion(currentQuest);

                // insert answer and update the current quest number
                if (currentPart == 3 || currentPart == 4 || currentPart == 7) {
                    tripleSelected.forEach(s -> {
                        answer.put(currentQuest + 1, s);
                        currentQuest += 1;
                    });
                } else {
                    answer.put(currentQuest + 1, selected);
                    currentQuest++;
                }

                if (currentQuest >= questions.size()) {
                    showResultDialog(false);
                    return;
                }

                currentPart = questions.get(currentQuest).getPart();

                binding.questionId.setText(getString(R.string.number_question, currentQuest + 1));
                switch (currentPart) {
                    case 1:
                        fragmentClass = Part1Fragment.class;
                        break;
                    case 2:
                        fragmentClass = Part2Fragment.class;
                        break;
                    case 3:
                    case 4:
                        fragmentClass = Part3Fragment.class;
                        break;
                    case 5:
                    case 6:
                        fragmentClass = Part5Fragment.class;
                        break;
                    case 7:
                        fragmentClass = Part7Fragment.class;
                        break;
                }
                Fragment fm = getSupportFragmentManager().findFragmentByTag("PART");

                //reuse the fragment
                if (fm instanceof Part1Fragment && currentPart == 1) {
                    ((Part1Fragment) fm).setDefaultUI();
                    ((Part1Fragment) fm).showQuestion(questions.get(currentQuest));
                } else if (fm instanceof Part2Fragment && currentPart == 2) {
                    ((Part2Fragment) fm).setDefaultUI();
                    ((Part2Fragment) fm).showQuestion(questions.get(currentQuest), currentQuest + 1);
                } else if (fm instanceof Part3Fragment && (currentPart == 3 || currentPart == 4)) {
                    Question question = questions.get(currentQuest);
                    ArrayList<Question> biQuestion = new ArrayList<>(questions.stream().filter(q -> q.getAudioFile().equals(question.getAudioFile()))
                            .collect(Collectors.toList()));

                    ((Part3Fragment) fm).setDefaultUI();
                    ((Part3Fragment) fm).showQuestion(biQuestion);
                } else if (fm instanceof Part5Fragment && (currentPart == 5 || currentPart == 6)) {
                    ((Part5Fragment) fm).setDefaultUI();
                    ((Part5Fragment) fm).showQuestion(questions.get(currentQuest));
                } else if (fm instanceof Part7Fragment && currentPart == 7) {

                    Question question = questions.get(currentQuest);
                    ArrayList<Question> biQuestion = new ArrayList<>(questions.stream()
                            .filter(q -> {
                                if (q.getDescription() != null) {
                                    if (q.getDescription().equals(question.getDescription())) {
                                        return true;
                                    }
                                    return false;
                                }
                                return false;
                            })
                            .collect(Collectors.toList()));

                    ((Part7Fragment) fm).setDefaultUI();
                    ((Part7Fragment) fm).showQuestion(biQuestion, currentQuest + 1);
                } else {
                    openFragment(fragmentClass, questions.get(currentQuest));
                }
                break;
            case R.id.back:
                showExitDialog();
                break;
        }
    }

    @Override
    protected void onStop() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            currentAudio = mediaPlayer.getCurrentPosition();
        }
        super.onStop();
    }

    @Override
    protected void onRestart() {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(currentAudio);
            mediaPlayer.start();
        }
        super.onRestart();
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    private void saveProgress(boolean isCompleted) {
        User user = MyDatabase.getInstance(this).userDAO().getFirstUser();
        Progress progress = MyDatabase.getInstance(this).progressDAO().getByCourseId(courseID);
        Boolean isOnline = NetworkController.isOnline(this);

        if (isOnline) {
            user.setScore(this.score);
            MyDatabase.getInstance(this).userDAO().updateUser(user);
        }

        if (progress == null) {
            MyDatabase.getInstance(this).progressDAO().add(new Progress(0, courseID, time, answer, isCounting));
        } else {
            if (isCompleted) return;

            progress.setQuestions(answer);
            progress.setRemainTime(time);
            progress.setCounting(isCounting);

            MyDatabase.getInstance(this).progressDAO().update(progress);
        }

        if (user != null){
            int correctAnswer = getCorrectAnswer();
            if (correctAnswer > 198)
                this.score = 990;
            else if (correctAnswer == 0)
                this.score = 0;
            else
                this.score = correctAnswer * 5;

            Record record = MyDatabase.getInstance(this).recordDAO().getByCourseIdAndAccountId(courseID, user.getAccountId());
            if (record == null){
                MyDatabase.getInstance(this).recordDAO().addRecord(new Record(0, courseID, user.getAccountId(), score));
            }else{
                score = Math.max(score, record.getScore());
                record.setScore(score);
                MyDatabase.getInstance(this).recordDAO().updateRecord(record);
            }
        }
    }

    private void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build());

        mediaPlayer.setOnPreparedListener(this);
    }

    private boolean checkProgress() {
        Progress progress = MyDatabase.getInstance(this).progressDAO().getByCourseId(courseID);

        if (progress != null && progress.getQuestions().size() < 200) {
            showProgressDialog(progress);
            return true;
        }else if (progress != null && progress.getQuestions().size() >= 200){
            showResultDialog(true);
            return true;
        }
        return false;
    }

    private void openFragment(Class fragmentClass, Question question) {
        try {
            FragmentManager fm = getSupportFragmentManager();

            if (fragmentClass.newInstance() instanceof Part1Fragment) {
                Part1Fragment fragment = Part1Fragment.newInstance(question, "");
                fm.beginTransaction().replace(R.id.framelayout, fragment, "PART").commit();
            } else if (fragmentClass.newInstance() instanceof Part2Fragment) {
                Part2Fragment fragment = Part2Fragment.newInstance(question, questions.indexOf(question) + 1);
                fm.beginTransaction().replace(R.id.framelayout, fragment, "PART").commit();
            } else if (fragmentClass.newInstance() instanceof Part3Fragment) {
                ArrayList<Question> biQuestion = new ArrayList<>(questions.stream().filter(q -> q.getAudioFile().equals(question.getAudioFile()))
                        .collect(Collectors.toList()));
                Part3Fragment fragment = Part3Fragment.newInstance(biQuestion, "");
                fm.beginTransaction().replace(R.id.framelayout, fragment, "PART").commit();
            } else if (fragmentClass.newInstance() instanceof Part5Fragment) {
                Part5Fragment fragment = Part5Fragment.newInstance(question, "");
                fm.beginTransaction().replace(R.id.framelayout, fragment, "PART").commit();
            } else if (fragmentClass.newInstance() instanceof Part7Fragment) {
                ArrayList<Question> biQuestion = new ArrayList<>(questions.stream()
                        .filter(q -> {
                            if (q.getDescription() != null) {
                                if (q.getDescription().equals(question.getDescription())) {
                                    return true;
                                }
                                return false;
                            }
                            return false;
                        })
                        .collect(Collectors.toList()));

                Part7Fragment fragment = Part7Fragment.newInstance(biQuestion, currentQuest + 1);
                fm.beginTransaction().replace(R.id.framelayout, fragment, "PART").commit();
            }
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    private void startCountDown() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    if (time > 0) {
                        binding.pbTime.setProgress(time);
                        binding.txtDisplayTime.setText(convertTime(time));
                        time -= 1000;
                    } else {
                        binding.pbTime.setProgress(0);
                        binding.txtDisplayTime.setText(convertTime(0));
                        showResultDialog(false);
                    }
                });
            }
        }, 0, 1000);
    }

    private int getCorrectAnswer() {
        int correctAnswer = 0;
        Map<Integer, String> recordAnswer = MyDatabase.getInstance(this).progressDAO().getByCourseId(courseID)
                .getQuestions();

        for (int i = 0; i < 200; i++) {
            if (questions.get(i).getAnswer().equals(recordAnswer.get(i + 1)))
                correctAnswer++;
        }

        if (correctAnswer > 198)
            this.score = 990;
        else if (correctAnswer == 0)
            this.score = 0;
        else
            this.score = correctAnswer * 5;

        return correctAnswer;
    }

    private @NotNull String convertTime(long milliseconds) {
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milliseconds),
                TimeUnit.MILLISECONDS.toMinutes(milliseconds) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliseconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void startListening(String path) {
        try {
            AssetFileDescriptor afd = getAssets().openFd(path);
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.reset();
            }
            try {
                mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayQuestion(int numQuestion) {
        binding.questionId.setText(getString(R.string.number_question, numQuestion + 1));
        currentPart = questions.get(numQuestion).getPart();
    }

    private void showProgressDialog(Progress progress) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.progress_dialog_title))
                .setMessage(getString(R.string.progress_dialog_message))
                .setPositiveButton(getString(R.string.yes), ((dialog, which) -> {
                    int size = progress.getQuestions().size();
                    this.time = progress.getRemainTime();
                    this.answer = progress.getQuestions();
                    this.isCounting = progress.isCounting();

                    if (isCounting)
                        startCountDown();
                    else
                        setUIWithoutCountingMode();

                    if (time <= 0) return;
                    displayQuestion(size-1);

                    answer.putAll(progress.getQuestions());
                    currentPart = questions.get(size-1).getPart();
                    currentQuest = size;
                    switch (currentPart) {
                        case 1:
                            openFragment(Part1Fragment.class, questions.get(currentQuest));
                            break;
                        case 2:
                            openFragment(Part2Fragment.class, questions.get(currentQuest));
                            break;
                        case 3:
                        case 4:
                            openFragment(Part3Fragment.class, questions.get(currentQuest));
                            break;
                        case 5:
                        case 6:
                            openFragment(Part5Fragment.class, questions.get(currentQuest));
                            break;
                        case 7:
                            openFragment(Part7Fragment.class, questions.get(currentQuest));
                            break;
                    }
                }))
                .setNegativeButton(getString(R.string.no), ((dialog, which) -> {
                    dialog.dismiss();
                    showSelectMode();
//                    startTesting();
                }))
                .setCancelable(false)
                .create().show();
    }

    private void showResultDialog(boolean isCompleted) {
        binding.btnClick.setEnabled(false);

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        saveProgress(isCompleted);
        int correctAnswer = getCorrectAnswer();

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.result_test_dialog);
        dialog.setTitle("RESULT");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(dialog1 -> finish());

        TextInputLayout correct = dialog.findViewById(R.id.txtLayoutCorrect);
        TextInputLayout scoreLayout = dialog.findViewById(R.id.txtLayoutScore);
        TextInputLayout mode = dialog.findViewById(R.id.txtLayoutMode);

        Button btnNo = dialog.findViewById(R.id.btnNo);
        Button btnYes = dialog.findViewById(R.id.btnYes);

        correct.getEditText().setText(correctAnswer + "/" + 200);
        scoreLayout.getEditText().setText(score + "/" + 990);
        mode.getEditText().setText(isCounting ? getString(R.string.counting_time) : getString(R.string.without_counting_time));

        btnNo.setOnClickListener(v -> {
            dialog.dismiss();
            finish();
        });
        btnYes.setOnClickListener(v -> {
            dialog.dismiss();

            time = 2 * 60 * 60 * 1000;
            currentQuest = 0;
            currentPart = 1;
            score = 0;
            answer = new HashMap<>();
            tripleSelected = new ArrayList<>();

            initMediaPlayer();
            showSelectMode();
        });
        dialog.show();
    }

    private void showExitDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.exit_testing_title))
                .setMessage(getString(R.string.exit_testing_message))
                .setPositiveButton(getString(R.string.yes), ((dialog, which) -> {
                    saveProgress(false);
                    this.finish();
                }))
                .setNegativeButton(getString(R.string.no), ((dialog, which) -> {
                    dialog.dismiss();
                }))
                .setCancelable(false)
                .create().show();
    }

    private void showSelectMode() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.mode))
                .setPositiveButton(getString(R.string.counting_time), ((dialog, which) -> {
                    isCounting = true;
                    startTesting();
                }))
                .setNegativeButton(getString(R.string.without_counting_time), ((dialog, which) -> {
                    isCounting = false;
                    startTesting();
                }))
                .setOnCancelListener(dialog -> {
                    dialog.dismiss();
                    finish();
                })
                .create().show();
    }

    private void startTesting() {
        if (isCounting)
            startCountDown();
        else {
            setUIWithoutCountingMode();
        }
        displayQuestion(currentQuest);

        openFragment(Part1Fragment.class, questions.get(0));
    }

    public void setSelected(String answer) {
        this.selected = answer;
    }

    public void setTripleSelected(List<String> answer) {
        this.tripleSelected = answer;
    }

    public void changeButtonState(boolean isEnable) {
        binding.btnClick.setEnabled(isEnable);
    }

    private void setUIWithoutCountingMode() {
        binding.txtDisplayTime.setVisibility(View.GONE);
        binding.pbTime.setVisibility(View.GONE);
    }
}
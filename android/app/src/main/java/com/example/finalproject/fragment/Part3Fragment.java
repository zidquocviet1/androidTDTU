package com.example.finalproject.fragment;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.example.finalproject.ExamActivity;
import com.example.finalproject.R;
import com.example.finalproject.databinding.FragmentPart1Binding;
import com.example.finalproject.databinding.FragmentPart3Binding;
import com.example.finalproject.models.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Part3Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Part3Fragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentPart3Binding binding;
    private ExamActivity context;
    private String currentPathAudio;
    private List<Boolean> checkState;
    private List<String> tripleSelected;

    public Part3Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Part3Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Part3Fragment newInstance(ArrayList<Question> param1, String param2) {
        Part3Fragment fragment = new Part3Fragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPart3Binding.inflate(inflater, container, false);
        currentPathAudio = "";
        context = (ExamActivity) getActivity();

        setDefaultUI();
        showQuestion(getArguments().getParcelableArrayList(ARG_PARAM1));

        binding.txtA.setOnClickListener(this);
        binding.txtB.setOnClickListener(this);
        binding.txtC.setOnClickListener(this);
        binding.txtD.setOnClickListener(this);
        binding.txtQues2A.setOnClickListener(this);
        binding.txtQues2B.setOnClickListener(this);
        binding.txtQues2C.setOnClickListener(this);
        binding.txtQues2D.setOnClickListener(this);
        binding.txtQues3A.setOnClickListener(this);
        binding.txtQues3B.setOnClickListener(this);
        binding.txtQues3C.setOnClickListener(this);
        binding.txtQues3D.setOnClickListener(this);

        tripleSelected = new ArrayList<>(Arrays.asList("", "", ""));

        View view = binding.getRoot();
        return view;
    }

    public void setDefaultUI() {
        checkState = new ArrayList<>(Arrays.asList(false, false, false));
        binding.txtA.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
        binding.txtB.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
        binding.txtC.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
        binding.txtD.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
        binding.txtQues2A.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
        binding.txtQues2B.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
        binding.txtQues2C.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
        binding.txtQues2D.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
        binding.txtQues3A.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
        binding.txtQues3B.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
        binding.txtQues3C.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
        binding.txtQues3D.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
        context.changeButtonState(false);
    }

    public void showQuestion(ArrayList<Question> question) {
        binding.scrollView.fullScroll(ScrollView.FOCUS_UP);
        binding.question1.setText(question.get(0).getQuestion());
        binding.txtA.setText(question.get(0).getQuestionA());
        binding.txtB.setText(question.get(0).getQuestionB());
        binding.txtC.setText(question.get(0).getQuestionC());
        binding.txtD.setText(question.get(0).getQuestionD());

        binding.question2.setText(question.get(1).getQuestion());
        binding.txtQues2A.setText(question.get(1).getQuestionA());
        binding.txtQues2B.setText(question.get(1).getQuestionB());
        binding.txtQues2C.setText(question.get(1).getQuestionC());
        binding.txtQues2D.setText(question.get(1).getQuestionD());

        binding.question3.setText(question.get(2).getQuestion());
        binding.txtQues3A.setText(question.get(2).getQuestionA());
        binding.txtQues3B.setText(question.get(2).getQuestionB());
        binding.txtQues3C.setText(question.get(2).getQuestionC());
        binding.txtQues3D.setText(question.get(2).getQuestionD());

        if (!currentPathAudio.equals(question.get(0).getAudioFile())){
            context.getMediaPlayer().reset();
            currentPathAudio = question.get(0).getAudioFile();
            context.startListening(currentPathAudio);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txtA:
                binding.txtA.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_selected));
                binding.txtB.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                binding.txtC.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                binding.txtD.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                checkState.set(0, true);
                tripleSelected.set(0, binding.txtA.getText().toString());
                if (checkState.stream().noneMatch(c -> !c)){
                    context.changeButtonState(true);
                }
                context.setTripleSelected(tripleSelected);
                break;
            case R.id.txtB:
                binding.txtB.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_selected));
                binding.txtA.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                binding.txtC.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                binding.txtD.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                checkState.set(0, true);
                tripleSelected.set(0, binding.txtB.getText().toString());
                if (checkState.stream().noneMatch(c -> !c)){
                    context.changeButtonState(true);
                }
                context.setTripleSelected(tripleSelected);
                break;
            case R.id.txtC:
                binding.txtC.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_selected));
                binding.txtB.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                binding.txtA.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                binding.txtD.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                checkState.set(0, true);
                tripleSelected.set(0, binding.txtC.getText().toString());
                if (checkState.stream().noneMatch(c -> !c)){
                    context.changeButtonState(true);
                }
                context.setTripleSelected(tripleSelected);
                break;
            case R.id.txtD:
                binding.txtD.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_selected));
                binding.txtB.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                binding.txtC.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                binding.txtA.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                checkState.set(0, true);
                tripleSelected.set(0, binding.txtD.getText().toString());
                if (checkState.stream().noneMatch(c -> !c)){
                    context.changeButtonState(true);
                }
                context.setTripleSelected(tripleSelected);
                break;
            case R.id.txtQues2A:
                binding.txtQues2A.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_selected));
                binding.txtQues2B.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                binding.txtQues2C.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                binding.txtQues2D.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                checkState.set(1, true);
                tripleSelected.set(1, binding.txtQues2A.getText().toString());
                if (checkState.stream().noneMatch(c -> !c)){
                    context.changeButtonState(true);
                }
                context.setTripleSelected(tripleSelected);
                break;
            case R.id.txtQues2B:
                binding.txtQues2B.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_selected));
                binding.txtQues2A.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                binding.txtQues2C.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                binding.txtQues2D.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                checkState.set(1, true);
                tripleSelected.set(1, binding.txtQues2B.getText().toString());
                if (checkState.stream().noneMatch(c -> !c)){
                    context.changeButtonState(true);
                }
                context.setTripleSelected(tripleSelected);
                break;
            case R.id.txtQues2C:
                binding.txtQues2C.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_selected));
                binding.txtQues2B.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                binding.txtQues2A.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                binding.txtQues2D.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                checkState.set(1, true);
                tripleSelected.set(1, binding.txtQues2C.getText().toString());
                if (checkState.stream().noneMatch(c -> !c)){
                    context.changeButtonState(true);
                }
                context.setTripleSelected(tripleSelected);
                break;
            case R.id.txtQues2D:
                binding.txtQues2D.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_selected));
                binding.txtQues2B.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                binding.txtQues2C.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                binding.txtQues2A.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                checkState.set(1, true);
                tripleSelected.set(1, binding.txtQues2D.getText().toString());
                if (checkState.stream().noneMatch(c -> !c)){
                    context.changeButtonState(true);
                }
                context.setTripleSelected(tripleSelected);
                break;
            case R.id.txtQues3A:
                binding.txtQues3A.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_selected));
                binding.txtQues3B.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                binding.txtQues3C.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                binding.txtQues3D.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                checkState.set(2, true);
                tripleSelected.set(2, binding.txtQues3A.getText().toString());
                if (checkState.stream().noneMatch(c -> !c)){
                    context.changeButtonState(true);
                }
                context.setTripleSelected(tripleSelected);
                break;
            case R.id.txtQues3B:
                binding.txtQues3B.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_selected));
                binding.txtQues3A.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                binding.txtQues3C.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                binding.txtQues3D.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                checkState.set(2, true);
                tripleSelected.set(2, binding.txtQues3B.getText().toString());
                if (checkState.stream().noneMatch(c -> !c)){
                    context.changeButtonState(true);
                }
                context.setTripleSelected(tripleSelected);
                break;
            case R.id.txtQues3C:
                binding.txtQues3C.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_selected));
                binding.txtQues3B.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                binding.txtQues3A.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                binding.txtQues3D.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                checkState.set(2, true);
                tripleSelected.set(2, binding.txtQues3C.getText().toString());
                if (checkState.stream().noneMatch(c -> !c)){
                    context.changeButtonState(true);
                }
                context.setTripleSelected(tripleSelected);
                break;
            case R.id.txtQues3D:
                binding.txtQues3D.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_selected));
                binding.txtQues3B.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                binding.txtQues3C.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                binding.txtQues3A.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                checkState.set(2, true);
                tripleSelected.set(2, binding.txtQues3D.getText().toString());
                if (checkState.stream().noneMatch(c -> !c)){
                    context.changeButtonState(true);
                }
                context.setTripleSelected(tripleSelected);
                break;
        }
    }
}
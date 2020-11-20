package com.example.finalproject.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalproject.ExamActivity;
import com.example.finalproject.R;
import com.example.finalproject.databinding.FragmentPart2Binding;
import com.example.finalproject.models.Question;

import java.io.IOException;
import java.io.InputStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Part2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Part2Fragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentPart2Binding binding;
    private ExamActivity context;
    private String currentPathAudio;

    public Part2Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Part2Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Part2Fragment newInstance(Question param1, int param2) {
        Part2Fragment fragment = new Part2Fragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
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
        binding = FragmentPart2Binding.inflate(inflater, container, false);

        binding.txtA.setOnClickListener(this);
        binding.txtB.setOnClickListener(this);
        binding.txtC.setOnClickListener(this);

        currentPathAudio = "";
        context = (ExamActivity) getActivity();

        setDefaultUI();
        showQuestion(getArguments().getParcelable(ARG_PARAM1), getArguments().getInt(ARG_PARAM2));

        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.txtA:
                binding.txtA.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_selected));
                binding.txtB.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                binding.txtC.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                context.setSelected(binding.txtA.getText().toString());
                context.changeButtonState(true);
                break;
            case R.id.txtB:
                binding.txtB.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_selected));
                binding.txtA.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                binding.txtC.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                context.setSelected(binding.txtB.getText().toString());
                context.changeButtonState(true);
                break;
            case R.id.txtC:
                binding.txtC.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_selected));
                binding.txtB.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                binding.txtA.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                context.setSelected(binding.txtC.getText().toString());
                context.changeButtonState(true);
                break;
        }
    }
    public void setDefaultUI() {
        binding.txtA.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
        binding.txtB.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
        binding.txtC.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
        context.changeButtonState(false);
    }

    public void showQuestion(Question question, int numQuest) {
        binding.txtA.setText(question.getQuestionA());
        binding.txtB.setText(question.getQuestionB());
        binding.txtC.setText(question.getQuestionC());
        binding.question.setText(getString(R.string.number_question, numQuest));

        if (!currentPathAudio.equals(question.getAudioFile())){
            context.getMediaPlayer().reset();
            currentPathAudio = question.getAudioFile();
            context.startListening(currentPathAudio);
        }
    }
}
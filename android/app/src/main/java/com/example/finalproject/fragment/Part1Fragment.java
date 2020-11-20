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
import com.example.finalproject.databinding.FragmentPart1Binding;
import com.example.finalproject.models.Question;

import java.io.IOException;
import java.io.InputStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Part1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Part1Fragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentPart1Binding binding;
    private ExamActivity context;

    public Part1Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Part1Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Part1Fragment newInstance(Question param1, String param2) {
        Part1Fragment fragment = new Part1Fragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, param1);
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
        Log.e("TAG", "Fragment on create");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPart1Binding.inflate(inflater, container, false);

        binding.txtA.setOnClickListener(this);
        binding.txtB.setOnClickListener(this);
        binding.txtC.setOnClickListener(this);
        binding.txtD.setOnClickListener(this);
        context = (ExamActivity) getActivity();

        Log.e("TAG", "Fragment on createView");
        setDefaultUI();
        showQuestion(getArguments().getParcelable(ARG_PARAM1));

        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtA:
                binding.txtA.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_selected));
                binding.txtB.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                binding.txtC.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                binding.txtD.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                context.setSelected(binding.txtA.getText().toString());
                context.changeButtonState(true);
                break;
            case R.id.txtB:
                binding.txtB.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_selected));
                binding.txtA.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                binding.txtC.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                binding.txtD.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                context.setSelected(binding.txtB.getText().toString());
                context.changeButtonState(true);
                break;
            case R.id.txtC:
                binding.txtC.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_selected));
                binding.txtB.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                binding.txtA.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                binding.txtD.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                context.setSelected(binding.txtC.getText().toString());
                context.changeButtonState(true);
                break;
            case R.id.txtD:
                binding.txtD.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_selected));
                binding.txtB.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                binding.txtC.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                binding.txtA.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
                context.setSelected(binding.txtD.getText().toString());
                context.changeButtonState(true);
                break;
        }
    }

    public void setDefaultUI() {
        binding.txtA.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
        binding.txtB.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
        binding.txtC.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
        binding.txtD.setBackground(ContextCompat.getDrawable(context, R.drawable.answer_1));
        context.changeButtonState(false);
    }

    public void showQuestion(Question question) {
        if (context.getMediaPlayer() != null && context.getMediaPlayer().isPlaying()) {
            context.getMediaPlayer().stop();
        }
        try {
            if (question.getDescription() != null) {
                InputStream is = context.getAssets().open(question.getDescription());
                Drawable drawable = Drawable.createFromStream(is, null);
                binding.imageQuestion.setImageDrawable(drawable);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        binding.txtA.setText(question.getQuestionA());
        binding.txtB.setText(question.getQuestionB());
        binding.txtC.setText(question.getQuestionC());
        binding.txtD.setText(question.getQuestionD());

        if (context.getMediaPlayer() != null) {
            context.getMediaPlayer().reset();
            context.startListening(question.getAudioFile());
        }
    }
}
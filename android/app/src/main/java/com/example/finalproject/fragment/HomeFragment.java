package com.example.finalproject.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalproject.ExamActivity;
import com.example.finalproject.HomeActivity;
import com.example.finalproject.R;
import com.example.finalproject.databinding.FragmentHomeBinding;
import com.example.finalproject.models.Course;
import com.example.finalproject.models.adapter.CourseAdapter;
import com.example.finalproject.models.ItemClickListener;
import com.example.finalproject.models.Word;
import com.example.finalproject.models.adapter.WordAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements ItemClickListener, View.OnClickListener {
    private FragmentHomeBinding binding;
    private CourseAdapter courseAdapter;
    private WordAdapter wordsAdapter;
    private List<Course> courses;
    private List<Word> words;
    private HomeActivity context;
    private List<Word> vocabulary = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "title";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
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
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        context = (HomeActivity) getActivity();
        binding.txtSeall.setClickable(false);
        binding.txtSeall.setOnClickListener(this);

        observeViewModel(context);
        initRecyclerView();

        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(Object object, int position) {
        if (object instanceof CourseAdapter) {
            Intent intent = new Intent(context, ExamActivity.class);
            intent.putExtra("course", courseAdapter.getItem(position));
            startActivity(intent);
        } else if (object instanceof WordAdapter) {
        }
    }

    @Override
    public void onClick(View v) {
        // load all word into vocabulary activity
        // the current problem is the room database is loading so slow with the large data
        if (v.getId() == R.id.txtSeall){

        }
    }

    private void observeViewModel(HomeActivity activity) {
        activity.getHomeViewModel().getCourses().observe(this, courses -> {
            courseAdapter.setData(courses);
        });
        activity.getHomeViewModel().get30Words().observe(this, words -> {
            wordsAdapter.setData(words);
        });
        activity.getHomeViewModel().getWords().observe(this, words -> {
            if (words != null){
                vocabulary.addAll(words);
                binding.txtSeall.setClickable(true);
            }
        });
        activity.getHomeViewModel().getUser().observe(this, user -> {
            if (user != null)
                binding.txtHello.setText(getString(R.string.hello, user.getName()));
            else
                binding.txtHello.setText(getString(R.string.hello, "Guys"));
        });
    }

    private void initRecyclerView() {
        courses = new ArrayList<>();
        words = new ArrayList<>();

        courseAdapter = new CourseAdapter(context, courses);
        wordsAdapter = new WordAdapter(context, words);

        binding.rvCourse.setAdapter(courseAdapter);
        binding.rvCourse.setHasFixedSize(true);
        binding.rvCourse.setLayoutManager(new GridLayoutManager(context, 1, RecyclerView.HORIZONTAL, false));

        binding.rvVocab.setAdapter(wordsAdapter);
        binding.rvVocab.setHasFixedSize(true);
        binding.rvVocab.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        binding.rvVocab.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));

        wordsAdapter.setOnItemClickListener(this);
        courseAdapter.setOnItemClickListener(this);
    }

}
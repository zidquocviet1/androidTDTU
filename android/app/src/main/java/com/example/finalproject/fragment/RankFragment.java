package com.example.finalproject.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.finalproject.HomeActivity;
import com.example.finalproject.R;
import com.example.finalproject.api.ToeicAPI;
import com.example.finalproject.databinding.FragmentRankBinding;
import com.example.finalproject.models.Course;
import com.example.finalproject.models.User;
import com.example.finalproject.models.adapter.UserAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RankFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<User> users;
    private List<Course> courseList;
    private FragmentRankBinding binding;
    private HomeActivity context;
    private UserAdapter userAdapter;

    public RankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RankFragment newInstance(String param1, String param2) {
        RankFragment fragment = new RankFragment();
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
        binding = FragmentRankBinding.inflate(inflater, container, false);

        context = (HomeActivity) getActivity();

        View view = binding.getRoot();
        users = new ArrayList<>();
        courseList = new ArrayList<>();
        initRecyclerView();

        binding.spCourse.setEnabled(false);
        binding.txtAll.setTextColor(context.getColor(R.color.title));
        binding.txtAll.setOnClickListener(this);
        binding.chkFilter.setOnClickListener(this);

        observeViewModel();
        loadLeaderBoard();

        return view;
    }

    private void loadLeaderBoard() {
        if (context.isServerAndNetworkAvailable()) {
            ToeicAPI.getUserByScore(context);
        }
    }
    private void initRecyclerView() {
        users = new ArrayList<>();

        userAdapter = new UserAdapter(context, users);

        binding.rvRankUser.setAdapter(userAdapter);
        binding.rvRankUser.setHasFixedSize(true);
    }
    private void observeViewModel() {
        context.getHomeViewModel().getUsers().observe(this, users -> {
            if (users != null) {
                users = users.stream().filter(u -> u.getScore() > 0).collect(Collectors.toList());
                userAdapter.setData(users);
            }
        });
        context.getHomeViewModel().getCourses().observe(this, courses -> {
            if (courses != null){
                courseList.addAll(courses);
            }
        });
        context.getHomeViewModel().getNetworkState().observe(this, aBoolean -> {
            if (!aBoolean) {
                binding.txtContent.setText(getString(R.string.connection));
                binding.txtContent.setVisibility(View.VISIBLE);
                binding.rvRankUser.setVisibility(View.GONE);
            } else {
                binding.txtContent.setVisibility(View.GONE);
                binding.rvRankUser.setVisibility(View.VISIBLE);
            }
        });
        context.getHomeViewModel().getServerState().observe(this, aBoolean -> {
            if (!aBoolean) {
                binding.txtContent.setText(getString(R.string.server_error));
                binding.txtContent.setVisibility(View.VISIBLE);
                binding.rvRankUser.setVisibility(View.GONE);
            } else {
                binding.txtContent.setVisibility(View.GONE);
                binding.rvRankUser.setVisibility(View.VISIBLE);
            }
        });
    }
    private void openSpinner(){
        ArrayAdapter<Course> arrayAdapter = new ArrayAdapter<Course>(context,
                android.R.layout.simple_spinner_item, courseList);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spCourse.setAdapter(arrayAdapter);
        binding.spCourse.setSelection(-1);
        binding.spCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                long courseId = arrayAdapter.getItem(position).getId();
                if (context.isServerAndNetworkAvailable()) {
                    ToeicAPI.getRankByCourse(context, courseId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txtAll:
                binding.chkFilter.setChecked(false);
                binding.spCourse.setEnabled(false);
                binding.txtAll.setTextColor(context.getColor(R.color.title));
                loadLeaderBoard();
                break;
            case R.id.chkFilter:
                if (binding.chkFilter.isChecked()){
                    binding.spCourse.setEnabled(true);
                    binding.txtAll.setTextColor(context.getColor(R.color.content));
                    openSpinner();
                }else{
                    binding.spCourse.setEnabled(false);
                    binding.txtAll.setTextColor(context.getColor(R.color.title));
                    loadLeaderBoard();
                }
                break;
        }
    }
}
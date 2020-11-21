package com.example.finalproject.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalproject.CommentActivity;
import com.example.finalproject.HomeActivity;
import com.example.finalproject.databinding.FragmentCourseBinding;
import com.example.finalproject.models.Course;
import com.example.finalproject.models.ItemClickListener;
import com.example.finalproject.models.MyDatabase;
import com.example.finalproject.models.User;
import com.example.finalproject.models.adapter.CourseInfoAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class CourseFragment extends Fragment implements ItemClickListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private FragmentCourseBinding binding;
    private CourseInfoAdapter courseInfoAdapter;
    private List<Course> courseList;
    private List<Course> comments;
    private HomeActivity context;
    private boolean isServer;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CourseFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static CourseFragment newInstance(int columnCount) {
        CourseFragment fragment = new CourseFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCourseBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        context = (HomeActivity) getActivity();
        comments = new ArrayList<>();
        initRecyclerView();

        courseInfoAdapter.setListener(this);
        courseInfoAdapter.setData(MyDatabase.getInstance(context).courseDAO().getAllCourse());

        observeViewModel(context);
        return view;
    }

    private void observeViewModel(HomeActivity activity) {
        activity.getHomeViewModel().getComments().observe(this, courses -> {
            if (courses != null) {
                comments.addAll(courses);
                showNumberComment();
            }
        });
        activity.getHomeViewModel().getServerState().observe(this, aBoolean -> {
            isServer = aBoolean;
        });
    }

    private void showNumberComment() {
        if (comments != null) {
            comments.forEach(c -> {
                courseInfoAdapter.setDataComment(c.getId(), c.getComment().size());
            });
        } else {
            courseList.forEach(c -> {
                courseInfoAdapter.setDataComment(c.getId(), 0);
            });
        }
    }

    private void initRecyclerView() {
        courseList = new ArrayList<>();

        courseInfoAdapter = new CourseInfoAdapter(context, courseList);

        binding.rvCourseInfo.setAdapter(courseInfoAdapter);
        binding.rvCourseInfo.setHasFixedSize(true);
        binding.rvCourseInfo.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
    }

    @Override
    public void onItemClick(Object object, int position) {
        User user = context.getHomeViewModel().getUser().getValue();
        boolean isNetwork = context.getHomeViewModel().getNetworkState().getValue();

        Intent intent = new Intent(context, CommentActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("course", courseInfoAdapter.getItem(position));
        intent.putExtra("server", isServer);
        intent.putExtra("network", isNetwork);
        startActivity(intent);
    }
}
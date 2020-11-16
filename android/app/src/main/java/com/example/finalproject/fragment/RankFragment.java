package com.example.finalproject.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.finalproject.HomeActivity;
import com.example.finalproject.R;
import com.example.finalproject.api.UserAPI;
import com.example.finalproject.databinding.FragmentHomeBinding;
import com.example.finalproject.databinding.FragmentRankBinding;
import com.example.finalproject.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RankFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<User> users;
    private FragmentRankBinding binding;
    private HomeActivity context;

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
//        initRecyclerView();

        observeViewModel();
        loadLeaderBoard();

        return view;
    }

    private void loadLeaderBoard() {
        if (context.getHomeViewModel().getUsers().getValue() == null) {
            if (context.isServerAndNetworkAvailable()) {
                UserAPI.getUserByScore(context);
            }
//            else if (!context.getHomeViewModel().getNetworkState().getValue()) {
//                binding.txtContent.setText(getString(R.string.connection));
//                binding.txtContent.setVisibility(View.VISIBLE);
//            } else if (!context.getHomeViewModel().getServerState().getValue()) {
//                binding.txtContent.setText(getString(R.string.server_error));
//                binding.txtContent.setVisibility(View.VISIBLE);
//            }
        }
    }

    private void observeViewModel() {
        context.getHomeViewModel().getUsers().observe(this, users -> {
            if (users != null)
                this.users.addAll(users);
        });
        context.getHomeViewModel().getNetworkState().observe(this, aBoolean -> {
            if (!aBoolean) {
                binding.txtContent.setText(getString(R.string.connection));
                binding.txtContent.setVisibility(View.VISIBLE);
            } else
                binding.txtContent.setVisibility(View.GONE);
        });
        context.getHomeViewModel().getServerState().observe(this, aBoolean -> {
            if (!aBoolean) {
                binding.txtContent.setText(getString(R.string.server_error));
                binding.txtContent.setVisibility(View.VISIBLE);
            } else
                binding.txtContent.setVisibility(View.GONE);
        });
    }
}
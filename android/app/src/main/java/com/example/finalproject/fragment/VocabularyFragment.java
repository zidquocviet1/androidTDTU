package com.example.finalproject.fragment;

import android.app.SearchManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.SearchView;

import com.example.finalproject.HomeActivity;
import com.example.finalproject.R;
import com.example.finalproject.databinding.FragmentVocabularyBinding;
import com.example.finalproject.models.Word;
import com.example.finalproject.models.adapter.WordAdapter;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VocabularyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VocabularyFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentVocabularyBinding binding;
    private List<Word> words = new ArrayList<>();
    private WordAdapter wordAdapter;
    private HomeActivity context;

    public VocabularyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VocabularyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VocabularyFragment newInstance(String param1, String param2) {
        VocabularyFragment fragment = new VocabularyFragment();
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
        binding = FragmentVocabularyBinding.inflate(inflater, container, false);
        context = (HomeActivity) getActivity();

        initRecyclerView();
        observeViewModel(context);

        View view = binding.getRoot();
        TextInputEditText search = (TextInputEditText) view.findViewById(R.id.input);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                wordAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return view;
    }


    private void initRecyclerView() {
        words = new ArrayList<>();

        wordAdapter = new WordAdapter(context, words);

        binding.rvVocab.setAdapter(wordAdapter);
        binding.rvVocab.setHasFixedSize(true);
        binding.rvVocab.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        binding.rvVocab.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
    }

    private void observeViewModel(HomeActivity activity) {
        activity.getHomeViewModel().getWords().observe(this, words -> {
            if (words != null){
                wordAdapter.setData(words);
            }
        });
    }
}
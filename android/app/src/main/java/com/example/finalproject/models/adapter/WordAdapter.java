package com.example.finalproject.models.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.models.ItemClickListener;
import com.example.finalproject.models.MyDatabase;
import com.example.finalproject.models.Word;

import java.util.ArrayList;
import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordVH> implements Filterable {
    private Context context;
    private List<Word> data;
    private List<Word> dataFiltered;
    private ItemClickListener listener;

    public WordAdapter(Context context, List<Word> data) {
        this.context = context;
        this.data = data;
        this.dataFiltered = data;
    }
    public void setData(List<Word> data){
        this.data = data;
        this.dataFiltered = data;
        notifyItemRangeInserted(0, data.size() -1);
    }
    public void setOnItemClickListener(ItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    dataFiltered = data;
                } else {
                    List<Word> filteredList = new ArrayList<>();
                    for (Word row : data) {

                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    dataFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = dataFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                dataFiltered = (List<Word>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class WordVH extends RecyclerView.ViewHolder{
        private TextView txtName, txtDes, txtPro;
        private CheckBox chkWasLearnt;

        public WordVH(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtDes = itemView.findViewById(R.id.txtDes);
            txtPro = itemView.findViewById(R.id.txtPro);
            chkWasLearnt = itemView.findViewById(R.id.chkWasLearnt);
        }
    }
    @NonNull
    @Override
    public WordAdapter.WordVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_info, parent, false);
        return new WordVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordAdapter.WordVH holder, int position) {
        Word word = dataFiltered.get(position);
        holder.txtName.setText(word.getName());
        holder.txtDes.setText(word.getDescription());
        holder.txtPro.setText(word.getPronounce());
        holder.chkWasLearnt.setChecked(word.isWasLearnt());

        holder.itemView.setOnClickListener((view) -> {
            if (listener != null)
                listener.onItemClick(this, position);
        });
        holder.chkWasLearnt.setOnClickListener(v -> {
            word.setWasLearnt(holder.chkWasLearnt.isChecked());
            MyDatabase.getInstance(context).wordDAO().updateWord(word);
        });
    }

    @Override
    public int getItemCount() {
        return dataFiltered.size();
    }


}

package com.example.finalproject.models.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.models.ItemClickListener;
import com.example.finalproject.models.Word;

import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordVH>{
    private Context context;
    private List<Word> data;
    private ItemClickListener listener;

    public WordAdapter(Context context, List<Word> data) {
        this.context = context;
        this.data = data;
    }
    public void setData(List<Word> data){
        this.data = data;
        notifyItemRangeInserted(0, data.size() -1);
    }
    public void setOnItemClickListener(ItemClickListener listener){
        this.listener = listener;
    }
    public static class WordVH extends RecyclerView.ViewHolder{
        private TextView txtName, txtDes, txtPro;

        public WordVH(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtDes = itemView.findViewById(R.id.txtDes);
            txtPro = itemView.findViewById(R.id.txtPro);
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
        Word word = data.get(position);
        holder.txtName.setText(word.getName());
        holder.txtDes.setText(word.getDescription());
        holder.txtPro.setText(word.getPronounce());

        holder.itemView.setOnClickListener((view) -> {
            if (listener != null)
                listener.onItemClick(this, position);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}

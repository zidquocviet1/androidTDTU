package com.example.finalproject.models.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.models.Comment;
import com.example.finalproject.models.Course;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentVH>{
    private Context context;
    private List<Comment> data;

    public CommentAdapter(Context context, List<Comment> data){
        this.context = context;
        this.data = data;
    }

    public void setData(List<Comment> comments) {
        this.data = comments;
//        notifyItemRangeChanged(0, comments.size() - 1);
        notifyDataSetChanged();
    }

    public static class CommentVH extends RecyclerView.ViewHolder{
        private TextView txtName, txtContent;
        private ImageView imgAvatar;

        public CommentVH(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtContent = itemView.findViewById(R.id.txtContent);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
        }
    }
    @NonNull
    @Override
    public CommentAdapter.CommentVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_info, parent, false);
        return new CommentVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.CommentVH holder, int position) {
        holder.txtName.setText(data.get(position).getUser().getName());
        holder.txtContent.setText(data.get(position).getDescription());
        holder.imgAvatar.setImageDrawable(ContextCompat.getDrawable(context, UserAdapter.images[0]));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}

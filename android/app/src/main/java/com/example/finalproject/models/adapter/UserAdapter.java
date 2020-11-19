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
import com.example.finalproject.models.ItemClickListener;
import com.example.finalproject.models.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserVH>{
    private Context context;
    private List<User> data;
    private ItemClickListener listener;
    public static final int[] images = {R.drawable.user_1,
            R.drawable.user_2,
            R.drawable.user_3,
            R.drawable.user_4,
            R.drawable.user_5};

    public UserAdapter(Context context, List<User> data) {
        this.context = context;
        this.data = data;
    }
    public void setData(List<User> data){
        this.data = data;
        notifyItemRangeInserted(0, data.size() -1);
    }
    public void setOnItemClickListener(ItemClickListener listener){
        this.listener = listener;
    }

    public static class UserVH extends RecyclerView.ViewHolder{
        private TextView txtName, txtScore;
        private ImageView imgAvatar;

        public UserVH(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtScore = itemView.findViewById(R.id.txtScore);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
        }
    }
    @NonNull
    @Override
    public UserAdapter.UserVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_user, parent, false);
        return new UserVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserVH holder, int position) {
        User user = data.get(position);
        holder.txtName.setText(user.getName());
        holder.txtScore.setText(context.getString(R.string.score, user.getScore()));
        holder.imgAvatar.setImageDrawable(ContextCompat.getDrawable(context, images[user.getAvatar()]));

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

package com.example.finalproject.models;

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

import java.util.List;
import java.util.Random;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseVH> {
    private Context context;
    private List<Course> data;
    private ItemClickListener listener;
    static final int[] images = {R.drawable.image_1,
            R.drawable.image_2,
            R.drawable.image_3,
            R.drawable.image_5};

    public CourseAdapter(Context context, List<Course> data) {
        this.context = context;
        this.data = data;
    }

    public void setData(List<Course> data){
        this.data = data;
        notifyItemRangeInserted(0, data.size() -1);
    }

    public void setOnItemClickListener(ItemClickListener listener){
        this.listener = listener;
    }
    public static class CourseVH extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView txtName;
        private TextView txtDes;

        public CourseVH(@NonNull View itemView) {
            super(itemView);
            imageView  = itemView.findViewById(R.id.imageView);
            txtName = itemView.findViewById(R.id.txtName);
            txtDes = itemView.findViewById(R.id.txtDescription);
        }
    }
    @NonNull
    @Override
    public CourseAdapter.CourseVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.course_info, parent, false);
        return new CourseVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.CourseVH holder, int position) {
        Course c = data.get(position);
        int image = new Random().nextInt(3);

        holder.imageView.setImageDrawable(ContextCompat.getDrawable(context, images[image]));
        holder.txtName.setText(c.getName());
        holder.txtDes.setText(c.getDescription());

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

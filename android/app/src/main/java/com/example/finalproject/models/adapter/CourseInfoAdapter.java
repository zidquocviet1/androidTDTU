package com.example.finalproject.models.adapter;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.finalproject.R;
import com.example.finalproject.models.Course;

import java.util.List;
import java.util.Random;

/**
 * {@link RecyclerView.Adapter} that can display a.
 * TODO: Replace the implementation with code for your data type.
 */
public class CourseInfoAdapter extends RecyclerView.Adapter<CourseInfoAdapter.ViewHolder> {

    private List<Course> mValues;
    private Context context;
    static final int[] images = {R.drawable.image_1,
            R.drawable.image_2,
            R.drawable.image_3,
            R.drawable.image_5};

    public CourseInfoAdapter(Context context, List<Course> items) {
        mValues = items;
        this.context = context;
    }

    public void setData(List<Course> data){
        this.mValues = data;
        notifyItemRangeInserted(0, data.size() -1);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_course_info, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        int image = new Random().nextInt(4);

        holder.imageView.setImageDrawable(ContextCompat.getDrawable(context, images[image]));
        holder.mItem = mValues.get(position);
        holder.txtName.setText(mValues.get(position).getName());
        holder.txtDescription.setText(mValues.get(position).getDescription());
        holder.txtRatio.setText(mValues.get(position).getRating()+"/"+5);
        holder.rbStar.setRating(mValues.get(position).getRating());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView txtDescription;
        public final TextView txtName;
        public final TextView txtRatio;
        public final RatingBar rbStar;
        public final ImageView imageView;
        public Course mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            txtName = view.findViewById(R.id.txtName);
            txtDescription = view.findViewById(R.id.txtDescription);
            rbStar = view.findViewById(R.id.rbStar);
            imageView = view.findViewById(R.id.imageView);
            txtRatio = view.findViewById(R.id.txtRatio);
        }
    }
}
package com.example.btscanning;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Class item);
    }

    private Context mCtx;
    private List<Class> classList;
    private final OnItemClickListener listener;

    public ClassAdapter(Context mCtx, List<Class> classList, OnItemClickListener listener) {
        this.mCtx = mCtx;
        this.classList = classList;
        this.listener = listener;
    }

    class ClassViewHolder extends RecyclerView.ViewHolder{

        TextView courseID, name, startEndTime;

        public ClassViewHolder(@NonNull View itemView) {
            super(itemView);

            courseID = itemView.findViewById(R.id.course_id);
            name = itemView.findViewById(R.id.name);
            startEndTime = itemView.findViewById(R.id.start_end_time);

        }

        public void bind(final Class item, final OnItemClickListener listener){
            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_listclasses, null);
        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder classViewHolder, int i) {
        Class newClass = classList.get(i);

        String newString = newClass.getStartTime()+"-"+newClass.getEndTime();

        classViewHolder.courseID.setText(newClass.getCourse_id());
        classViewHolder.name.setText(newClass.getName());
        classViewHolder.startEndTime.setText(newString);

        classViewHolder.bind(newClass, listener);

    }

    @Override
    public int getItemCount() {
        return classList.size();
    }

}

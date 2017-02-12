package com.devhack.taskglide.ui;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.devhack.taskglide.R;
import com.devhack.taskglide.activities.MainActivity;
import com.devhack.taskglide.models.Task;
import com.makeramen.roundedimageview.RoundedImageView;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Michael Yoon Huh on 2/11/2017.
 */

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksViewHolder> {

    private static final String LOG_TAG = TasksAdapter.class.getSimpleName();

    private Context context;
    private List<Task> taskList;

    /** CONSTRUCTOR METHODS ____________________________________________________________________ **/

    public TasksAdapter(List<Task> taskList, Context context){
        this.taskList = taskList;
        this.context = context;
    }

    /** RECYCLER VIEW METHODS __________________________________________________________________ **/

    @Override
    public TasksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_tasks, parent, false);
        TasksViewHolder viewHolder = new TasksViewHolder(view, new TasksViewHolder.OnViewHolderClickListener() {

            @Override
            public void onCheckboxChecked(CompoundButton view, boolean isChecked) {

            }

            @Override
            public void onSignClicked(View view, int position) {
                ((MainActivity) context).displaySignBottomDialog(taskList.get(position));
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TasksViewHolder holder, int position) {
        boolean isTaskFinished = taskList.get(position).isFinished();
        String taskDescription = taskList.get(position).getTaskDescription();

        holder.taskCheckbox.setChecked(isTaskFinished);
        holder.taskTextView.setText(taskDescription);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    /** SUBCLASSES _____________________________________________________________________________ **/

    public static class TasksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        @BindView(R.id.adapter_tasks_checkbox) AppCompatCheckBox taskCheckbox;
        @BindView(R.id.adapter_tasks_text) TextView taskTextView;
        @BindView(R.id.adapter_tasks_sign) RoundedImageView taskSignButton;

        public OnViewHolderClickListener viewHolderClickListener;

        TasksViewHolder(View itemView, OnViewHolderClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            if (listener != null) {
                viewHolderClickListener = listener;
                taskCheckbox.setOnCheckedChangeListener(this);
                taskSignButton.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            int itemPos = getAdapterPosition();
            viewHolderClickListener.onSignClicked(v, itemPos);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            viewHolderClickListener.onCheckboxChecked(buttonView, isChecked);
        }

        public interface OnViewHolderClickListener {
            void onCheckboxChecked(CompoundButton view, boolean isChecked);
            void onSignClicked(View view, int position);
        }
    }
}

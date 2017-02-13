package com.devhack.taskglide.ui.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
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
    private TaskListener taskListener;

    /** CONSTRUCTOR METHODS ____________________________________________________________________ **/

    public TasksAdapter(List<Task> taskList, TaskListener listener, Context context){
        this.taskList = taskList;
        this.taskListener = listener;
        this.context = context;
    }

    /** RECYCLER VIEW METHODS __________________________________________________________________ **/

    @Override
    public TasksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_tasks, parent, false);
        TasksViewHolder viewHolder = new TasksViewHolder(view, new TasksViewHolder.OnViewHolderClickListener() {

            @Override
            public void onCheckboxChecked(CompoundButton view, boolean isChecked, int position) {
                Task task = taskList.get(position);
                if (isChecked) {
                    task.setStatus(1);
                    //((MainActivity) context).displaySignBottomDialog(taskList.get(position));
                } else {
                    task.setStatus(0);
                }
                taskList.set(position, task);
                taskListener.sendUpdatedTaskList(taskList);
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
        int taskStatus = taskList.get(position).getStatus();
        String taskName = taskList.get(position).getName();
        taskName = taskName.replace("_", " ");

        switch (taskStatus) {

            case 0:
                holder.taskTextView.setTextColor(ContextCompat.getColor(context, android.R.color.black));
                break;
            case 1:
                holder.taskCheckbox.setEnabled(false);
                holder.taskTextView.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray));
                //holder.taskTextView.setPaintFlags(holder.taskTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                break;
            case 2:
                holder.taskCheckbox.setChecked(true);
                holder.taskCheckbox.setEnabled(false);
                break;
        }

        holder.taskTextView.setText(taskName);
    }

    @Override
    public int getItemCount() {
        if (taskList != null) {
            return taskList.size();
        } else {
            return 0;
        }
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
            int itemPos = getAdapterPosition();
            viewHolderClickListener.onCheckboxChecked(buttonView, isChecked, itemPos);
        }

        public interface OnViewHolderClickListener {
            void onCheckboxChecked(CompoundButton view, boolean isChecked, int position);
            void onSignClicked(View view, int position);
        }
    }

    /** INTERFACE ______________________________________________________________________________ **/

    public interface TaskListener {
        void sendUpdatedTaskList(List<Task> taskList);
    }
}

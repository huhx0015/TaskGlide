package com.devhack.taskglide.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.devhack.taskglide.R;
import com.devhack.taskglide.activities.MainActivity;
import com.devhack.taskglide.models.File;
import com.squareup.picasso.Picasso;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Michael Yoon Huh on 2/11/2017.
 */

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.FilesViewHolder> {

    private static final String LOG_TAG = FilesAdapter.class.getSimpleName();

    private Context context;
    private List<File> fileList;

    /** CONSTRUCTOR METHODS ____________________________________________________________________ **/

    public FilesAdapter(List<File> fileList, Context context){
        this.fileList = fileList;
        this.context = context;
    }

    /** RECYCLER VIEW METHODS __________________________________________________________________ **/

    @Override
    public FilesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_files, parent, false);
        FilesViewHolder viewHolder = new FilesViewHolder(view, new FilesViewHolder.OnViewHolderClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                ((MainActivity) context).displayImagePreview(fileList.get(position).getImageResource());
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FilesViewHolder holder, int position) {
        String fileName = fileList.get(position).getFileName();
        String fileUrl = fileList.get(position).getImageUrl();
        int fileResource = fileList.get(position).getImageResource();

        holder.fileName.setText(fileName);

        if (fileUrl != null) {
            Picasso.with(context)
                    .load(fileUrl)
                    .into(holder.fileImage);
        } else {
            Picasso.with(context)
                    .load(fileResource)
                    .into(holder.fileImage);
        }
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    /** SUBCLASSES _____________________________________________________________________________ **/

    public static class FilesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.adapter_files_layout) RelativeLayout fileLayout;
        @BindView(R.id.adapter_files_image) ImageView fileImage;
        @BindView(R.id.adapter_files_name) TextView fileName;

        public OnViewHolderClickListener viewHolderClickListener;

        FilesViewHolder(View itemView, OnViewHolderClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            if (listener != null) {
                viewHolderClickListener = listener;
                fileLayout.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            int itemPos = getAdapterPosition();
            viewHolderClickListener.onItemClicked(v, itemPos);
        }

        public interface OnViewHolderClickListener {
            void onItemClicked(View view, int position);
        }
    }
}

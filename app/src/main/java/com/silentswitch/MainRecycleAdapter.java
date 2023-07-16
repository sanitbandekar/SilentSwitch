package com.silentswitch;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainRecycleAdapter extends RecyclerView.Adapter<MainRecycleAdapter.ViewHolder> {
    List<SilentModel> silentModels;
    private Context context;

    public MainRecycleAdapter(List<SilentModel> silentModels, Context context) {
        this.silentModels = silentModels;
        this.context = context;
    }

    public void setSilentModels(List<SilentModel> silentModels) {
        this.silentModels = silentModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MainRecycleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_time, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainRecycleAdapter.ViewHolder holder, int position) {
        SilentModel silentModel = silentModels.get(position);
        holder.title.setText(silentModel.getName());
        String sTime = DateUtils.formatDateTime(context, silentModel.getStartTime(), DateUtils.FORMAT_SHOW_TIME);
        String endTime = DateUtils.formatDateTime(context, silentModel.getEndTime(), DateUtils.FORMAT_SHOW_TIME);

        holder.startTime.setText(sTime);
        holder.endTime.setText(endTime);
    }

    @Override
    public int getItemCount() {
        return silentModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title, startTime, endTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            startTime = itemView.findViewById(R.id.start_time_item);
            endTime = itemView.findViewById(R.id.end_time_item);
        }
    }
}

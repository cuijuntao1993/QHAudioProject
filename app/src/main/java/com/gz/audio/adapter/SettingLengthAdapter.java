package com.gz.audio.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gz.audio.R;
import com.gz.audio.entiy.VoiceLengthBean;
import com.gz.audio.interfaces.OnViewItemListener;
import com.gz.audio.utils.VoiceUtil;

import java.util.List;

/**
 * Created by Liuyz on 2018/5/22.
 */

public class SettingLengthAdapter extends RecyclerView.Adapter<SettingLengthAdapter.MyViewHolder> {
    private Activity context;
    private List<VoiceLengthBean> list;
    private OnViewItemListener listener;

    public SettingLengthAdapter(Activity activity, List<VoiceLengthBean> response, OnViewItemListener listener) {
        this.context = activity;
        this.list = response;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.setting_length_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final VoiceLengthBean model = list.get(position);
        holder.length.setText(VoiceUtil.getVoiceLenght(model.getFilelength()));
        if (model.getFilestatus()) {
            holder.circle.setVisibility(View.VISIBLE);
        } else {
            holder.circle.setVisibility(View.INVISIBLE);
        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size() <= 0 ? 0 : list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout layout;
        private TextView length;
        private ImageView circle;

        public MyViewHolder(View view) {
            super(view);
            layout = (LinearLayout) view.findViewById(R.id.layout);
            length = (TextView) view.findViewById(R.id.length);
            circle = (ImageView) view.findViewById(R.id.circle);

        }
    }
}

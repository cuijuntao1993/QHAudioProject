package com.gz.audio.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gz.audio.R;
import com.gz.audio.entiy.ECG_Records;
import com.gz.audio.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by boya on 2018/9/15.
 */

public class mHistoryAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<ECG_Records> mDatas;
    ArrayList<Double> dataList,tempList,resultList;
    //MyAdapter需要一个Context，通过Context获得Layout.inflater，然后通过inflater加载item的布局
    public mHistoryAdapter(Context context, List<ECG_Records> datas) {
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.history_item, parent, false); //加载布局
            holder = new ViewHolder();
            holder.data = (TextView) convertView.findViewById(R.id.date);
            holder.submit = (TextView) convertView.findViewById(R.id.submit);
            holder.tu_img = (ImageView) convertView.findViewById(R.id.tu_img);
            holder.diagnose_abstract = (TextView) convertView.findViewById(R.id.diagnose_abstract);
            convertView.setTag(holder);
        } else {   //else里面说明，convertView已经被复用了，说明convertView中已经设置过tag了，即holder
            holder = (ViewHolder) convertView.getTag();
        }

        ECG_Records model = mDatas.get(position);
        String tel = model.getPhoneNumber();
        holder.tu_img.setImageBitmap(FileUtils.Bytes2Bimap(model.getXinDianByShort()));
        if (model.getState() == 0) {
            holder.submit.setText("未上传");
        } else if(model.getState() == 1){
            holder.submit.setText("已上传");
        }else if(model.getState() == 2){
            holder.submit.setText("已完成");
        }
        holder.data.setText(model.getStartTime());
        if("".equals(model.getDiagnose_abstract())){
            holder.diagnose_abstract.setText("等待获取");
        }else{
            holder.diagnose_abstract.setText(model.getDiagnose_abstract());
        }
        return convertView;
    }

    public class ViewHolder {
        TextView data;
        TextView submit;
        ImageView tu_img;
        TextView diagnose_abstract;

    }
}

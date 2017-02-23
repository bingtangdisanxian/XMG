package com.example.chenqi.mobilphone.app_lock_unlock.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chenqi.mobilphone.R;

import java.util.List;

/**
 * Created by chenqi on 2017/2/22.
 * 描述:
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter {
    private final List<WaitMVBean.DataBean.ComingBean> list;
    private final Context context;
    private final LayoutInflater inflater;

    public MyRecyclerAdapter(Context context, List<WaitMVBean.DataBean.ComingBean> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(inflater.inflate(R.layout.data_item, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder holder1 = (MyViewHolder) holder;
        holder1.setDate(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView mvName;
        TextView tvPeople;
        TextView tvProfessional;
        TextView mvDec;
        TextView mvDate;

        public MyViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            mvName = (TextView) itemView.findViewById(R.id.mv_name);
            tvPeople = (TextView) itemView.findViewById(R.id.tv_people);
            tvProfessional = (TextView) itemView.findViewById(R.id.tv_professional);
            mvDec = (TextView) itemView.findViewById(R.id.mv_dec);
            mvDate = (TextView) itemView.findViewById(R.id.mv_date);
        }

        public void setDate(int position) {
            WaitMVBean.DataBean.ComingBean comingBean = list.get(position);
            mvName.setText(comingBean.getNm());
            mvDate.setText(comingBean.getShowInfo());
            mvDec.setText(comingBean.getScm());
            String url = comingBean.getImg();
            String imageurl = url.replaceAll("w.h", "50.80");//字符串替换
            Log.e("图片路径", url);
            Glide.with(context).load(imageurl).into(image);
        }
    }
}
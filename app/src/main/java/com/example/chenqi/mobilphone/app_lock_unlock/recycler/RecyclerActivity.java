package com.example.chenqi.mobilphone.app_lock_unlock.recycler;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.chenqi.mobilphone.R;
import com.example.chenqi.mobilphone.base.BaseActivity;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenqi on 2017/2/22.
 * 描述:完成recyclerView的吸顶效果
 */
public class RecyclerActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private String url = "http://api.meituan.com/mmdb/movie/v2/list/rt/order/coming.json?ci=1&limit=12&token=&__vhost=api.maoyan.com&utm_campaign=AmovieBmovieCD-1&movieBundleVersion=6801&utm_source=xiaomi&utm_medium=android&utm_term=6.8.0&utm_content=868030022327462&net=255&dModel=MI%205&uuid=0894DE03C76F6045D55977B6D4E32B7F3C6AAB02F9CEA042987B380EC5687C43&lat=40.100673&lng=116.378619&__skck=6a375bce8c66a0dc293860dfa83833ef&__skts=1463704714271&__skua=7e01cf8dd30a179800a7a93979b430b2&__skno=1a0b4a9b-44ec-42fc-b110-ead68bcc2824&__skcy=sXcDKbGi20CGXQPPZvhCU3%2FkzdE%3D";
    private WaitMVBean waitMVBean;
    private List<WaitMVBean.DataBean.ComingBean> list;
    private List<NameBean> beanList;
    @Override
    protected void initView() {
        setContentView(R.layout.activity_recycler);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_recycler);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,1);
        mRecyclerView.setLayoutManager(gridLayoutManager);
    }

    @Override
    protected void initData() {
        getDataFromNet();
        super.initData();
    }

    private void getDataFromNet() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                processData(s);

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Logger.v("网络错误"+volleyError);
            }
        });
        requestQueue.add(stringRequest);
    }

    private void processData(String s) {
        final Gson gson = new Gson();
        waitMVBean = gson.fromJson(s, WaitMVBean.class);
        list = waitMVBean.getData().getComing();
        setPullAction(list);

        mRecyclerView.addItemDecoration(new SectionDecoration(beanList, this, new SectionDecoration.DecorationCallback() {
            @Override
            public String getGroupId(int position) {
                if (beanList.get(position).getName() != null) {
                    return beanList.get(position).getName();
                }
                return "-1";
            }

            //获取同一组的内容
            @Override
            public String getGroupFirstLine(int position) {
                if (beanList.get(position).getName() != null) {
                    return beanList.get(position).getName();
                }
                return "";
            }
        }));
        MyRecyclerAdapter myRecyclerAdapter = new MyRecyclerAdapter(RecyclerActivity.this, list);
        mRecyclerView.setAdapter(myRecyclerAdapter);
    }

    private void setPullAction(List<WaitMVBean.DataBean.ComingBean> list){
        beanList=new ArrayList<>();
        for (int i=0;i<list.size();i++){
            NameBean nameBean=new NameBean();
            String name=list.get(i).getComingTitle();
            nameBean.setName(name);
            beanList.add(nameBean);
        }
    }
}

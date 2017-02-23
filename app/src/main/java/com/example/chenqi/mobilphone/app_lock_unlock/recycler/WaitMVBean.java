package com.example.chenqi.mobilphone.app_lock_unlock.recycler;

import java.util.List;

/**
 * Created by chenqi on 2017/2/22.
 * 描述:
 */
public class WaitMVBean {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        private List<ComingBean> coming;

        public List<ComingBean> getComing() {
            return coming;
        }

        public static class ComingBean {

            private String comingTitle;
            private String id;
            private String img;
            private String nm;
            private String scm;
            private String showInfo;

            public String getComingTitle() {
                return comingTitle;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            String getImg() {
                return img;
            }

            String getNm() {
                return nm;
            }

            String getScm() {
                return scm;
            }

            String getShowInfo() {
                return showInfo;
            }
        }
    }
}

package com.hst.Carlease.http.bean;

import com.bigkoo.pickerview.model.IPickerViewData;

import java.util.List;

/**
 * Author:lsh
 * Version: 1.0
 * Description:
 * Date: 2017/8/15
 */

public class AreaBean extends BaseBean{


    private List<ModelBean> model;

    public List<ModelBean> getModel() {
        return model;
    }

    public void setModel(List<ModelBean> model) {
        this.model = model;
    }

    public static class ModelBean implements IPickerViewData{
        public ModelBean(int area_ID, String area_Name) {
            Area_ID = area_ID;
            Area_Name = area_Name;
        }

        /**
         * Area_ID : 11
         * Area_ParentID : 0
         * Area_Name : 北京市
         * Area_Remark :
         * Area_AGID : 3
         */

        private int Area_ID;
        private int Area_ParentID;
        private String Area_Name;
        private String Area_Remark;
        private int Area_AGID;

        public int getArea_ID() {
            return Area_ID;
        }

        public void setArea_ID(int Area_ID) {
            this.Area_ID = Area_ID;
        }

        public int getArea_ParentID() {
            return Area_ParentID;
        }

        public void setArea_ParentID(int Area_ParentID) {
            this.Area_ParentID = Area_ParentID;
        }

        public String getArea_Name() {
            return Area_Name;
        }

        public void setArea_Name(String Area_Name) {
            this.Area_Name = Area_Name;
        }

        public String getArea_Remark() {
            return Area_Remark;
        }

        public void setArea_Remark(String Area_Remark) {
            this.Area_Remark = Area_Remark;
        }

        public int getArea_AGID() {
            return Area_AGID;
        }

        public void setArea_AGID(int Area_AGID) {
            this.Area_AGID = Area_AGID;
        }

        @Override
        public String getPickerViewText() {
            return Area_Name;
        }
    }
}

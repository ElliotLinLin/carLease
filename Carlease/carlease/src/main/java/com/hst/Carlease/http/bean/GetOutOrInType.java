package com.hst.Carlease.http.bean;

import com.bigkoo.pickerview.model.IPickerViewData;

/**
 * Author:lsh
 * Version: 1.0
 * Description:
 * Date: 2017/8/30
 */

public class GetOutOrInType extends BaseBean  implements IPickerViewData {



    /**
     * COP_ID : 1
     * COP_ParentID : 0
     * COP_PartsName : 正常租赁
     * COP_IsAvailable : True
     * COP_Sort : 1
     * COP_Position :
     * COP_Remark :
     */

    private String COP_ID;
    private String COP_ParentID;
    private String COP_PartsName;
    private String COP_IsAvailable;
    private String COP_Sort;
    private String COP_Position;
    private String COP_Remark;

    public String getCOP_ID() {
        return COP_ID;
    }

    public void setCOP_ID(String COP_ID) {
        this.COP_ID = COP_ID;
    }

    public String getCOP_ParentID() {
        return COP_ParentID;
    }

    public void setCOP_ParentID(String COP_ParentID) {
        this.COP_ParentID = COP_ParentID;
    }

    public String getCOP_PartsName() {
        return COP_PartsName;
    }

    public void setCOP_PartsName(String COP_PartsName) {
        this.COP_PartsName = COP_PartsName;
    }

    public String getCOP_IsAvailable() {
        return COP_IsAvailable;
    }

    public void setCOP_IsAvailable(String COP_IsAvailable) {
        this.COP_IsAvailable = COP_IsAvailable;
    }

    public String getCOP_Sort() {
        return COP_Sort;
    }

    public void setCOP_Sort(String COP_Sort) {
        this.COP_Sort = COP_Sort;
    }

    public String getCOP_Position() {
        return COP_Position;
    }

    public void setCOP_Position(String COP_Position) {
        this.COP_Position = COP_Position;
    }

    public String getCOP_Remark() {
        return COP_Remark;
    }

    public void setCOP_Remark(String COP_Remark) {
        this.COP_Remark = COP_Remark;
    }

    @Override
    public String getPickerViewText() {
        return COP_PartsName;
    }
}

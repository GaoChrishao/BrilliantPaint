package com.gaoch.brilliantpic.myclass;

import androidx.annotation.Keep;

@Keep
public class Style {
    private Integer id;
    private String type;  //类别
    private String name;    //图片名称
    private String modelname;   //模型名称
    private String picurl;  //图片地址

    public Style() {
    }
    @Override
    public String toString() {
        return "Style [id=" + id + ", type=" + type + ",name=" + name + ",modelname="+modelname+",picurl="+picurl+"]";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModelname() {
        return modelname;
    }

    public void setModelname(String modelname) {
        this.modelname = modelname;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }
}

package com.ktkj.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class FinalSelRetEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String staffId;
    private String staffName;
    private String dept;
    private String city;
    private String cityId;
    private String station;
    private String stationId;
    private String selStation;
    private String selStationId;
    private Integer totalCount;//总票数
    private Integer totalGet;//总得票数
    private Double percentGet;//得票率
    private Map<String,Integer> qusInfo;//投票按各个职位统计

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getSelStation() {
        return selStation;
    }

    public void setSelStation(String selStation) {
        this.selStation = selStation;
    }

    public String getSelStationId() {
        return selStationId;
    }

    public void setSelStationId(String selStationId) {
        this.selStationId = selStationId;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getTotalGet() {
        return totalGet;
    }

    public void setTotalGet(Integer totalGet) {
        this.totalGet = totalGet;
    }


    public Map<String, Integer> getQusInfo() {
        return qusInfo;
    }

    public void setQusInfo(Map<String, Integer> qusInfo) {
        this.qusInfo = qusInfo;
    }

    public Double getPercentGet() {
        return percentGet;
    }

    public void setPercentGet(Double percentGet) {
        this.percentGet = percentGet;
    }
}

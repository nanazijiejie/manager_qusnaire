package com.ktkj.entity;

import java.io.Serializable;

public class SelRetEntity implements Serializable {
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
    private Integer totalCount;
    private String qusStation;
    private String qusStationId;

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

    public String getQusStation() {
        return qusStation;
    }

    public void setQusStation(String qusStation) {
        this.qusStation = qusStation;
    }

    public String getQusStationId() {
        return qusStationId;
    }

    public void setQusStationId(String qusStationId) {
        this.qusStationId = qusStationId;
    }
}

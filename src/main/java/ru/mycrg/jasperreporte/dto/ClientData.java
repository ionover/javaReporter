package ru.mycrg.jasperreporte.dto;

import java.util.List;

public class ClientData {

    String picture;
    String title;
    String column;
    String value;
    String crs;
    String area;
    List<CoordinatesDto> coordinates;

    public ClientData() {

    }

    public ClientData(String picture, String title, String column, String value, String crs, String area,
                      List<CoordinatesDto> coordinates) {
        this.picture = picture;
        this.title = title;
        this.column = column;
        this.value = value;
        this.crs = crs;
        this.area = area;
        this.coordinates = coordinates;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCrs() {
        return crs;
    }

    public void setCrs(String crs) {
        this.crs = crs;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public List<CoordinatesDto> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<CoordinatesDto> coordinates) {
        this.coordinates = coordinates;
    }
}

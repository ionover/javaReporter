package ru.mycrg.jasperreporte.dto;

import java.util.List;

public class ClientData {

    String picture;
    String title;
    String column;
    String value;
    String crs;
    Double area;
    List<CoordinatesDto> coords;

    public ClientData() {

    }

    public ClientData(String picture, String title, String column, String value, String crs, Double area,
                      List<CoordinatesDto> coords) {
        this.picture = picture;
        this.title = title;
        this.column = column;
        this.value = value;
        this.crs = crs;
        this.area = area;
        this.coords = coords;
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

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public List<CoordinatesDto> getCoords() {
        return coords;
    }

    public void setCoords(List<CoordinatesDto> coords) {
        this.coords = coords;
    }
}

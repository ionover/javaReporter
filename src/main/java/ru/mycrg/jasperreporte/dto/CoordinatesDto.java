package ru.mycrg.jasperreporte.dto;

public class CoordinatesDto {

    private Integer num;
    private Double x;
    private Double y;

    public CoordinatesDto() {
    }

    public CoordinatesDto(Integer num, Double x, Double y) {
        this.num = num;
        this.x = x;
        this.y = y;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}

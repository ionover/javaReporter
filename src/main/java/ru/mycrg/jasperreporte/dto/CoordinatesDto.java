package ru.mycrg.jasperreporte.dto;

public class CoordinatesDto {

    private long number;
    private String x;
    private String y;

    public CoordinatesDto() {
    }

    public CoordinatesDto(long number, String x, String y) {
        this.number = number;
        this.x = x;
        this.y = y;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }
}

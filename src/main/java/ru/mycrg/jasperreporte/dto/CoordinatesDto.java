package ru.mycrg.jasperreporte.dto;

public class CoordinatesDto {
    
    private String x;
    private String y;
    
    public CoordinatesDto() {
    }
    
    public CoordinatesDto(String x, String y) {
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
}

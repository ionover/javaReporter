package ru.mycrg.jasperreporte.dto;

public class ReportDto {

    String outputFormat;
    ClientData d;

    public ReportDto() {
    }

    public ReportDto(String outputFormat, ClientData d) {
        this.outputFormat = outputFormat;
        this.d = d;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }

    public ClientData getD() {
        return d;
    }

    public void setD(ClientData d) {
        this.d = d;
    }
}

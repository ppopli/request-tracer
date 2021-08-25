package com.pulkit.requesttracer.datasource.model;

import lombok.Data;

import java.util.Date;

@Data
public class LogDTO {

    private String traceId;
    private String spanId;
    private Date spanStartTime;
    private Date spanEndTime;
    private String serviceName;
    private String request;
    private String response;
}

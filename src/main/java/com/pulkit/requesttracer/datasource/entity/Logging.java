package com.pulkit.requesttracer.datasource.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Data
@DynamicInsert
@DynamicUpdate
@Entity(name="logging")
public class Logging {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Column(name="service_name")
    private String serviceName;

    @Column(name="trace_id")
    private String traceId;

    @Column(name="span_id")
    private String spanId;

    @Column(name="span_start_time")
    private Date spanStartTime;

    @Column(name="span_end_time")
    private Date spanEndTime;

    @Column(name="request")
    private String request;

    @Column(name="response")
    private String response;

    @Column(name="created_on")
    private Date createdOn;

    @Column(name="updated_on")
    private Date updatedOn;

}

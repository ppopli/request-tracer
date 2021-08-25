package com.pulkit.requesttracer.datasource.dataservice.impl;

import com.pulkit.requesttracer.datasource.dataservice.LoggingDataService;
import com.pulkit.requesttracer.datasource.entity.Logging;
import com.pulkit.requesttracer.datasource.model.LogDTO;
import com.pulkit.requesttracer.datasource.repository.master.LoggingMasterRepository;
import com.pulkit.requesttracer.datasource.repository.slave.LoggingSlaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(value = "request.tracing.enabled")
public class LoggingDataServiceImpl implements LoggingDataService {

    @Autowired(required = false)
    private LoggingMasterRepository loggingMasterRepository;

    @Autowired(required = false)
    private LoggingSlaveRepository loggingSlaveRepository;

    @Override
    public Logging logData(LogDTO dto) {
        Logging log = new Logging();
        log.setServiceName(dto.getServiceName());
        log.setTraceId(dto.getTraceId());
        log.setSpanId(dto.getSpanId());
        if (null != dto.getRequest()) {
            log.setRequest(dto.getRequest());
            log.setSpanStartTime(dto.getSpanStartTime());
        }

        if (null != dto.getResponse()) {
            log.setResponse(dto.getResponse());
            log.setSpanEndTime(dto.getSpanEndTime());
        }
        return loggingMasterRepository.save(log);
    }
}

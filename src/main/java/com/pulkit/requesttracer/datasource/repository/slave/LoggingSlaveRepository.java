package com.pulkit.requesttracer.datasource.repository.slave;

import com.pulkit.requesttracer.datasource.entity.Logging;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoggingSlaveRepository  extends JpaRepository<Logging, Long> {
    List<Logging> findByTraceId(String traceId);
}

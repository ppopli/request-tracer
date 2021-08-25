package com.pulkit.requesttracer.datasource.repository.master;

import com.pulkit.requesttracer.datasource.entity.Logging;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoggingMasterRepository extends JpaRepository<Logging, Long> {
    List<Logging> findByTraceId(String traceId);
}

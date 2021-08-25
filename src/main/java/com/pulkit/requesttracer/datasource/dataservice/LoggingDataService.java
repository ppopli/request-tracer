package com.pulkit.requesttracer.datasource.dataservice;

import com.pulkit.requesttracer.datasource.entity.Logging;
import com.pulkit.requesttracer.datasource.model.LogDTO;

public interface LoggingDataService {

    Logging logData(LogDTO dto);
}

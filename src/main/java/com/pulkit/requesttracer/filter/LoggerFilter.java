package com.pulkit.requesttracer.filter;


import brave.Tracer;
import com.pulkit.requesttracer.datasource.dataservice.LoggingDataService;
import com.pulkit.requesttracer.datasource.model.LogDTO;
import com.pulkit.requesttracer.helper.CustomHttpRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Component
@ConditionalOnProperty(value = "request.tracing.enabled")
public class LoggerFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(LoggerFilter.class);

    @Autowired
    private LoggingDataService loggingDataService;

    @Autowired
    private Tracer tracer;

    @Value("${spring.application.name:unknown}")
    private String serviceName;

    private void logRequest(CustomHttpRequestWrapper request) {
        String body = request.getBody();

        log.info("Request = {}",  body);
        Date spanStartTime = new Date(System.currentTimeMillis());

        LogDTO dto = new LogDTO();
        setSpanAndTraceId(dto);
        dto.setServiceName(serviceName);
        dto.setRequest(body);
        dto.setSpanStartTime(spanStartTime);
        loggingDataService.logData(dto);
    }

    private void setSpanAndTraceId(LogDTO dto) {
        String traceId = tracer.currentSpan().context().traceIdString();
        String spanId = tracer.currentSpan().context().spanIdString();
        List<Object>  extra = tracer.currentSpan().context().extra();
        dto.setTraceId(traceId);
        dto.setSpanId(spanId);
    }

    private void logResponse(String response) {
        LogDTO dto = new LogDTO();
        setSpanAndTraceId(dto);
        dto.setServiceName(serviceName);
        dto.setResponse(response);
        dto.setSpanEndTime(new Date(System.currentTimeMillis()));
        loggingDataService.logData(dto);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        boolean isRequestLogged = false;
        CustomHttpRequestWrapper requestWrapper = new CustomHttpRequestWrapper(httpServletRequest);
        ContentCachingResponseWrapper contentCachingResponseWrapper = new ContentCachingResponseWrapper(httpServletResponse);
        try {
            log.info("Handling request = {}", requestWrapper.getRequestURI());
            log.info("Request Method = {}", requestWrapper.getMethod());
            try {
                logRequest(requestWrapper);
                isRequestLogged = true;
            } catch (Exception e) {
                log.info("unable to log request = {}", requestWrapper.getBody());
                isRequestLogged = true;
            }
            filterChain.doFilter(requestWrapper, contentCachingResponseWrapper);

            String responseBody = new String(contentCachingResponseWrapper.getContentAsByteArray(), contentCachingResponseWrapper.getCharacterEncoding());
            contentCachingResponseWrapper.copyBodyToResponse();
            log.info("response = {}", responseBody);
            try {
                logResponse(responseBody);
            } catch (Exception e) {
                log.info("Unable to log response = {}", responseBody);
            }
        } catch (Exception e) {
            log.info("Exception occurred = {}", e);
            if (isRequestLogged) {
                String errorResponse = e.getMessage();
                log.error("error occurred = {}", errorResponse);
                logResponse(errorResponse);
            }
            throw e;
        }
    }
}

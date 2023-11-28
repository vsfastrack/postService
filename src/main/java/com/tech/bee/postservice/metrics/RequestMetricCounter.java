package com.tech.bee.postservice.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RequestMetricCounter {

    private final MeterRegistry meterRegistry;

    @Autowired
    public RequestMetricCounter(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void increment(String endpointName , String method) {
        Counter.builder("endpoint_hits_total")
                .description("Counts the number of hits for a specific endpoint")
                .tags("endpoint", endpointName , "method",method)
                .register(meterRegistry)
                .increment();
    }
    public void increment(String endpointName , String method , Integer responseCode) {
        Counter.builder("endpoint_hits_total")
                .description("Counts the number of hits for a specific endpoint")
                .tags("endpoint", endpointName , "method",method , "responseCode",responseCode.toString())
                .register(meterRegistry)
                .increment();
    }
}

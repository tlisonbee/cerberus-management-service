/*
 * Copyright (c) 2017 Nike, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.nike.cerberus.service;

import com.nike.riposte.metrics.codahale.CodahaleMetricsCollector;
import com.signalfx.codahale.metrics.SettableLongGauge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class MetricsService {

    private static final Logger log = LoggerFactory.getLogger(MetricsService.class);

    private final CodahaleMetricsCollector metricsCollector;

    @Inject
    public MetricsService(CodahaleMetricsCollector metricsCollector) {
        this.metricsCollector = metricsCollector;
    }

    /**
     * Find gauge with the given name and set it's value.
     *
     * Create a gauge with the given name if one does not already exist.
     *
     * @param name  Name of the gauge
     * @param value  Value of the gauge
     */
    public void setGaugeValue(String name, long value) {
        boolean isGaugeAlreadyRegistered = metricsCollector.getMetricRegistry().getGauges().containsKey(name);

        final SettableLongGauge gauge;
        try {
            gauge = isGaugeAlreadyRegistered ?
                (SettableLongGauge) metricsCollector.getMetricRegistry().getGauges().get(name) :
                metricsCollector.getMetricRegistry().register(name, new SettableLongGauge());

            gauge.setValue(value);
        } catch (IllegalArgumentException e) {
            log.error("Failed to get or create settable gauge, a non-gauge metric with name: {} is probably registered", name);
        }
    }
}

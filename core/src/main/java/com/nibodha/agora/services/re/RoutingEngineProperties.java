/*
 * Copyright 2016 Nibodha Technologies Pvt. Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nibodha.agora.services.re;

import org.apache.camel.LoggingLevel;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author gibugeorge on 16/03/16.
 * @version 1.0
 */
@ConfigurationProperties(prefix = "platform.routingengine")
public class RoutingEngineProperties {

    private boolean traceEnabled;
    private boolean taceExceptions;
    private boolean traceInterceptors;
    private LoggingLevel logLevel = LoggingLevel.ERROR;
    private String logName = "com.nibodh.ip.re";

    public boolean isTraceEnabled() {
        return traceEnabled;
    }

    public void setTraceEnabled(boolean traceEnabled) {
        this.traceEnabled = traceEnabled;
    }

    public boolean isTaceExceptions() {
        return taceExceptions;
    }

    public void setTaceExceptions(boolean taceExceptions) {
        this.taceExceptions = taceExceptions;
    }

    public boolean isTraceInterceptors() {
        return traceInterceptors;
    }

    public void setTraceInterceptors(boolean traceInterceptors) {
        this.traceInterceptors = traceInterceptors;
    }

    public LoggingLevel getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(LoggingLevel logLevel) {
        this.logLevel = logLevel;
    }

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }
}

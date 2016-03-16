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

package com.nibodha.ip.domain;

import java.util.Map;

/**
 * @author gibugeorge on 16/03/16.
 * @version 1.0
 */
public class Message<P> {

    private String endpoint;
    private Map<String, Object> headers;
    private P payload;
    private ErrorInfo errorInfo;

    public Message(final String endpoint, final P payload) {
        this(endpoint, payload, null);
    }

    public Message(final String endpoint, final P payload, final Map<String, Object> headers) {
        this.endpoint = endpoint;
        this.payload = payload;
        this.headers = headers;
    }

    public Message(final String endpoint, final ErrorInfo errorInfo, final Map<String, Object> headers) {
        this.endpoint = endpoint;
        this.errorInfo = errorInfo;
        this.headers = headers;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(final String endpoint) {
        this.endpoint = endpoint;
    }

    public P getPayload() {
        return payload;
    }

    public void setPayload(final P payload) {
        this.payload = payload;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Object> headers) {
        this.headers = headers;
    }

    public ErrorInfo getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(ErrorInfo errorInfo) {
        this.errorInfo = errorInfo;
    }
}

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

/**
 * @author gibugeorge on 22/03/16.
 * @version 1.0
 */
public class AuditInfo {

    public static final String ID_KEY = "AUDIT_INFO_ID";

    private final String id;
    private final String heading;
    private StringBuilder address = new StringBuilder();
    private String contentType;
    private String encoding;
    private String httpMethod;
    private Object headers;
    private String message;
    private String payload;
    private int responseCode;

    public AuditInfo(final String id, final String heading) {
        this.id = id;
        this.heading = heading;
    }

    public String getId() {
        return id;
    }


    public String getHeading() {
        return heading;
    }


    public StringBuilder getAddress() {
        return address;
    }

    public void setAddress(StringBuilder address) {
        this.address = address;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public Object getHeaders() {
        return headers;
    }

    public void setHeader(Object headers) {
        this.headers = headers;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(heading);
        buffer.append("\nID: ").append(id);
        if (address != null && address.length() > 0) {
            buffer.append("\nAddress: ");
            buffer.append(address);
        }
        if (responseCode != 0) {
            buffer.append("\nResponse-Code: ");
            buffer.append(responseCode);
        }
        if (encoding != null && encoding.length() > 0) {
            buffer.append("\nEncoding: ");
            buffer.append(encoding);
        }
        if (httpMethod != null && httpMethod.length() > 0) {
            buffer.append("\nHttp-Method: ");
            buffer.append(httpMethod);
        }
        buffer.append("\nContent-Type: ");
        buffer.append(contentType);
        buffer.append("\nHeaders: ");
        buffer.append(headers);
        if (message != null && message.length() > 0) {
            buffer.append("\nMessages: ");
            buffer.append(message);
        }
        if (payload != null && payload.length() > 0) {
            buffer.append("\nPayload: ");
            buffer.append(payload);
        }
        buffer.append("\n--------------------------------------");
        return buffer.toString();
    }
}

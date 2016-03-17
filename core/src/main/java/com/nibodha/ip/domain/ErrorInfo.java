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

import com.nibodha.ip.exceptions.ExceptionType;

import java.io.Serializable;

/**
 * @author gibugeorge on 08/03/16.
 * @version 1.0
 */
public class ErrorInfo implements Serializable {

    private String message;
    private ExceptionType type;
    private int statusCode;


    public ErrorInfo() {

    }

    public ErrorInfo(final ExceptionType type, final String message) {
        this.type = type;
        this.message = message;
    }


    public String getMessage() {
        return message;
    }

    public ExceptionType getType() {
        return type;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setType(ExceptionType type) {
        this.type = type;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}

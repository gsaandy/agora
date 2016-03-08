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

import com.nibodha.ip.exceptions.PlatformRuntimeException;

/**
 * @author gibugeorge on 08/03/16.
 * @version 1.0
 */
public class Error {

    private final Exception exception;

    private final String errorType;

    private final String errorCode;

    public Error(final Exception exception, final String errorCode, final String errorType) {
        this.exception = exception;
        this.errorCode = errorCode;
        this.errorType = errorType;
    }

    public Exception getException() {
        return exception;
    }

    public String getErrorType() {
        return errorType;
    }

    public String getErrorCode() {
        return errorCode;
    }
}

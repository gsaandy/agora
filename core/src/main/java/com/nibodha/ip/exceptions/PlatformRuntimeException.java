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

package com.nibodha.ip.exceptions;

/**
 * @author Gibu George (gibu.george@nibodha.com)
 * @version 1.0
 */

public class PlatformRuntimeException extends RuntimeException {

    private ExceptionType type;

    public PlatformRuntimeException(final ExceptionType type,
                                    final String message, final Exception e) {
        super(message, e);
        this.type = type;
    }

    public PlatformRuntimeException(final ExceptionType type, final String message) {
        super(message);
        this.type = type;
    }

    public PlatformRuntimeException(final String message, Exception e) {
        this(Type.GENERIC, message, e);
    }

    public PlatformRuntimeException(final String message) {
        this(Type.GENERIC, message);
    }


    public ExceptionType getType() {
        return type;
    }

    public enum Type implements ExceptionType {
        GENERIC,
        ROUTING_ENGINE_RS_ENDPOINT_CONFIG,
        DATA_SOURCE_CONFIG,
        AUTHENTICATION_FAILURE
    }
}

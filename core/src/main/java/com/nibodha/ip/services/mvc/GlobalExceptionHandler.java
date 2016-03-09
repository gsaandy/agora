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

package com.nibodha.ip.services.mvc;

import com.nibodha.ip.domain.ErrorInfo;
import com.nibodha.ip.exceptions.PlatformRuntimeException;
import com.nibodha.ip.services.security.exceptions.AuthenticationFailureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * @author gibugeorge on 09/03/16.
 * @version 1.0
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(value = INTERNAL_SERVER_ERROR)
    public
    @ResponseBody
    ErrorInfo defaultErrorHandler(final HttpServletRequest request, final Exception exception) {

        ErrorInfo errorInfo;
        if (exception instanceof PlatformRuntimeException) {
            errorInfo = new ErrorInfo(((PlatformRuntimeException) exception).getType(), exception.getMessage());
        } else {
            errorInfo = new ErrorInfo(PlatformRuntimeException.Type.GENERIC, exception.getMessage());
        }
        LOGGER.error("Exception of type {} occured in mvc layer for request URI {}", errorInfo.getType(), request.getRequestURI(), exception);
        return errorInfo;
    }

    @ExceptionHandler(value = AuthenticationFailureException.class)
    @ResponseStatus(value = UNAUTHORIZED)
    public
    @ResponseBody
    ErrorInfo authenticationFailureHandler(final HttpServletRequest request, final Exception exception) {

        final ErrorInfo errorInfo = new ErrorInfo(((AuthenticationFailureException) exception).getType(), exception.getMessage());
        LOGGER.error("Exception of type {} occured in mvc layer for request URI {}", errorInfo.getType(), request.getRequestURI(), exception);
        return errorInfo;
    }
}

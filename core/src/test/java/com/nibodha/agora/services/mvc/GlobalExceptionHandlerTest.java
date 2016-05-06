/*
 * Copyright (c) Nibodha Technologies Pvt. Ltd. 2016. All rights reserved.  http://www.nibodha.com
 */

package com.nibodha.agora.services.mvc;

import com.nibodha.agora.exceptions.ExceptionType;
import com.nibodha.agora.exceptions.PlatformRuntimeException;
import com.nibodha.agora.services.security.exceptions.AuthenticationFailureException;
import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * @author gibugeorge on 04/05/16.
 * @version 1.0
 */
public class GlobalExceptionHandlerTest {

    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new SampleController())
            .setControllerAdvice(new GlobalExceptionHandler()).build();

    @Test
    public void whenInstanceOfPlatformRuntimeExceptionThenSpecificExceptionTypeIsAddedToErrorInfo() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/tests/sampleException")).andExpect(new ResultMatcher() {
            @Override
            public void match(MvcResult result) throws Exception {
                result.getResponse().getContentAsString().contains("SAMPLE_FAILURE");
            }
        }).andExpect(MockMvcResultMatchers.status().is(INTERNAL_SERVER_ERROR.value()));


    }

    @Test
    public void whenExceptionOtherThanInstanceOfPREThenGenricFailureIsAddedToErrorInfo() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tests/runtimeException")).andExpect(new ResultMatcher() {
            @Override
            public void match(MvcResult result) throws Exception {
                result.getResponse().getContentAsString().contains("GENERIC_FAILURE");
            }
        }).andExpect(MockMvcResultMatchers.status().is(INTERNAL_SERVER_ERROR.value()));
    }

    @Test
    public void whenAuthentcationFailureThenStatusIsUnAuthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tests/authFailure")).andExpect(new ResultMatcher() {
            @Override
            public void match(MvcResult result) throws Exception {
                result.getResponse().getContentAsString().contains("AUTHENTICATION_FAILURE");
            }
        }).andExpect(MockMvcResultMatchers.status().is(UNAUTHORIZED.value()));
    }

    @Controller
    private class SampleController {
        @RequestMapping("/tests/sampleException")
        public void throwSampleFailure() {
            throw new SampleException(SampleException.Type.SAMPLE_FAILURE, "Sample Failure");
        }

        @RequestMapping("/tests/runtimeException")
        public void throwRuntimeException() {
            throw new RuntimeException("Exception");
        }

        @RequestMapping("/tests/authFailure")
        public void throwAuthFailure() {
            throw new AuthenticationFailureException("authFailure");
        }
    }

    private static class SampleException extends PlatformRuntimeException {

        public SampleException(ExceptionType type, String message) {
            super(type, message);
        }

        public enum Type implements ExceptionType {
            SAMPLE_FAILURE
        }
    }


}

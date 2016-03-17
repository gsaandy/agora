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

package com.nibodha.ip.services.re.components.rs.interceptors;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.io.CacheAndWriteOutputStream;
import org.apache.cxf.message.Message;

import java.io.OutputStream;
import java.io.Writer;

/**
 * @author gibugeorge on 16/03/16.
 * @version 1.0
 */
public class AuditLogOutInterceptor extends LoggingOutInterceptor {

    private static final String AUDIT_SETUP = AuditLogOutInterceptor.class.getName() + ".audit-setup";

    @Override
    public void handleMessage(final Message message) throws Fault {
        final OutputStream os = message.getContent(OutputStream.class);
        final Writer iowriter = message.getContent(Writer.class);
        if (os == null && iowriter == null) {
            return;
        }
        if (writer != null) {
            // Write the output while caching it for the log message
            boolean hasLogged = message.containsKey(AUDIT_SETUP);
            if (!hasLogged) {
                message.put(AUDIT_SETUP, Boolean.TRUE);
                if (os != null) {
                    final CacheAndWriteOutputStream newOut = new CacheAndWriteOutputStream(os);
                    if (limit > 0) {
                        newOut.setCacheLimit(limit);
                    }
                    message.setContent(OutputStream.class, newOut);
                } else {
                    //message.setContent(Writer.class, new LogWriter(logger, message, iowriter));
                }
            }
        }
    }
}

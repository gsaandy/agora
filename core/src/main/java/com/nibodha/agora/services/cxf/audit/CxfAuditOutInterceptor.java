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

package com.nibodha.agora.services.cxf.audit;

import com.nibodha.agora.domain.AuditInfo;
import com.nibodha.agora.services.audit.AuditContext;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.io.CacheAndWriteOutputStream;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.io.CachedOutputStreamCallback;
import org.apache.cxf.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * @author gibugeorge on 22/03/16.
 * @version 1.0
 */
public class CxfAuditOutInterceptor extends AbstractAuditInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CxfAuditOutInterceptor.class);
    private static final String AUDIT_SETUP = "AUDIT_SETUP";

    public CxfAuditOutInterceptor(final AuditContext auditContext) {
        super(auditContext);

    }

    @Override
    public void handleMessage(Message message) throws Fault {
        final OutputStream os = message.getContent(OutputStream.class);
        boolean hasLogged = message.containsKey(AUDIT_SETUP);
        if (!hasLogged) {
            message.put(AUDIT_SETUP, Boolean.TRUE);
            if (os != null) {
                final CacheAndWriteOutputStream cacheAndWriteOutputStream = new CacheAndWriteOutputStream(os);
                message.setContent(OutputStream.class, cacheAndWriteOutputStream);
                cacheAndWriteOutputStream.registerCallback(new AuditStreamCallback(getAuditInfo(message), os, message));
            }
        }
        final AuditInfo auditInfo = getAuditInfo(message);
        audit(auditInfo);
    }

    private AuditInfo getAuditInfo(final Message message) {
        String id = (String) message.getExchange().get(AuditInfo.ID_KEY);
        if (id == null) {
            id = UUID.randomUUID().toString();
            message.getExchange().put(AuditInfo.ID_KEY, id);
        }
        final AuditInfo buffer
                = new AuditInfo(id, "Outbound Message\n---------------------------");

        Integer responseCode = (Integer) message.get(Message.RESPONSE_CODE);
        if (responseCode != null) {
            buffer.setResponseCode(responseCode);
        }

        String encoding = (String) message.get(Message.ENCODING);
        if (encoding != null) {
            buffer.setEncoding(encoding);
        }
        String httpMethod = (String) message.get(Message.HTTP_REQUEST_METHOD);
        if (httpMethod != null) {
            buffer.setHttpMethod(httpMethod);
        }
        String address = (String) message.get(Message.ENDPOINT_ADDRESS);
        if (address != null) {
            buffer.getAddress().append(address);
            String uri = (String) message.get(Message.REQUEST_URI);
            if (uri != null && !address.startsWith(uri)) {
                if (!address.endsWith("/") && !uri.startsWith("/")) {
                    buffer.getAddress().append("/");
                }
                buffer.getAddress().append(uri);
            }
        }
        String ct = (String) message.get(Message.CONTENT_TYPE);
        if (ct != null) {
            buffer.setContentType(ct);
        }
        Object headers = message.get(Message.PROTOCOL_HEADERS);
        if (headers != null) {
            buffer.setHeader(headers);
        }
        return buffer;
    }

    class AuditStreamCallback implements CachedOutputStreamCallback {

        private final AuditInfo auditInfo;
        private final Message message;
        private final OutputStream origStream;


        public AuditStreamCallback(final AuditInfo auditInfo, final OutputStream os, final Message message) {
            this.auditInfo = auditInfo;
            this.message = message;
            this.origStream = os;
        }

        @Override
        public void onClose(final CachedOutputStream os) {
            try {
                final StringBuilder stringBuilder = new StringBuilder();
                os.writeCacheTo(stringBuilder);
                auditInfo.setPayload(stringBuilder.toString());
            } catch (IOException e) {
                LOGGER.error("Exception while converting cached stream to String", e);
            }
            try {
                //empty out the cache
                os.lockOutputStream();
                os.resetOut(null, false);
            } catch (Exception ex) {
                //ignore
            }
            message.setContent(OutputStream.class,
                    origStream);
        }

        @Override
        public void onFlush(final CachedOutputStream os) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Flushing cached output stream");
            }
        }
    }
}

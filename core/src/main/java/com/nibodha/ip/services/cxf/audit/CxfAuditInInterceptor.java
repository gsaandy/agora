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

package com.nibodha.ip.services.cxf.audit;

import com.nibodha.ip.domain.AuditInfo;
import com.nibodha.ip.services.audit.AuditContext;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.io.DelegatingInputStream;
import org.apache.cxf.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.UUID;

/**
 * @author gibugeorge on 28/03/16.
 * @version 1.0
 */
public class CxfAuditInInterceptor extends AbstractAuditInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CxfAuditInInterceptor.class);


    public CxfAuditInInterceptor(AuditContext auditContext) {
        super(auditContext);
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        final AuditInfo auditInfo = getAuditInfo(message);
        InputStream is = message.getContent(InputStream.class);
        if (is != null) {
            readMessageBody(message, is, auditInfo);
        }
        audit(auditInfo);
    }


    private AuditInfo getAuditInfo(final Message message) {
        String id = (String) message.getExchange().get(AuditInfo.ID_KEY);
        if (id == null) {
            id = UUID.randomUUID().toString();
            message.getExchange().put(AuditInfo.ID_KEY, id);
        }
        final AuditInfo buffer
                = new AuditInfo(id, "Inbound Message\n---------------------------");

        if (!Boolean.TRUE.equals(message.get(Message.DECOUPLED_CHANNEL_MESSAGE))) {
            // avoid logging the default responseCode 200 for the decoupled responses
            Integer responseCode = (Integer) message.get(Message.RESPONSE_CODE);
            if (responseCode != null) {
                buffer.setResponseCode(responseCode);
            }
        }

        String encoding = (String) message.get(Message.ENCODING);
        if (encoding != null) {
            buffer.setEncoding(encoding);
        }
        String httpMethod = (String) message.get(Message.HTTP_REQUEST_METHOD);
        if (httpMethod != null) {
            buffer.setHttpMethod(httpMethod);
        }
        String uri = (String) message.get(Message.REQUEST_URL);
        if (uri == null) {
            String address = (String) message.get(Message.ENDPOINT_ADDRESS);
            uri = (String) message.get(Message.REQUEST_URI);
            if (uri != null && uri.startsWith("/")) {
                if (address != null && !address.startsWith(uri)) {
                    if (address.endsWith("/") && address.length() > 1) {
                        address = address.substring(0, address.length());
                    }
                    uri = address + uri;
                }
            } else {
                uri = address;
            }
        }
        if (uri != null) {
            buffer.getAddress().append(uri);
            String query = (String) message.get(Message.QUERY_STRING);
            if (query != null) {
                buffer.getAddress().append("?").append(query);
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

    protected void readMessageBody(Message message, InputStream is, AuditInfo auditInfo) {
        final CachedOutputStream cachedOutputStream = new CachedOutputStream();
        try {
            // use the appropriate input stream and restore it later
            InputStream bis = is instanceof DelegatingInputStream
                    ? ((DelegatingInputStream) is).getInputStream() : is;


            //only copy up to the limit since that's all we need to log
            //we can stream the rest
            IOUtils.copy(bis, cachedOutputStream);
            cachedOutputStream.flush();
            bis = new SequenceInputStream(cachedOutputStream.getInputStream(), bis);

            // restore the delegating input stream or the input stream
            if (is instanceof DelegatingInputStream) {
                ((DelegatingInputStream) is).setInputStream(bis);
            } else {
                message.setContent(InputStream.class, bis);
            }

            final StringBuilder builder = new StringBuilder();
            cachedOutputStream.writeCacheTo(builder);
            auditInfo.setPayload(builder.toString());

        } catch (Exception e) {
            throw new Fault(e);
        } finally {
            try {
                cachedOutputStream.close();
            } catch (IOException e) {

            }

        }
    }
}

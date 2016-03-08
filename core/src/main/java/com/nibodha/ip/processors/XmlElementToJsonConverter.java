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

package com.nibodha.ip.processors;

import com.nibodha.ip.xstream.HierarchicalStreamCopier;
import com.nibodha.ip.xstream.StaxReader;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.StreamException;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.json.JettisonStaxWriter;
import com.thoughtworks.xstream.io.xml.AbstractPullReader;
import com.thoughtworks.xstream.io.xml.QNameMap;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.io.xml.StaxWriter;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.mapped.Configuration;
import org.codehaus.jettison.mapped.MappedXMLStreamWriter;
import org.springframework.stereotype.Component;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * @author gibugeorge on 14/12/15.
 * @version 1.0
 */
@Component
public class XmlElementToJsonConverter implements Processor {

    private final HierarchicalStreamCopier copier = new HierarchicalStreamCopier();
    private final CustomJettisonMappedXmlDriver jettisonDriver;
    private final CustomStaxDriver staxDriver = new CustomStaxDriver();


    public XmlElementToJsonConverter() {
        Configuration configuration = new Configuration();
        configuration.setAttributeKey("");
        jettisonDriver = new CustomJettisonMappedXmlDriver(configuration);

    }

    @Override
    public void process(final Exchange exchange) throws Exception {
        final String body = exchange.getIn().getBody(String.class);
        if (StringUtils.isNotBlank(body)) {
            final HierarchicalStreamReader sourceReader = staxDriver.createReader(new StringReader(body));
            final StringWriter buffer = new StringWriter();
            final HierarchicalStreamWriter destinationWriter = jettisonDriver.createWriter(buffer);

            copier.copy(sourceReader, destinationWriter);
            exchange.getIn().setBody(buffer.toString());
            sourceReader.close();
            destinationWriter.close();
        }
    }

    class CustomJettisonMappedXmlDriver extends JettisonMappedXmlDriver {
        CustomJettisonMappedXmlDriver(final Configuration configuration) {
            super(configuration);
        }

        public HierarchicalStreamWriter createWriter(final Writer writer) {

            try {
                if (useSerializeAsArray) {
                    MappedXMLStreamWriter xmlStreamWriter = (MappedXMLStreamWriter) mof.createXMLStreamWriter(writer);
                    xmlStreamWriter.setValueKey("value");
                    return new JettisonStaxWriter(new QNameMap(), xmlStreamWriter, getNameCoder(), convention);
                } else {
                    return new StaxWriter(new QNameMap(), mof.createXMLStreamWriter(writer), getNameCoder());
                }
            } catch (final XMLStreamException e) {
                throw new StreamException(e);
            }
        }
    }

    class CustomStaxDriver extends StaxDriver {

        public AbstractPullReader createStaxReader(XMLStreamReader in) {
            return new StaxReader(super.getQnameMap(), in, getNameCoder());
        }
    }

}

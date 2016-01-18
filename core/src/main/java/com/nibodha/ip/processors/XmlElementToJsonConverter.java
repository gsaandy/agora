/*
 * Copyright 2016 Nibodha Trechnologies Pvt. Ltd.
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
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.xml.DomReader;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import java.io.StringWriter;

/**
 * @author gibugeorge on 14/12/15.
 * @version 1.0
 */
@Component
public class XmlElementToJsonConverter implements Processor {

    private final HierarchicalStreamCopier copier = new HierarchicalStreamCopier();
    private final JettisonMappedXmlDriver jettisonDriver = new JettisonMappedXmlDriver();

    @Override
    public void process(final Exchange exchange) throws Exception {
        final Element element = exchange.getIn().getBody(Element.class);
        if (element != null) {
            final HierarchicalStreamReader sourceReader = new DomReader(element);
            final StringWriter buffer = new StringWriter();
            final HierarchicalStreamWriter destinationWriter = jettisonDriver.createWriter(buffer);
            copier.copy(sourceReader, destinationWriter);
            exchange.getIn().setBody(buffer.toString());
        }
    }
}
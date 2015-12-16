/*
 * Copyright (c) Nibodha Technologies Pvt. Ltd. 2015. All rights reserved.  http://www.nibodha.com
 */

package com.nibodha.ip.processors;

import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.copy.HierarchicalStreamCopier;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.xml.DomReader;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import java.io.StringWriter;

/**
 * @author gibugeorge on 14/12/15.
 * @version 1.0
 */
@Component
public class XmlElementToJsonConverter implements Processor {
    private static final Logger LOGGER= LoggerFactory.getLogger(XmlElementToJsonConverter.class);
    @Override
    public void process(final Exchange exchange) throws Exception {
        final Element element = exchange.getIn().getBody(Element.class);
        final HierarchicalStreamReader sourceReader =  new DomReader(element);

        final StringWriter buffer = new StringWriter();
        final JettisonMappedXmlDriver jettisonDriver = new JettisonMappedXmlDriver();
        jettisonDriver.createWriter(buffer);
        final HierarchicalStreamWriter destinationWriter = jettisonDriver.createWriter(buffer);

        final HierarchicalStreamCopier copier = new HierarchicalStreamCopier();
        copier.copy(sourceReader, destinationWriter);
        exchange.getIn().setBody(buffer.toString());

    }

}
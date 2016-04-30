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

package com.nibodha.agora.xstream;

import com.thoughtworks.xstream.converters.ErrorWriter;
import com.thoughtworks.xstream.io.StreamException;
import com.thoughtworks.xstream.io.naming.NameCoder;
import com.thoughtworks.xstream.io.xml.*;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * @author gibugeorge on 08/03/16.
 * @version 1.0
 */
public class StaxReader extends AbstractPullReader {


    private final QNameMap qnameMap;
    private final XMLStreamReader in;
    protected static final int CDATA = 5;

    public StaxReader(final QNameMap qnameMap, final XMLStreamReader in, final NameCoder replacer) {
        super(replacer);
        this.qnameMap = qnameMap;
        this.in = in;
        moveDown();
    }

    @Override
    protected int pullNextEvent() {
        try {
            switch (in.next()) {
                case XMLStreamConstants.START_DOCUMENT:
                case XMLStreamConstants.START_ELEMENT:
                    return START_NODE;
                case XMLStreamConstants.END_DOCUMENT:
                case XMLStreamConstants.END_ELEMENT:
                    return END_NODE;
                case XMLStreamConstants.CHARACTERS:
                    return TEXT;
                case XMLStreamConstants.CDATA:
                    return TEXT;
                case XMLStreamConstants.COMMENT:
                    return COMMENT;
                default:
                    return OTHER;
            }
        } catch (XMLStreamException e) {
            throw new StreamException(e);
        }
    }

    @Override
    protected String pullElementName() {
        // let the QNameMap handle any mapping of QNames to Java class names
        QName qname = in.getName();
        return qnameMap.getJavaClassName(qname);
    }

    @Override
    protected String pullText() {
        return in.getText();
    }

    @Override
    public String getAttribute(String name) {
        return in.getAttributeValue(null, encodeAttribute(name));
    }

    @Override
    public String getAttribute(int index) {
        return in.getAttributeValue(index);
    }

    @Override
    public int getAttributeCount() {
        return in.getAttributeCount();
    }

    @Override
    public String getAttributeName(int index) {
        return decodeAttribute(in.getAttributeLocalName(index));
    }

    @Override
    public void appendErrors(ErrorWriter errorWriter) {
        errorWriter.add("line number", String.valueOf(in.getLocation().getLineNumber()));
    }

    @Override
    public void close() {
        try {
            in.close();
        } catch (XMLStreamException e) {
            throw new StreamException(e);
        }
    }

}

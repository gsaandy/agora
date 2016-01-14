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

package com.nibodha.ip.xstream;

import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author gibugeorge on 14/01/16.
 * @version 1.0
 */
public class HierarchicalStreamCopier {
    public void copy(final HierarchicalStreamReader source, final HierarchicalStreamWriter destination) {
        destination.startNode(source.getNodeName());
        int attributeCount = source.getAttributeCount();
        for (int i = 0; i < attributeCount; i++) {
            destination.addAttribute(source.getAttributeName(i), source.getAttribute(i));
        }
        String value = source.getValue();
        if (StringUtils.isNotEmpty(value)) {
            value = value.replaceAll("\\n", "").replaceAll("\\r", "").trim();
        }
        if (StringUtils.isNotEmpty(value)) {
            destination.setValue(value);
        }
        while (source.hasMoreChildren()) {
            source.moveDown();
            copy(source, destination);
            source.moveUp();
        }
        destination.endNode();
    }
}

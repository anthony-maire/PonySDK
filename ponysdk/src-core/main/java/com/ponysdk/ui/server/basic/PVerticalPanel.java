/*
 * Copyright (c) 2011 PonySDK
 *  Owners:
 *  Luciano Broussal  <luciano.broussal AT gmail.com>
 *  Mathieu Barbier   <mathieu.barbier AT gmail.com>
 *  Nicolas Ciaravola <nicolas.ciaravola.pro AT gmail.com>
 *  
 *  WebSite:
 *  http://code.google.com/p/pony-sdk/
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.ponysdk.ui.server.basic;

import com.ponysdk.core.stm.TxnObject;
import com.ponysdk.core.stm.TxnObjectImpl;
import com.ponysdk.ui.terminal.Dictionnary.PROPERTY;
import com.ponysdk.ui.terminal.WidgetType;
import com.ponysdk.ui.terminal.basic.PHorizontalAlignment;
import com.ponysdk.ui.terminal.basic.PVerticalAlignment;

/**
 * A panel that lays all of its widgets out in a single vertical column.
 * <p>
 * <img class='gallery' src='doc-files/PVerticalPanel.png'/>
 * </p>
 */
public class PVerticalPanel extends PCellPanel implements HasPAlignment {

    private final TxnObjectImpl<PHorizontalAlignment> horizontalAlignment = new TxnObjectImpl<PHorizontalAlignment>(PHorizontalAlignment.ALIGN_LEFT);
    private final TxnObjectImpl<PVerticalAlignment> verticalAlignment = new TxnObjectImpl<PVerticalAlignment>(PVerticalAlignment.ALIGN_TOP);

    @Override
    protected WidgetType getWidgetType() {
        return WidgetType.VERTICAL_PANEL;
    }

    @Override
    public void setHorizontalAlignment(final PHorizontalAlignment horizontalAlignment) {
        this.horizontalAlignment.set(horizontalAlignment);
    }

    @Override
    public void setVerticalAlignment(final PVerticalAlignment verticalAlignment) {
        this.verticalAlignment.set(verticalAlignment);
    }

    public PHorizontalAlignment getHorizontalAlignment() {
        return horizontalAlignment.get();
    }

    public PVerticalAlignment getVerticalAlignment() {
        return verticalAlignment.get();
    }

    @Override
    public void insert(final PWidget child, final int beforeIndex) {
        super.insert(child, beforeIndex);
    }

    @Override
    public void beforeFlush(final TxnObject<?> txnObject) {
        if (txnObject == horizontalAlignment) {
            saveUpdate(PROPERTY.HORIZONTAL_ALIGNMENT, horizontalAlignment.get().ordinal());
        } else if (txnObject == verticalAlignment) {
            saveUpdate(PROPERTY.VERTICAL_ALIGNMENT, verticalAlignment.get().ordinal());
        } else {
            super.beforeFlush(txnObject);
        }
    }

}

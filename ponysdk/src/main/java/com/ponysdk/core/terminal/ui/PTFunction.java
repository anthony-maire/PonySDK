/*
 * Copyright (c) 2018 PonySDK
 *  Owners:
 *  Luciano Broussal  <luciano.broussal AT gmail.com>
 *	Mathieu Barbier   <mathieu.barbier AT gmail.com>
 *	Nicolas Ciaravola <nicolas.ciaravola.pro AT gmail.com>
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

package com.ponysdk.core.terminal.ui;

import com.ponysdk.core.terminal.UIBuilder;
import com.ponysdk.core.terminal.model.ReaderBuffer;

public class PTFunction extends AbstractPTObject {

    private JsTextFunction function;

    public static native JsTextFunction eval(String code) /*-{
                                                          return new Function('args',code);
                                                          }-*/;

    @Override
    public void create(final ReaderBuffer buffer, final int objectId, final UIBuilder uiBuilder) {
        super.create(buffer, objectId, uiBuilder);
        function = eval(buffer.readBinaryModel().getStringValue());
    }

    public JsTextFunction getFunction() {
        return function;
    }

}

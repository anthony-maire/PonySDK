/*
 * Copyright (c) 2011 PonySDK
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

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.ponysdk.core.model.ClientToServerModel;
import com.ponysdk.core.model.ServerToClientModel;
import com.ponysdk.core.terminal.PonySDK;
import com.ponysdk.core.terminal.UIBuilder;
import com.ponysdk.core.terminal.instruction.PTInstruction;
import com.ponysdk.core.terminal.model.BinaryModel;
import com.ponysdk.core.terminal.model.ReaderBuffer;

import elemental.client.Browser;
import elemental.events.Event;
import elemental.events.EventListener;
import elemental.html.Window;

public class PTWindow extends AbstractPTObject {

    private static final Logger log = Logger.getLogger(PTWindow.class.getName());

    private static final String EMPTY = "";

    private Window window;
    private String url;
    private String name;
    private String features;

    private UIBuilder uiService;

    private boolean ponySDKStarted = false;

    @Override
    public void create(final ReaderBuffer buffer, final int objectId, final UIBuilder builder) {
        super.create(buffer, objectId, builder);

        if (log.isLoggable(Level.INFO)) log.log(Level.INFO, "PTWindowID created : " + objectID);

        uiService = builder;

        url = buffer.readBinaryModel().getStringValue();
        if (url == null)
            url = GWT.getHostPageBaseURL() + "?" +
                    ClientToServerModel.WINDOW_ID.toStringValue() + "=" + objectId + "&" +
                    ClientToServerModel.UI_CONTEXT_ID.toStringValue() + "=" + PonySDK.uiContextId;

        name = buffer.readBinaryModel().getStringValue();
        if (name == null)
            name = EMPTY;

        features = buffer.readBinaryModel().getStringValue();
        if (features == null)
            features = EMPTY;

        PTWindowManager.get().register(this);
    }

    @Override
    public boolean update(final ReaderBuffer buffer, final BinaryModel binaryModel) {
        if (ServerToClientModel.OPEN.equals(binaryModel.getModel())) {
            window = Browser.getWindow().open(url, name, features);
            window.setOnbeforeunload(new EventListener() {

                @Override
                public void handleEvent(final Event event) {
                    final PTInstruction instruction = new PTInstruction(objectID);
                    instruction.put(ClientToServerModel.HANDLER_CLOSE);
                    uiService.sendDataToServer(instruction);
                    PTWindowManager.get().unregister(PTWindow.this);
                }
            });
            return true;
        }
        if (ServerToClientModel.CLOSE.equals(binaryModel.getModel())) {
            close(false);
            return true;
        }
        return false;
    }

    public void close(final boolean forced) {
        window.close();
    }

    public void postMessage(final ReaderBuffer buffer) {
        window.postMessage(buffer.getMessage(), "*");
    }

    public void setReady() {
        ponySDKStarted = true;
        setTitle(name, window);//workaround to set title on google chrome

        final PTInstruction instruction = new PTInstruction(objectID);
        instruction.put(ClientToServerModel.HANDLER_OPEN);
        uiService.sendDataToServer(instruction);

        //

    }

    public boolean isReady() {
        return ponySDKStarted;
    }

    public final native void setTitle(String title, Window window) /*-{
                                                                            window.document.title = title;
                                                                            }-*/;

}

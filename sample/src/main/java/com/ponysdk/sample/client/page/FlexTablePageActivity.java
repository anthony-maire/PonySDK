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

package com.ponysdk.sample.client.page;

import java.util.Timer;
import java.util.TimerTask;

import com.ponysdk.core.socket.ConnectionListener;
import com.ponysdk.ui.server.basic.PCommand;
import com.ponysdk.ui.server.basic.PFlexTable;
import com.ponysdk.ui.server.basic.PLabel;
import com.ponysdk.ui.server.basic.PPusher;
import com.ponysdk.ui.server.basic.PScrollPanel;

public class FlexTablePageActivity extends SamplePageActivity {

    private PLabel[][] labels;

    public FlexTablePageActivity() {
        super("Flex Table", "Table");
    }

    @Override
    protected void onFirstShowPage() {
        final PPusher pusher = PPusher.initialize();

        super.onFirstShowPage();

        final PFlexTable table = new PFlexTable();
        table.setCellPadding(0);
        table.setCellSpacing(0);
        table.setSizeFull();

        labels = new PLabel[100][10];

        for (int r = 0; r < 100; r++) {
            for (int c = 0; c < 10; c++) {
                final PLabel pLabel = new PLabel(r + "_" + c);
                labels[r][c] = pLabel;
                pLabel.setWidth("40px");
                table.setWidget(r, c, pLabel);
            }
        }

        pusher.addConnectionListener(new ConnectionListener() {

            final Timer timer = new Timer();

            @Override
            public void onOpen() {

                timer.scheduleAtFixedRate(new TimerTask() {

                    @Override
                    public void run() {
                        pusher.execute(new PCommand() {

                            @Override
                            public void execute() {
                                for (int r = 0; r < 100; r++) {
                                    for (int c = 0; c < 10; c++) {
                                        final int d = (int) (Math.random() * 255);
                                        final int d2 = (int) (Math.random() * 255);
                                        final int d3 = (int) (Math.random() * 255);
                                        labels[r][c].setText(d + "");
                                        labels[r][c].setStyleProperty("backgroundColor", "rgb(" + d + "," + d2 + "," + d3 + ")");
                                    }
                                }
                            }
                        });
                    }
                }, 0, 300);
            }

            @Override
            public void onClose() {
                timer.cancel();
            }
        });

        final PScrollPanel scrollPanel = new PScrollPanel();
        scrollPanel.setWidget(table);
        scrollPanel.setSizeFull();

        examplePanel.setWidget(scrollPanel);
    }
}

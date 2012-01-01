/*
 * Copyright (c) 2009-2011, bad robot (london) ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package bad.robot.turtle.server;

import com.google.code.tempusfugit.temporal.Condition;
import org.mortbay.jetty.Server;

public class ServerRunningCondition implements Condition {
    private final Server server;

    public static ServerRunningCondition running(Server server) {
        return new ServerRunningCondition(server);
    }

    private ServerRunningCondition(Server server) {
        this.server = server;
    }

    @Override
    public boolean isSatisfied() {
        return server.isRunning();
    }
}
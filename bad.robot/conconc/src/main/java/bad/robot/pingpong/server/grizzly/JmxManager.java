/*
 * Copyright (c) 2009-2011, bad robot (london) ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package bad.robot.pingpong.server.grizzly;

import com.sun.grizzly.http.Management;
import org.apache.commons.modeler.Registry;

import javax.management.ObjectName;

class JmxManager implements Management {
    
    public void registerComponent(Object bean, ObjectName objectName, String type) throws Exception {
        Registry.getRegistry().registerComponent(bean, objectName, type);
    }

    public void unregisterComponent(ObjectName objectName) throws Exception {
        Registry.getRegistry().unregisterComponent(objectName);
    }
}

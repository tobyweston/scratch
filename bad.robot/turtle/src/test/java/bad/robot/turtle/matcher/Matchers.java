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

package bad.robot.turtle.matcher;


import org.hamcrest.TypeSafeMatcher;

import javax.ws.rs.core.Response;

public class Matchers {

    public static TypeSafeMatcher<Response> hasStatus(Response.Status status) {
        return new ResponseStatusCodeMatcher(status);
    }

    public static TypeSafeMatcher<Response> responseBodyContains(String string) {
        return new ResponseBodyMatcher(string);
    }

    public static TypeSafeMatcher<Response> hasHeader(String header, String expected) {
        return new ResponseHeaderMatcher(header, expected);
    }
    
}

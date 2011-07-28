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

package bad.robot.turtle.http;

import bad.robot.http.HttpResponse;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class HttpResponseStatusCodeMatcher extends TypeSafeMatcher<HttpResponse> {
    private final int expected;

    public HttpResponseStatusCodeMatcher(int expected) {
        this.expected = expected;
    }

    public static HttpResponseStatusCodeMatcher hasStatus(int expected) {
        return new HttpResponseStatusCodeMatcher(expected);
    }

    @Override
    public boolean matchesSafely(HttpResponse actual) {
        return actual.getStatusCode() == expected;
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(expected);
    }
}

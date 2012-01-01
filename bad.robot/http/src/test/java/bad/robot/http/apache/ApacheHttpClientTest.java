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

package bad.robot.http.apache;

import bad.robot.http.HttpClientBuilder;
import bad.robot.http.HttpException;
import bad.robot.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@RunWith(JMock.class)
public class ApacheHttpClientTest {

    private static final String ANY_URL = "http://not.real.url";
    private final Mockery context = new Mockery();
    private final HttpClientBuilder builder = context.mock(HttpClientBuilder.class);
    private final HttpClient client = context.mock(HttpClient.class, "apache http");
    private final HttpResponse response = context.mock(HttpResponse.class);

    @Test
    public void delegateToApacheForGet() throws IOException {
        context.checking(new Expectations() {{
            one(builder).build(); will(returnValue(client));
            one(client).execute(with(any(HttpGet.class)), with(any(ResponseHandler.class))); will(returnValue(response));
        }});
        ApacheHttpClient http = new ApacheHttpClient(builder);
        http.get(anyUrl());
    }

    @Test (expected = HttpException.class)
    public void wrapExceptionsForGet() throws MalformedURLException {
        context.checking(new Expectations(){{
            one(builder).build(); will(returnValue(client));
            one(client); will(throwException(new IOException()));
        }});
        ApacheHttpClient http = new ApacheHttpClient(builder);
        http.get(anyUrl());
    }

    private static URL anyUrl() throws MalformedURLException {
        return new URL(ANY_URL);
    }
}
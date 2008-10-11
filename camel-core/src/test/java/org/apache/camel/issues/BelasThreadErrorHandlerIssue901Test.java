/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.issues;

import org.apache.camel.ContextTestSupport;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Unit test to verify that error handling using thread() pool also works as expected.
 */
public class BelasThreadErrorHandlerIssue901Test extends ContextTestSupport {
    private static final transient Log LOG = LogFactory.getLog(BelasThreadErrorHandlerIssue901Test.class);
    private String msg1 = "Message Intended For Processor #1",
        msg2 = "Message Intended For Processor #2",
        msg3 = "Message Intended For Processor #3";
    private int callCounter1 = 0, callCounter2 = 0, callCounter3 = 0;
    private int redelivery = 1;

    protected void setUp() throws Exception {
        disableJMX();
        super.setUp();
    }

    public void testThreadErrorHandlerLogging() throws Exception {
        MockEndpoint handled = getMockEndpoint("mock:handled");

        template.sendBody("seda:errorTest", msg1);

        handled.expectedMessageCount(1);
        handled.expectedBodiesReceived(msg3);

        // TODO: Enable this when looking into this issue
        //Thread.sleep(3000);

        assertMockEndpointsSatisfied();

        assertEquals(1, callCounter1);
        assertEquals(1, callCounter2);
        assertEquals(1 + redelivery, callCounter3);  // Only this should be more then 1
    }

    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() throws Exception {
                //getContext().addInterceptStrategy(new Tracer());
                errorHandler(deadLetterChannel("mock:handled").maximumRedeliveries(redelivery));
                
                from("seda:errorTest")
                    // TODO: When using thread there is a multi threading / concurreny issue in Camel
                    // hard to debug as it tend only to surface when unit test is running really fast
                    // (no break points)

                    //.thread(5).maxSize(5)
                    // Processor #1
                    .process(new Processor() {
                        public void process(Exchange exchange) throws Exception {
                            callCounter1++;
                            LOG.debug("Processor #1 Received A " + exchange.getIn().getBody());
                            exchange.getOut().setBody(msg2);
                        }
                    })
                    // Processor #2
                    .process(new Processor() {
                        public void process(Exchange exchange) throws Exception {
                            callCounter2++;
                            LOG.debug("Processor #2 Received A " + exchange.getIn().getBody());
                            exchange.getOut().setBody(msg3);
                        }
                    })
                    // Processor #3
                    .process(new Processor() {
                        public void process(Exchange exchange) throws Exception {
                            //Thread.sleep(100);
                            callCounter3++;
                            LOG.debug("Processor #3 Received A " + exchange.getIn().getBody());
                            throw new Exception("Forced exception by unit test");
                        }
                    });
            }
        };
    }

}

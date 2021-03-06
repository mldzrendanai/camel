/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.azure.blob;

import com.microsoft.azure.storage.AccessCondition;
import com.microsoft.azure.storage.OperationContext;
import com.microsoft.azure.storage.blob.BlobRequestOptions;

public class BlobServiceRequestOptions {
    private AccessCondition accessCond;
    private BlobRequestOptions requestOpts;
    private OperationContext opContext;

    public AccessCondition getAccessCond() {
        return accessCond;
    }

    public void setAccessCond(AccessCondition accessCond) {
        this.accessCond = accessCond;
    }

    public BlobRequestOptions getRequestOpts() {
        return requestOpts;
    }

    public void setRequestOpts(BlobRequestOptions requestOpts) {
        this.requestOpts = requestOpts;
    }

    public OperationContext getOpContext() {
        return opContext;
    }

    public void setOpContext(OperationContext opContext) {
        this.opContext = opContext;
    }
}

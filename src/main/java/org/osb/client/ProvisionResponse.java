/*
 * Copyright Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags and
 * the COPYRIGHT.txt file distributed with this work.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.osb.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProvisionResponse extends Response {

    @JsonProperty("dashboard_url")
    private String dashboardUrl;
    @JsonProperty("operation")
    private String operation;
    
    @JsonProperty("dashboard_url")
    public String getDashboardUrl() {
        return dashboardUrl;
    }

    @JsonProperty("dashboard_url")
    public void setDashboardUrl(String dashboardUrl) {
        this.dashboardUrl = dashboardUrl;
    }

    @JsonProperty("operation")
    public String getOperation() {
        return operation;
    }

    @JsonProperty("operation")
    public void setOperation(String operation) {
        this.operation = operation;
    }

    @JsonIgnore
    public boolean isProvisioned() {
        return responseCode == 200;
    }
    
    @JsonIgnore
    public boolean isProvisionInProgress() {
        return responseCode == 201 || responseCode == 202;
    } 
    
    @JsonIgnore
    public boolean isProvisionFailed() {
        return this.responseCode != 200 || responseCode != 201 || responseCode != 202;
    }
}
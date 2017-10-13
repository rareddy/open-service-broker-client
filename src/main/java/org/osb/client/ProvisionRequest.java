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

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProvisionRequest {

    @JsonProperty("service_id")
    private String serviceId;
    @JsonProperty("plan_id")
    private String planId;
    @JsonProperty("context")
    private Parameters context;
    @JsonProperty("organization_guid")
    private String organizationGuid;
    @JsonProperty("space_guid")
    private String spaceGuid;
    @JsonProperty("parameters")
    private Parameters parameters;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("service_id")
    public String getServiceId() {
        return serviceId;
    }

    @JsonProperty("service_id")
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    @JsonProperty("plan_id")
    public String getPlanId() {
        return planId;
    }

    @JsonProperty("plan_id")
    public void setPlanId(String planId) {
        this.planId = planId;
    }

    @JsonProperty("context")
    public Parameters getContext() {
        return context;
    }

    @JsonProperty("context")
    public void setContext(Parameters context) {
        this.context = context;
    }

    @JsonProperty("organization_guid")
    public String getOrganizationGuid() {
        return organizationGuid;
    }

    @JsonProperty("organization_guid")
    public void setOrganizationGuid(String organizationGuid) {
        this.organizationGuid = organizationGuid;
    }

    @JsonProperty("space_guid")
    public String getSpaceGuid() {
        return spaceGuid;
    }

    @JsonProperty("space_guid")
    public void setSpaceGuid(String spaceGuid) {
        this.spaceGuid = spaceGuid;
    }

    @JsonProperty("parameters")
    public Parameters getParameters() {
        return parameters;
    }

    @JsonProperty("parameters")
    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
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
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Service {

    @JsonProperty("name")
    private String name;
    @JsonProperty("id")
    private String id;
    @JsonProperty("description")
    private String description;
    @JsonProperty("tags")
    private List<String> tags = null;
    @JsonProperty("requires")
    private List<String> requires = null;
    @JsonProperty("bindable")
    private Boolean bindable;
    @JsonProperty("metadata")
    private ServiceMetadata metadata;
    @JsonProperty("dashboard_client")
    private DashboardClient dashboardClient;
    @JsonProperty("plan_updateable")
    private Boolean planUpdateable;
    @JsonProperty("plans")
    private List<Plan> plans = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("tags")
    public List<String> getTags() {
        return tags;
    }

    @JsonProperty("tags")
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @JsonProperty("requires")
    public List<String> getRequires() {
        return requires;
    }

    @JsonProperty("requires")
    public void setRequires(List<String> requires) {
        this.requires = requires;
    }

    @JsonProperty("bindable")
    public Boolean getBindable() {
        return bindable;
    }

    @JsonProperty("bindable")
    public void setBindable(Boolean bindable) {
        this.bindable = bindable;
    }

    @JsonProperty("metadata")
    public ServiceMetadata getMetadata() {
        return metadata;
    }

    @JsonProperty("metadata")
    public void setMetadata(ServiceMetadata metadata) {
        this.metadata = metadata;
    }

    @JsonProperty("dashboard_client")
    public DashboardClient getDashboardClient() {
        return dashboardClient;
    }

    @JsonProperty("dashboard_client")
    public void setDashboardClient(DashboardClient dashboardClient) {
        this.dashboardClient = dashboardClient;
    }

    @JsonProperty("plan_updateable")
    public Boolean getPlanUpdateable() {
        return planUpdateable;
    }

    @JsonProperty("plan_updateable")
    public void setPlanUpdateable(Boolean planUpdateable) {
        this.planUpdateable = planUpdateable;
    }

    @JsonProperty("plans")
    public List<Plan> getPlans() {
        return plans;
    }

    @JsonProperty("plans")
    public void setPlans(List<Plan> plans) {
        this.plans = plans;
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

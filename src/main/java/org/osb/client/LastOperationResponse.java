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
public class LastOperationResponse extends Response{
    public enum State {IN_PROGRESS, SUCCEEDED, FAILED, UNKNOWN};
    
    @JsonProperty("state")
    private String state;
    @JsonProperty("description")
    private String description;

    @JsonProperty("state")
    public String getState() {
        return state;
    }
    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }
    
    @JsonIgnore
    public boolean success() {
        return responseCode == 200;
    }
    
    @JsonIgnore
    public State getStateEnum() {
        if (state.equalsIgnoreCase("in progress")) {
            return State.IN_PROGRESS;
        } else if (state.equalsIgnoreCase("succeeded")) {
            return State.SUCCEEDED;
        } else if (state.equalsIgnoreCase("failed")) {
            return State.FAILED;
        }
        return State.UNKNOWN;
    }
}
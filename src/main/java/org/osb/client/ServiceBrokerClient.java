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

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ServiceBrokerClient {
	private static final String VERSION_HEADER = "X-Broker-API-Version";
	private static final String DEFAULT_VERSION = "2.13";
	private static final String ORIGINATING_HEADER = "X-Broker-API-Originating-Identity";
	
	/**
     * This endpoint returns a list of all services available on the broker.
     * Platforms query this endpoint from all brokers in order to present an
     * aggregated user-facing catalog.
     * 
     * @param baseUrl Base URL
     * @param authHeader This will be used with "Authorization" header
     * @return {@link CatalogResponse}
     */
	public static CatalogResponse getCatalog(String baseUrl, String authHeader) {
	    try {
	        String url = baseUrl + "/v2/catalog";
	        CloseableHttpClient client = buildHttpClient();  
	        HttpGet request = new HttpGet(URLEncoder.encode(url, "UTF-8"));

	        request.addHeader(VERSION_HEADER, DEFAULT_VERSION);
            request.addHeader("Authorization", authHeader);
            
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException(response.getStatusLine().getReasonPhrase());
            }
            ObjectMapper objectMapper = new ObjectMapper();
            CatalogResponse catalog = objectMapper.readValue(response.getEntity().getContent(), CatalogResponse.class);
            catalog.setResponseCode(200);
            return catalog;
        } catch (UnsupportedOperationException | IOException | KeyManagementException | NoSuchAlgorithmException
                | KeyStoreException e) {
            throw new RuntimeException(e);
        }
	}
	
	/**
     * When a broker returns status code 202 Accepted for Provision, Update, or
     * Deprovision, the platform will begin polling the
     * /v2/service_instances/:instance_id/last_operation endpoint to obtain the
     * state of the last requested operation
     * 
     * @param baseUrl Base URL
     * @param authHeader This will be used with "Authorization" header
     * @param service_id service's id, see catalog's service definition
     * @param plan_id plan's id,  see catalog's service definition 
     * @param instance_id globally unique instance id
     * @param operation this is string from original response of async call.
     * @return {@link LastOperationResponse}
     */
    public static LastOperationResponse lastOperation(String baseUrl, String authHeader, String service_id,
            String plan_id, String instance_id, String operation) {
        try {
            String url = baseUrl+"/v2/service_instances/"+instance_id+"/last_operation";
            CloseableHttpClient client = buildHttpClient();  
            HttpGet request = new HttpGet(URLEncoder.encode(url, "UTF-8"));
            URIBuilder uri = new URIBuilder(request.getURI()).addParameter("service_id", service_id)
                    .addParameter("plan_id", plan_id);
            if (operation != null && !operation.isEmpty()) {
                uri.setParameter("operation", operation);
            }
            ((HttpRequestBase) request).setURI(uri.build());            
            
            request.addHeader(VERSION_HEADER, DEFAULT_VERSION);
            request.addHeader("Authorization", authHeader);

            HttpResponse response = client.execute(request);
            ObjectMapper objectMapper = new ObjectMapper();
            LastOperationResponse last = objectMapper.readValue(response.getEntity().getContent(),
                    LastOperationResponse.class);
            last.setResponseCode(response.getStatusLine().getStatusCode());
            return last;
        } catch (UnsupportedOperationException | IOException | KeyManagementException | NoSuchAlgorithmException
                | KeyStoreException | URISyntaxException e) {
            throw new RuntimeException(e);
        }        
	}

    private static CloseableHttpClient buildHttpClient()
            throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        // no verification of host for now.
        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (certificate, authType) -> true)
                .build();

        CloseableHttpClient client = HttpClients.custom().setSSLContext(sslContext)
                .setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
        return client;
    }
    
    /**
     * When the broker receives a provision request from the platform, it MUST take
     * whatever action is necessary to create a new resource.
     * 
     * @param baseUrl Base URL
     * @param authHeader This will be used with "Authorization" header
     * @param originatingIdentity Platform Originating identity 
     * @param instance_id globally unique instance id
     * @param accepts_incomplete true is async operations are allowed
     * @param input {@link ProvisionRequest}
     * @return {@link ProvisionResponse}
     */
    public static ProvisionResponse provision(String baseUrl, String authHeader, String originatingIdentity,
            String instance_id, boolean accepts_incomplete, ProvisionRequest input) {
        try {
            String url = baseUrl+"/v2/service_instances/"+instance_id;
            CloseableHttpClient client = buildHttpClient();  
            
            HttpPut request = new HttpPut(URLEncoder.encode(url, "UTF-8"));
            URIBuilder uri = new URIBuilder(request.getURI()).addParameter("accepts_incomplete",
                    Boolean.toString(accepts_incomplete));
            ((HttpRequestBase) request).setURI(uri.build());            
            
            request.addHeader(VERSION_HEADER, DEFAULT_VERSION);
            request.addHeader(ORIGINATING_HEADER, originatingIdentity);
            request.addHeader("Authorization", authHeader);
            request.addHeader("Content-Type", "application/json");

            ObjectMapper objectMapper = new ObjectMapper();            
            HttpEntity entity = new StringEntity(objectMapper.writeValueAsString(input)); 
            request.setEntity(entity);
            
            HttpResponse response = client.execute(request);            
            ProvisionResponse resp = objectMapper.readValue(response.getEntity().getContent(), ProvisionResponse.class);
            resp.setResponseCode(response.getStatusLine().getStatusCode());
            return resp;
        } catch (UnsupportedOperationException | IOException | KeyManagementException | NoSuchAlgorithmException
                | KeyStoreException | URISyntaxException e) {
            throw new RuntimeException(e);
        }        
    }

    /**
     * users can upgrade or downgrade their service instance to other plans. By
     * modifying parameters, users can change configuration options that are
     * specific to a service or plan.
     * 
     * @param baseUrl Base URL
     * @param authHeader This will be used with "Authorization" header
     * @param originatingIdentity Platform Originating identity 
     * @param instance_id globally unique instance id
     * @param accepts_incomplete true for async operations
     * @param input {@link UpdateRequest}
     * @return {@link UpdateResponse}
     */
    public static UpdateResponse update(String baseUrl, String authHeader, String originatingIdentity,
            String instance_id, boolean accepts_incomplete, UpdateRequest input) {
        try {
            String url = baseUrl + "/v2/service_instances/"+instance_id;
            CloseableHttpClient client = buildHttpClient();  
            
            HttpPatch request = new HttpPatch(URLEncoder.encode(url, "UTF-8"));
            URIBuilder uri = new URIBuilder(request.getURI()).addParameter("accepts_incomplete",
                    Boolean.toString(accepts_incomplete));
            ((HttpRequestBase) request).setURI(uri.build());            
            
            request.addHeader(VERSION_HEADER, DEFAULT_VERSION);
            request.addHeader(ORIGINATING_HEADER, originatingIdentity);
            request.addHeader("Authorization", authHeader);
            request.addHeader("Content-Type", "application/json");

            ObjectMapper objectMapper = new ObjectMapper();
            
            HttpEntity entity = new StringEntity(objectMapper.writeValueAsString(input)); 
            request.setEntity(entity);
            HttpResponse response = client.execute(request);
            
            UpdateResponse last = objectMapper.readValue(response.getEntity().getContent(), UpdateResponse.class);
            last.setResponseCode(response.getStatusLine().getStatusCode());
            return last;
        } catch (UnsupportedOperationException | IOException | KeyManagementException | NoSuchAlgorithmException
                | KeyStoreException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * If bindable:true is declared for a service or plan in the Catalog endpoint,
     * the platform MAY request generation of a service binding
     * 
     * @param baseUrl Base URL
     * @param authHeader This will be used with "Authorization" header
     * @param originatingIdentity Platform Originating identity 
     * @param instance_id globally unique instance id
     * @param bind_id binding id, needs to be unique
     * @param input {@link BindRequest}
     * @param clazz - Allowed are {@link CredentialResponse}, {@link VolumeMountResponse} or {@link String}
     * @param <T> This is the type parameter
     * @return T
     */
    public static <T> T bind(String baseUrl, String authHeader, String originatingIdentity, String instance_id,
            String bind_id, BindRequest input, Class<T> clazz) {
        try {
            String url = baseUrl+"/v2/service_instances/"+instance_id+"/service_bindings/"+bind_id;            
            CloseableHttpClient client = buildHttpClient();  
            
            HttpPut request = new HttpPut(URLEncoder.encode(url, "UTF-8"));
            
            request.addHeader(VERSION_HEADER, DEFAULT_VERSION);
            request.addHeader(ORIGINATING_HEADER, originatingIdentity);
            request.addHeader("Authorization", authHeader);
            request.addHeader("Content-Type", "application/json");

            ObjectMapper objectMapper = new ObjectMapper();
            
            HttpEntity entity = new StringEntity(objectMapper.writeValueAsString(input)); 
            request.setEntity(entity);
            HttpResponse response = client.execute(request);
            
            T last = objectMapper.readValue(response.getEntity().getContent(), clazz);
            if (last.getClass().isAssignableFrom(Response.class)) {
                ((Response)last).setResponseCode(response.getStatusLine().getStatusCode());
            }
            return clazz.cast(last);
        } catch (UnsupportedOperationException | IOException | KeyManagementException | NoSuchAlgorithmException
                | KeyStoreException e) {
            throw new RuntimeException(e);
        }        
    }    
    
    /**
     * When a broker receives an unbind request from the marketplace, it MUST delete
     * any resources associated with the binding. In the case where credentials were
     * generated, this might result in requests to the service instance failing to
     * authenticate
     * 
     * @param baseUrl Base URL
     * @param authHeader This will be used with "Authorization" header
     * @param originatingIdentity Platform Originating identity 
     * @param service_id service's id, see catalog's service definition
     * @param plan_id plan's id,  see catalog's service definition 
     * @param instance_id globally unique instance id
     * @param bind_id binding id, needs to be unique
     * @return {@link Response}
     */
    public static Response unbind(String baseUrl, String authHeader, String originatingIdentity, String service_id,
            String plan_id, String instance_id, String bind_id) {
        try {
            String url = baseUrl+"/v2/service_instances/"+instance_id+"/service_bindings/"+bind_id;                    
            CloseableHttpClient client = buildHttpClient();  
            
            HttpDelete request = new HttpDelete(URLEncoder.encode(url, "UTF-8"));
            URIBuilder uri = new URIBuilder(request.getURI()).addParameter("service_id", service_id)
                    .addParameter("plan_id", plan_id);
            ((HttpRequestBase) request).setURI(uri.build());            
            
            request.addHeader(VERSION_HEADER, DEFAULT_VERSION);
            request.addHeader(ORIGINATING_HEADER, originatingIdentity);
            request.addHeader("Authorization", authHeader);

            HttpResponse response = client.execute(request);
                
            ObjectMapper objectMapper = new ObjectMapper();
            Response last = objectMapper.readValue(response.getEntity().getContent(), Response.class);
            last.setResponseCode(response.getStatusLine().getStatusCode());
            return last;
        } catch (UnsupportedOperationException | IOException | KeyManagementException | NoSuchAlgorithmException
                | KeyStoreException | URISyntaxException e) {
            throw new RuntimeException(e);
        }        
    }    
    
    /**
     * When a broker receives a deprovision request from the marketplace, it MUST
     * delete any resources it created during the provision. Usually this means that
     * all resources are immediately reclaimed for future provisions
     * 
     * @param baseUrl Base URL
     * @param authHeader This will be used with "Authorization" header
     * @param originatingIdentity Platform Originating identity 
     * @param service_id service's id, see catalog's service definition
     * @param plan_id plan's id,  see catalog's service definition 
     * @param instance_id globally unique instance id
     * @param accepts_incomplete true, if async operations supported
     * @return {@link UpdateResponse}
     */
    public static UpdateResponse deprovision(String baseUrl, String authHeader, String originatingIdentity,
            String service_id, String plan_id, String instance_id, boolean accepts_incomplete) {
        try {
            String url = baseUrl +"/v2/service_instances/"+instance_id;            
            CloseableHttpClient client = buildHttpClient();  
            
            HttpDelete request = new HttpDelete(URLEncoder.encode(url, "UTF-8"));
            URIBuilder uri = new URIBuilder(request.getURI()).addParameter("accepts_incomplete",
                    Boolean.toString(accepts_incomplete));
            uri.addParameter("service_id", service_id).addParameter("plan_id", plan_id);
            ((HttpRequestBase) request).setURI(uri.build());            
            
            request.addHeader(VERSION_HEADER, DEFAULT_VERSION);
            request.addHeader(ORIGINATING_HEADER, originatingIdentity);
            request.addHeader("Authorization", authHeader);

            ObjectMapper objectMapper = new ObjectMapper();
            HttpResponse response = client.execute(request);
            
            UpdateResponse last = objectMapper.readValue(response.getEntity().getContent(), UpdateResponse.class);
            last.setResponseCode(response.getStatusLine().getStatusCode());
            return last;
        } catch (UnsupportedOperationException | IOException | KeyManagementException | NoSuchAlgorithmException
                | KeyStoreException | URISyntaxException e) {
            throw new RuntimeException(e);
        }        
    }     
}

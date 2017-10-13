package org.osb.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class ResponseTests {
    @Test
    public void readJsonWithObjectMapper() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        CatalogResponse response = objectMapper.readValue(new File("src/test/resources/catalog.json"), CatalogResponse.class);
        assertNotNull(response);
        assertEquals(1, response.getServices().size());
        
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        objectMapper.writeValue(System.out, response);
    }
    
    @Test
    public void getCatalog() throws IOException {
        CatalogResponse response = ServiceBrokerClient.getCatalog(
                "https://asb-1338-ansible-service-broker.172.17.0.1.nip.io/ansible-service-broker/v2/catalog",
                "bearer RQfvXrh1V-bRvqvsYdS0BpkqcjyFupcd7lfCZR7xt9M");
        
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        objectMapper.writeValue(System.out, response);
    }    
}

package org.superbiz.moviefun.blobstore;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.util.Objects;

public class ServiceCredentials {

    private final String vcapServices;

    public ServiceCredentials(String vcapServices) {
        this.vcapServices = vcapServices;
    }

    public String getCredential(String serviceName, String serviceType, String credentialKey) {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode root;

        try {
            root = objectMapper.readTree(vcapServices);
        } catch (IOException e) {
            throw new IllegalStateException("No VCAP_SERVICES found", e);
        }

        JsonNode services = root.path(serviceType);

        for (JsonNode service : services) {
            if (Objects.equals(service.get("name").asText(), serviceName)) {
                return service.get("credentials").get(credentialKey).asText();
            }
        }

        throw new IllegalStateException("No "+ serviceName + " found in VCAP_SERVICES");
    }


}

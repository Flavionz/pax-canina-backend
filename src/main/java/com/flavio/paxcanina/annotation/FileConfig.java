package com.flavio.paxcanina.annotation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileConfig {

    @Value("${file.default.accepted.types}")
    private String[] defaultAcceptedTypes;

    public String[] getDefaultAcceptedTypes() {
        return defaultAcceptedTypes;
    }
}

package com.ffdev.diff;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
        info = @Info(
                title = "Diff API",
                version = "1.0.0",
                description = "APIs to verify side-by-side diffs"
        ),
        externalDocs = @ExternalDocumentation(
                description = "Github Repo",
                url = "https://github.com/felipeguilhermefs/diff"
        )
)
@SpringBootApplication
public class DiffApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiffApplication.class, args);
    }

}

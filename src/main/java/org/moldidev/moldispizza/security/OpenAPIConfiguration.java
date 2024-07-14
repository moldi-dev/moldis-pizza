package org.moldidev.moldispizza.security;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "moldi-dev",
                        email = "moldovanandrei632@gmail.com"
                ),
                description = "OpenAPI specification for Moldi's Pizza",
                title = "OpenAPI specification - moldi-dev",
                version = "4.2"
        )
)
@SecurityScheme(
        name = "bearerAuth",
        description = "Sign in to get your JWT",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenAPIConfiguration {

}

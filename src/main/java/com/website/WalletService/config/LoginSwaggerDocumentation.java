package com.website.WalletService.config;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ApiListingScannerPlugin;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
public class LoginSwaggerDocumentation implements ApiListingScannerPlugin {

    @Override
    public List<ApiDescription> apply(DocumentationContext context) {
        return new ArrayList<>(
                Collections.singletonList(
                        new ApiDescription(null, "/login", "", Collections.singletonList(
                                new OperationBuilder(new CachingOperationNameGenerator())
                                        .tags(Set.of("Log In"))
                                        .summary("Provides a JWT for use with subsequent requests in the \"Authorization\" header of the response")
                                        .method(HttpMethod.POST)
                                        .uniqueId("login")
                                        .parameters(Collections.singletonList(
                                                new ParameterBuilder()
                                                        .name("body")
                                                        .required(true)
                                                        .description("The body of request")
                                                        .parameterType("body")
                                                        .modelRef(new ModelRef("object"))
                                                        .build()
                                                )
                                        )
                                        .notes("A valid body for this request would look like this:\n{\"username\":\"myUsername2\",\"password\":\"myPassword543\"}")
                                        .responseMessages(responseMessages())
                                        .responseModel(new ModelRef(("string")))
                                        .build()
                        ), false)));
    }

    private Set<ResponseMessage> responseMessages() { //<8>
        return Set.of(new ResponseMessageBuilder()
                        .message("Success - find your token in the \"Authorization\" header")
                        .code(200)
                        .build(),
                new ResponseMessageBuilder()
                        .message("Forbidden - login was rejected, please ensure your credentials are valid")
                        .code(403)
                        .build()
        );
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return DocumentationType.SWAGGER_2.equals(delimiter);
    }

}

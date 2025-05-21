
package com.example.customermanagement;

import com.example.customermanagement.config.OpenApiConfig;
import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OpenApiConfigTest {

    @Test
    void testOpenAPIConfigLoads() {
        OpenApiConfig config = new OpenApiConfig();
        OpenAPI openAPI = config.customerOpenAPI();

        assertNotNull(openAPI);
        assertNotNull(openAPI.getInfo());
        assertEquals("Customer Management API", openAPI.getInfo().getTitle());
        assertEquals("1.0.0", openAPI.getInfo().getVersion());
    }
}

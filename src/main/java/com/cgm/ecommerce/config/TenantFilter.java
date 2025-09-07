package com.cgm.ecommerce.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TenantFilter implements Filter {

    private static final String TENANT_HEADER = "X-Tenant-ID";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        try {
            if (request instanceof HttpServletRequest httpReq) {
                String tenantId = httpReq.getHeader(TENANT_HEADER);
                if (tenantId == null || tenantId.isBlank()) {
                    tenantId = "tenant_default";
                }
                TenantContext.setCurrentTenant(tenantId);
            } else {
                TenantContext.setCurrentTenant("tenant_default");
            }

            chain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}

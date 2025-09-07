package com.cgm.ecommerce.config;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

public class TenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {

    @Override
    public String resolveCurrentTenantIdentifier() {
        return TenantContext.getCurrentTenant();
    }

    /**
     * If true, Hibernate will validate whether an existing session's tenant id can continue.
     * true is a safe default.
     */
    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}

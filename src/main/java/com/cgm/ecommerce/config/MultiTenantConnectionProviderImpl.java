package com.cgm.ecommerce.config;

import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;

import javax.sql.DataSource;
import java.util.Map;

/**
 * Selects a DataSource for the current tenant identifier.
 * The map is injected from configuration.
 */
public class MultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

    private final Map<String, DataSource> dataSources;

    public MultiTenantConnectionProviderImpl(Map<String, DataSource> dataSources) {
        this.dataSources = dataSources;
    }

    /**
     * Called by Hibernate when it needs any DataSource (e.g. for schema queries). Return a default.
     */
    @Override
    protected DataSource selectAnyDataSource() {
        return dataSources.getOrDefault("tenant_default", dataSources.values().stream().findFirst().orElse(null));
    }

    /**
     * Called by Hibernate to get DataSource for a specific tenant identifier.
     */
    @Override
    protected DataSource selectDataSource(Object tenantIdentifier) {
        return dataSources.getOrDefault(tenantIdentifier.toString(), selectAnyDataSource());
    }
}

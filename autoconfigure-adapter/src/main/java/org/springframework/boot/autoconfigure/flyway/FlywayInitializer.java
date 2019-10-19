/*
 * Copyright 2002-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.autoconfigure.flyway;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.callback.Callback;
import org.flywaydb.core.api.migration.JavaMigration;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.GenericApplicationContext;

import javax.sql.DataSource;

/**
 * {@link ApplicationContextInitializer} adapter for {@link FlywayAutoConfiguration}.
 */
public class FlywayInitializer implements ApplicationContextInitializer<GenericApplicationContext> {
	private final FlywayProperties flywayProperties;
	private final DataSourceProperties dataSourceProperties;

	public FlywayInitializer(@NotNull FlywayProperties flywayProperties, DataSourceProperties dataSourceProperties) {
		this.flywayProperties = flywayProperties;
		this.dataSourceProperties = dataSourceProperties;
	}

	@Override
	public void initialize(GenericApplicationContext context) {
		FlywayAutoConfiguration.FlywayConfiguration flywayConfiguration = new FlywayAutoConfiguration.FlywayConfiguration();
		context.registerBean(Flyway.class, () -> flywayConfiguration.flyway(
				flywayProperties, dataSourceProperties, context,
				context.getBeanProvider(DataSource.class), context.getBeanProvider(DataSource.class), // TODO any way to use qualifier ?
				context.getBeanProvider(FlywayConfigurationCustomizer.class), context.getBeanProvider(JavaMigration.class),
				context.getBeanProvider(Callback.class)
		));
		context.registerBean(FlywayMigrationInitializer.class, () -> new FlywayMigrationInitializer(
				context.getBean(Flyway.class), context.getBeanProvider(FlywayMigrationStrategy.class).getIfAvailable())
		);
		context.registerBean(FlywaySchemaManagementProvider.class, () -> new FlywaySchemaManagementProvider(
				context.getBeanProvider(Flyway.class)
		));
	}
}

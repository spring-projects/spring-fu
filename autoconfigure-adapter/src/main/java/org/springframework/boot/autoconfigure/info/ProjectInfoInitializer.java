/*
 * Copyright 2002-2018 the original author or authors.
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

package org.springframework.boot.autoconfigure.info;

import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.GenericApplicationContext;

import java.util.function.Supplier;

/**
 * {@link ApplicationContextInitializer} adapter for {@link ProjectInfoAutoConfiguration}.
 */
public class ProjectInfoInitializer implements ApplicationContextInitializer<GenericApplicationContext> {

  private final ProjectInfoProperties properties;

  public ProjectInfoInitializer(ProjectInfoProperties properties) {
    this.properties = properties;
  }

  @Override
  public void initialize(GenericApplicationContext context) {
    Supplier<ProjectInfoAutoConfiguration> projectInfoAutoConfigurationSupplier = new Supplier<ProjectInfoAutoConfiguration>() {

      private ProjectInfoAutoConfiguration configuration;

      @Override
      public ProjectInfoAutoConfiguration get() {
        if (configuration == null) {
          configuration = new ProjectInfoAutoConfiguration(properties);
        }
        return configuration;
      }
    };

    context.registerBean(BuildProperties.class, () -> {
      try {
        return projectInfoAutoConfigurationSupplier.get().buildProperties();
      } catch (Exception e) {
        e.printStackTrace();
      }
      return null;
    });

    context.registerBean(GitProperties.class, () -> {
      try {
        return projectInfoAutoConfigurationSupplier.get().gitProperties();
      } catch (Exception e) {
        e.printStackTrace();
      }
      return null;
    });
  }
}

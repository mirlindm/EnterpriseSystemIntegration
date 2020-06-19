/*
 * Copyright 2019 the original author or authors.
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
package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * @author Greg Turnquist
 */
@Configuration
@EnableHypermediaSupport(type = HypermediaType.HAL_FORMS)
class HypermediaConfiguration {
    @Bean
    public static HalObjectMapperConfigurer halObjectMapperConfigurer() {
        return new HalObjectMapperConfigurer();
    }

    private static class HalObjectMapperConfigurer
            implements BeanPostProcessor, BeanFactoryAware {

        private BeanFactory beanFactory;

        /**
         * Assume any {@link ObjectMapper} starts with {@literal _hal} and ends with {@literal Mapper}.
         */
        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName)
                throws BeansException {
            if (bean instanceof ObjectMapper && beanName.startsWith("_hal") && beanName.endsWith("Mapper")) {
                postProcessHalObjectMapper((ObjectMapper) bean);
            }
            return bean;
        }

        private void postProcessHalObjectMapper(ObjectMapper objectMapper) {
            try {
                Jackson2ObjectMapperBuilder builder = this.beanFactory.getBean(Jackson2ObjectMapperBuilder.class);
                builder.configure(objectMapper);
            } catch (NoSuchBeanDefinitionException ex) {
                // No Jackson configuration required
            }
        }

        @Override
        public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
            this.beanFactory = beanFactory;
        }
    }

}
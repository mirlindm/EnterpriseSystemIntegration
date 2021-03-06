package com.buildit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * @author Greg Turnquist
 */
@Configuration
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL_FORMS)
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
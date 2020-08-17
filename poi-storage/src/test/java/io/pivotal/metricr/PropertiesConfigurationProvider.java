package io.pivotal.metricr;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "cf")
public class PropertiesConfigurationProvider implements ConfigurationProvider, InitializingBean {
    org.slf4j.Logger logger = LoggerFactory.getLogger(PropertiesConfigurationProvider.class);

    List<CloudFoundryProperties> configurations;

    public List<CloudFoundryProperties> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(List<CloudFoundryProperties> configurations) {
        this.configurations = configurations;
    }

    @Override
    public List<CloudFoundryProperties> getConfiguration() {
        return configurations;
    }

    @Override
    public void afterPropertiesSet() {
        logger.info("finished config setting");
    }
}

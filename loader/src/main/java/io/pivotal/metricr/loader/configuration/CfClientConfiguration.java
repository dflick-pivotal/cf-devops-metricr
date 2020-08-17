package io.pivotal.metricr.loader.configuration;

import org.cloudfoundry.reactor.DefaultConnectionContext;
import org.cloudfoundry.reactor.ProxyConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class CfClientConfiguration {

	@Value("${io.pivotal.metricr.connection-pool-size}")
	Integer connectionPoolSize;

	@Value("${spring.cloud.cloudfoundry.url}") 
	String apiHost;
	@Value("${io.pivotal.metricr.proxy-host}")
	String proxyHost;
	@Value("${io.pivotal.metricr.proxy-port}")
	Integer proxyPort;
	@Value("${io.pivotal.metricr.proxy-type}")
	String proxyType;

	@Bean
	@Profile("withsocks")
	DefaultConnectionContext connectionContext() {
		
        // workaround to get the proxy type (SOCKS5, SOCKS4 or HTTP) into the Config
        ProxyConfiguration proxyConfiguration = null;
        if (!proxyHost.isEmpty()) {
            proxyConfiguration = ProxyConfiguration.builder()
                    .host(proxyHost)
                    .port(proxyPort)
//                    .username(null == cfP.getProxyUsername() ?
//                            Optional.empty() :
//                            Optional.of(cfP.getProxyUsername()))
//                    .password(null == cfP.getProxyPassword() ?
//                            Optional.empty() :
//                            Optional.of(cfP.getProxyPassword()))
                    .build();
            if (!proxyType.isEmpty()) {
                proxyConfiguration.setProxyType(proxyType);
            }
        }

		return DefaultConnectionContext.builder().apiHost(apiHost).connectionPoolSize(connectionPoolSize).proxyConfiguration(proxyConfiguration).skipSslValidation(true).build();
	}

}

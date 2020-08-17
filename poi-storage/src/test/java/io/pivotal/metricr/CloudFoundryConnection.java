package io.pivotal.metricr;

import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.doppler.DopplerClient;
import org.cloudfoundry.operations.CloudFoundryOperations;
import org.cloudfoundry.operations.DefaultCloudFoundryOperations;
import org.cloudfoundry.reactor.ConnectionContext;
import org.cloudfoundry.reactor.DefaultConnectionContext;
import org.cloudfoundry.reactor.ProxyConfiguration;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.cloudfoundry.reactor.doppler.ReactorDopplerClient;
import org.cloudfoundry.reactor.routing.ReactorRoutingClient;
import org.cloudfoundry.reactor.tokenprovider.PasswordGrantTokenProvider;
import org.cloudfoundry.reactor.uaa.ReactorUaaClient;
import org.cloudfoundry.routing.RoutingClient;
import org.cloudfoundry.uaa.UaaClient;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class CloudFoundryConnection {
    static org.slf4j.Logger logger = LoggerFactory.getLogger(CloudFoundryConnection.class);
    private final String name;
    private final CloudFoundryOperations ops;
    private final CloudFoundryClient client;
    private final DopplerClient dopplerClient;
    private final ReactorUaaClient uaaClient;
    private final RoutingClient routingClient;

    public CloudFoundryConnection(CloudFoundryProperties cfP) {
        this.name = cfP.getName();

        PasswordGrantTokenProvider tokenProvider = PasswordGrantTokenProvider.builder()
                .password(cfP.getPassword())
                .username(cfP.getUsername())
                .build();

        // workaround to get the proxy type (SOCKS5, SOCKS4 or HTTP) into the Config
        ProxyConfiguration proxyConfiguration = null;
        if (null != cfP.getProxyHost()) {
            proxyConfiguration = ProxyConfiguration.builder()
                    .host(cfP.getProxyHost())
                    .port(cfP.getProxyPort())
                    .username(null == cfP.getProxyUsername() ?
                            Optional.empty() :
                            Optional.of(cfP.getProxyUsername()))
                    .password(null == cfP.getProxyPassword() ?
                            Optional.empty() :
                            Optional.of(cfP.getProxyPassword()))
                    .build();
            if (null != cfP.getProxyType()) {
                proxyConfiguration.setProxyType(cfP.getProxyType());
            }
        }

        DefaultConnectionContext.Builder connectionContextBuilder = DefaultConnectionContext.builder()
                .apiHost(cfP.getUrl())
                .skipSslValidation(cfP.getSkipSslValidation())
                .proxyConfiguration(null == proxyConfiguration ?
                        Optional.empty() :
                        Optional.of(proxyConfiguration));
        if (null != cfP.getConnectionPoolSize()) {
            connectionContextBuilder.connectionPoolSize(cfP.getConnectionPoolSize());
        }
        ConnectionContext connectionContext = connectionContextBuilder.build();

        dopplerClient = ReactorDopplerClient.builder()
                .connectionContext(connectionContext)
                .tokenProvider(tokenProvider)
                .build();

        routingClient = ReactorRoutingClient.builder()
                .connectionContext(connectionContext)
                .tokenProvider(tokenProvider)
                .build();

        uaaClient = ReactorUaaClient.builder()
                .connectionContext(connectionContext)
                .tokenProvider(tokenProvider)
                .build();

        client = ReactorCloudFoundryClient.builder()
                .connectionContext(connectionContext)
                .tokenProvider(tokenProvider)
                .build();

        ops = DefaultCloudFoundryOperations.builder()
                .cloudFoundryClient(client)
                .dopplerClient(dopplerClient)
                .routingClient(routingClient)
                .uaaClient(uaaClient)
                .organization(cfP.getOrg())
                .space(cfP.getSpace())
                .build();

        logger.info("name:" + this.name);
        logger.info("ops:" + ops);
        logger.info("client:" + client);
        logger.info("dopplerClient" + ":" + dopplerClient);
        logger.info("uaaClient: " + uaaClient);
    }

    public String getName() {
        return name;
    }

    public CloudFoundryOperations getOps() {
        return this.ops;
    }

    public CloudFoundryClient getClient() {
        return client;
    }

    public DopplerClient getDopplerClient() {
        return dopplerClient;
    }

    public UaaClient getUaaClient() {
        return uaaClient;
    }

    public RoutingClient getRoutingClient() {
        return routingClient;
    }
}

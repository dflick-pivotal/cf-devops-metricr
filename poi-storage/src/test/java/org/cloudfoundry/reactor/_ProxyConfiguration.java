/*
 * Copyright 2013-2018 the original author or authors.
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

package org.cloudfoundry.reactor;

//import org.immutables.value.Value;
import reactor.ipc.netty.options.ClientOptions;
import reactor.ipc.netty.options.ClientProxyOptions;

import java.util.Optional;
import java.util.function.Function;

/**
 * Proxy configuration
 */
//@Value.Immutable
abstract class _ProxyConfiguration {
    private String proxyType;

    public void configure(ClientOptions.Builder<?> options) {
        options.proxy(typeSpec -> {
            ClientProxyOptions.Builder builder = typeSpec
                    .type(null == proxyType ?
                            ClientProxyOptions.Proxy.HTTP :
                            ClientProxyOptions.Proxy.valueOf(proxyType))
                    .host(getHost());

            getPort().ifPresent(builder::port);
            getUsername().ifPresent(builder::username);
            getPassword().map(password -> (Function<String, String>) s -> password).ifPresent(builder::password);

            return builder;
        });
    }

    /**
     * The proxy host
     */
    abstract String getHost();

    /**
     * The proxy password
     */
    abstract Optional<String> getPassword();

    /**
     * The proxy port
     */
    abstract Optional<Integer> getPort();

    /**
     * The proxy username
     */
    abstract Optional<String> getUsername();

    public String getProxyType() {
        return proxyType;
    }

    public void setProxyType(String proxyType) {
        this.proxyType = proxyType;
    }
}

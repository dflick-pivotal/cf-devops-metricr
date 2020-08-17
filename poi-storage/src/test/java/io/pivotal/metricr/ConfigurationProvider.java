package io.pivotal.metricr;

import java.util.List;

public interface ConfigurationProvider {
    List<CloudFoundryProperties> getConfiguration();
}

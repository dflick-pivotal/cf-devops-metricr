package io.pivotal.metricr.poistorage;

import io.pivotal.metricr.Storage;
import io.pivotal.metricr.StorageProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("poi")
public class PoiStorageProvider implements StorageProvider {
    @Override
    public Storage getStorage() {
        return new PoiStorage();
    }
}

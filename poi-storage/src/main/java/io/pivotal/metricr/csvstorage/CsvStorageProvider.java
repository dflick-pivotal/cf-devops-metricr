package io.pivotal.metricr.csvstorage;

import io.pivotal.metricr.Storage;
import io.pivotal.metricr.StorageProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("csv")
public class CsvStorageProvider implements StorageProvider {
    @Override
    public Storage getStorage() {
        return new CsvStorage();
    }
}

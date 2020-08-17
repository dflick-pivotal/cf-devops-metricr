package io.pivotal.metricr;

import io.pivotal.metricr.loader.service.CfLoaderService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class TasKpiExporter implements CommandLineRunner {
    @Autowired
    CfLoaderService cfLoaderService;

    @Autowired
    ConfigurationProvider configurationProvider;

    @Autowired
    StorageProvider storageProvider;

    org.slf4j.Logger logger = LoggerFactory.getLogger(TasKpiExporter.class);

    public static void main(String[] args) {
        SpringApplication.run(TasKpiExporter.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // load configuration
        // with list of target foundations to read from
        List<CloudFoundryProperties> connectionConfigurations = configurationProvider.getConfiguration();
        logger.info("number of configurations found:" + connectionConfigurations.size());

        Storage storage = storageProvider.getStorage();
        // for each configuration...
        try {
            for (CloudFoundryProperties cfP : connectionConfigurations) {
                // connect to the CF instance
                CloudFoundryConnection cfCon = new CloudFoundryConnection(cfP);
                // log connection details
                CloudFoundryApi.logConnectionDetails(cfCon);

                // load the data from the connected instance
                CloudFoundryInstanceData instanceData = CloudFoundryApi.loadInstanceData(cfLoaderService, cfCon);

                // store results
                storage.storeInstanceData(instanceData);
            }
        } finally {
            storage.close();
        }
        logger.info("all done");
    }
}

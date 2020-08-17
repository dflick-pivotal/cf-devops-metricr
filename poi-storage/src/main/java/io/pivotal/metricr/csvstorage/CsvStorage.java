package io.pivotal.metricr.csvstorage;

import io.pivotal.metricr.CloudFoundryInstanceData;
import io.pivotal.metricr.Storage;
import io.pivotal.metricr.domain.Application;
import io.pivotal.metricr.domain.Event;
import io.pivotal.metricr.domain.Organization;
import io.pivotal.metricr.domain.Space;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CsvStorage implements Storage {
    static org.slf4j.Logger logger = LoggerFactory.getLogger(CsvStorage.class);

    @Override
    public void storeInstanceData(CloudFoundryInstanceData instanceData) {
        logger.info("storing csv data for instance:" + instanceData.getName());
        printOrgsInCSVFormat(instanceData.getOrgs());
        printSpacesInCSVFormat(instanceData.getSpaces());
        printApplicationsInCSVFormat(instanceData.getApplications());
        printEventsInCSVFormat(instanceData.getEvents());
    }

    @Override
    public void writeTo(OutputStream outputStream) {
        // todo
    }

    @Override
    public void close() {
        // nothing to do
    }

    private void printSpacesInCSVFormat(List<Space> spaces) {
        logger.info(String.format("%s,%s", "ID", "NAME"));
        for (int i = 0; i < spaces.size(); i++) {
            Space space =  spaces.get(i);
            logger.info(String.format("%s,%s",
                    space.getGuid(), space.getName()));
        }
    }

    private void printOrgsInCSVFormat(List<Organization> orgs) {
        logger.info(String.format("%s,%s", "ID", "NAME"));
        for (int i = 0; i < orgs.size(); i++) {
            Organization organization =  orgs.get(i);

            logger.info(String.format("%s,%s",
                    organization.getGuid(), organization.getName()));
        }
    }

    private void printApplicationsInCSVFormat(List<Application> applications) {
        System.out.printf("%s,%s,%s,%s,%s,%s,%s,%s\n", "NAME", "SPACE", "BUILDPACK", "DETECTEDBUILDPACK", "MEMORY", "INSTANCES", "STATE", "DISKQUOTA");
        for (Application val : applications) {
            System.out.printf("%s,%s,%s,%s,%s,%s,%s,%s\n",
                    val.getName(), val.getSpace(), val.getBuildpack(), val.getDetectedBuildpack(), val.getMemory(), val.getInstances(), val.getState(), val.getDiskQuota());
        }
    }

    private void printEventsInCSVFormat(List<Event> events) {
        System.out.printf("%s,%s,%s,%s,%s,%s,%s\n", "DATE", "ORG", "SPACE", "ACTEE-TYPE", "ACTEE-NAME", "ACTOR", "EVENT TYPE");
        for (Event val : events) {
            Instant time = val.getTimestamp();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneOffset.UTC);

            System.out.printf("%s,%s,%s,%s,%s,%s,%s\n",
                    formatter.format(time), val.getOrganizationName(), val.getSpaceName(),
                    val.getActeeType(), val.getActeeName(), val.getActorName(), val.getType());
        }
    }
}

package io.pivotal.metricr;

import io.pivotal.metricr.domain.*;
import io.pivotal.metricr.loader.service.CfLoaderService;
import org.cloudfoundry.client.v2.info.GetInfoRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class CloudFoundryApi {
    static Logger logger = LoggerFactory.getLogger(CloudFoundryApi.class);

    public static CloudFoundryInstanceData loadInstanceData(CfLoaderService cfLoaderService, CloudFoundryConnection cfCon) {
        CloudFoundryInstanceData result = new CloudFoundryInstanceData(cfCon.getName());

        ArrayList<Organization> orgs;
        ArrayList<Space> spaces;
        ArrayList<Stack> stacks;
        List<Buildpack> buildpacks;
        List<Application> applications;
        List<Event> events;
        List<ServiceInstance> serviceInstances;
        List<Service> services;

        orgs = cfLoaderService.loadOrganizations();
        spaces = cfLoaderService.loadSpaces();
        stacks = cfLoaderService.loadStacks();
        buildpacks = cfLoaderService.loadBuildpacks();
        serviceInstances = cfLoaderService.loadServiceInstances();
        services = cfLoaderService.loadServices();

        applications = cfLoaderService.loadApplications(stacks, spaces);
        events = cfLoaderService.loadEvents(Instant.parse("2019-01-01T00:00:00Z"), orgs, spaces);

        result.setOrgs(orgs);
        result.setSpaces(spaces);
        result.setBuildpacks(buildpacks);
        result.setApplications(applications);
        result.setEvents(events);

        return result;
    }

    public static void logConnectionDetails(CloudFoundryConnection cfCon) {
        cfCon.getClient().info()
                .get(GetInfoRequest.builder().build())
                .subscribe(
                        response -> logger.info(
                                "connected to:" + cfCon.getName() + ", API version:" + response.getApiVersion())
                );
    }
}

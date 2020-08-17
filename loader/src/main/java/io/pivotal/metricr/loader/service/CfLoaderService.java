package io.pivotal.metricr.loader.service;

import io.pivotal.metricr.domain.*;

import java.time.Instant;
import java.util.ArrayList;

public interface CfLoaderService {
    ArrayList<Stack> loadStacks();

    ArrayList<Buildpack> loadBuildpacks();

    ArrayList<Space> loadSpaces();

    ArrayList<Organization> loadOrganizations();

    ArrayList<Service> loadServices();

    ArrayList<ServiceInstance> loadServiceInstances();

    ArrayList<Application> loadApplications(ArrayList<Stack> stacks, ArrayList<Space> spaces);

    ArrayList<Event> loadEvents(Instant newestEventTimestamp, ArrayList<Organization> organizations, ArrayList<Space> spaces);
}

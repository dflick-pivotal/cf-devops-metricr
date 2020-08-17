package io.pivotal.metricr.loader.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import io.pivotal.metricr.domain.*;
import io.pivotal.metricr.loader.helper.*;
import lombok.extern.log4j.Log4j2;
import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.client.v2.applications.ListApplicationsRequest;
import org.cloudfoundry.client.v2.buildpacks.ListBuildpacksRequest;
import org.cloudfoundry.client.v2.events.EventResource;
import org.cloudfoundry.client.v2.events.ListEventsRequest;
import org.cloudfoundry.client.v2.events.ListEventsResponse;
import org.cloudfoundry.client.v2.organizations.ListOrganizationsRequest;
import org.cloudfoundry.client.v2.serviceinstances.ListServiceInstancesRequest;
import org.cloudfoundry.client.v2.services.ListServicesRequest;
import org.cloudfoundry.client.v2.spaces.ListSpacesRequest;
import org.cloudfoundry.client.v2.stacks.ListStacksRequest;
import org.cloudfoundry.util.PaginationUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

@Component
@Log4j2
@Profile("!offline")
public class CfLoaderServiceImpl implements CfLoaderService {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private CloudFoundryClient client;

//	@Autowired
//	DefaultConnectionContext context;

    public CfLoaderServiceImpl(CloudFoundryClient client) {
        this.client = client;
    }

    public ArrayList<Stack> loadStacks() {

        ArrayList<Stack> list = new ArrayList<Stack>();
        StackHelper helper = new StackHelper();

        log.info("Start loading Stacks now {}", dateFormat.format(new Date()));

        // load stacks
        PaginationUtils
                .requestClientV2Resources(page -> client.stacks().list(ListStacksRequest.builder().page(page).build()))
                .toIterable().forEach(resp -> list.add(helper.toEntity(resp)));

        log.info("loaded {} stacks", list.size());

        return list;
    }

    public ArrayList<Buildpack> loadBuildpacks() {

        ArrayList<Buildpack> list = new ArrayList<Buildpack>();
        BuildpackHelper helper = new BuildpackHelper();

        log.info("Start loading buildpacks now {}", dateFormat.format(new Date()));

        // load buildpacks
        PaginationUtils
                .requestClientV2Resources(
                        page -> client.buildpacks().list(ListBuildpacksRequest.builder().page(page).build()))
                .toIterable().forEach(resp -> list.add(helper.toEntity(resp)));

        log.info("loaded {} buildpacks", list.size());

        return list;
    }

    public ArrayList<Space> loadSpaces() {

        ArrayList<Space> list = new ArrayList<Space>();
        SpaceHelper helper = new SpaceHelper();

        log.info("Start loading space now {}", dateFormat.format(new Date()));

        // load spaces
        PaginationUtils
                .requestClientV2Resources(page -> client.spaces().list(ListSpacesRequest.builder().page(page).build()))
                .toIterable().forEach(resp -> list.add(helper.toEntity(resp)));

        log.info("loaded {} spaces", list.size());

        return list;
    }

    public ArrayList<Organization> loadOrganizations() {

        ArrayList<Organization> list = new ArrayList<Organization>();
        OrganizationHelper helper = new OrganizationHelper();

        log.info("Start loading organizations now {}", dateFormat.format(new Date()));

        // load organizations
        PaginationUtils
                .requestClientV2Resources(
                        page -> client.organizations().list(ListOrganizationsRequest.builder().page(page).build()))
                .toIterable().forEach(resp -> list.add(helper.toEntity(resp)));

        log.info("loaded {} organizations", list.size());

        return list;
    }

    public ArrayList<Service> loadServices() {

        ArrayList<Service> list = new ArrayList<Service>();
        ServiceHelper helper = new ServiceHelper();

        log.info("Start loading services now {}", dateFormat.format(new Date()));

        // load services
        PaginationUtils
                .requestClientV2Resources(
                        page -> client.services().list(ListServicesRequest.builder().page(page).build()))
                .toIterable().forEach(resp -> list.add(helper.toEntity(resp)));

        log.info("loaded {} services", list.size());

        return list;
    }

    public ArrayList<ServiceInstance> loadServiceInstances() {

        ArrayList<ServiceInstance> list = new ArrayList<ServiceInstance>();
        ServiceInstanceHelper helper = new ServiceInstanceHelper();

        log.info("Start loading serviceinstances now {}", dateFormat.format(new Date()));

        // load service instances
        PaginationUtils.requestClientV2Resources(
                page -> client.serviceInstances().list(ListServiceInstancesRequest.builder().page(page).build()))
                .toIterable().forEach(resp -> list.add(helper.toEntity(resp)));

        log.info("loaded {} service instances", list.size());

        return list;
    }

    public ArrayList<Application> loadApplications(ArrayList<Stack> stacks, ArrayList<Space> spaces) {

        ArrayList<Application> list = new ArrayList<Application>();
        ApplicationHelper helper = new ApplicationHelper();

        log.info("Start loading applications now {}", dateFormat.format(new Date()));

        // load applications
        PaginationUtils
                .requestClientV2Resources(
                        page -> client.applicationsV2().list(ListApplicationsRequest.builder().page(page).build()))
                .toIterable().forEach(resp -> list.add(helper.toEntity(resp, stacks, spaces)));

        log.info("loaded {} applications", list.size());

        return list;
    }

    public ArrayList<Event> loadEvents(Instant timestamp, ArrayList<Organization> organizations, ArrayList<Space> spaces) {

        ArrayList<Event> list = new ArrayList<Event>();
        EventHelper helper = new EventHelper();

//        ArrayList<EventResource> listEventResource = new ArrayList<EventResource>();

        log.info("Start loading events now {}", dateFormat.format(new Date()));

        // load events
        PaginationUtils
                .requestClientV2Resources(page -> client.events()
                        .list(ListEventsRequest.builder().timestamp(timestamp.toString())
                                .page(page).build()))
                .toIterable().forEach(resp -> {
            list.add(helper.toEntity(resp, organizations, spaces));
//            listEventResource.add(resp);
        });
        log.info("loaded {} events", list.size());

//        ListEventsResponse response =
//                ListEventsResponse.builder()
//                        .addAllResources(listEventResource)
//                        .totalPages(1)
//                        .totalResults(list.size())
//                        .nextUrl(null)
//                        .previousUrl(null)
//                        .build();
//
//		ObjectMapper objectMapper = new ObjectMapper()
//				.disable(FAIL_ON_UNKNOWN_PROPERTIES)
//				.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
//				.registerModule(new Jdk8Module());
//		try {
//			objectMapper.writeValue(new File("events.json"), response);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		return list;
    }
}

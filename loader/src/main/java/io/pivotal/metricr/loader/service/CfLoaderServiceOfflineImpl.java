package io.pivotal.metricr.loader.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import io.pivotal.metricr.domain.*;
import io.pivotal.metricr.loader.helper.*;
import lombok.extern.log4j.Log4j2;
import org.cloudfoundry.client.v2.applications.ListApplicationsResponse;
import org.cloudfoundry.client.v2.buildpacks.ListBuildpacksResponse;
import org.cloudfoundry.client.v2.events.ListEventsResponse;
import org.cloudfoundry.client.v2.organizations.ListOrganizationsResponse;
import org.cloudfoundry.client.v2.serviceinstances.ListServiceInstancesResponse;
import org.cloudfoundry.client.v2.services.ListServicesResponse;
import org.cloudfoundry.client.v2.spaces.ListSpacesResponse;
import org.cloudfoundry.client.v2.stacks.ListStacksResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

@Component
@Log4j2
@Profile("offline")
public class CfLoaderServiceOfflineImpl implements CfLoaderService {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private ObjectMapper objectMapper;

    public CfLoaderServiceOfflineImpl() {
        this.objectMapper = new ObjectMapper()
                .disable(FAIL_ON_UNKNOWN_PROPERTIES)
                .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
                .registerModule(new Jdk8Module())
                .setSerializationInclusion(NON_NULL);
    }

    public ArrayList<Stack> loadStacks() {
        ArrayList<Stack> list = new ArrayList<Stack>();
        StackHelper helper = new StackHelper();

        log.info("Start loading Stacks now {}", dateFormat.format(new Date()));

		InputStream inputStream = getClass()
				.getClassLoader().getResourceAsStream("data/stacks.json");

		try {
			objectMapper.readValue(inputStream, ListStacksResponse.class)
					.getResources()
					.forEach(resource -> list.add(helper.toEntity(resource)));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

        log.info("loaded {} stacks", list.size());

        return list;
    }

    public ArrayList<Buildpack> loadBuildpacks() {
        ArrayList<Buildpack> list = new ArrayList<Buildpack>();
        BuildpackHelper helper = new BuildpackHelper();

        log.info("Start loading buildpacks now {}", dateFormat.format(new Date()));

		InputStream inputStream = getClass()
				.getClassLoader().getResourceAsStream("data/buildpacks.json");

		try {
			objectMapper.readValue(inputStream, ListBuildpacksResponse.class)
					.getResources()
					.forEach(resource -> list.add(helper.toEntity(resource)));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

        log.info("loaded {} buildpacks", list.size());

        return list;
    }

    public ArrayList<Space> loadSpaces() {

        ArrayList<Space> list = new ArrayList<Space>();
        SpaceHelper helper = new SpaceHelper();

        log.info("Start loading space now {}", dateFormat.format(new Date()));

		InputStream inputStream = getClass()
				.getClassLoader().getResourceAsStream("data/spaces.json");

		try {
			objectMapper.readValue(inputStream, ListSpacesResponse.class)
					.getResources()
					.forEach(resource -> list.add(helper.toEntity(resource)));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

        log.info("loaded {} spaces", list.size());

        return list;
    }

    public ArrayList<Organization> loadOrganizations() {

        ArrayList<Organization> list = new ArrayList<Organization>();
        OrganizationHelper helper = new OrganizationHelper();

        log.info("Start loading organizations now {}", dateFormat.format(new Date()));

		InputStream inputStream = getClass()
				.getClassLoader().getResourceAsStream("data/organizations.json");

		try {
			objectMapper.readValue(inputStream, ListOrganizationsResponse.class)
					.getResources()
					.forEach(resource -> list.add(helper.toEntity(resource)));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

        log.info("loaded {} organizations", list.size());

        return list;
    }

    public ArrayList<Service> loadServices() {

        ArrayList<Service> list = new ArrayList<Service>();
        ServiceHelper helper = new ServiceHelper();

        log.info("Start loading services now {}", dateFormat.format(new Date()));

		InputStream inputStream = getClass()
				.getClassLoader().getResourceAsStream("data/services.json");

		try {
			objectMapper.readValue(inputStream, ListServicesResponse.class)
					.getResources()
					.forEach(resource -> list.add(helper.toEntity(resource)));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

        log.info("loaded {} services", list.size());

        return list;
    }

    public ArrayList<ServiceInstance> loadServiceInstances() {

        ArrayList<ServiceInstance> list = new ArrayList<ServiceInstance>();
        ServiceInstanceHelper helper = new ServiceInstanceHelper();

        log.info("Start loading serviceinstances now {}", dateFormat.format(new Date()));

		InputStream inputStream = getClass()
				.getClassLoader().getResourceAsStream("data/serviceinstances.json");

		try {
			objectMapper.readValue(inputStream, ListServiceInstancesResponse.class)
					.getResources()
					.forEach(resource -> list.add(helper.toEntity(resource)));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

        log.info("loaded {} service instances", list.size());

        return list;
    }

    public ArrayList<Application> loadApplications(ArrayList<Stack> stacks, ArrayList<Space> spaces) {

        ArrayList<Application> list = new ArrayList<Application>();
        ApplicationHelper helper = new ApplicationHelper();

        log.info("Start loading applications now {}", dateFormat.format(new Date()));

		InputStream inputStream = getClass()
				.getClassLoader().getResourceAsStream("data/applications.json");

		try {
			objectMapper.readValue(inputStream, ListApplicationsResponse.class)
					.getResources()
					.forEach(resource -> list.add(helper.toEntity(resource, stacks, spaces)));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

        log.info("loaded {} applications", list.size());

        return list;
    }

    public ArrayList<Event> loadEvents(Instant timestamp, ArrayList<Organization> organizations, ArrayList<Space> spaces) {

        ArrayList<Event> list = new ArrayList<Event>();
        EventHelper helper = new EventHelper();

        log.info("Start loading events now {}", dateFormat.format(new Date()));

		InputStream inputStream = getClass()
				.getClassLoader().getResourceAsStream("data/events.json");

		try {
			objectMapper.readValue(inputStream, ListEventsResponse.class)
					.getResources()
					.forEach(resp -> list.add(helper.toEntity(resp, organizations, spaces)));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
        log.info("loaded {} events", list.size());

        return list;
    }
}

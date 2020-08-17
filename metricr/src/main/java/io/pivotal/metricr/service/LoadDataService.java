package io.pivotal.metricr.service;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.pivotal.metricr.domain.Application;
import io.pivotal.metricr.domain.Buildpack;
import io.pivotal.metricr.domain.Event;
import io.pivotal.metricr.domain.EventTimeStampRange;
import io.pivotal.metricr.domain.Organization;
import io.pivotal.metricr.domain.PerDaySummary;
import io.pivotal.metricr.domain.Service;
import io.pivotal.metricr.domain.ServiceInstance;
import io.pivotal.metricr.domain.Space;
import io.pivotal.metricr.domain.Stack;
import io.pivotal.metricr.loader.service.CfLoaderService;
import io.pivotal.metricr.repository.ApplicationRepository;
import io.pivotal.metricr.repository.BuildpackRepository;
import io.pivotal.metricr.repository.EventRepository;
import io.pivotal.metricr.repository.EventTimeStampRangeRepository;
import io.pivotal.metricr.repository.OrganizationRepository;
import io.pivotal.metricr.repository.PerDaySummaryRepository;
import io.pivotal.metricr.repository.ServiceInstanceRepository;
import io.pivotal.metricr.repository.ServiceRepository;
import io.pivotal.metricr.repository.SpaceRepository;
import io.pivotal.metricr.repository.StackRepository;
import lombok.extern.log4j.Log4j2;

@Component
@EnableScheduling
@Log4j2
public class LoadDataService {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	@Autowired
	public EventTimeStampRange eventTimeStampRange;
	private SpaceRepository spaceRepository;
	private OrganizationRepository organizationRepository;
	private BuildpackRepository buildpackRepository;
	private ServiceRepository serviceRepository;
	private ServiceInstanceRepository serviceInstanceRepository;
	private StackRepository stackRepository;
	private ApplicationRepository applicationRepository;
	private EventRepository eventRepository;
	private EventTimeStampRangeRepository eventTimeStampRangeRepository;

	private PerDaySummaryRepository repositoryDayStatistic;

	@Autowired
	CfLoaderService loader;

	private ArrayList<Organization> organizations = new ArrayList<>();
	private ArrayList<Space> spaces = new ArrayList<>();
	private ArrayList<Buildpack> buildpacks = new ArrayList<>();
	private ArrayList<Service> services = new ArrayList<>();
	private ArrayList<ServiceInstance> serviceInstances = new ArrayList<>();
	private ArrayList<Application> applications = new ArrayList<>();
	private ArrayList<Event> events = new ArrayList<>();
	private ArrayList<Stack> stacks = new ArrayList<>();

	public LoadDataService(EventRepository eventRepository, ApplicationRepository applicationRepository,
			SpaceRepository spaceRepository, OrganizationRepository organizationRepository,
			BuildpackRepository buildpackRepository, ServiceRepository serviceRepository,
			ServiceInstanceRepository serviceInstanceRepository, StackRepository stackRepository,
			PerDaySummaryRepository repositoryDayStatistic,
			EventTimeStampRangeRepository eventTimeStampRangeRepository) {
		this.eventRepository = eventRepository;
		this.applicationRepository = applicationRepository;
		this.spaceRepository = spaceRepository;
		this.organizationRepository = organizationRepository;
		this.buildpackRepository = buildpackRepository;
		this.serviceRepository = serviceRepository;
		this.serviceInstanceRepository = serviceInstanceRepository;
		this.stackRepository = stackRepository;
		this.repositoryDayStatistic = repositoryDayStatistic;
		this.eventTimeStampRangeRepository = eventTimeStampRangeRepository;
	}

	@PostConstruct
	void init() {
		if (this.eventTimeStampRangeRepository.existsById((long) 1) == true) {
			EventTimeStampRange etsr = this.eventTimeStampRangeRepository.findById((long) 1).get();
			eventTimeStampRange.setCurrentEventTimestamp(etsr.getCurrentEventTimestamp()); 
			eventTimeStampRange.setOldestEventTimestamp(etsr.getOldestEventTimestamp()); 
			eventTimeStampRange.setNewestEventTimestamp(etsr.getNewestEventTimestamp()); 
		}
//		else
//		{
//			this.eventTimeStampRange = new EventTimeStampRange();
//		}
	}

	public void deleteAll() {
		this.eventRepository.deleteAll();
		this.applicationRepository.deleteAll();
		this.spaceRepository.deleteAll();
		this.organizationRepository.deleteAll();
		this.buildpackRepository.deleteAll();
		this.serviceRepository.deleteAll();
		this.serviceInstanceRepository.deleteAll();
		this.stackRepository.deleteAll();
		this.repositoryDayStatistic.deleteAll();
		this.eventTimeStampRangeRepository.deleteAll();
	}

	// Run at 22 pm
	@Scheduled(cron = "0 0 22 * * *")
//	@Scheduled(cron = "0 */1 * * * *")
//	@PostConstruct
	public void loadData() {

		organizations.clear();
		spaces.clear();
		buildpacks.clear();
		services.clear();
		serviceInstances.clear();
		applications.clear();
		events.clear();
		stacks.clear();

		log.info("Start loading Data {}", dateFormat.format(new Date()));

		stacks = loader.loadStacks();
		buildpacks = loader.loadBuildpacks();
		spaces = loader.loadSpaces();
		organizations = loader.loadOrganizations();
		services = loader.loadServices();
		serviceInstances = loader.loadServiceInstances();
		applications = loader.loadApplications(stacks, spaces);
		log.info("events = loader.loadEvents: {}", eventTimeStampRange.getNewestEventTimestamp());
		events = loader.loadEvents(eventTimeStampRange.getNewestEventTimestamp(), organizations, spaces);
		this.updateTimestampRange(events);
		eventTimeStampRangeRepository.save(eventTimeStampRange); 

		log.info("Finished loading Data {}", dateFormat.format(new Date()));

//		saveMetricrDayStatisticEntity();

		saveSpaces();
		log.info("Spaces saved: " + spaceRepository.count());
		saveOrganizations();
		log.info("Organizations saved: " + organizationRepository.count());
		saveServices();
		log.info("Services saved: " + serviceRepository.count());
		saveServiceInstances();
		log.info("Service Instances saved: " + serviceInstanceRepository.count());
		saveBuildpacks();
		log.info("Buildpacks saved: " + buildpackRepository.count());
		saveStacks();
		log.info("Stacks saved: " + stackRepository.count());
		
		saveEvents();
		this.eventTimeStampRangeRepository.save(this.eventTimeStampRange);
		log.info("Events saved: " + eventRepository.count());
		
		saveApplications();
		log.info("Applications saved: " + applicationRepository.count());

		log.info("Oldest Event:" + eventTimeStampRange.getOldestEventTimestamp().toString());
		log.info("Latest Event:" + eventTimeStampRange.getNewestEventTimestamp().toString());
		log.info("Current Event:" + eventTimeStampRange.getCurrentEventTimestamp().toString());
	}

	public void saveSpaces() {
		spaceRepository.saveAll(spaces);
	}

	public void saveOrganizations() {
		organizationRepository.saveAll(organizations);
	}

	public void saveServices() {
		serviceRepository.saveAll(services);
	}

	public void saveServiceInstances() {
		serviceInstanceRepository.saveAll(serviceInstances);
	}

	public void saveBuildpacks() {
		buildpackRepository.saveAll(buildpacks);
	}

	public void saveStacks() {
		stackRepository.saveAll(stacks);
	}

	public void saveApplications() {
		applicationRepository.saveAll(applications);
	}

	public void saveEvents() {
		eventRepository.saveAll(events);
	}

	void updateTimestampRange(ArrayList<Event> events) {
		events.stream().forEach((event) -> {
			if (eventTimeStampRange.getOldestEventTimestamp().compareTo(eventTimeStampRange.getInitTimestamp()) == 0) {
				eventTimeStampRange.setOldestEventTimestamp(event.getTimestamp());
				eventTimeStampRange.setCurrentEventTimestamp(eventTimeStampRange.getOldestEventTimestamp());
				eventTimeStampRange.setNewestEventTimestamp(eventTimeStampRange.getOldestEventTimestamp());
			} else {
				Instant tempEventTimestamp = event.getTimestamp();
				if (tempEventTimestamp.compareTo(eventTimeStampRange.getOldestEventTimestamp()) < 0) {
					eventTimeStampRange.setOldestEventTimestamp(tempEventTimestamp);
					eventTimeStampRange.setCurrentEventTimestamp(tempEventTimestamp);
				}
				if (tempEventTimestamp.compareTo(eventTimeStampRange.getNewestEventTimestamp()) > 0) {
					eventTimeStampRange.setNewestEventTimestamp(tempEventTimestamp);
				}
			}
		});
	}

	public PerDaySummary saveMetricrDayStatisticEntity() {
		PerDaySummary day = new PerDaySummary();

//		day.setTimestamp(Instant.now());
		day.setNumberOfApplications(applications.size());

		applications.stream().forEach((application) -> {
			day.setNumberOfApplicationInstances(day.getNumberOfApplications() + application.getInstances());
		});

		day.setNumberOfServices(services.size());

		day.setNumberOfServiceInstances(serviceInstances.size());

		day.setNumberOfOrgs(organizations.size());
		day.setNumberOfSpace(spaces.size());
		day.setNumberOfPushes(100);
		day.setNumberOfBuildpacks(buildpacks.size());

		return this.repositoryDayStatistic.save(day);
	}

}

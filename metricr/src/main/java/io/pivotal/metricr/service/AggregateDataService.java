package io.pivotal.metricr.service;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.client.v2.applications.ApplicationResource;
import org.cloudfoundry.client.v2.buildpacks.BuildpackResource;
import org.cloudfoundry.client.v2.events.EventResource;
import org.cloudfoundry.client.v2.organizations.OrganizationResource;
import org.cloudfoundry.client.v2.serviceinstances.ServiceInstanceResource;
import org.cloudfoundry.client.v2.services.ServiceResource;
import org.cloudfoundry.client.v2.spaces.SpaceResource;
import org.cloudfoundry.client.v2.stacks.StackResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.pivotal.metricr.domain.Event;
import io.pivotal.metricr.domain.EventTimeStampRange;
import io.pivotal.metricr.domain.PerDaySummary;
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
public class AggregateDataService {

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

	private PerDaySummaryRepository perDaySummaryRepository;

	private CloudFoundryClient client;

	private final Map<String, OrganizationResource> organizations = new ConcurrentHashMap<>();
	private final Map<String, SpaceResource> spaces = new ConcurrentHashMap<>();
	private final Map<String, BuildpackResource> buildpacks = new ConcurrentHashMap<>();
	private final Map<String, ServiceResource> services = new ConcurrentHashMap<>();
	private final Map<String, ServiceInstanceResource> serviceInstances = new ConcurrentHashMap<>();
	private final Map<String, ApplicationResource> applications = new ConcurrentHashMap<>();
	private final Map<String, EventResource> events = new ConcurrentHashMap<>();
	private final Map<String, StackResource> stacks = new ConcurrentHashMap<>();

	public AggregateDataService(EventRepository eventRepository, ApplicationRepository applicationRepository,
			SpaceRepository spaceRepository, OrganizationRepository organizationRepository,
			BuildpackRepository buildpackRepository, ServiceRepository serviceRepository,
			ServiceInstanceRepository serviceInstanceRepository, StackRepository stackRepository,
			PerDaySummaryRepository perDaySummaryRepository, CloudFoundryClient client, EventTimeStampRangeRepository eventTimeStampRangeRepository) {
		this.eventRepository = eventRepository;
		this.applicationRepository = applicationRepository;
		this.spaceRepository = spaceRepository;
		this.organizationRepository = organizationRepository;
		this.buildpackRepository = buildpackRepository;
		this.serviceRepository = serviceRepository;
		this.serviceInstanceRepository = serviceInstanceRepository;
		this.stackRepository = stackRepository;
		this.perDaySummaryRepository = perDaySummaryRepository;
		this.client = client;
		this.eventTimeStampRangeRepository = eventTimeStampRangeRepository;
	}

	// Run each day at 11 pm
	@Scheduled(cron = "0 0 23 * * *")
//	@Scheduled(cron = "0 */1 * * * *")
	public void aggregateData() {

		// oldest and newest Event verwenden
//		String day ="2020-03-17";
		String startOfDayTime = "T00:00:00Z";
		String endOfDayTime = "T23:59:59Z";

		Instant tempTimestamp = eventTimeStampRange.getCurrentEventTimestamp();
		log.info(tempTimestamp.toString());
		String day;
		Instant startOfDayTimestamp;
		Instant endOfDayTimestamp;
		List<Event> eventsPerDay;
		while ((eventTimeStampRange.getNewestEventTimestamp().compareTo(tempTimestamp)) > 0) {
			day = tempTimestamp.toString().substring(0, 10);
			startOfDayTimestamp = Instant.parse(day + startOfDayTime);
			endOfDayTimestamp = Instant.parse(day + endOfDayTime);

			eventsPerDay = this.eventRepository.findAllByTypeAndTimestampBetween("audit.app.upload-bits", startOfDayTimestamp,
					endOfDayTimestamp);
            tempTimestamp = tempTimestamp.plusSeconds(86400);
			log.info("startOfDayTimestamp: " + startOfDayTimestamp);
			log.info("endOfDayTimestamp: " + endOfDayTimestamp);
			eventsPerDay.forEach(log::info);
			eventsPerDay.forEach(log::info);	
			
			PerDaySummary perDaySummary = new PerDaySummary();
			perDaySummary.setDay(startOfDayTimestamp);
			perDaySummary.setNumberOfPushes(eventsPerDay.size());
			perDaySummaryRepository.save(perDaySummary);
			eventsPerDay.clear();
		}
		eventTimeStampRange.setCurrentEventTimestamp(tempTimestamp);
		eventTimeStampRangeRepository.save(eventTimeStampRange);
		perDaySummaryRepository.findAll().forEach(log::info);
	}

}

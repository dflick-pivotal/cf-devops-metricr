package io.pivotal.metricr.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

import io.pivotal.metricr.domain.Application;
import io.pivotal.metricr.domain.Buildpack;
import io.pivotal.metricr.domain.Event;
import io.pivotal.metricr.domain.Organization;
import io.pivotal.metricr.domain.Service;
import io.pivotal.metricr.domain.ServiceInstance;
import io.pivotal.metricr.domain.Space;
import io.pivotal.metricr.domain.Stack;
import io.pivotal.metricr.repository.ApplicationRepository;
import io.pivotal.metricr.repository.BuildpackRepository;
import io.pivotal.metricr.repository.EventRepository;
import io.pivotal.metricr.repository.OrganizationRepository;
import io.pivotal.metricr.repository.ServiceInstanceRepository;
import io.pivotal.metricr.repository.ServiceRepository;
import io.pivotal.metricr.repository.SpaceRepository;
import io.pivotal.metricr.repository.StackRepository;

@Controller
@RequestMapping("/api/v1")
public class CsvController {

	private SpaceRepository spaceRepository;
	private OrganizationRepository organizationRepository;
	private BuildpackRepository buildpackRepository;
	private ServiceRepository serviceRepository;
	private ServiceInstanceRepository serviceInstanceRepository;
	private StackRepository stackRepository;
	private ApplicationRepository applicationRepository;
	private EventRepository eventRepository;

	public CsvController(EventRepository eventRepository, ApplicationRepository applicationRepository,
			SpaceRepository spaceRepository, OrganizationRepository organizationRepository,
			BuildpackRepository buildpackRepository, ServiceRepository serviceRepository,
			ServiceInstanceRepository serviceInstanceRepository, StackRepository stackRepository) {
		this.eventRepository = eventRepository;
		this.applicationRepository = applicationRepository;
		this.spaceRepository = spaceRepository;
		this.organizationRepository = organizationRepository;
		this.buildpackRepository = buildpackRepository;
		this.serviceRepository = serviceRepository;
		this.serviceInstanceRepository = serviceInstanceRepository;
		this.stackRepository = stackRepository;
	}

	@GetMapping("/export-events")
	public void exportEventsAsCSV(HttpServletResponse response) throws Exception {

		// set file name and content type
		String filename = "events.csv";

		response.setContentType("text/csv");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

		// create a csv writer
		StatefulBeanToCsv<Event> writer = new StatefulBeanToCsvBuilder<Event>(response.getWriter())
				.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).withSeparator(CSVWriter.DEFAULT_SEPARATOR)
				.withOrderedResults(false).build();

		// write all users to csv file
		writer.write(eventRepository.findAll());

	}
	
	@GetMapping("/export-spaces")
	public void exportSpacesAsCSV(HttpServletResponse response) throws Exception {

		// set file name and content type
		String filename = "spaces.csv";

		response.setContentType("text/csv");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

		// create a csv writer
		StatefulBeanToCsv<Space> writer = new StatefulBeanToCsvBuilder<Space>(response.getWriter())
				.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).withSeparator(CSVWriter.DEFAULT_SEPARATOR)
				.withOrderedResults(false).build();

		// write all users to csv file
		writer.write(spaceRepository.findAll());

	}

	@GetMapping("/export-organizations")
	public void exportOrganizationsAsCSV(HttpServletResponse response) throws Exception {

		// set file name and content type
		String filename = "organizations.csv";

		response.setContentType("text/csv");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

		// create a csv writer
		StatefulBeanToCsv<Organization> writer = new StatefulBeanToCsvBuilder<Organization>(response.getWriter())
				.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).withSeparator(CSVWriter.DEFAULT_SEPARATOR)
				.withOrderedResults(false).build();

		// write all users to csv file
		writer.write(organizationRepository.findAll());

	}

	@GetMapping("/export-buildpacks")
	public void exportBuildpacksAsCSV(HttpServletResponse response) throws Exception {

		// set file name and content type
		String filename = "buildpacks.csv";

		response.setContentType("text/csv");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

		// create a csv writer
		StatefulBeanToCsv<Buildpack> writer = new StatefulBeanToCsvBuilder<Buildpack>(response.getWriter())
				.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).withSeparator(CSVWriter.DEFAULT_SEPARATOR)
				.withOrderedResults(false).build();

		// write all users to csv file
		writer.write(buildpackRepository.findAll());

	}

	@GetMapping("/export-services")
	public void exportServicesAsCSV(HttpServletResponse response) throws Exception {

		// set file name and content type
		String filename = "services.csv";

		response.setContentType("text/csv");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

		// create a csv writer
		StatefulBeanToCsv<Service> writer = new StatefulBeanToCsvBuilder<Service>(response.getWriter())
				.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).withSeparator(CSVWriter.DEFAULT_SEPARATOR)
				.withOrderedResults(false).build();

		// write all users to csv file
		writer.write(serviceRepository.findAll());

	}

	@GetMapping("/export-serviceinstances")
	public void exportServiceInstancesAsCSV(HttpServletResponse response) throws Exception {

		// set file name and content type
		String filename = "serviceinstances.csv";

		response.setContentType("text/csv");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

		// create a csv writer
		StatefulBeanToCsv<ServiceInstance> writer = new StatefulBeanToCsvBuilder<ServiceInstance>(response.getWriter())
				.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).withSeparator(CSVWriter.DEFAULT_SEPARATOR)
				.withOrderedResults(false).build();

		// write all users to csv file
		writer.write(serviceInstanceRepository.findAll());

	}

	@GetMapping("/export-stacks")
	public void exportStacksAsCSV(HttpServletResponse response) throws Exception {

		// set file name and content type
		String filename = "stacks.csv";

		response.setContentType("text/csv");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

		// create a csv writer
		StatefulBeanToCsv<Stack> writer = new StatefulBeanToCsvBuilder<Stack>(response.getWriter())
				.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).withSeparator(CSVWriter.DEFAULT_SEPARATOR)
				.withOrderedResults(false).build();

		// write all users to csv file
		writer.write(stackRepository.findAll());

	}

	@GetMapping("/export-applications")
	public void exportApplicationsAsCSV(HttpServletResponse response) throws Exception {

		// set file name and content type
		String filename = "applications.csv";

		response.setContentType("text/csv");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

		// create a csv writer
		StatefulBeanToCsv<Application> writer = new StatefulBeanToCsvBuilder<Application>(response.getWriter())
				.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).withSeparator(CSVWriter.DEFAULT_SEPARATOR)
				.withOrderedResults(false).build();

		// write all users to csv file
		writer.write(applicationRepository.findAll());

	}

}
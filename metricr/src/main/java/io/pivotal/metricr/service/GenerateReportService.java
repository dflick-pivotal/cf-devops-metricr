package io.pivotal.metricr.service;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.pivotal.metricr.CloudFoundryInstanceData;
import io.pivotal.metricr.Storage;
import io.pivotal.metricr.StorageProvider;
import io.pivotal.metricr.domain.Application;
import io.pivotal.metricr.domain.Buildpack;
import io.pivotal.metricr.domain.Event;
import io.pivotal.metricr.domain.Organization;
import io.pivotal.metricr.domain.Report;
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
import io.pivotal.metricr.repository.ReportRepository;
import io.pivotal.metricr.repository.ServiceInstanceRepository;
import io.pivotal.metricr.repository.ServiceRepository;
import io.pivotal.metricr.repository.SpaceRepository;
import io.pivotal.metricr.repository.StackRepository;

@Component
public class GenerateReportService {

    @Autowired
    StorageProvider storageProvider;

    @Autowired
    CfLoaderService cfLoaderService;

    @Value("${spring.cloud.cloudfoundry.url}")
    String url;

    private SpaceRepository spaceRepository;
    private OrganizationRepository organizationRepository;
    private BuildpackRepository buildpackRepository;
    private ServiceRepository serviceRepository;
    private ServiceInstanceRepository serviceInstanceRepository;
    private StackRepository stackRepository;
    private ApplicationRepository applicationRepository;
    private EventRepository eventRepository;
    private EventTimeStampRangeRepository eventTimeStampRangeRepository;
    private ReportRepository reportRepository;

    private PerDaySummaryRepository repositoryDayStatistic;

    public GenerateReportService(EventRepository eventRepository, ApplicationRepository applicationRepository,
                                 SpaceRepository spaceRepository, OrganizationRepository organizationRepository,
                                 BuildpackRepository buildpackRepository, ServiceRepository serviceRepository,
                                 ServiceInstanceRepository serviceInstanceRepository, StackRepository stackRepository,
                                 PerDaySummaryRepository repositoryDayStatistic,
                                 EventTimeStampRangeRepository eventTimeStampRangeRepository,
                                 ReportRepository reportRepository) {
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
        this.reportRepository = reportRepository;
    }

    public void generate() {
        CloudFoundryInstanceData instanceData = new CloudFoundryInstanceData(url);

        ArrayList<Organization> organizations = cfLoaderService.loadOrganizations();
        ArrayList<Space> spaces = cfLoaderService.loadSpaces();
        ArrayList<Stack> stacks = cfLoaderService.loadStacks();
        ArrayList<Buildpack> buildpacks = cfLoaderService.loadBuildpacks();
        ArrayList<ServiceInstance> serviceInstances = cfLoaderService.loadServiceInstances();
        ArrayList<Service> services = cfLoaderService.loadServices();

        ArrayList<Application> applications = cfLoaderService.loadApplications(stacks, spaces);
        ArrayList<Event> events = cfLoaderService.loadEvents(Instant.parse("2019-01-01T00:00:00Z"), organizations, spaces);

        instanceData.setOrgs(organizations);
        instanceData.setSpaces(spaces);
        instanceData.setStacks(stacks);
        instanceData.setBuildpacks(buildpacks);
        instanceData.setApplications(applications);
        instanceData.setEvents(events);

        Storage storage = storageProvider.getStorage();
        storage.storeInstanceData(instanceData);
        try (OutputStream os = getFileOutputStream()) {
            storage.writeTo(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        storage.close();
    }

    public void generateReport() {
        CloudFoundryInstanceData instanceData = new CloudFoundryInstanceData(url);

        List<Organization> organizations = organizationRepository.findAll();
        List<Space> spaces = spaceRepository.findAll();
        List<Stack> stacks = stackRepository.findAll();
        List<Buildpack> buildpacks = buildpackRepository.findAll();
        List<ServiceInstance> serviceInstances = serviceInstanceRepository.findAll();
        List<Service> services = serviceRepository.findAll();

        List<Application> applications = applicationRepository.findAll();
        List<Event> events = eventRepository.findAll();

        instanceData.setOrgs(organizations);
        instanceData.setSpaces(spaces);
        instanceData.setStacks(stacks);
        instanceData.setBuildpacks(buildpacks);
        instanceData.setApplications(applications);
        instanceData.setEvents(events);

        Storage storage = storageProvider.getStorage();
        storage.storeInstanceData(instanceData);
        FileOutputStream result = null;
        Date date = new Date();
        String fileName = new SimpleDateFormat("yyyyMMddHHmmssSSS'_cf-kpi.xlsx'").format(date);
        FileOutputStream os;
		try {
			os = new FileOutputStream(fileName);
	        storage.writeTo(os);
	        storage.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        Report report = new Report();
        report.setPath(fileName);
        report.setCreatedAt(date.toInstant());
        reportRepository.save(report);
    }

    public void generateFromDb(ByteArrayOutputStream stream) {
        CloudFoundryInstanceData instanceData = new CloudFoundryInstanceData(url);

        List<Organization> organizations = organizationRepository.findAll();
        List<Space> spaces = spaceRepository.findAll();
        List<Stack> stacks = stackRepository.findAll();
        List<Buildpack> buildpacks = buildpackRepository.findAll();
        List<ServiceInstance> serviceInstances = serviceInstanceRepository.findAll();
        List<Service> services = serviceRepository.findAll();

        List<Application> applications = applicationRepository.findAll();
        List<Event> events = eventRepository.findAll();

        instanceData.setOrgs(organizations);
        instanceData.setSpaces(spaces);
        instanceData.setStacks(stacks);
        instanceData.setBuildpacks(buildpacks);
        instanceData.setApplications(applications);
        instanceData.setEvents(events);

        Storage storage = storageProvider.getStorage();
        storage.storeInstanceData(instanceData);
        storage.writeTo(stream);
 //       storage.close();
    }

    private OutputStream getFileOutputStream() throws FileNotFoundException {
        FileOutputStream result = null;
        String fileName = new SimpleDateFormat("yyyyMMddHHmmssSSS'_cf-kpi.xlsx'").format(new Date());
        result = new FileOutputStream(fileName);
        return result;
    }
}

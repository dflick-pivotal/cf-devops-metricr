package io.pivotal.metricr.loader.helper;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

import org.cloudfoundry.client.v2.applications.ApplicationResource;

import io.pivotal.metricr.domain.Application;
import io.pivotal.metricr.domain.Space;
import io.pivotal.metricr.domain.Stack;

public class ApplicationHelper {

	public Application toEntity(ApplicationResource cfApplication, ArrayList<Stack> stacksList,
			ArrayList<Space> spacesList) {
		Application application = new Application();
		
		Map<String, Stack> stacks = stacksList.stream()
			      .collect(Collectors.toMap(Stack::getGuid, stack -> stack));

		Map<String, Space> spaces = spacesList.stream()
			      .collect(Collectors.toMap(Space::getGuid, space -> space));

		application.setDateLoaded(new Date());
		 
		application.setDetectedBuildpack(cfApplication.getEntity().getDetectedBuildpack());
		application.setDetectedBuildpackGuid(cfApplication.getEntity().getDetectedBuildpackId());
//		application.setEnvironment(cfApplication.getEntity().getEnvironmentJsons().toString());

		application.setBuildpack(cfApplication.getEntity().getBuildpack());
		application.setPackageState(cfApplication.getEntity().getPackageState());
		application.setPackageUpdatedAt(cfApplication.getEntity().getPackageUpdatedAt());
		application.setVersion(cfApplication.getEntity().getVersion());
		application.setCommand(cfApplication.getEntity().getCommand());
		application.setConsole(cfApplication.getEntity().getConsole());
		application.setDebug(cfApplication.getEntity().getDebug());
		
		String dsc = cfApplication.getEntity().getDetectedStartCommand();
		application.setDetectedStartCommand(dsc.length() > 1024 ? dsc.substring(0, 1023) : dsc);

		application.setDiego(cfApplication.getEntity().getDiego());
		application.setDiskQuota(cfApplication.getEntity().getDiskQuota());
		application.setDockerImage(cfApplication.getEntity().getDockerImage());
		application.setInstances(cfApplication.getEntity().getInstances());
		application.setMemory(cfApplication.getEntity().getMemory());
		application.setName(cfApplication.getEntity().getName());
		application.setProduction(cfApplication.getEntity().getProduction());

		String spaceGuid = cfApplication.getEntity().getSpaceId();
		application.setSpaceGuid(spaceGuid);
		if (!spaceGuid.isEmpty())
			application.setSpace(spaces.get(spaceGuid).getName());

		String stackGuid = cfApplication.getEntity().getStackId();
		application.setStackGuid(stackGuid);
		if (!stackGuid.isEmpty())
			application.setStack(stacks.get(stackGuid).getName());

		application.setState(cfApplication.getEntity().getState());

		application.setStagingTaskId(cfApplication.getEntity().getStagingTaskId());
		application.setHealthCheckType(cfApplication.getEntity().getHealthCheckType());
		application.setHealthCheckTimeout(cfApplication.getEntity().getHealthCheckTimeout());
		application.setHealthCheckHttpEndpoint(cfApplication.getEntity().getHealthCheckHttpEndpoint());
		application.setStagingFailedReason(cfApplication.getEntity().getStagingFailedReason());
		application.setStagingFailedDescription(cfApplication.getEntity().getStagingFailedDescription());
		application.setDockerCredentials(cfApplication.getEntity().getDockerCredentials().toString());
		application.setEnableSsh(cfApplication.getEntity().getEnableSsh());
		application.setPorts(cfApplication.getEntity().getPorts().toString());
		application.setSpaceUrl(cfApplication.getEntity().getSpaceUrl());
		application.setStackUrl(cfApplication.getEntity().getStackUrl());
		application.setRoutesUrl(cfApplication.getEntity().getRoutesUrl());
		application.setEventsUrl(cfApplication.getEntity().getEventsUrl());
		application.setServiceBindingsUrl(cfApplication.getEntity().getServiceBindingsUrl());
		application.setRouteMappingsUrl(cfApplication.getEntity().getRouteMappingsUrl());

		application.setCreatedAt(Instant.parse(cfApplication.getMetadata().getCreatedAt()));
		application.setGuid(cfApplication.getMetadata().getId());

		String updatedAt = cfApplication.getMetadata().getUpdatedAt();
		if (!(updatedAt==null))
			application.setUpdatedAt(Instant.parse(cfApplication.getMetadata().getUpdatedAt()));
		
		application.setUrl(cfApplication.getMetadata().getUrl());

		return application;
	}

}

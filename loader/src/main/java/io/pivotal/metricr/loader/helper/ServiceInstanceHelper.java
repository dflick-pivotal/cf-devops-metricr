package io.pivotal.metricr.loader.helper;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

import org.cloudfoundry.client.v2.serviceinstances.ServiceInstanceResource;
import org.cloudfoundry.client.v2.spaces.SpaceResource;
import org.cloudfoundry.client.v2.stacks.StackResource;

import io.pivotal.metricr.domain.ServiceInstance;

public class ServiceInstanceHelper {

	public ServiceInstance toEntity(ServiceInstanceResource cfServiceInstance) {
		ServiceInstance serviceInstance = new ServiceInstance();

		serviceInstance.setDateLoaded(new Date());

		serviceInstance.setName(cfServiceInstance.getEntity().getName());
		serviceInstance.setCredentials(cfServiceInstance.getEntity().getCredentials().toString());
		serviceInstance.setServicePlanGuid(cfServiceInstance.getEntity().getServicePlanId());
		serviceInstance.setSpaceGuid(cfServiceInstance.getEntity().getSpaceId());
//		serviceInstance.setGatewayData(cfServiceInstance.getEntity().getGatewayData().toString());
		serviceInstance.setDashboardUrl(cfServiceInstance.getEntity().getDashboardUrl());
		serviceInstance.setType(cfServiceInstance.getEntity().getType());
		serviceInstance.setLastOperation(cfServiceInstance.getEntity().getLastOperation().toString());
		serviceInstance.setTags(cfServiceInstance.getEntity().getTags().toString());
//		serviceInstance.setMaintenanceInfo(cfServiceInstance.getEntity().getMaintenanceInfo());
		serviceInstance.setServiceGuid(cfServiceInstance.getEntity().getServiceId());
		serviceInstance.setSpaceUrl(cfServiceInstance.getEntity().getSpaceUrl());
		serviceInstance.setServicePlanUrl(cfServiceInstance.getEntity().getServicePlanUrl());
		serviceInstance.setServiceBindingsUrl(cfServiceInstance.getEntity().getServiceBindingsUrl());
		serviceInstance.setServiceKeysUrl(cfServiceInstance.getEntity().getServiceKeysUrl());
		serviceInstance.setRoutesUrl(cfServiceInstance.getEntity().getRoutesUrl());
		serviceInstance.setServiceUrl(cfServiceInstance.getEntity().getServiceUrl());
//		serviceInstance.setSharedFromUrl(cfServiceInstance.getEntity().getSharedFromUrl());
//		serviceInstance.setSharedToUrl(cfServiceInstance.getEntity().getSharedToUrl());
//		serviceInstance.setServiceInstanceParametersUrl(cfServiceInstance.getEntity().getServiceInstanceParametersUrl());

		serviceInstance.setCreatedAt(Instant.parse(cfServiceInstance.getMetadata().getCreatedAt()));
		serviceInstance.setGuid(cfServiceInstance.getMetadata().getId());

		String updatedAt = cfServiceInstance.getMetadata().getUpdatedAt();
		if (!(updatedAt==null))
			serviceInstance.setUpdatedAt(Instant.parse(cfServiceInstance.getMetadata().getUpdatedAt()));
		
		serviceInstance.setUrl(cfServiceInstance.getMetadata().getUrl());

		return serviceInstance;
	}

}

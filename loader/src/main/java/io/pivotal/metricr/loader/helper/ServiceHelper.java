package io.pivotal.metricr.loader.helper;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

import org.cloudfoundry.client.v2.services.ServiceResource;
import org.cloudfoundry.client.v2.spaces.SpaceResource;
import org.cloudfoundry.client.v2.stacks.StackResource;

import io.pivotal.metricr.domain.Service;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ServiceHelper {

	public Service toEntity(ServiceResource cfService) {
		Service service = new Service();

//		log.info(cfService);

		service.setDateLoaded(new Date());

		service.setLabel(cfService.getEntity().getLabel());
		service.setProvider(cfService.getEntity().getProvider());
		service.setServiceUrl(cfService.getEntity().getUrl());
		service.setDescription(cfService.getEntity().getDescription());
		service.setLongDescription(cfService.getEntity().getLongDescription());
		service.setVersion(cfService.getEntity().getVersion());
		service.setInfoUrl(cfService.getEntity().getInfoUrl());
		service.setActive(cfService.getEntity().getActive());
		service.setBindable(cfService.getEntity().getBindable());
		service.setUniqueId(cfService.getEntity().getUniqueId());
//		service.setExtra(cfService.getEntity().getExtra());
//		service.setTags(cfService.getEntity().getTags().toString());
		service.setRequires(cfService.getEntity().getRequires().toString());
		service.setDocumentationUrl(cfService.getEntity().getDocumentationUrl());
		service.setServiceBrokerGuid(cfService.getEntity().getServiceBrokerId());
		
//		service.setServiceBrokerName(cfService.getEntity().getServiceBrokerName());
		service.setPlanUpdateable(cfService.getEntity().getPlanUpdateable());
//		service.setBindingsRetrievable(cfService.getEntity().getBindingsRetrievable());
//		service.setInstancesRetrievable(cfService.getEntity().getInstancesRetrievable());
//		service.setAllowContextUpdates(cfService.getEntity().getAllowContextUpdates());
		service.setServicePlansUrl(cfService.getEntity().getServicePlansUrl());
		
		service.setCreatedAt(Instant.parse(cfService.getMetadata().getCreatedAt()));
		service.setGuid(cfService.getMetadata().getId());

		String updatedAt = cfService.getMetadata().getUpdatedAt();
		if (!(updatedAt==null))
			service.setUpdatedAt(Instant.parse(cfService.getMetadata().getUpdatedAt()));
		
		service.setUrl(cfService.getMetadata().getUrl());

		return service;
	}

}

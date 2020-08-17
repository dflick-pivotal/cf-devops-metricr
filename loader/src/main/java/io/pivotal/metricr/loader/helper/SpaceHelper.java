package io.pivotal.metricr.loader.helper;

import java.time.Instant;
import java.util.Date;

import org.cloudfoundry.client.v2.spaces.SpaceResource;

import io.pivotal.metricr.domain.Space;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class SpaceHelper {

	public Space toEntity(SpaceResource cfSpace) {
		Space space = new Space();

//		log.info(cfSpace);
		space.setDateLoaded(new Date());
		space.setName(cfSpace.getEntity().getName());
		space.setOrganizationGuid(cfSpace.getEntity().getOrganizationId());
		space.setSpaceQuotaDefinitionGuid(cfSpace.getEntity().getSpaceQuotaDefinitionId());
		space.setIsolationSegmentGuid(cfSpace.getEntity().getIsolationSegmentId());
		space.setAllowSsh(cfSpace.getEntity().getAllowSsh());
		space.setOrganizationUrl(cfSpace.getEntity().getOrganizationUrl());
		space.setDevelopersUrl(cfSpace.getEntity().getDevelopersUrl());
		space.setManagersUrl(cfSpace.getEntity().getManagersUrl());
		space.setAuditorsUrl(cfSpace.getEntity().getAuditorsUrl());
		space.setAppsUrl(cfSpace.getEntity().getApplicationsUrl());
		space.setRoutesUrl(cfSpace.getEntity().getRoutesUrl());
		space.setDomainsUrl(cfSpace.getEntity().getDomainsUrl());
		space.setServiceInstancesUrl(cfSpace.getEntity().getServiceInstancesUrl());
		space.setAppEventsUrl(cfSpace.getEntity().getApplicationEventsUrl());
		space.setEventsUrl(cfSpace.getEntity().getEventsUrl());
		space.setSecurityGroupsUrl(cfSpace.getEntity().getSecurityGroupsUrl());
		space.setStagingSecurityGroupsUrl(cfSpace.getEntity().getStagingSecurityGroupsUrl());

		space.setCreatedAt(Instant.parse(cfSpace.getMetadata().getCreatedAt()));
		space.setGuid(cfSpace.getMetadata().getId());

		String updatedAt = cfSpace.getMetadata().getUpdatedAt();
		if (!(updatedAt==null))
			space.setUpdatedAt(Instant.parse(cfSpace.getMetadata().getUpdatedAt()));
		
		space.setUrl(cfSpace.getMetadata().getUrl());

		return space;
	}

}

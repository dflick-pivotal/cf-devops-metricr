package io.pivotal.metricr.loader.helper;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

import org.cloudfoundry.client.v2.organizations.OrganizationResource;
import org.cloudfoundry.client.v2.spaces.SpaceResource;
import org.cloudfoundry.client.v2.stacks.StackResource;

import io.pivotal.metricr.domain.Organization;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class OrganizationHelper {

	public Organization toEntity(OrganizationResource cfOrganization) {
		Organization organization = new Organization();

//		log.info(cfOrganization);

		organization.setDateLoaded(new Date());

		organization.setName(cfOrganization.getEntity().getName());
		organization.setBillingEnabled(cfOrganization.getEntity().getBillingEnabled());
		organization.setQuotaDefinitionGuid(cfOrganization.getEntity().getQuotaDefinitionId());
		organization.setStatus(cfOrganization.getEntity().getStatus());
		organization.setDefaultIsolationSegmentGuid(cfOrganization.getEntity().getDefaultIsolationSegmentId());
		organization.setQuotaDefinitionUrl(cfOrganization.getEntity().getQuotaDefinitionUrl());
		organization.setSpacesUrl(cfOrganization.getEntity().getSpacesUrl());
		organization.setDomainsUrl(cfOrganization.getEntity().getDomainsUrl());
		organization.setPrivateDomainsUrl(cfOrganization.getEntity().getPrivateDomainsUrl());
		organization.setUsersUrl(cfOrganization.getEntity().getUsersUrl());
		organization.setManagersUrl(cfOrganization.getEntity().getManagersUrl());
		organization.setBillingManagersUrl(cfOrganization.getEntity().getBillingManagersUrl());
		organization.setAuditorsUrl(cfOrganization.getEntity().getAuditorsUrl());
		organization.setAppEventsUrl(cfOrganization.getEntity().getApplicationEventsUrl());
		organization.setSpaceQuotaDefinitionsUrl(cfOrganization.getEntity().getSpaceQuotaDefinitionsUrl());
				
		organization.setCreatedAt(Instant.parse(cfOrganization.getMetadata().getCreatedAt()));
		organization.setGuid(cfOrganization.getMetadata().getId());

		String updatedAt = cfOrganization.getMetadata().getUpdatedAt();
		if (!(updatedAt==null))
			organization.setUpdatedAt(Instant.parse(cfOrganization.getMetadata().getUpdatedAt()));
		
		organization.setUrl(cfOrganization.getMetadata().getUrl());

		return organization;
	}

}

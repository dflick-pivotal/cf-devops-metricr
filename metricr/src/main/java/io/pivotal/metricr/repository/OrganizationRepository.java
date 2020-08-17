package io.pivotal.metricr.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.pivotal.metricr.domain.CompositeKey;
import io.pivotal.metricr.domain.Organization;

@RepositoryRestResource(collectionResourceRel = "organizations", path = "organizations")
public interface OrganizationRepository extends PagingAndSortingRepository<Organization, CompositeKey> {
	List<Organization> findAll();
	List<Organization> findByDateLoaded(Date dateLoaded);
	List<Organization> findByGuid(String guid);
}
package io.pivotal.metricr.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.pivotal.metricr.domain.CompositeKey;
import io.pivotal.metricr.domain.Service;

@RepositoryRestResource(collectionResourceRel = "services", path = "services")
public interface ServiceRepository extends PagingAndSortingRepository<Service, CompositeKey> {
	List<Service> findAll();
	List<Service> findByDateLoaded(Date dateLoaded);
	List<Service> findByGuid(String guid);
}
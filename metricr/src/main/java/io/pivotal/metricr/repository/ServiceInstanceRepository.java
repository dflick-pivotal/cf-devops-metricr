package io.pivotal.metricr.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.pivotal.metricr.domain.CompositeKey;
import io.pivotal.metricr.domain.ServiceInstance;

@RepositoryRestResource(collectionResourceRel = "service_instances", path = "service_instances")
public interface ServiceInstanceRepository extends PagingAndSortingRepository<ServiceInstance, CompositeKey> {
	List<ServiceInstance> findAll();
	List<ServiceInstance> findByDateLoaded(Date dateLoaded);
	List<ServiceInstance> findByGuid(String guid);
}
package io.pivotal.metricr.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.pivotal.metricr.domain.Application;
import io.pivotal.metricr.domain.CompositeKey;

@RepositoryRestResource(collectionResourceRel = "applications", path = "applications")
public interface ApplicationRepository extends PagingAndSortingRepository<Application, CompositeKey> {
	List<Application> findAll();
	List<Application> findByDateLoaded(Date dateLoaded);
	List<Application> findByGuid(String guid);
}
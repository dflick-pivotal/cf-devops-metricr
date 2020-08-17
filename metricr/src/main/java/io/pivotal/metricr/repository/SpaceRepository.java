package io.pivotal.metricr.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.pivotal.metricr.domain.CompositeKey;
import io.pivotal.metricr.domain.Space;

@RepositoryRestResource(collectionResourceRel = "spaces", path = "spaces")
public interface SpaceRepository extends PagingAndSortingRepository<Space, CompositeKey> {
	List<Space> findAll();
	List<Space> findByDateLoaded(Date dateLoaded);
	List<Space> findByGuid(String guid);
} 
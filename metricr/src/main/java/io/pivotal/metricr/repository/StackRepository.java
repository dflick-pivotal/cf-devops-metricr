package io.pivotal.metricr.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.pivotal.metricr.domain.CompositeKey;
import io.pivotal.metricr.domain.Stack;

@RepositoryRestResource(collectionResourceRel = "stacks", path = "stacks")
public interface StackRepository extends PagingAndSortingRepository<Stack, CompositeKey> {
	List<Stack> findAll();
	List<Stack> findByDateLoaded(Date dateLoaded);
	List<Stack> findByGuid(String guid);
}
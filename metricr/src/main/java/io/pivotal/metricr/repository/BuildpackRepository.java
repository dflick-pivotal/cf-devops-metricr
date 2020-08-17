package io.pivotal.metricr.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.pivotal.metricr.domain.Buildpack;
import io.pivotal.metricr.domain.CompositeKey;

@RepositoryRestResource(collectionResourceRel = "buildpacks", path = "buildpacks")
public interface BuildpackRepository extends PagingAndSortingRepository<Buildpack, CompositeKey> {
	List<Buildpack> findAll();
	List<Buildpack> findByDateLoaded(Date dateLoaded);
	List<Buildpack> findByGuid(String guid);
}
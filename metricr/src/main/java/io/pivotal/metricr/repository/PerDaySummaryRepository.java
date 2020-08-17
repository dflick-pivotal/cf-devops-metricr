package io.pivotal.metricr.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.pivotal.metricr.domain.PerDaySummary;

@RepositoryRestResource(collectionResourceRel = "perday", path = "perday")
public interface PerDaySummaryRepository extends PagingAndSortingRepository<PerDaySummary, Long> {
	List<PerDaySummary> findAll();
}
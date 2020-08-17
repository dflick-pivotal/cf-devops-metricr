package io.pivotal.metricr.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.pivotal.metricr.domain.Report;

@RepositoryRestResource(collectionResourceRel = "reports", path = "reports")
public interface ReportRepository extends PagingAndSortingRepository<Report, Long> {
	List<Report> findAll();
}
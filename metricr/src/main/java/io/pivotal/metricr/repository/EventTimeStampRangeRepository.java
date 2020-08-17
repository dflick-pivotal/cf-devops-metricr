package io.pivotal.metricr.repository;

import org.springframework.data.repository.CrudRepository;

import io.pivotal.metricr.domain.EventTimeStampRange;

public interface EventTimeStampRangeRepository extends CrudRepository<EventTimeStampRange, Long> {
}
package io.pivotal.metricr.domain;

import java.time.Instant;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
public class PerDaySummary {

	public PerDaySummary() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Basic
	private Instant day;

	private Integer numberOfApplications;
	private Integer numberOfApplicationInstances;
	private Integer numberOfServices;
	private Integer numberOfServiceInstances;
	private Integer numberOfOrgs;
	private Integer numberOfSpace;
	private Integer numberOfPushes;
	private Integer numberOfBuildpacks;
}

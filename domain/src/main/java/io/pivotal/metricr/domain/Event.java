package io.pivotal.metricr.domain;

import java.time.Instant;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
public class Event {
	
	public Event() {
	}
	  
	@Id
	private String guid;

	private String actee;
	private String acteeName;
	private String acteeType;
	private String actor;
	private String actorName;
	private String actorType;
	private String actorUsername;
	private String organizationGuid;
	private String spaceGuid;
	@Column(length = 1024)
	private String metadatas;

	@Basic
	private Instant timestamp;
	private String type;

	// Metadata
	@Basic
	private Instant createdAt;
	@Basic
	private Instant updatedAt;
	private String url;

	// extended information
	private String organizationName;
	private String spaceName;
}

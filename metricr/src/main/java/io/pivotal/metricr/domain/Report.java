package io.pivotal.metricr.domain;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Report {
	
    @Id
    @GeneratedValue
    private long id;
    
	private String path = "";
	private Instant createdAt = Instant.parse("2019-01-01T00:00:00Z");
}

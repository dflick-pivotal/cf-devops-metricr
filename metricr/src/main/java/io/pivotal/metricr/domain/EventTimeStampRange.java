package io.pivotal.metricr.domain;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class EventTimeStampRange {
	
    @Id
    private long id =1l;
    
    private final String initTimestampString = "2019-01-01T00:00:00Z";
    private final Instant initTimestamp= Instant.parse(initTimestampString);

	private Instant oldestEventTimestamp = Instant.parse(initTimestampString);
	private Instant newestEventTimestamp = Instant.parse(initTimestampString);
	private Instant currentEventTimestamp = Instant.parse(initTimestampString);

}

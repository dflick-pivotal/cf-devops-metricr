package io.pivotal.metricr.repository;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.pivotal.metricr.domain.Event;

//public interface EventResourceRepository extends CrudRepository<EventResource, Long> {
//
//	  Iterable<EventResource> findByActorName(String actorName);
//	  Iterable<EventResource> findByOrganizationGuidAndActorName(String organizationGuid, String actorName);	  
//	}

@RepositoryRestResource(collectionResourceRel = "events", path = "events")
public interface EventRepository extends PagingAndSortingRepository<Event, Long> {
	List<Event> findAll();

	List<Event> findByActorName(String actorName);

	List<Event> findByOrganizationGuidAndActorName(String organizationGuid, String actorName);

	List<Event> findByType(String type);

	List<Event> findByCreatedAt(Instant createdAt);

	List<Event> findByTimestamp(Instant timestamp);

	List<Event> findAllByTypeAndTimestampBetween(String type, Instant timestampStart, Instant timestampEnd);

	List<Event> findAllByTimestampBetween(Date timeStart, Date timeEnd);

//	+-------------------+---------------+------+-----+---------+-------+
//	| Field             | Type          | Null | Key | Default | Extra |
//	+-------------------+---------------+------+-----+---------+-------+
//	| guid              | varchar(255)  | NO   | PRI | NULL    |       |
//	| actee             | varchar(255)  | YES  |     | NULL    |       |
//	| actee_name        | varchar(255)  | YES  |     | NULL    |       |
//	| actee_type        | varchar(255)  | YES  |     | NULL    |       |
//	| actor             | varchar(255)  | YES  |     | NULL    |       |
//	| actor_name        | varchar(255)  | YES  |     | NULL    |       |
//	| actor_type        | varchar(255)  | YES  |     | NULL    |       |
//	| actor_username    | varchar(255)  | YES  |     | NULL    |       |
//	| created_at        | datetime      | YES  |     | NULL    |       |
//	| metadatas         | varchar(1024) | YES  |     | NULL    |       |
//	| organization_guid | varchar(255)  | YES  |     | NULL    |       |
//	| organization_name | varchar(255)  | YES  |     | NULL    |       |
//	| space_guid        | varchar(255)  | YES  |     | NULL    |       |
//	| space_name        | varchar(255)  | YES  |     | NULL    |       |
//	| timestamp         | datetime      | YES  |     | NULL    |       |
//	| type              | varchar(255)  | YES  |     | NULL    |       |
//	| updated_at        | datetime      | YES  |     | NULL    |       |
//	| url               | varchar(255)  | YES  |     | NULL    |       |
//	+-------------------+---------------+------+-----+---------+-------+

	// Queries:
	// - user activity  (top user) 
	// - user activity timeline
	// - org activity (top orgs)
	// - org activity timeline
	// - org creation rate (audit.organization.create)
	// - space creation rate (audit.space.create)
	// - user creation rate () ==> what is the event?
	// - cf push rate (audit.app.upload-bits)
	// pushes per day: SELECT DATE(created_at) as DATE, count(*) FROM event GROUP BY DATE(created_at) ORDER BY DATE(created_at);
	// pushes per day and org (SELECT DATE(created_at) as DATE, organization_name, actor_username, count(*) FROM event WHERE type='audit.app.upload-bits' GROUP BY DATE(created_at),organization_name,actor_username ORDER BY DATE(created_at);)
	// - cf push rate per org
	// - app crash rate (SELECT actee_name,count(*) from event WHERE actor_type='process' GROUP BY actee_name ORDER BY count(*) DESC;)
	// - route creation rate (audit.route.create)
	// - service instance creation rate (audit.service.create)
	// - service instance binding rate (audit.service_binding.create)
	// - correlation int with org
	// - scale count
	// SELECT actor_type, count(*) from event GROUP BY actor_type ORDER BY count(*) DESC;	
	
// SELECT actor_type, count(*) from event GROUP BY actor_type ORDER BY count(*) DESC;	
// SELECT actor_username, count(*) from event GROUP BY actor_username ORDER BY count(*) DESC;	
// SELECT actor_username, count(*) from event WHERE actor_type=user GROUP BY actor_username ORDER BY count(*) DESC;	
//	@Query ("SELECT actorUsername from event GROUP BY actorUsername ORDER BY count(*) DESC")
//	List<ResponseStringAndInt> findTop20EventGeneratingUsers();
	
	/*
	 * @Query("select a from Article a where a.creationDateTime <= :creationDateTime"
	 * ) List<Article> findAllWithCreationDateTimeBefore(
	 * 
	 * @Param("creationDateTime") Date creationDateTime);
	 */
}
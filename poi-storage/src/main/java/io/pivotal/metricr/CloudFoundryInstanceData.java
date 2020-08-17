package io.pivotal.metricr;

import io.pivotal.metricr.domain.*;

import java.time.Instant;
import java.util.List;

public class CloudFoundryInstanceData {
    private final String name;
    private final Instant creationDate;
    List<Organization> orgs;
    List<Space> spaces;
    List<Buildpack> buildpacks;
    List<Application> applications;
    List<Event> events;
    private List<Stack> stacks;

    public CloudFoundryInstanceData(String name) {
        this.name = name;
        this.creationDate = Instant.now();
    }

    public String getName() { return this.name; }

    public Instant getCreationDate() { return creationDate; }

    public List<Organization> getOrgs() {
        return orgs;
    }

    public void setOrgs(List<Organization> orgs) {
        this.orgs = orgs;
    }

    public List<Space> getSpaces() {
        return spaces;
    }

    public void setSpaces(List<Space> spaces) {
        this.spaces = spaces;
    }

    public List<Buildpack> getBuildpacks() {
        return buildpacks;
    }

    public void setBuildpacks(List<Buildpack> buildpacks) {
        this.buildpacks = buildpacks;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void setStacks(List<Stack> stacks) {
        this.stacks = stacks;
    }
}

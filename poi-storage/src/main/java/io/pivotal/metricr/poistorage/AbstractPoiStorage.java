package io.pivotal.metricr.poistorage;

import io.pivotal.metricr.CloudFoundryInstanceData;
import io.pivotal.metricr.Storage;
import io.pivotal.metricr.domain.*;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public abstract class AbstractPoiStorage implements Storage {
    protected static final String[] headerApplicationsTable = { "INSTANCE_NAME", "DATE", "NAME", "SPACE", "BUILDPACK",
            "MEMORY", "INSTANCES", "STATE", "DISKQUOTA" };
    protected static final String[] headerEventsTable = { "INSTANCE_NAME", "DATE", "ID", "ORG", "SPACE", "EVENTTYPE",
            "ACTOR", "ACTEE-TYPE", "ACTEE-NAME", "TIMESTAMP", "METADATA" };
    protected static final String[] headerIdNameTable = { "INSTANCE_NAME", "DATE", "ID", "NAME" };

    protected Object[][] createCellsForBuildpacksTable(String name, Instant creationDate, List<Buildpack> buildpacks) {
        Object[][] cells = new Object[buildpacks.size()][headerIdNameTable.length];
        int index = 0;
        for (Buildpack item : buildpacks) {
            cells[index][0] = name;
            cells[index][1] = item.getDateLoaded();
            cells[index][2] = item.getGuid();
            cells[index][3] = item.getName();
            index++;
        }
        return cells;
    }

    protected Object[][] createCellsForOrgsTable(String name, Instant creationDate, List<Organization> orgs) {
        Object[][] cells = new Object[orgs.size()][headerIdNameTable.length];
        int index = 0;
        for (Organization item : orgs) {
            cells[index][0] = name;
            cells[index][1] = item.getDateLoaded();
            cells[index][2] = item.getGuid();
            cells[index][3] = item.getName();
            index++;
        }
        return cells;
    }

    protected Object[][] createCellsForSpacesTable(String name, Instant creationDate, List<Space> spaces) {
        Object[][] cells = new Object[spaces.size()][headerIdNameTable.length];
        int index = 0;
        for (Space item : spaces) {
            cells[index][0] = name;
            cells[index][1] = item.getDateLoaded();
            cells[index][2] = item.getGuid();
            cells[index][3] = item.getName();
            index++;
        }
        return cells;
    }

    @Override
    public void storeInstanceData(CloudFoundryInstanceData instanceData) {
        PoiStorage.logger.info("storing Excel data for instance:" + instanceData.getName());

        // store data inside workbook
        storeOrgs(instanceData.getName(), instanceData.getCreationDate(), instanceData.getOrgs());
        storeSpaces(instanceData.getName(), instanceData.getCreationDate(), instanceData.getSpaces());
        storeBuildpacks(instanceData.getName(), instanceData.getCreationDate(), instanceData.getBuildpacks());
        storeApplications(instanceData.getName(), instanceData.getCreationDate(), instanceData.getApplications());
        if (instanceData.getEvents() != null && instanceData.getEvents().size() > 0)
            storeEvents(instanceData.getName(), instanceData.getCreationDate(), instanceData.getEvents());
    }

    protected abstract void storeEvents(String name, Instant creationDate, List<Event> events);

    protected abstract void storeApplications(String name, Instant creationDate, List<Application> applications);

    protected abstract void storeBuildpacks(String name, Instant creationDate, List<Buildpack> buildpacks);

    protected abstract void storeSpaces(String name, Instant creationDate, List<Space> spaces);

    protected abstract void storeOrgs(String name, Instant creationDate, List<Organization> orgs);

    protected void saveWorkbook(Workbook workbook) {
        FileOutputStream fileOut = null;
        try {
            String fileName = new SimpleDateFormat("yyyyMMddHHmmssSSS'_cf-kpi.xlsx'").format(new Date());
            fileOut = new FileOutputStream(fileName);

            workbook.write(fileOut);
            fileOut.close();
        } catch (FileNotFoundException e) {
            PoiStorage.logger.error("could not save workbook", e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            PoiStorage.logger.error("could not save workbook", e);
            throw new RuntimeException(e);
        }
    }
}

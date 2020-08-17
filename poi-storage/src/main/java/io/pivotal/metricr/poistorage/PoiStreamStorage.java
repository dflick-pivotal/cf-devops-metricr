package io.pivotal.metricr.poistorage;

import io.pivotal.metricr.Storage;
import io.pivotal.metricr.domain.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.time.Instant;
import java.util.List;

public class PoiStreamStorage extends AbstractPoiStorage implements Storage {
    static org.slf4j.Logger logger = LoggerFactory.getLogger(PoiStreamStorage.class);

    private final SXSSFWorkbook workbook;
    private final WorkbookUtils workbookUtils;

    public PoiStreamStorage() {
        this.workbook = new SXSSFWorkbook(1000); // keep 1000 rows in memory, exceeding rows will be flushed to disk
        this.workbookUtils = new WorkbookUtils(workbook);
    }

    @Override
    public void writeTo(OutputStream outputStream) {
        try {
            workbook.write(outputStream);
        } catch (IOException e) {
            logger.error("could not write workbook", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        // save Excel file
        saveWorkbook(workbook);

        // dispose of temporary files backing this workbook on disk
        workbook.dispose();
    }

    @Override
    protected void storeEvents(String name, Instant creationDate, List<Event> events) {
        // create cell data structure for spaces to be stored in respective table
        Object[][] cells = new Object[events.size()][headerEventsTable.length];
        int index = 0;
        for (Event event : events) {
            int i = 0;
            cells[index][i++] = name;
            cells[index][i++] = event.getCreatedAt();
            cells[index][i++] = event.getGuid();
            cells[index][i++] = event.getOrganizationName();
            cells[index][i++] = event.getSpaceName();
            cells[index][i++] = event.getType();
            cells[index][i++] = event.getActor();
            cells[index][i++] = event.getActeeType();
            cells[index][i++] = event.getActeeName();
            cells[index][i++] = event.getTimestamp();
            cells[index][i++] = event.getMetadatas();
            index++;
        }
        workbookUtils.createSheetWithTableData("table_events", headerEventsTable, cells);
    }

    @Override
    protected void storeApplications(String name, Instant creationDate, List<Application> applications) {
        // create cell data structure for spaces to be stored in respective table
        Object[][] cells = new Object[applications.size()][headerApplicationsTable.length];
        int index = 0;
        for (Application application : applications) {
            cells[index][0] = name;
            cells[index][1] = application.getDateLoaded();
            cells[index][2] = application.getName();
            cells[index][3] = application.getSpace();
            cells[index][4] = application.getBuildpack();
            cells[index][5] = application.getMemory();
            cells[index][6] = application.getInstances();
            cells[index][7] = application.getState();
            cells[index][8] = application.getDiskQuota();
            index++;
        }
        workbookUtils.createSheetWithTableData("table_applications", headerApplicationsTable, cells);
    }

    @Override
    protected void storeBuildpacks(String name, Instant creationDate, List<Buildpack> buildpacks) {
        // create cell data structure for spaces to be stored in respective table
        Object[][] cells = createCellsForBuildpacksTable(name, creationDate, buildpacks);
        workbookUtils.createSheetWithTableData("table_buildpacks", headerIdNameTable, cells);
    }

    @Override
    protected void storeSpaces(String name, Instant creationDate, List<Space> spaces) {
        // create cell data structure for spaces to be stored in respective table
        Object[][] cells = createCellsForSpacesTable(name, creationDate, spaces);
        workbookUtils.createSheetWithTableData("table_spaces", headerIdNameTable, cells);
    }

    protected void storeOrgs(String name, Instant creationDate, List<Organization> orgs) {
        // create cell data structure for spaces to be stored in respective table
        Object[][] cells = createCellsForOrgsTable(name, creationDate, orgs);
        workbookUtils.createSheetWithTableData("table_orgs", headerIdNameTable, cells);
    }
}

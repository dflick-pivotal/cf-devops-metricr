package io.pivotal.metricr.poistorage;

import io.pivotal.metricr.CloudFoundryInstanceData;
import io.pivotal.metricr.Storage;
import io.pivotal.metricr.domain.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public class PoiStorage extends AbstractPoiStorage {
	static org.slf4j.Logger logger = LoggerFactory.getLogger(PoiStorage.class);

	protected static final String TEMPLATE_NAME = "/poi/cf-kpi-template.xlsx";

	private final XSSFWorkbook workbook;
	private final WorkbookUtils workbookUtils;

	public PoiStorage() {
		// open template workbook
		this.workbook = openWorkbookTemplate();
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
		try {
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void storeEvents(String name, Instant creationDate, List<Event> events) {
		// create cell data structure for spaces to be stored in respective table
		Object[][] cells = new Object[events.size()][headerEventsTable.length];
		int index = 0;
		for (Event event : events) {
			int i = 0;
			cells[index][i++] = name;
			cells[index][i++] = creationDate;
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
		workbookUtils.storeValuesInTable("table_events", headerEventsTable, cells);
	}

	protected void storeApplications(String name, Instant creationDate, List<Application> applications) {
		// create cell data structure for spaces to be stored in respective table
		Object[][] cells = new Object[applications.size()][headerApplicationsTable.length];
		int index = 0;
		for (Application application : applications) {
			cells[index][0] = name;
			cells[index][1] = creationDate;
			cells[index][2] = application.getName();
			cells[index][3] = application.getSpace();
			cells[index][4] = application.getBuildpack();
			cells[index][5] = application.getMemory();
			cells[index][6] = application.getInstances();
			cells[index][7] = application.getState();
			cells[index][8] = application.getDiskQuota();
			index++;
		}
		workbookUtils.storeValuesInTable("table_applications", headerApplicationsTable, cells);
	}

	protected void storeBuildpacks(String name, Instant creationDate, List<Buildpack> buildpacks) {
		// create cell data structure for spaces to be stored in respective table
		Object[][] cells = createCellsForBuildpacksTable(name, creationDate, buildpacks);
		workbookUtils.storeValuesInTable("table_buildpacks", headerIdNameTable, cells);
	}

	protected void storeOrgs(String name, Instant creationDate, List<Organization> orgs) {
		// create cell data structure for spaces to be stored in respective table
		Object[][] cells = createCellsForOrgsTable(name, creationDate, orgs);
		workbookUtils.storeValuesInTable("table_orgs", headerIdNameTable, cells);
	}

	protected void storeSpaces(String name, Instant creationDate, List<Space> spaces) {
		// create cell data structure for spaces to be stored in respective table
		Object[][] cells = createCellsForSpacesTable(name, creationDate, spaces);
		workbookUtils.storeValuesInTable("table_spaces", headerIdNameTable, cells);
	}

	private XSSFWorkbook openWorkbookTemplate() {
		// Create a InputStream object
		// for getting the information of the file
		InputStream is = getClass().getResourceAsStream(TEMPLATE_NAME);

		// Getting the workbook instance for XLSX file
		XSSFWorkbook workbook = null;
		try {
			workbook = new XSSFWorkbook(is);
		} catch (IOException e) {
			logger.error("could not open workbook template", e);
			throw new RuntimeException(e);
		}

		return workbook;
	}
}

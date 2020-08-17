package io.pivotal.metricr.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.pivotal.metricr.service.AggregateDataService;
import io.pivotal.metricr.service.GenerateReportService;
import io.pivotal.metricr.service.LoadDataService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping(value = "/api/v1")
class ApiController {

	@Autowired
	private AggregateDataService aggregateDataService;

	@Autowired
	private LoadDataService loadDataService;

	@Autowired
	private GenerateReportService generateReportService;

	@GetMapping("/load")
	String loadData() {
		LoadDataThread t = new LoadDataThread();
		t.start();
		return "loading";
	}

	@GetMapping("/aggregate")
	String agregateData() {
		aggregateDataService.aggregateData();
		return "aggregated";
	}

	@GetMapping("/generateFromCf")
	String generateReportFromCf() {
		generateReportService.generate();
		return "Report generated";
	}

	@GetMapping("/delete")
	String deleteAll() {
		loadDataService.deleteAll();
		return "deleted";
	}

	@GetMapping("/generateReport")
	public String generateReport() {
		GenerateReportThread t = new GenerateReportThread();
		t.start();
		return "generating";
	}

	@GetMapping("/excel")
	public ResponseEntity<ByteArrayResource> downloadTemplate() throws Exception {
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			generateReportService.generateFromDb(stream);
			HttpHeaders header = new HttpHeaders();
			header.setContentType(new MediaType("application", "force-download"));
			String fileName = new SimpleDateFormat("yyyyMMddHHmmssSSS'_cf-kpi.xlsx'").format(new Date());
			header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
			return new ResponseEntity<>(new ByteArrayResource(stream.toByteArray()), header, HttpStatus.CREATED);
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/zip")
	public void downloadZip(HttpServletResponse response) throws Exception {
		response.setContentType("application/octet-stream");
		String fileName = new SimpleDateFormat("yyyyMMddHHmmssSSS'_cf-kpi.zip'").format(new Date());
		response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
		response.setStatus(HttpServletResponse.SC_OK);

		ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());

		ClassPathResource resource = new ClassPathResource("template.xlsx");
		zipOut.putNextEntry(new ZipEntry(resource.getFilename()));	
		StreamUtils.copy(resource.getInputStream(), zipOut);
		zipOut.closeEntry();

		zipOut.putNextEntry(new ZipEntry("data.xlsx"));
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		generateReportService.generateFromDb(byteOut);
		byteOut.writeTo(zipOut);
		zipOut.closeEntry();

		zipOut.close();
	}

	public class LoadDataThread extends Thread {
		public void run() {
			loadDataService.loadData();
		}
	}

	public class GenerateReportThread extends Thread {
		public void run() {
			generateReportService.generateReport();
		}
	}
}
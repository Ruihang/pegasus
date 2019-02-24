package net.brilliance.controller.project;

import java.net.URI;
import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import net.brilliance.common.CommonConstants;
import net.brilliance.domain.entity.general.Project;
import net.brilliance.framework.logging.LogService;
import net.brilliance.manager.catalog.ProjectServiceManager;


@RequestMapping(CommonConstants.REST_API + "project")
@RestController
public class ProjectAPIController {
	@Inject 
	private LogService log;

	@Inject
	private ProjectServiceManager serviceManager;

	@RequestMapping(value = "/fetchByLicense/{license}", method = RequestMethod.GET)
	public @ResponseBody Project fetchByLicense(HttpServletRequest request, @PathVariable("license") String license) {
		Project fetchedObject = null;
		try {
			Optional<Project> fetchedProject = this.serviceManager.getByCode(license);
			if (!fetchedProject.isPresent()) {
				fetchedObject = fetchDummyProject();
			}else{
				fetchedObject = fetchedProject.get();
			}
			System.out.println("Found project: [" + fetchedObject + "]");
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return fetchedObject;
	}

	@RequestMapping(value = "/get/{name}", method = RequestMethod.GET)
	public @ResponseBody Project fetchByName(HttpServletRequest request, @PathVariable("name") String name) {
		Project fetchedObject = null;
		try {
			Optional<Project> fetchedProject = this.serviceManager.getByName(name);
			if (!fetchedProject.isPresent()) {
				fetchedObject = fetchDummyProject();
			}else{
				fetchedObject = fetchedProject.get();
			}
			System.out.println("Found project: [" + fetchedObject + "]");
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return fetchedObject;
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<?> add(HttpServletRequest request, @RequestBody Project project) {
		ResponseEntity<?> responseEntity = null;
		try {
			this.serviceManager.save(project);
			URI projectUri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(project.getId()).toUri();

			responseEntity = ResponseEntity.created(projectUri).build();
		} catch (Exception e) {
			log.error(e.getMessage());
			responseEntity = ResponseEntity.noContent().build();
		}
		return responseEntity;
	}

	private Project fetchDummyProject(){
		return Project.builder()
				.code("PROJ-XSWZAQ")
				.name("Dự án thử nghiệm nên chưa có trong hệ thống")
				.build();
	}
}

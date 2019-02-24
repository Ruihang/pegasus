package net.brilliance.controller.client;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;
import net.brilliance.common.CommonConstants;
import net.brilliance.domain.entity.contact.ContactProc;
import net.brilliance.manager.contact.ContactProfileManager;

@Slf4j
@RequestMapping(CommonConstants.REST_API + "contact")
@RestController
public class ContactAPIController { 

	@Autowired
	private ContactProfileManager serviceManager;

	@RequestMapping(value = "/get/{name}", method = RequestMethod.GET)
	public @ResponseBody ContactProc get(HttpServletRequest request, @PathVariable("name") String name) {
		ContactProc fetchedObject = null;
    	try {
			fetchedObject = this.serviceManager.getByName(name);
			if (null==fetchedObject){
				fetchedObject = new ContactProc();
				fetchedObject.setFullName("Trần Trừng Trị");
				//fetchedObject.setLogin("CTA-01");
			}
			System.out.println("Found contact: [" + fetchedObject + "]");
    	} catch (Exception e) {
			log.error(e.getMessage());
		}
		return fetchedObject;
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<?> add(HttpServletRequest request, @RequestBody ContactProc contact){
		ResponseEntity<?> responseEntity = null;
		try {
    		this.serviceManager.save(contact);
    		URI projectUri = ServletUriComponentsBuilder
					.fromCurrentRequest().path("/contact/{id}")
					.buildAndExpand(contact.getId()).toUri();

    		responseEntity = ResponseEntity.created(projectUri).build();
		} catch (Exception e) {
			log.error(e.getMessage());
			responseEntity = ResponseEntity.noContent().build();
		}
		return responseEntity;
	}
}

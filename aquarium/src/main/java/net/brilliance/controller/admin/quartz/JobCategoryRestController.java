package net.brilliance.controller.admin.quartz;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.brilliance.controller.base.BaseRestController;
import net.brilliance.controller.controller.constants.ControllerConstants;
import net.brilliance.domain.entity.schedule.JobCategory;
import net.brilliance.framework.model.SearchParameter;
import net.brilliance.service.api.admin.quartz.JobCategoryService;

@RestController
@RequestMapping("/" + ControllerConstants.REQUEST_URI_JOB_CATEGORY)
public class JobCategoryRestController extends BaseRestController<JobCategory>{
	@Inject 
	private JobCategoryService businessService;

	@RequestMapping(path = "/list", method = RequestMethod.GET)
	public List<JobCategory> onListCatalogueSubtypes(HttpServletRequest request, HttpServletResponse response, Model model) {
		log.info("Rest::Come to job category data listing ...>>>>>>");
		PageRequest pageRequest = PageRequest.of(0, 500, Sort.Direction.ASC, "id");
		SearchParameter searchParameter = SearchParameter.getInstance()
				.setPageable(pageRequest);
		Page<JobCategory> objects = businessService.getObjects(searchParameter);
		log.info("Rest::Leave from job category data loading. >>>>>>");
		return objects.getContent();
	}
}

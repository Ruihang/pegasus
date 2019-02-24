package net.brilliance.controller.admin.org;

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
import net.brilliance.domain.entity.admin.EnterpriseUnit;
import net.brilliance.framework.model.SearchParameter;
import net.brilliance.service.api.admin.EnterpriseUnitService;

@RestController
@RequestMapping(ControllerConstants.URI_ENTERPRISE_UNIT)
public class EnterpriseUnitRestController extends BaseRestController<EnterpriseUnit> {
	@Inject
	private EnterpriseUnitService businessService;

	@RequestMapping(path = "/list", method = RequestMethod.GET)
	public List<EnterpriseUnit> list(HttpServletRequest request, HttpServletResponse response, Model model) {
		PageRequest pageRequest = PageRequest.of(0, 500, Sort.Direction.ASC, "id");
		SearchParameter searchParameter = SearchParameter.getInstance().setPageable(pageRequest);
		Page<EnterpriseUnit> objects = businessService.getObjects(searchParameter);
		System.out.println("COME !");
		return objects.getContent();
	}
}

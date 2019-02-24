package net.brilliance.controller.dashboard;

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

import lombok.extern.slf4j.Slf4j;
import net.brilliance.controller.base.BaseRestController;
import net.brilliance.controller.controller.constants.ControllerConstants;
import net.brilliance.domain.entity.system.DigitalDashlet;
import net.brilliance.framework.model.SearchParameter;
import net.brilliance.service.api.dashboard.DashletService;

@Slf4j
@RestController
@RequestMapping("/" + ControllerConstants.REQUEST_ADMIN_DASHLET)
public class DashletRestController extends BaseRestController <DigitalDashlet>{
	@Inject 
	private DashletService businessManager;

	@RequestMapping(path = "/list", method = RequestMethod.GET)
	public List<DigitalDashlet> listBizObjects(HttpServletRequest request, HttpServletResponse response, Model model) {
		log.info("DashletRestController::listBizObjects enter...>>>>>>");
		PageRequest pageRequest = PageRequest.of(0, 500, Sort.Direction.ASC, "id");
		SearchParameter searchParameter = SearchParameter.getInstance()
				.setPageable(pageRequest);
		Page<DigitalDashlet> objects = businessManager.getObjects(searchParameter);
		log.info("DashletRestController::listBizObjects leave...>>>>>>");
		return objects.getContent();
	}
}

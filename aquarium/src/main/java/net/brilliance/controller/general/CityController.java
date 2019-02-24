package net.brilliance.controller.general;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;
import net.brilliance.common.CommonConstants;
import net.brilliance.common.CommonUtility;
import net.brilliance.common.ListUtility;
import net.brilliance.controller.base.BaseController;
import net.brilliance.controller.controller.constants.ControllerConstants;
import net.brilliance.domain.entity.crx.contact.Contact;
import net.brilliance.domain.entity.general.City;
import net.brilliance.framework.model.SearchParameter;
import net.brilliance.model.SelectItem;
import net.brilliance.model.ui.UISelectItem;
import net.brilliance.runnable.UpdateSystemSequenceThread;
import net.brilliance.service.api.contact.ContactService;
import net.brilliance.service.api.general.CityService;

@Slf4j
@Controller
@RequestMapping(ControllerConstants.URI_CITY)
public class CityController extends BaseController { 
	private String PAGE_CONTEXT_PREFIX = ControllerConstants.CONTEXT_WEB_PAGES + "general/city";

	@Inject
  private TaskExecutor taskExecutor;
	
	@Inject
	private ApplicationContext applicationContext;

	@Inject
	private CityService businessService;

	@Inject
	private ContactService contactService;

	@RequestMapping(path={"/", ""}, method=RequestMethod.GET)
	public String viewDefaultPage(){
		return getDefaultPage();
	}

	@Override
	protected String performListing(Model model, HttpServletRequest request) {
		return PAGE_CONTEXT_PREFIX + ControllerConstants.BROWSE;
	}

	/**
	 * Export catalogs.
   */
	@RequestMapping(value = "/export", method = RequestMethod.GET)
	public String exports(Model model, HttpServletRequest request) {
		log.info("Exporting enterprise .....");
		return PAGE_CONTEXT_PREFIX + ControllerConstants.BROWSE;
	}

	/**
	 * Create a new department and place in Model attribute.
	 */
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String createForm(Model model) {
		City newCity = City
		.builder()
		.build();
		model.addAttribute(net.brilliance.common.CommonConstants.FETCHED_OBJECT, newCity);
		return PAGE_CONTEXT_PREFIX + ControllerConstants.EDIT;
	}

	/**
	 * Create/update a contact.
	*/
	@RequestMapping(value="/create", method = RequestMethod.POST)
	public String create(@Valid City uiBizObject, BindingResult bindingResult,
			Model model, HttpServletRequest httpServletRequest,
			RedirectAttributes redirectAttributes, Locale locale) {
		
		if (bindingResult.hasErrors()) {
			model.addAttribute(net.brilliance.common.CommonConstants.FETCHED_OBJECT, uiBizObject);
			return PAGE_CONTEXT_PREFIX + ControllerConstants.EDIT;
		}

		log.info("Creating/updating catalogue subtype");
		
		model.asMap().clear();
		//redirectAttributes.addFlashAttribute("message", new Message("success", messageSource.getMessage("general_save_success", new Object[] {}, locale)));

		businessService.saveOrUpdate(uiBizObject);
		//systemSequenceManager.registerSequence(uiBizObject.getCode());

		UpdateSystemSequenceThread updateSystemSequenceThread = applicationContext.getBean(UpdateSystemSequenceThread.class, uiBizObject.getName());
		taskExecutor.execute(updateSystemSequenceThread);
		//TODO: Pay attention please
		String ret = "redirect:/catalogSubtype/"+uiBizObject.getId().toString();
		return ret;//"redirect:/department/" + UrlUtil.encodeUrlPathSegment(department.getId().toString(), httpServletRequest);
	}

	/**
	 * Retrieve the book with the specified id for the update form.
	 */
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model model) {
			model.addAttribute(net.brilliance.common.CommonConstants.FETCHED_OBJECT, businessService.getObject(id));
			return PAGE_CONTEXT_PREFIX + ControllerConstants.EDIT;
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, Model model) {
		log.info("Fetch business object of catalogue subtype with id: " + id);

		model.addAttribute(ControllerConstants.FETCHED_OBJECT, businessService.getObject(id));
		
		return PAGE_CONTEXT_PREFIX + ControllerConstants.VIEW;
	}

	@Override
	protected List<SelectItem> suggestItems(String keyword) {
		List<SelectItem> suggestedItems = new ArrayList<>();
		Page<City> fetchedObjects = this.businessService.searchObjects(keyword, null);
		for (City bizObject :fetchedObjects.getContent()) {
			suggestedItems.add(SelectItem.builder().build().instance(bizObject.getId(), bizObject.getName(), bizObject.getName()));
		}
		return suggestedItems;
	}

	@Override
	protected String performSearch(SearchParameter params) {
		Map<String, Object> parameters = new HashMap<>();
		Page<City> pageContentData = businessService.search(parameters);
		params.getModel().addAttribute(ControllerConstants.FETCHED_OBJECT, pageContentData);
		/*HttpSession session = super.getSession();
		session.setAttribute(CommonConstants.CACHED_PAGE_MODEL, params.getModel());*/
		Gson gson = new Gson();
		//return gson.toJson(pageContentData.getContent());
		return PAGE_CONTEXT_PREFIX + "Browse :: result-teable " + gson.toJson(pageContentData.getContent());
	}

	private String getDefaultPage(){
		return PAGE_CONTEXT_PREFIX + ControllerConstants.BROWSE;
	}

	@RequestMapping(value = "/suggestCoordinator", method = RequestMethod.GET)
	public @ResponseBody List<UISelectItem> suggestObject(HttpServletRequest request, @RequestParam("keyword") String keyword) {
		log.info("Enter keyword: " + keyword);
		Page<Contact> contacts = contactService.searchObjects(keyword, null);
		System.out.println(contacts.getContent());
		List<UISelectItem> uiSelectItems = ListUtility.createSelectItems(contacts.getContent(), 
				ListUtility.createMap(
  				CommonConstants.PROPERTY_KEY, "id", 
  				CommonConstants.PROPERTY_CODE, "code", 
  				CommonConstants.PROPERTY_NAME, "name", 
  				CommonConstants.PROPERTY_NAME_LOCAL, "nameLocal")
				);
		if (CommonUtility.isNull(uiSelectItems)){
			uiSelectItems = ListUtility.createArrayList();
		}
		return uiSelectItems;
	}
}

package net.brilliance.controller.cspx;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;
import net.brilliance.common.CommonConstants;
import net.brilliance.controller.base.BaseController;
import net.brilliance.controller.controller.constants.ControllerConstants;
import net.brilliance.domain.entity.general.RefBook;
import net.brilliance.domain.entity.stock.Store;
import net.brilliance.framework.model.SearchParameter;
import net.brilliance.helper.GlobalDataServicesRepository;
import net.brilliance.manager.catalog.RefBookManager;
import net.brilliance.manager.catalog.BookServiceManager;
import net.brilliance.manager.contact.ClientProfileManager;
import net.brilliance.manager.stock.StoreManager;
import net.brilliance.model.base.IDataContainer;
import net.brilliance.officesuite.spreadsheet.StreamingReader;
import net.brilliance.service.BookService;
import net.brilliance.service.helper.GlobalDataServiceHelper;
import net.brilliance.util.ImageUtil;
import net.brilliance.util.Message;
import net.brilliance.util.UrlUtil;

@Slf4j
@Controller
@RequestMapping(ControllerConstants.URI_CSPX_BOOK)
public class CspxBookController extends BaseController{ 
	private static final String PAGE_CONTEXT = ControllerConstants.CONTEXT_WEB_PAGES + "cspx/book";
	@Inject
	private RefBookManager bookService;

	@Inject
	private BookServiceManager serviceManager;

	@Inject
	private BookService businessService;

	@Inject
	private StoreManager storeManager;

	@Inject
	private GlobalDataServiceHelper globalDataServiceHelper;

	@Autowired
	private MessageSource messageSource;

	@Inject
	private ClientProfileManager clientProfileManager;

	@RequestMapping(path={"/", ""}, method=RequestMethod.GET)
	public String viewDefaultPage(){
		return PAGE_CONTEXT + ControllerConstants.BROWSE;
	}

	/**
	 * List all books.
   */
	@RequestMapping(value = {"/list"}, method = RequestMethod.GET)
	public String list(Model model) {
		log.info("Testing store manager......");
		if (storeManager.count() <= 1){
			storeManager.setupMasterData();
		}

		//loadXlsxData();

		Page<Store> results = storeManager.searchStore("VINA", null);
		System.out.println(results);
		log.info("Finished testing store manager");
		
		log.info("Listing books");

		IDataContainer<String> stringDataContainer = null;
		try {
			stringDataContainer = GlobalDataServicesRepository.builder().build().readCsvFile("/config/liquibase/data/books.csv", true, "~");
			System.out.println(stringDataContainer);
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		//System.out.println("Client profile service bean: " + clientProfileService.getAll());

		List<RefBook> books = bookService.findAll();
		if (books.isEmpty()){
			bookService.restoreDefaultBooks();
			books = bookService.findAll();
		}
		model.addAttribute(ControllerConstants.FETCHED_OBJECTS, books);

		log.info("No. of books: " + books.size());
		
		clientProfileManager.setupDefaultMasterData();
		return "redirect:/book/listBook/1";
		//return PAGE_CONTEXT + "bookBrowse";
	}

	@RequestMapping(value = "/listBook/{pageNumber}", method = RequestMethod.GET)
	public String list(@PathVariable Integer pageNumber, Model model) {
		System.out.println("Listing books at: " + Calendar.getInstance().getTime());
		
		Page<RefBook> page = serviceManager.getList(pageNumber);
		int current = page.getNumber() + 1; 
		int begin = Math.max(1, current - CommonConstants.DEFAULT_PAGE_SIZE); 
		int end = Math.min(begin + CommonConstants.DEFAULT_PAGE_SIZE, page.getTotalPages());
		model.addAttribute(ControllerConstants.FETCHED_OBJECTS, page); 
		model.addAttribute("beginIndex", begin); 
		model.addAttribute("endIndex", end); 
		model.addAttribute("currentIndex", current);
		System.out.println("The list of clients is prepared. "); 
		System.out.println("Total books: " + serviceManager.count());
		return PAGE_CONTEXT + "bookBrowse";

	}

	/**
	 * Retrieve the book with the specified id.
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, Model model) {
		log.info("Listing book with id: " + id);

		RefBook book = bookService.findById(id);
		model.addAttribute(ControllerConstants.FETCHED_OBJECT, book);
		
		return PAGE_CONTEXT + "bookShow";
	}

	/**
	 * Retrieve the book with the specified id for the update form.
	 */
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model model) {
		model.addAttribute(ControllerConstants.FETCHED_OBJECT, bookService.findById(id));
        return PAGE_CONTEXT + "bookCreate";
    }

	/**
	 * Create a new book and place in Model attribute.
	 */
	@RequestMapping(value="/create", method=RequestMethod.GET)
    public String createForm(Model model) {
		model.addAttribute(ControllerConstants.FETCHED_OBJECT, new RefBook());
        return PAGE_CONTEXT + "bookCreate";
    }

	/**
	 * Create/update a book.
	*/
	@RequestMapping(value="/create", method = RequestMethod.POST)
	public String create(@Valid RefBook book, BindingResult bindingResult,
			Model model, HttpServletRequest httpServletRequest,
			RedirectAttributes redirectAttributes, Locale locale,
			@RequestParam(value = "file", required = false) MultipartFile file) {
		
		if (bindingResult.hasErrors()) {
			model.addAttribute(ControllerConstants.FETCHED_OBJECT, book);
			return PAGE_CONTEXT + "bookCreate";
		}
		
		log.info("Creating/updating book");
		
		model.asMap().clear();
		redirectAttributes.addFlashAttribute("message", new Message(
				"success", messageSource.getMessage("book_save_success", new Object[] {}, locale)));

		// Process upload file
		if (!file.isEmpty()
				&& (file.getContentType().equals(MediaType.IMAGE_JPEG_VALUE) || 
					file.getContentType().equals(MediaType.IMAGE_PNG_VALUE) ||
					file.getContentType().equals(MediaType.IMAGE_GIF_VALUE))) {
			
			log.info("File name: " + file.getName());
			log.info("File size: " + file.getSize());
			log.info("File content type: " + file.getContentType());

			byte[] fileContent = null;
			String imageString = null;

			try {
				InputStream inputStream = file.getInputStream();
				fileContent = IOUtils.toByteArray(inputStream);

				// Convert byte[] into String image
				imageString = ImageUtil.encodeToString(fileContent);
				
				book.setPhoto(imageString);

			} catch (IOException ex) {
				log.error("Error saving uploaded file");
				book.setPhoto(ImageUtil.smallNoImage());
			}
		} else { // File is improper type or no file was uploaded.

			// If book already exists, load its image into the 'book' object.
			if (book.getId() != null) {
				RefBook savedBook = bookService.findById(book.getId());
				book.setPhoto(savedBook.getPhoto());

			} else {// Else set to default no-image picture.
				book.setPhoto(ImageUtil.smallNoImage());
			}
		}

		bookService.save(book);

		return "redirect:/" + UrlUtil.encodeUrlPathSegment(book.getId().toString(), httpServletRequest);
	}

	/**
	 * Returns the photo for the book with the specified id.
	 */
	@RequestMapping(value = "/photo/{id}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	@ResponseBody
	public byte[] downloadPhoto(@PathVariable("id") Long id) {

		RefBook book = bookService.findById(id);
		log.info("Downloading photo for id: {} with size: {}", book.getId(), book.getPhoto().length());

		// Convert String image into byte[]
		byte[] imageBytes = ImageUtil.decode(book.getPhoto());
		
		return imageBytes;
	}

	/**
	 * Deletes the book with the specified id.
	 */
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String delete(@PathVariable Long id, Model model, Locale locale) {
		log.info("Deleting book with id: " + id);
		RefBook book = bookService.findById(id);

		if (book != null) {
			bookService.delete(book);
			log.info("Book deleted successfully");

			model.addAttribute("message",	new Message("success", messageSource.getMessage(
							"book_delete_success", new Object[] {}, locale)));
		}

		List<RefBook> books = bookService.findAll();
		model.addAttribute(ControllerConstants.FETCHED_OBJECTS, books);

		return PAGE_CONTEXT + "bookBrowse";
	}
	
	@RequestMapping(value="/reset", method=RequestMethod.GET)
	public String resetDatabase(Model model) {
		log.info("Resetting database to original state");
		
		bookService.deleteAll();
		bookService.restoreDefaultBooks();
		
		List<RefBook> books = bookService.findAll();
		model.addAttribute(ControllerConstants.FETCHED_OBJECTS, books);

		return PAGE_CONTEXT + "bookBrowse";
	}

	protected void loadXlsxData(){
		ClassPathResource resource = new ClassPathResource("/config/liquibase/data/forum-structure.xlsx");

		BufferedReader br = null;
		InputStream is = null;
		try {
			//is = resource.getInputStream();
			is = new FileInputStream(resource.getFile());
			Workbook workbook = StreamingReader.builder()
			.rowCacheSize(100)    // number of rows to keep in memory (defaults to 10)
      .bufferSize(4096)     // buffer size to use when reading InputStream to file (defaults to 1024)
      .open(is);            // InputStream or File for XLSX file (required)

			for (Sheet sheet : workbook){
				try {
				  System.out.println(sheet.getSheetName());
				  for (Row r : sheet) {
				    for (Cell c : r) {
				      System.out.println(c.getStringCellValue());
				    }
				  }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}
	}

	@Override
	protected String performSearch(SearchParameter params) {
		// TODO Auto-generated method stub
		return null;
	}
}

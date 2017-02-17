package org.budnikov.staff.controller;

import org.apache.commons.io.IOUtils;
import org.budnikov.staff.domain.History;
import org.budnikov.staff.domain.Person;
import org.budnikov.staff.service.HistoryService;
import org.budnikov.staff.service.PersonService;
import org.budnikov.staff.util.Message;
import org.budnikov.staff.util.UrlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@RequestMapping(value = "/persons")
@Controller
public class PersonController {

    private MessageSource messageSource;
    private PersonService personService;
    private HistoryService historyService;

    private final Logger logger = LoggerFactory.getLogger(PersonController.class);

    //  метод выводит список персон
    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        logger.info("Listing Persons");
        List<Person> persons = personService.findAll();
        model.addAttribute("persons", persons);
        logger.info("No of person " + persons.size());
        return "persons/list";
    }

    //    Просмотр историй для персоны
    @RequestMapping(value = "/{id}/histories", method = RequestMethod.GET)
    public String listHistoriesForPerson(@PathVariable("id") Long id, Model model) {
        Set<History> histories = historyService.findHistoryForPersonById(id);
        Person person = personService.findById(id);
        model.addAttribute("lastName", person.getLastName());
        model.addAttribute("firstName", person.getFirstName());
        model.addAttribute("patronymic", person.getPatronymic());
        model.addAttribute("histories", histories);
        return "histories/list";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(@PathVariable("id") Long id, Model model) {
        Person person = personService.findById(id);
        model.addAttribute("person", person);
        return "persons/show";
    }

    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.POST)
    public String update(@Valid Person person, BindingResult bindingResult, Model uiModel,
                         HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes,
                         Locale locale, @RequestParam(value = "file", required = false) Part file) {
        logger.info("Updating person");
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("message", new Message("error",
                    messageSource.getMessage("person_save_fail", new Object[]{}, locale)));
            uiModel.addAttribute("person", person);
            return "persons/update";
        }
        uiModel.asMap().clear();
        redirectAttributes.addFlashAttribute("message", new Message("success",
                messageSource.getMessage("person_save_success", new Object[]{}, locale)));
        // Process upload file
        savePhoto(person, file);

//        получает Id персоны
        Long id = person.getId();
//        вытягивает все истории персоны
        Set<History> histories = historyService.findHistoryForPersonById(id);
//        сохраняет все записи в персону
        person.setHistories(histories);

//        сохраняет персону
        personService.save(person);
        return "redirect:/persons/" + UrlUtil.encodeUrlPathSegment(person.getId().toString(),
                httpServletRequest);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("person", personService.findById(id));
        return "persons/update";
    }

    @RequestMapping(params = "form", method = RequestMethod.POST)
    public String create(@Valid Person person, BindingResult bindingResult,
                         Model uiModel, HttpServletRequest httpServletRequest,
                         RedirectAttributes redirectAttributes, Locale locale,
                         @RequestParam(value = "file", required = false) Part file) {
        logger.info("Creating  person");
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("message", new Message("error",
                    messageSource.getMessage("person_save_fail",
                            new Object[]{}, locale)));
            uiModel.addAttribute("person", person);
            return "persons/create";
        }
        uiModel.asMap().clear();
        redirectAttributes.addFlashAttribute("message",
                new Message("success",
                        messageSource.getMessage("person_save_success",
                                new Object[]{}, locale)));
        // Process upload file
        savePhoto(person, file);
        personService.save(person);
        logger.info("Person id:  " + person.getId());
        return "redirect:/persons/" + UrlUtil.encodeUrlPathSegment(person.getId().toString(),
                httpServletRequest);
    }

    @RequestMapping(value = "/photo/{id}", method = RequestMethod.GET)
    @ResponseBody
    public byte[] downloadPhoto(@PathVariable("id") Long id) {
        Person person = personService.findById(id);

        if (person.getPhoto() != null) {
            logger.info("Downloading photo for id: {} with size: {}", person.getId(),
                    person.getPhoto().length);
        }

        return person.getPhoto();
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(params = "form", method = RequestMethod.GET)
    public String createForm(Model uiModel) {
        Person person = new Person();
        uiModel.addAttribute("person", person);
        return "persons/create";
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable("id") Long id) {
        personService.delete(id);
        logger.info("Person deleted");
        return "redirect:/persons";
    }

    public void savePhoto(Person person, Part file) {
        // Process upload file
        if (file != null) {
            logger.info("File name: " + file.getName());
            logger.info("File size: " + file.getSize());
            logger.info("File content type: " + file.getContentType());
            byte[] fileContent = null;
            try {
                InputStream inputStream = file.getInputStream();
                if (inputStream == null) logger.info("File inputstream is null");
                fileContent = IOUtils.toByteArray(inputStream);
                person.setPhoto(fileContent);
            } catch (IOException ex) {
                logger.error("Error saving uploaded file");
            }
            person.setPhoto(fileContent);
        }
    }

    @Autowired
    public void setHistoryService(HistoryService historyService) {
        this.historyService = historyService;
    }

    @Autowired
    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

}

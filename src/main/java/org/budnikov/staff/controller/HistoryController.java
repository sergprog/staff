package org.budnikov.staff.controller;

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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Locale;


@Controller
@RequestMapping("/histories")
public class HistoryController {

    private MessageSource messageSource;
    private HistoryService historyService;
    private PersonService personService;

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

    private final Logger logger = LoggerFactory.getLogger(HistoryController.class);

    //    Создать запись для персоны
    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String createHistoryProfile(@PathVariable("id") Long id, Model model) {
        Person person = personService.findById(id);
        History history = new History();
        person.addHistory(history);
        model.addAttribute("history", history);
        return "histories/create";
    }

    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.POST)
    public String addHistoryFromForm(@Valid History history, BindingResult bindingResult,
                                     Model uiModel, HttpServletRequest httpServletRequest,
                                     RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Save history");
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("message", new Message("error",
                    messageSource.getMessage("history_save_fail",
                            new Object[]{}, locale)));
            uiModel.addAttribute("history", history);
            return "histories/create";
        }
        uiModel.asMap().clear();
        redirectAttributes.addFlashAttribute("message",
                new Message("success",
                        messageSource.getMessage("history_save_success",
                                new Object[]{}, locale)));
        this.historyService.save(history);
        Person person = history.getPerson();
        logger.info("History Saved");
        return "redirect:/persons/" + UrlUtil.encodeUrlPathSegment(person.getId().toString(),
                httpServletRequest) + "/histories";
    }


    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public String editHistory(@PathVariable("id") Long id, Model model) {
        model.addAttribute("history", historyService.findById(id));
        return "histories/update";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public String editHistoryFromForm(@Valid History history, BindingResult bindingResult,
                                      Model uiModel, HttpServletRequest httpServletRequest,
                                      RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Save history");
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("message", new Message("error",
                    messageSource.getMessage("history_save_fail", new Object[]{}, locale)));
            uiModel.addAttribute("history", history);
            return "histories/update";
        }
        uiModel.asMap().clear();
        redirectAttributes.addFlashAttribute("message", new Message("success",
                messageSource.getMessage("history_save_success", new Object[]{}, locale)));

        this.historyService.save(history);
        logger.info("History Saved");
        Person person = history.getPerson();
        return "redirect:/persons/" + UrlUtil.encodeUrlPathSegment(person.getId().toString(),
                httpServletRequest) + "/histories";
    }

    //  метод удаляет объект History по id
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deleteHistory(@PathVariable("id") Long id, HttpServletRequest httpServletRequest) {
        History history = historyService.findById(id);
        Person person = history.getPerson();
        historyService.delete(id);
        return "redirect:/persons/" + UrlUtil.encodeUrlPathSegment(person.getId().toString(),
                httpServletRequest) + "/histories";
    }

}

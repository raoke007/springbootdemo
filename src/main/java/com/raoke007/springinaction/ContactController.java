package com.raoke007.springinaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * <p>description<p>
 *
 * @author raoke007
 * @date 2019/3/12 16:25
 */
@Controller
@RequestMapping("/contact")
public class ContactController {

    private final ContactRepository contactRepository;

    @Autowired
    public ContactController(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String home(Model model) {
        List<Contact> contacts = contactRepository.findAll();
        model.addAttribute("contacts", contacts);

        return "home";
    }

    @RequestMapping(value = "/addition", method = RequestMethod.POST)
    public String submit(Contact contact) {
        contactRepository.save(contact);

        return "redirect:/contact/list";
    }

    @RequestMapping("/{id}/detail")
    public Contact getContact(@PathVariable String id) {
        Contact contact = contactRepository.findById(id);

        return contact;
    }

    @RequestMapping("/{id}/deletion")
    public String deleteContact(@PathVariable String id) {
        contactRepository.removeById(id);

        return "redirect:/contact/list";
    }
}

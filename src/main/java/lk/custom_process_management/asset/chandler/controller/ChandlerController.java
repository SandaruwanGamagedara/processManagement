package lk.custom_process_management.asset.chandler.controller;


import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lk.custom_process_management.asset.chandler.entity.Chandler;
import lk.custom_process_management.asset.chandler.service.ChandlerService;
import lk.custom_process_management.util.interfaces.AbstractController;
import lk.custom_process_management.util.service.EmailService;
import lk.custom_process_management.util.service.MakeAutoGenerateNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping( "/chandler" )
public class ChandlerController implements AbstractController< Chandler, Integer > {
  private final ChandlerService chandlerService;
  private final MakeAutoGenerateNumberService makeAutoGenerateNumberService;
  private final EmailService emailService;

  @Autowired
  public ChandlerController(ChandlerService chandlerService,
                            MakeAutoGenerateNumberService makeAutoGenerateNumberService, EmailService emailService) {
    this.chandlerService = chandlerService;
    this.makeAutoGenerateNumberService = makeAutoGenerateNumberService;
    this.emailService = emailService;
  }

  private String commonThings(Model model, Chandler chandler, Boolean addState) {
    model.addAttribute("chandler", chandler);
    model.addAttribute("addStatus", addState);
    return "chandler/addChandler";
  }

  @GetMapping
  public String findAll(Model model) {
    model.addAttribute("chandlers", chandlerService.findAll());
    return "chandler/chandler";
  }

  @GetMapping( "/add" )
  public String addForm(Model model) {
    return commonThings(model, new Chandler(), true);
  }

  @PostMapping( value = {"/save", "/update"} )
  public String persist(@Valid @ModelAttribute Chandler chandler, BindingResult bindingResult,
                        RedirectAttributes redirectAttributes, Model model) {
    if ( bindingResult.hasErrors() ) {
      return commonThings(model, chandler, true);
    }
    if ( chandler.getContactOne() != null ) {
      chandler.setContactOne(makeAutoGenerateNumberService.phoneNumberLengthValidator(chandler.getContactOne()));
    }
    if ( chandler.getContactTwo() != null ) {
      chandler.setContactTwo(makeAutoGenerateNumberService.phoneNumberLengthValidator(chandler.getContactTwo()));
    }
    //if chandler has id that chandler is not a new chandler
    if ( chandler.getId() == null ) {
      //if there is not chandler in db
      Chandler DBChandler = chandlerService.lastSupplier();

      if ( DBChandler == null ) {
        //need to generate new one
        chandler.setCode("SLCC" + makeAutoGenerateNumberService.numberAutoGen(null).toString());
      } else {
        //if there is chandler in db need to get that chandler's code and increase its value
        String previousCode = DBChandler.getCode().substring(4);
        chandler.setCode("SLCC" + makeAutoGenerateNumberService.numberAutoGen(previousCode).toString());
      }
      //send welcome message and email
      if ( chandler.getEmail() != null ) {
          emailService.sendEmail(chandler.getEmail(), "Welcome To SLC", "Your have successfully register on our database. \n Thank\n Sri Lanka Customs");
      }
    }
    redirectAttributes.addFlashAttribute("chandlerDetail",
                                         chandlerService.persist(chandler));
    return "redirect:/chandler";
  }

  @GetMapping( "/edit/{id}" )
  public String edit(@PathVariable Integer id, Model model) {
    return commonThings(model, chandlerService.findById(id), false);
  }

  @GetMapping( "/delete/{id}" )
  public String delete(@PathVariable Integer id, Model model) {
    chandlerService.delete(id);
    return "redirect:/chandler";
  }

  @GetMapping( "/{id}" )
  public String view(@PathVariable Integer id, Model model) {
    model.addAttribute("chandlerDetail", chandlerService.findById(id));
    return "chandler/chandler-detail";
  }

  @GetMapping( value = "/findAll" )
  @ResponseBody
  public MappingJacksonValue findAll() {

    //MappingJacksonValue
    List< Chandler > chandlers = chandlerService.findAll();
    //Create new mapping jackson value and set it to which was need to filter
    MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(chandlers);

    //simpleBeanPropertyFilter :-  need to give any id to addFilter method and created filter which was mentioned
    // what parameter's necessary to provide
    SimpleBeanPropertyFilter simpleBeanPropertyFilter = SimpleBeanPropertyFilter
        .filterOutAllExcept("id", "name");

    FilterProvider filters = new SimpleFilterProvider()
        .addFilter("Chandler", simpleBeanPropertyFilter);
    mappingJacksonValue.setFilters(filters);
    return mappingJacksonValue;
  }
}

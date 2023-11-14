package cz.czechitas.java2webapps.lekce8.controller;

import cz.czechitas.java2webapps.lekce8.entity.Osoba;
import cz.czechitas.java2webapps.lekce8.repository.OsobaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;

@Controller
public class OsobaController {

  private final OsobaRepository osobaRepository;
  @Autowired
  public OsobaController(OsobaRepository osobaRepository) {
    this.osobaRepository = osobaRepository;
  }
//  private final List<Osoba> seznamOsob = List.of(
//          new Osoba(1L, "Božena", "Němcová", LocalDate.of(1820, 2, 4), "Vídeň", null, null)
//  );


  @InitBinder
  public void nullStringBinding(WebDataBinder binder) {
    //prázdné textové řetězce nahradit null hodnotou
    binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
  }

  @GetMapping("/")
  public ModelAndView seznam() {
    return new ModelAndView("seznam")
            .addObject("osoby", osobaRepository.findAll());
  }

  @GetMapping("/novy")
  public ModelAndView novy() {
    return new ModelAndView("detail")
            .addObject("osoba", new Osoba());
  }

  @PostMapping("/novy")
  public String pridat(@ModelAttribute("osoba") @Valid Osoba osoba, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "detail";
    }
   // lektor přidal jako pojistku          osoba.setId(null);
    osobaRepository.save(osoba);
    //TODO uložit údaj o nové osobě - funguje
    return "redirect:/";
  }

  @GetMapping("/{id:[0-9]+}")
  public ModelAndView detail(@PathVariable long id) {
    //TODO načíst údaj o osobě - funguje
    return new ModelAndView("detail")
            .addObject("osoba", osobaRepository.findById(id).get());
  }

  @PostMapping("/{id:[0-9]+}")
  public String ulozit(@PathVariable long id, @ModelAttribute("osoba") @Valid Osoba osoba, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "detail";
    }
    osobaRepository.save(osoba);
    //TODO uložit údaj o osobě - funugje, @PathVariable long id se může smazat, je zbytečné
    return "redirect:/";
  }

  @PostMapping(value = "/{id:[0-9]+}", params = "akce=smazat")
  public String smazat(@PathVariable long id) {
    osobaRepository.deleteById(id);
    //TODO smazat údaj o osobě - funguje
    return "redirect:/";
  }
}

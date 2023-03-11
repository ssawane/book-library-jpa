package com.mara.jpalibrary.controllers;

import com.mara.jpalibrary.models.Book;
import com.mara.jpalibrary.models.Person;
import com.mara.jpalibrary.services.BooksService;
import com.mara.jpalibrary.services.PeopleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BooksService booksService;
    private final PeopleService peopleService;

    public BooksController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String index(Model model, @RequestParam Optional<Integer> page,
                        @RequestParam Optional<Integer> books_per_page,
                        @RequestParam(defaultValue = "false") boolean sort_by_year) {
        model.addAttribute("books", booksService.findAll(page, books_per_page, sort_by_year));
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@ModelAttribute("person") Person person,
                       @PathVariable("id") int id, Model model) {
        model.addAttribute("book", booksService.findOne(id));
        Optional<Person> bookOwner = booksService.getOwnerByBookId(id);
        if (bookOwner.isPresent()) // true
            model.addAttribute("owner", bookOwner.get());
        else
            model.addAttribute("people", peopleService.findAll());
        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(Model model){
        model.addAttribute("book", new Book());
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "books/new";
        booksService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", booksService.findOne(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult, @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "books/edit";
        booksService.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        booksService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/appoint")
    public String appoint(@ModelAttribute("person") Person person, @PathVariable("id") int id) {
        booksService.appointToPerson(person, id);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id) {
        booksService.releaseFromPerson(id);
        return "redirect:/books/" + id;
    }

    @GetMapping("/search")
    public String search() {
        return "books/search";
    }

    @GetMapping(value = "/search", params = "title_part")
    public String search(@RequestParam(value = "title_part") String titlePart, Model model) {
        model.addAttribute("books", booksService.findByTitleContaining(titlePart));
        return "books/search";
    }
}
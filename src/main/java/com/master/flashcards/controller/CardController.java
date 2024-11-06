package com.master.flashcards.controller;

import com.master.flashcards.entity.Card;
import com.master.flashcards.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CardController {

    private final CardService cardService;

    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("themes", cardService.getThemes());
        return "home";
    }

    @GetMapping("/new_theme")
    public String showCreateThemePage() {
        return "new_theme"; // Вернуть имя HTML-шаблона для отображения страницы
    }

    @PostMapping("/new_theme")
    public String createTheme(@RequestParam String new_theme,
                              @RequestParam(required = false) String changed_theme,
                              RedirectAttributes redirectAttributes) {
        List<String> cardsList = cardService.getThemes(); // Получаем список тем

        if (new_theme != null && !cardsList.contains(new_theme)) {
            cardService.addTheme(new_theme); // Добавить новую тему
            redirectAttributes.addFlashAttribute("message", "Новая тема успешно добавлена!");
        } else if (cardsList.contains(new_theme) && (changed_theme != null && !cardsList.contains(changed_theme))) {
            cardService.renameTheme(new_theme, changed_theme); // Переименовать тему
            redirectAttributes.addFlashAttribute("message", "Тема успешно переименована!");
        } else {
            redirectAttributes.addFlashAttribute("message", "Тема с таким названием уже существует!");
        }

        return "redirect:/"; // Перенаправление на главную страницу
    }

    @GetMapping("/theme/{theme}")
    public String viewTheme(@PathVariable String theme, Model model) {
        model.addAttribute("theme", theme);
        model.addAttribute("cards", cardService.getCards(theme));
        return "theme";
    }

    @PostMapping("/theme/{theme}/delete")
    public String deleteTheme(@PathVariable String theme) {
        cardService.deleteTheme(theme);
        return "redirect:/"; // Перенаправление после удаления
    }

    @PostMapping("/theme/{theme}/practice-buttons")
    public String handlePracticeButtons(@PathVariable String theme, @RequestParam String action) {
        // Handle the button actions like increase, decrease, etc.
        // Your logic here
        return "redirect:/theme/" + theme + "/practice"; // Redirect back to the practice page
    }

    @GetMapping("/theme/{theme}/{id}/view")
    public String viewCard(@PathVariable String theme, @PathVariable int id, Model model) {
        List<Card> cards = cardService.getCards(theme); // Предполагаем, что `getCardByTheme` вернет нужную карточку
        if (!cards.isEmpty()) {
            model.addAttribute("card", cards.get(id));
        }
        model.addAttribute("theme", theme);
        return "card_view"; // Имя HTML-шаблона
    }

    @GetMapping("/theme/{theme}/practice")
    public String practice(@PathVariable String theme, Model model) {
        List<Card> cards = cardService.getCards(theme);

        // Проверьте, есть ли карточки для данной темы
        if (!cards.isEmpty()) {
            model.addAttribute("card", cards.get(0)); // Передаем первую карточку в модель
        } else {
            model.addAttribute("message", "Нет доступных карточек для этой темы."); // Сообщение, если карточек нет
        }
        model.addAttribute("theme", theme); // Передаем тему в модель

        return "practice"; // Возвращаем представление для практики
    }

    @GetMapping("/theme/{theme}/new_card")
    public String newCardForm(@PathVariable String theme, Model model) {
        model.addAttribute("theme", theme);
        return "new_card"; // Вернуть имя вашего шаблона
    }

    @PostMapping("/theme/{theme}/new_card")
    public String addCard(@PathVariable String theme,
                          @RequestParam String front,
                          @RequestParam String back) {
        Card card = new Card(theme, front, back);
        cardService.addCard(card);
        return "redirect:/theme/" + theme; // Перенаправление на страницу темы после добавления карточки
    }

    @PostMapping("/theme/{theme}/{id}/delete")
    public String deleteCard(@PathVariable String theme, @PathVariable String id) {
        cardService.deleteCard(theme, id);
        return "redirect:/theme/" + theme;
    }
}
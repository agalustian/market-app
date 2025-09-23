package ru.market.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.market.models.Item;
import ru.market.services.ItemsService;

@Controller
@RequestMapping("/items")
public class ItemsController {

  private final ItemsService itemsService;

  ItemsController(final ItemsService itemsService) {
    this.itemsService = itemsService;
  }

  @GetMapping("/{itemId}")
  String getItemById(@PathVariable("itemId") Integer itemId, final Model model) {
    Item item = itemsService.getItemById(itemId);

    model.addAttribute("item", item);

    return "item";
  }

}

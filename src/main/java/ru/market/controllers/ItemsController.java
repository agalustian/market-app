package ru.market.controllers;

import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.market.dto.ItemDTO;
import ru.market.dto.ItemsDTO;
import ru.market.dto.ItemsSort;
import ru.market.dto.Paging;
import ru.market.models.Item;
import ru.market.services.ItemsService;

@Controller
@RequestMapping("/items")
public class ItemsController {

  private static final String ITEMS_VIEW = "items";

  private final ItemsService itemsService;

  ItemsController(final ItemsService itemsService) {
    this.itemsService = itemsService;
  }

  @GetMapping("/")
  String search(
      @RequestParam(value = "search", required = false, defaultValue = "") String search,
      @RequestParam(value = "sort", required = false) ItemsSort sort,
      @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
      @RequestParam(value = "pageSize", required = false) Integer pageSize,
      final Model model
  ) {
    if (search.isEmpty()) {
      return ITEMS_VIEW;
    }

    ItemsSort sortValue = sort != null ? sort : ItemsSort.NO;

    List<Item> items = itemsService.search(search, sortValue, PageRequest.of(pageNumber, pageSize));
    Integer itemsTotalCount = itemsService.searchCount(search);

    model.addAttribute("items",
        ItemsDTO.from(items, search, sortValue, new Paging(pageNumber, pageSize, itemsTotalCount)));

    return ITEMS_VIEW;
  }

  @GetMapping("/{itemId}")
  String getItemById(@PathVariable("itemId") Integer itemId, final Model model) {
    Item item = itemsService.getItemById(itemId);

    if (item == null) {
      return ITEMS_VIEW;
    }

    model.addAttribute("item", ItemDTO.from(item, 10, "test-path"));

    return "item";
  }

}

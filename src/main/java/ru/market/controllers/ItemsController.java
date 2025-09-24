package ru.market.controllers;

import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.market.dto.CartAction;
import ru.market.dto.ItemDTO;
import ru.market.dto.ItemsDTO;
import ru.market.dto.ItemsSort;
import ru.market.dto.Paging;
import ru.market.models.Item;
import ru.market.services.CartsService;
import ru.market.services.ItemsService;

@Controller
@RequestMapping("/items")
public class ItemsController {
  // TODO move to environment variables
  private static final Integer ITEMS_CHUNK_SIZE = 3;

  // TODO for test
  private static final Integer CART_ID = 1;

  private static final String ITEMS_VIEW = "items";

  private final ItemsService itemsService;

  private final CartsService cartsService;

  ItemsController(final ItemsService itemsService, CartsService cartsService) {
    this.itemsService = itemsService;
    this.cartsService = cartsService;
  }

  @GetMapping("/")
  String search(
      @RequestParam(value = "search", required = false, defaultValue = "") String search,
      @RequestParam(value = "sort", required = false, defaultValue = "NO") ItemsSort sort,
      @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
      @RequestParam(value = "pageSize", required = false, defaultValue = "5") Integer pageSize,
      final Model model
  ) {
    if (search.isEmpty()) {
      return ITEMS_VIEW;
    }

    List<Item> items = itemsService.search(search, sort, PageRequest.of(pageNumber, pageSize));
    Integer itemsTotalCount = itemsService.searchCount(search);

    model.addAttribute("items", ItemsDTO.from(items, ITEMS_CHUNK_SIZE));
    model.addAttribute("search", search);
    model.addAttribute("sort", sort);
    model.addAttribute("paging", new Paging(pageNumber, pageSize, itemsTotalCount));

    return ITEMS_VIEW;
  }

  @PostMapping
  String addRemoveToCart(
      @RequestParam("id") Integer itemId,
      @RequestParam("action") CartAction action,
      @RequestParam(value = "search", required = false, defaultValue = "") String search,
      @RequestParam(value = "sort", required = false) ItemsSort sort,
      @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
      @RequestParam(value = "pageSize", required = false) Integer pageSize
  ) {
    cartsService.addRemoveToCart(CART_ID, itemId, action);

    return String.format("redirect:/items?search=%s&sort=%s&pageNumber=%s&pageSize=%s", search, sort, pageNumber,
        pageSize);
  }

  @PostMapping("/{itemId}")
  String addRemoveToCart(@PathVariable Integer itemId, @RequestParam CartAction action, Model model) {
    cartsService.addRemoveToCart(CART_ID, itemId, action);

    Item item = itemsService.getItemById(itemId);

    model.addAttribute("item", item);

    return "item";
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

package ru.market.dto;

import java.util.List;

public record CartItemsDTO (List<ItemDTO> items, Integer total) {
}

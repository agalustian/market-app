package ru.market.shopfront.dto;

public record SearchRequest(String search, ItemsSort sort, Integer pageNumber, Integer pageSize) {
}

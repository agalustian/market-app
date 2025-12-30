package ru.market.dto;

public record SearchRequest(String search, ItemsSort sort, Integer pageNumber, Integer pageSize) {
}

package ru.market.dto;

public record AddRemoveToCartRequest(Integer itemId, CartAction action) {
}

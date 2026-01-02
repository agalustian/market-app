package ru.market.shopfront.dto;

public record AddRemoveToCartRequest(Integer itemId, CartAction action) {
}

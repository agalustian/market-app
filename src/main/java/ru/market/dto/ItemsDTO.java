package ru.market.dto;

import java.util.ArrayList;
import java.util.List;
import ru.market.models.Item;

public record ItemsDTO(List<List<ItemDTO>> items) {

  public static ItemsDTO from(List<Item> items, Integer chunkSize) {
    if (items.isEmpty()) {
      return new ItemsDTO(List.of(fillEmptyDTO(new ArrayList<>(), chunkSize)));
    }

    List<List<ItemDTO>> lists = new ArrayList<>();

    for (var i = 0; i < items.size(); i += chunkSize) {
      lists.add(getSubList(items, i, chunkSize));
    }

    return new ItemsDTO(lists);
  }

  private static List<ItemDTO> getSubList(List<Item> items, Integer index, Integer chunkSize) {
    var subList = items.subList(index, Math.min(index + chunkSize, items.size())).stream().map(ItemDTO::from).toList();

    return subList.size() == chunkSize ? subList : fillEmptyDTO(subList, chunkSize);
  }

  private static List<ItemDTO> fillEmptyDTO(List<ItemDTO> itemsDTO, Integer chunkSize) {
    List<ItemDTO> list = new ArrayList<>(itemsDTO);

    var size = list.size();
    for (var i = 0; i < chunkSize - size; i += 1) {
      list.add(ItemDTO.empty());
    }

    return list;
  }

}

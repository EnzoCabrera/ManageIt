package com.example.estoque.mapper;

import com.example.estoque.dtos.itemDtos.ItemResponseDto;
import com.example.estoque.entities.ItemEntities.Item;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper {

    public ItemResponseDto toDto(Item item) {
        ItemResponseDto dto = new ItemResponseDto();
        dto.setCodord(item.getCodord().getCodord());
        dto.setCodprod(item.getCodprod().getCodProd());
        dto.setQuantity(item.getQuantity());
        dto.setPriceInCents(item.getPriceInCents());
        dto.setDiscountPercent(item.getDiscountPercent());
        dto.setTotalInCents(item.getTotalInCents());
        return dto;
    }
}

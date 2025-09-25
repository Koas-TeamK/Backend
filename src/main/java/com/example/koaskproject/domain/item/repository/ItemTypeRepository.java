package com.example.koaskproject.domain.item.repository;

import com.example.koaskproject.domain.item.entity.Item;
import com.example.koaskproject.domain.item.entity.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemTypeRepository extends JpaRepository<ItemType, Long> {
}

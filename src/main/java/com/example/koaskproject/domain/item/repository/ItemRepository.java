package com.example.koaskproject.domain.item.repository;

import com.example.koaskproject.domain.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long>
{
    Optional<Item> findByLimitedNumberAndItemType_Name(String serialNumber,String name );


}

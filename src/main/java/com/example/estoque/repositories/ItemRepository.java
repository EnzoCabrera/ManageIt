package com.example.estoque.repositories;

import com.example.estoque.entities.ItemEntities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long>, JpaSpecificationExecutor<Item> {

    // Custom query to confirm that the item is not deleted
    Optional<Item> findBycoditeAndIsDeletedFalse(Long codite);
}

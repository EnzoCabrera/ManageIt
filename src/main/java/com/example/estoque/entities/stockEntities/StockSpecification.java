package com.example.estoque.entities.stockEntities;

import com.example.estoque.entities.customerEntities.Customer;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigInteger;

public class StockSpecification {

    //Filter for product code
    public static Specification<Stock> hasCodprod(Long codprod) {
        return (root, query, cb) ->
                codprod == null ? null : cb.equal(root.get("codProd"), codprod);
    }

    //Filter for product name
    public static Specification<Stock> hasName(String productName) {
        return (root, query, cb) ->
                productName == null ? null : cb.equal(root.get("productName"), productName);
    }

    //Filter for product not deleted
    public static Specification<Stock> isNotDeleted() {
        return (root, query, cb) ->
                cb.isFalse(root.get("isDeleted"));
    }
}

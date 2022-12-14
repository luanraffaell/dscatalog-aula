package com.devsuperior.dscatalog.tests;

import java.time.Instant;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

public class Factory {
	public static Product createProduct() {
		Product product = new Product(1L, "Phone", "Good phone", 800.00, "http://img.img", Instant.parse("2020-10-20T03:00:00Z"));
		product.getCategorias().add(new Category(2L, "Electronics"));
		return product;
	}
	public static ProductDTO createProductDTO() {
		Product product = createProduct();
		return new ProductDTO(product, product.getCategorias());
	}
}

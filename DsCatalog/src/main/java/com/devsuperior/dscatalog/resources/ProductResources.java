package com.devsuperior.dscatalog.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;

@RestController
@RequestMapping(value ="/products")
public class ProductResources {
	@Autowired
	ProductService productService;
	
	@GetMapping
	public ResponseEntity<Page<ProductDTO>> findAll(Pageable pageable){
		//PAGE, SIZE, SORT
		//http://localhost:8080/products?page=1&size=5&sort=name,asc
		Page<ProductDTO> list = productService.findAllPaged(pageable);
		return ResponseEntity.ok().body(list);
		//http://localhost:8080/categories?page=1&linesPerPage=5&direction=ASC&orderBy=id
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ProductDTO> findById(@PathVariable Long id){
		return ResponseEntity.ok().body(productService.findById(id));
	}
	
	@PostMapping
	public ResponseEntity<ProductDTO> insert(@RequestBody ProductDTO product){
		product = productService.save(product);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(product.getId()).toUri();
		return ResponseEntity.created(uri).body(product);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ProductDTO> update(@PathVariable Long id,@RequestBody ProductDTO product){
		product = productService.update(id,product);
		return ResponseEntity.ok().body(product);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		productService.delete(id);
		return ResponseEntity.noContent().build();
	}
}

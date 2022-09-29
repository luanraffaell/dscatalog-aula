package com.devsuperior.dscatalog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;

@Service // vai registrar com um componente spring, ou seja, o spring irá gerenciar a
			// injeção de dependência dessa classe
public class ProductService {

	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	CategoryRepository categoryRepository;

	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(Pageable pageable) {
		Page<Product> list = productRepository.findAll(pageable);
		return list.map(x -> new ProductDTO(x));

	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Product not found"));
		return new ProductDTO(product, product.getCategorias());
	}

	@Transactional
	public ProductDTO save(ProductDTO productDTO) {
		Product product = new Product();
		copyDtoToEntity(productDTO, product);
		product = productRepository.save(product);
		productDTO.setId(product.getId());
		return productDTO;
	}

	@Transactional
	public ProductDTO update(Long id, ProductDTO productDTO) {
		Product product = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));
		copyDtoToEntity(productDTO, product);
		productRepository.save(product);
		return new ProductDTO(product);
	}

	public void delete(Long id) {
		try {
			productRepository.deleteById(id);

		} catch (EmptyResultDataAccessException e) {
			throw new EntityNotFoundException("Id not found " + id);
		} catch (DataIntegrityViolationException e) {
			throw new DataBaseException("Integrit violation");
		}

	}

	private void copyDtoToEntity(ProductDTO productDTO, Product product) {
		product.setName(productDTO.getName());
		product.setDescription(productDTO.getDescription());
		product.setImgUrl(productDTO.getImgUrl());
		product.setDate(productDTO.getDate());
		product.setPrice(productDTO.getPrice());
		
		product.getCategorias().clear();
		
		for(CategoryDTO car : productDTO.getCategories()) {
			Category category = categoryRepository.getOne(car.getId());
			product.getCategorias().add(category);
		}
		

	}
}

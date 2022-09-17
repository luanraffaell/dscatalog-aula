package com.devsuperior.dscatalog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;

@Service //vai registrar com um componente spring, ou seja, o spring irá gerenciar a injeção de dependência dessa classe
public class ProductService {
	
	@Autowired
	ProductRepository productRepository;
	
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(PageRequest pageRequest){
		Page<Product> list = productRepository.findAll(pageRequest);
		return list.map(x -> new ProductDTO(x));
		
	}
	
	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Product product = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found"));
		return new ProductDTO(product,product.getCategorias());
	}
	@Transactional
	public ProductDTO save(ProductDTO productDTO) {
		Product product = new Product();
		product.setName(productDTO.getName());
		product = productRepository.save(product);
		productDTO.setId(product.getId());
		return productDTO;
	}
	
	@Transactional
	public ProductDTO update(Long id,ProductDTO product) {
		ProductDTO productPesist = findById(id);
		productPesist.setName(product.getName());
		
		Product productUpdated = new Product();
		productUpdated.setName(productPesist.getName());
		productUpdated.setId(productPesist.getId());
		productRepository.save(productUpdated);
		return productPesist;
	}
	
	
	public void delete(Long id) {
		try {
		productRepository.deleteById(id);
		
		}catch(EmptyResultDataAccessException e){
			throw new EntityNotFoundException("Id not found "+id);
		}catch(DataIntegrityViolationException e) {
			throw new DataBaseException("Integrit violation");
		}
		
	}
}

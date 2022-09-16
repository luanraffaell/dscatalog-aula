package com.devsuperior.dscatalog.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;

@Service //vai registrar com um componente spring, ou seja, o spring irá gerenciar a injeção de dependência dessa classe
public class CategoryService {
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll(){
		List<Category> list = categoryRepository.findAll();
		return list.stream().map(x -> new CategoryDTO(x))
			.collect(Collectors.toList());
		
	}

	public CategoryDTO findById(Long id) {
		Category category = categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Category not found"));
		return new CategoryDTO(category);
	}
}

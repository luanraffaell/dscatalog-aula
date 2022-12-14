package com.devsuperior.dscatalog.services;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;

@Service //vai registrar com um componente spring, ou seja, o spring irá gerenciar a injeção de dependência dessa classe
public class CategoryService {
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Transactional(readOnly = true)
	public Page<CategoryDTO> findAllPaged(PageRequest pageRequest){
		Page<Category> list = categoryRepository.findAll(pageRequest);
		return list.map(x -> new CategoryDTO(x));
		
	}

	public CategoryDTO findById(Long id) {
		Category category = categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Category not found"));
		return new CategoryDTO(category);
	}
	@Transactional
	public CategoryDTO save(CategoryDTO categoryDTO) {
		Category category = new Category();
		category.setName(categoryDTO.getName());
		category.setCreatedAt(Instant.now());
		category = categoryRepository.save(category);
		categoryDTO.setId(category.getId());
		return categoryDTO;
	}
	
	@Transactional
	public CategoryDTO update(Long id,CategoryDTO category) {
		CategoryDTO categoryPesist = findById(id);
		categoryPesist.setName(category.getName());
		
		Category categoryUpdated = new Category();
		categoryUpdated.setName(categoryPesist.getName());
		categoryUpdated.setId(categoryPesist.getId());
		categoryUpdated.setUpdateAt(Instant.now());
		categoryRepository.save(categoryUpdated);
		return categoryPesist;
	}
	
	
	public void delete(Long id) {
		try {
		categoryRepository.deleteById(id);
		
		}catch(EmptyResultDataAccessException e){
			throw new EntityNotFoundException("Id not found "+id);
		}catch(DataIntegrityViolationException e) {
			throw new DataBaseException("Integrit violation");
		}
		
	}
}

package com.lutheone.dscommerce.services;

import com.lutheone.dscommerce.dto.CategoryDTO;
import com.lutheone.dscommerce.repositories.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        return repository.findAll().stream()
                .map(CategoryDTO::new)
                .toList();
    }
}

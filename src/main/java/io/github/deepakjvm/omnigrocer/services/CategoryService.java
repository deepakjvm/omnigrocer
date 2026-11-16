package io.github.deepakjvm.omnigrocer.services;

import java.util.List;

import org.springframework.stereotype.Service;

import io.github.deepakjvm.omnigrocer.models.Category;
import io.github.deepakjvm.omnigrocer.repository.CategoryRepository;

@Service
public class CategoryService {
	private final CategoryRepository categoryRepository;

	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	public Category addCategory(String name) {
		Category category = new Category();
		category.setName(name);
		return this.categoryRepository.save(category);
	}

	public List<Category> getCategories() {
		return this.categoryRepository.findAll();
	}

	public Boolean deleteCategory(int id) {
		if (this.categoryRepository.existsById(id)) {
			this.categoryRepository.deleteById(id);
			return true;
		}
		return false;
	}

	public Category updateCategory(int id, String name) {
		Category category = this.categoryRepository.findById(id).orElse(null);
		if (category == null) {
			return null;
		}
		category.setName(name);
		return this.categoryRepository.save(category);
	}

	public Category getCategory(int id) {
		return this.categoryRepository.findById(id).orElse(null);
	}
}

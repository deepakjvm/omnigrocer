package io.github.deepakjvm.omnigrocer.services;

import java.util.List;

import org.springframework.stereotype.Service;

import io.github.deepakjvm.omnigrocer.models.Product;
import io.github.deepakjvm.omnigrocer.repository.ProductRepository;

@Service
public class ProductService {
	private final ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public List<Product> getProducts() {
		return this.productRepository.findAll();
	}

	public Product addProduct(Product product) {
		return this.productRepository.save(product);
	}

	public Product getProduct(int id) {
		return this.productRepository.findById(id).orElse(null);
	}

	public Product updateProduct(int id, Product product) {
		product.setId(id);
		return this.productRepository.save(product);
	}

	public boolean deleteProduct(int id) {
		if (this.productRepository.existsById(id)) {
			this.productRepository.deleteById(id);
			return true;
		}
		return false;
	}
}

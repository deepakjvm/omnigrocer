package io.github.deepakjvm.omnigrocer.seed;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import io.github.deepakjvm.omnigrocer.models.Category;
import io.github.deepakjvm.omnigrocer.models.Product;
import io.github.deepakjvm.omnigrocer.repository.CategoryRepository;
import io.github.deepakjvm.omnigrocer.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class ProductSeeder {
    private static final ObjectMapper mapper = new ObjectMapper();
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private List<Category> allCategories;

    public void seed() throws RuntimeException {
      allCategories = categoryRepository.findAll();

      try (InputStream inputStream =
          new ClassPathResource("data/products.json").getInputStream()) {
        List<ProductJson> productsJson = 
            mapper.readValue(inputStream, new TypeReference<List<ProductJson>>() {});
        List<Product> products = new ArrayList<Product>();
        for (ProductJson pj: productsJson) {
          Product product = new Product();
          product.setName(pj.getName());
          product.setImage(pj.getImage());
          product.setCategory(getCategory(pj.getCategory()));
          product.setQuantity(pj.getQuantity());
          product.setPrice(pj.getPrice());
          product.setWeight(pj.getWeight());
          product.setDescription(pj.getDescription());
          products.add(product);
        }
        productRepository.saveAll(products);
    } catch (IOException e) {
      throw new RuntimeException("Failed to seed cities from data/cities.json", e);
    }
  }

  private Category getCategory(String category) throws RuntimeException {
    Optional<Category> categoryOpt = allCategories.stream()
      .filter(c -> category.equals(c.getName()))
      .findFirst();
    if (categoryOpt.isEmpty()) {
      throw new RuntimeException(category + " category does not exist.");
    }
    return categoryOpt.get();
  }
}

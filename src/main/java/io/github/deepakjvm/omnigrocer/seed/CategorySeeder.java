package io.github.deepakjvm.omnigrocer.seed;

import io.github.deepakjvm.omnigrocer.models.Category;
import io.github.deepakjvm.omnigrocer.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CategorySeeder {

    private final CategoryRepository categoryRepository;

    public void seed() {
        List<String> categories = List.of(
          "Books",
          "Movies",
          "Music",
          "Games",
          "Electronics",
          "Computers",
          "Home",
          "Garden",
          "Tools",
          "Grocery",
          "Health",
          "Beauty",
          "Toys",
          "Kids",
          "Baby",
          "Clothing",
          "Shoes",
          "Jewelry",
          "Sports",
          "Outdoors",
          "Automotive",
          "Industrial"
        );

        List<Category> categoryEntities = categories.stream()
                .map(name -> {
                    Category category = new Category();
                    category.setName(name);
                    return category;
                })
                .toList();

        categoryRepository.saveAll(categoryEntities);
    }
}

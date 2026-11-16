package io.github.deepakjvm.omnigrocer.repository;

import io.github.deepakjvm.omnigrocer.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
}

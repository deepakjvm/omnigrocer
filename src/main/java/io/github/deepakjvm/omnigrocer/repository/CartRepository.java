package io.github.deepakjvm.omnigrocer.repository;

import io.github.deepakjvm.omnigrocer.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
}

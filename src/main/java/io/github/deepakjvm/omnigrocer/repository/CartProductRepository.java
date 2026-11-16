package io.github.deepakjvm.omnigrocer.repository;

import io.github.deepakjvm.omnigrocer.models.CartProduct;
import io.github.deepakjvm.omnigrocer.models.CartProductId;
import io.github.deepakjvm.omnigrocer.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, CartProductId> {
    @Query("SELECT cp.product FROM CartProduct cp WHERE cp.id.cartId = :cartId")
    List<Product> findProductsByCartId(@Param("cartId") Integer cartId);
}

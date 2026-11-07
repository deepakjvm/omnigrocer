package io.github.deepakjvm.ecommerce.services;

import java.util.List;

import io.github.deepakjvm.ecommerce.models.Cart;
import io.github.deepakjvm.ecommerce.repository.CartRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    private final CartRepository cartRepository;

    @Autowired
    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Cart addCart(Cart cart) {
        return cartRepository.save(cart);
    }

    public List<Cart> getCarts() {
        return this.cartRepository.findAll();
    }

    public void updateCart(Cart cart) {
        cartRepository.save(cart);
    }

    public void deleteCart(Cart cart) {
        cartRepository.delete(cart);
    }
}

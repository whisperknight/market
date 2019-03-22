package com.mycompany.service;

import java.util.List;

import com.mycompany.entity.Cart;
import com.mycompany.entity.CartProduct;
import com.mycompany.entity.Product;
import com.mycompany.entity.User;

public interface CartService {
	
	public void addProductToCart(Cart cart, Product product);
	
	public void deleteCartProduct(Cart cart, Product product);
	
	public void clearCart(Cart cart);
	
	public void updateCartProductNum(Cart cart, Product product, int num);
	
	public List<CartProduct> getCartProducts(User user);
}

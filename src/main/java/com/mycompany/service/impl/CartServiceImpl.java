package com.mycompany.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.mycompany.dao.BaseDao;
import com.mycompany.entity.Cart;
import com.mycompany.entity.CartProduct;
import com.mycompany.entity.Product;
import com.mycompany.entity.User;
import com.mycompany.service.CartService;

@Service("cartService")
@Transactional
public class CartServiceImpl implements CartService {

	@Resource
	BaseDao<CartProduct> baseDao;

	private final int MAX_CARTPRODUCT_NUM = 10000;

	@Override
	public void addProductToCart(Cart cart, Product product) {
		try {
			if (cart.getId() == 0 || product.getId() == 0)
				throw new Exception("传入参数Cart或Product为空！");
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<CartProduct> cartProducts = cart.getProducts();
		for (CartProduct cp : cartProducts) {
			if (cp.getProduct().getId() == product.getId()) {
				if (cp.getNum() < MAX_CARTPRODUCT_NUM) {
					cp.setNum(cp.getNum() + 1);
				}
				baseDao.merge(cp);
				return;
			}
		}

		CartProduct cartProduct = new CartProduct();
		cartProduct.setNum(1);
		cartProduct.setCart(cart);
		cartProduct.setProduct(product);
		cart.getProducts().add(cartProduct);
		baseDao.persist(cartProduct);
	}

	@Override
	public List<CartProduct> getCartProducts(User user) {
		return baseDao.query("from CartProduct where cart.id = ?1", CartProduct.class,
				user.getCart().getId());
	}

	@Override
	public void updateCartProductNum(Cart cart, Product product, int num) {
		try {
			if (cart.getId() == 0 || product.getId() == 0)
				throw new Exception("传入参数Cart或Product为空！");
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<CartProduct> cartProducts = cart.getProducts();
		for (CartProduct cp : cartProducts) {
			if (cp.getProduct().getId() == product.getId()) {
				if (cp.getNum() < MAX_CARTPRODUCT_NUM) {
					cp.setNum(num);
				}
				baseDao.merge(cp);
				return;
			}
		}
	}

	@Override
	public void deleteCartProduct(Cart cart, Product product) {
		try {
			if (cart.getId() == 0 || product.getId() == 0)
				throw new Exception("传入参数Cart或Product为空！");
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<CartProduct> cartProducts = cart.getProducts();
		for (CartProduct cp : cartProducts) {
			if (cp.getProduct().getId() == product.getId()) {
				cart.getProducts().remove(cp);
				baseDao.remove(cp);
				return;
			}
		}
	}

	/**
	 * 清空购物车
	 */
	@Override
	public void clearCart(Cart cart) {
		List<CartProduct> cartProducts = cart.getProducts();
		for (CartProduct cp : cartProducts)
			baseDao.remove(cp);
		cartProducts.clear();
	}

}

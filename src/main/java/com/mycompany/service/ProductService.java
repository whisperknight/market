package com.mycompany.service;

import java.util.List;

import com.mycompany.entity.PageBean;
import com.mycompany.entity.Product;
import com.mycompany.entity.SmallType;

public interface ProductService {
	
	public List<Product> getProducts(Product s_product);
	
	public List<Product> getProducts(Product s_product, PageBean pageBean);
	
	public Long getProductCount(Product s_product);
	
	public Product getProductById(int id);
	
	public void saveProduct(Product product);
	
	public void deleteProduct(Product product);
	
	public boolean existProductWithSmallType(SmallType smallType);
}

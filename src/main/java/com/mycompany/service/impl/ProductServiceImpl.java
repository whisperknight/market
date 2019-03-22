package com.mycompany.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.mycompany.dao.BaseDao;
import com.mycompany.entity.PageBean;
import com.mycompany.entity.Product;
import com.mycompany.entity.SmallType;
import com.mycompany.service.ProductService;
import com.mycompany.util.StringUtil;

@Service("productService")
@Transactional
public class ProductServiceImpl implements ProductService {

	@Resource
	private BaseDao<Product> baseDao;

	@Override
	public List<Product> getProducts(Product s_product) {
		StringBuffer jpql = new StringBuffer("from Product");
		List<Object> params = new ArrayList<>();
		if (s_product != null) {
			if (s_product.getBigType() != null) {
				jpql.append(" and bigType.id = ?1");
				params.add(s_product.getBigType().getId());
			} else if (s_product.getSmallType() != null) {
				jpql.append(" and smallType.id = ?1");
				params.add(s_product.getSmallType().getId());
			}
		}
		return baseDao.query(jpql.toString().replaceFirst("and", "where"), Product.class,
				params.toArray());
	}
	
	@Override
	public List<Product> getProducts(Product s_product, PageBean pageBean) {
		StringBuffer jpql = new StringBuffer("from Product");
		List<Object> params = new ArrayList<>();
		if (s_product != null) {
			if (s_product.getBigType() != null) {
				jpql.append(" and bigType.id = ?1");
				params.add(s_product.getBigType().getId());
			} else if (s_product.getSmallType() != null) {
				jpql.append(" and smallType.id = ?1");
				params.add(s_product.getSmallType().getId());
			} else if (StringUtil.isNotEmpty(s_product.getName())) {
				jpql.append(" and name like ?1");
				params.add("%" + s_product.getName() + "%");
			} else if (s_product.isHot()) {
				jpql.append(" and UNIX_TIMESTAMP(current_timestamp()) "
						+ "< UNIX_TIMESTAMP(hotEndTime) and hot=true order by hotStartTime desc");
			} else if (s_product.isOnSale()) {
				jpql.append(" and UNIX_TIMESTAMP(current_timestamp()) "
						+ "< UNIX_TIMESTAMP(onSaleEndTime) and onSale=true order by onSaleStartTime desc");
			} else
				jpql.append(
						" order by hot desc,hotStartTime desc, onSale desc,onSaleStartTime desc");
		} else
			jpql.append(" order by hot desc,hotStartTime desc, onSale desc,onSaleStartTime desc");

		if (pageBean != null)
			return baseDao.query(jpql.toString().replaceFirst("and", "where"), Product.class,
					pageBean, params.toArray());
		else
			return null;
	}

	@Override
	public Long getProductCount(Product s_product) {
		StringBuffer jpql = new StringBuffer("select count(*) from Product");
		List<Object> params = new ArrayList<>();
		if (s_product != null) {
			if (s_product.getBigType() != null) {
				jpql.append(" and bigType.id = ?1");
				params.add(s_product.getBigType().getId());
			} else if (s_product.getSmallType() != null) {
				jpql.append(" and smallType.id = ?1");
				params.add(s_product.getSmallType().getId());
			} else if (StringUtil.isNotEmpty(s_product.getName())) {
				jpql.append(" and name like ?1");
				params.add("%" + s_product.getName() + "%");
			} else if (s_product.isHot()) {
				jpql.append(" and UNIX_TIMESTAMP(current_timestamp()) "
						+ "< UNIX_TIMESTAMP(hotEndTime) and hot=true order by hotStartTime desc");
			} else if (s_product.isOnSale()) {
				jpql.append(" and UNIX_TIMESTAMP(current_timestamp()) "
						+ "< UNIX_TIMESTAMP(onSaleEndTime) and onSale=true order by onSaleStartTime desc");
			}
		}
		return baseDao.count(jpql.toString().replaceFirst("and", "where"), params.toArray());
	}

	@Override
	public Product getProductById(int id) {
		return baseDao.find(Product.class, id);
	}

	@Override
	public void saveProduct(Product product) {
		if (product.getId() != 0) {
			baseDao.merge(product);
		} else {
			baseDao.persist(product);
		}
	}

	@Override
	public void deleteProduct(Product product) {
		if (product != null && product.getId() != 0)
			baseDao.remove(product);
	}

	@Override
	public boolean existProductWithSmallType(SmallType smallType) {
		List<Product> list = baseDao.query("from Product where smallType.id = ?1", Product.class,
				smallType.getId());
		if (list.size() > 0)
			return true;
		else
			return false;
	}
}

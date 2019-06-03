package com.turvo.flashsale.dao;

import java.io.IOException;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.aspectj.apache.bcel.ExceptionConstants;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.turvo.flashsale.bean.ExceptionDate;
import com.turvo.flashsale.bean.Product;
import com.turvo.flashsale.bean.SoldProduct;
import com.turvo.flashsale.form.ProductForm;
import com.turvo.flashsale.pagination.PaginationResult;

@Transactional
@Repository
public class ProductDAO {

	@Autowired
	private EntityManager entityManager;

	public Product findProduct(String code) {
		try {
			String sql = "Select e from " + Product.class.getName() + " e Where e.code =:code ";

			Session session = this.entityManager.unwrap(Session.class);
			Query query = session.createQuery(sql);
			query.setParameter("code", code);
			return query.list() == null || query.list().isEmpty() ? null : (Product) query.list().get(0);
		} catch (NoResultException e) {
			return null;
		}
	}

	public SoldProduct findSoldQuantity(Date date, String code) {
		try {
			String sql = "Select e from " + SoldProduct.class.getName() + " e Where e.soldDate =:date "
					+ " and e.id=:code";

			Session session = this.entityManager.unwrap(Session.class);
			Query query = session.createQuery(sql);
			query.setParameter("date", date);
			query.setParameter("code", code);
			return (query.list() == null || query.list().isEmpty()) ? null : (SoldProduct) query.list().get(0);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void save(ProductForm productForm) {

		Session session = this.entityManager.unwrap(Session.class);
		String code = productForm.getCode();

		Product product = null;

		boolean isNew = false;
		if (code != null) {
			product = this.findProduct(code);
		}
		if (product == null) {
			isNew = true;
			product = new Product();
			product.setCreateDate(new Date());
		}
		product.setCode(code);
		product.setName(productForm.getName());
		product.setPrice(productForm.getPrice());

		if (productForm.getFileData() != null) {
			byte[] image = null;
			try {
				image = productForm.getFileData().getBytes();
			} catch (IOException e) {
			}
			if (image != null && image.length > 0) {
				product.setImage(image);
			}
		}
		if (isNew) {
			session.persist(product);
		}
		// If error in DB, Exceptions will be thrown out immediately
		session.flush();
	}

	public PaginationResult<Product> queryProducts(int page, int maxResult, int maxNavigationPage, String saleType) {
		String sql = "Select p from " + Product.class.getName() + " p ";

		sql += " Where p.saleType =:saleType";
		sql += " order by p.createDate desc ";
		//
		Session session = this.entityManager.unwrap(Session.class);
		Query query = session.createQuery(sql);

		query.setParameter("saleType", saleType);
		return new PaginationResult<Product>(query, page, maxResult, maxNavigationPage);
	}

	public Boolean findIfExceptionDate(Date date) {
		try {
			String sql = "Select e from " + ExceptionDate.class.getName() + " e Where e.starttime <=:date and"
					+ " e.endtime >=:date";

			Session session = this.entityManager.unwrap(Session.class);
			Query query = session.createQuery(sql);
			query.setParameter("date", date);
			return query.list() == null || query.list().isEmpty() ? null : true;
		} catch (NoResultException e) {
			return null;
		}
	}

}
package com.primeton.manage.employee.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.primeton.manage.employee.service.PageQuery;

public final class DataHelper {

	private DataHelper() { }

	public static Pageable toPageable(PageQuery query) {
		return new PageRequest(query.getPage() - 1, query.getSize());
	}

//	public static Pageable toPageable(PageQuery query, String sort) {
//		return new PageRequest(query.getPage() - 1, query.getSize(),
//				new Sort(query.isAsc() ? Direction.ASC : Direction.DESC, sort));
//	}
//
//	public static Pageable toPageable(PageQuery query, Sort sort) {
//		return new PageRequest(query.getPage() - 1, query.getSize(), sort);
//	}
//
//	public static long count(EntityManager em, String countSql, Consumer<Query> countQueryPreparer) {
//		Query countQuery = em.createNativeQuery(countSql);
//		if (countQueryPreparer != null)
//			countQueryPreparer.accept(countQuery);
//		return ((Number) countQuery.getSingleResult()).longValue();
//	}
//
//	public static <T> Page<T> query(EntityManager em, Class<T> resultClass, String countSql,
//			String listSql, Pageable pageable, Object... params) {
//		return query(em, resultClass, countSql, listSql, pageable, query -> setParameters(query, params));
//	}
//
//	public static <T> Page<T> query(EntityManager em, Class<T> resultClass, String countSql,
//			String listSql, Pageable pageable, List<?> params) {
//		return query(em, resultClass, countSql, listSql, pageable, query -> setParameters(query, params));
//	}
//
//	public static <T> Page<T> query(EntityManager em, Class<T> resultClass, String countSql,
//			String listSql, Pageable pageable, Consumer<Query> queryPreparer) {
//		return query(em, resultClass, countSql, queryPreparer, listSql, queryPreparer, pageable);
//	}
//
//	public static <T> Page<T> query(EntityManager em, String resultSetMapping, String countSql,
//			String listSql, Pageable pageable, Consumer<Query> queryPreparer) {
//		return query(em, resultSetMapping, countSql, queryPreparer, listSql, queryPreparer, pageable);
//	}
//
//	public static <T> Page<T> query(EntityManager em, Class<T> resultClass, String countSql, Consumer<Query> countQueryPreparer,
//			String listSql, Consumer<Query> listQueryPreparer, Pageable pageable) {
//		return query0(em, resultClass, null, countSql, countQueryPreparer, listSql, listQueryPreparer, pageable);
//	}
//
//	public static <T> Page<T> query(EntityManager em, String resultSetMapping, String countSql, Consumer<Query> countQueryPreparer,
//			String listSql, Consumer<Query> listQueryPreparer, Pageable pageable) {
//		return query0(em, null, resultSetMapping, countSql, countQueryPreparer, listSql, listQueryPreparer, pageable);
//	}
//
//	public static <T> List<T> query(EntityManager em, Class<T> resultClass, String sql, Object... params) {
//		return query0(em, resultClass, null, sql, query -> setParameters(query, params));
//	}
//
//	public static <T> List<T> query(EntityManager em, Class<T> resultClass, String sql, List<?> params) {
//		return query0(em, resultClass, null, sql, query -> setParameters(query, params));
//	}
//
//	public static <T> List<T> query(EntityManager em, Class<T> resultClass, String sql, Consumer<Query> queryPreparer) {
//		return query0(em, resultClass, null, sql, queryPreparer);
//	}
//
//	public static <T> List<T> query(EntityManager em, String resultSetMapping, String sql, Object... params) {
//		return query0(em, null, resultSetMapping, sql, query -> setParameters(query, params));
//	}
//
//	public static <T> List<T> query(EntityManager em, String resultSetMapping, String sql, List<?> params) {
//		return query0(em, null, resultSetMapping, sql, query -> setParameters(query, params));
//	}
//
//	public static <T> List<T> query(EntityManager em, String resultSetMapping, String sql, Consumer<Query> queryPreparer) {
//		return query0(em, null, resultSetMapping, sql, queryPreparer);
//	}
//
//	@SuppressWarnings("unchecked")
//	private static <T> List<T> query0(EntityManager em, Class<T> resultClass, String resultSetMapping, String sql, Consumer<Query> queryPreparer) {
//		Query listQuery;
//		if (resultClass != null)
//			listQuery = em.createNativeQuery(sql, resultClass);
//		else if (resultSetMapping != null)
//			listQuery = em.createNativeQuery(sql, resultSetMapping);
//		else
//			listQuery = em.createNativeQuery(sql);
//		if (queryPreparer != null)
//			queryPreparer.accept(listQuery);
//		return listQuery.getResultList();
//	}
//
//	private static <T> Page<T> query0(EntityManager em, Class<T> resultClass, String resultSetMapping,
//			String countSql, Consumer<Query> countQueryPreparer,
//			String listSql, Consumer<Query> listQueryPreparer, Pageable pageable) {
//		long total = count(em, countSql, countQueryPreparer);
//
//		List<T> content;
//		if (total > 0) {
//			String pagingSql = new StringBuilder(listSql)
//					.append(" limit ").append(pageable.getPageSize())
//					.append(" offset ").append(pageable.getOffset())
//					.toString();
//			content = query0(em, resultClass, resultSetMapping, pagingSql, listQueryPreparer);
//		} else {
//			content = Collections.emptyList();
//		}
//
//		return new org.springframework.data.domain.PageImpl<>(content, pageable, total);
//	}
//
//	private static void setParameters(Query query, Object[] params) {
//		if (params == null)
//			return;
//		for (int i = 0; i < params.length; i++) {
//			query.setParameter(i + 1, params[i]);
//		}
//	}
//
//	private static void setParameters(Query query, List<?> params) {
//		if (params == null)
//			return;
//		int i = 0;
//		for (Object param : params) {
//			i += 1;
//			query.setParameter(i, param);
//		}
//	}

}

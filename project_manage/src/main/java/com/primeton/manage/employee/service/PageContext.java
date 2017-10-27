package com.primeton.manage.employee.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * 分页上下文，在分页列表中可用于保存重复的数据项目（如用户、地区、大纲等），以达到精简分页数据的目的。
 * 
 * <p>
 * 上下文中以 <code>key</code> -> <code>values (Set&lt;Object&gt;)</code> 形式保存对象。
 * </p>
 * <p>
 * 考虑到大多数应用场景下，分页列表不会涉及多线程访问，因此本类并不是线程安全的。
 * </p>
 */
public class PageContext implements Map<String, Object> {

	private final Map<String, Object> map;

	public PageContext() {
		this(new HashMap<String, Object>());
	}

	public PageContext(Map<String, Object> map) {
		this.map = map;
	}

	/**
	 * 添加一个对象。
	 * 
	 * <ul>
	 * <li>如果 <code>value</code> 为 <code>null</code>，什么也不会发生</li>
	 * <li>如果 <code>key</code> 不存在，将创建新的集合</li>
	 * <li>如果 <code>key</code> 已存在，将添加到已有的集合中</li>
	 * </ul>
	 * 
	 * @param key 
	 * @param value
	 */
	public void addValue(String key, Object value) {
		if (value == null)
			return;
		Collection<Object> items = findOrNew(key);
		items.add(value);
	}

	/**
	 * 添加一组对象。
	 * 
	 * <ul>
	 * <li>如果 <code>values</code> 为 <code>null</code> 或空数组，什么也不会发生</li>
	 * <li>如果 <code>key</code> 不存在，将创建新的集合</li>
	 * <li>如果 <code>key</code> 已存在，将添加到已有的集合中</li>
	 * </ul>
	 * 
	 * @param key
	 * @param values
	 */
	public void addValue(String key, Object... values) {
		if (values == null || values.length == 0)
			return;
		addValues(key, Arrays.asList(values));
	}

	/**
	 * 添加一组对象。
	 * 
	 * <ul>
	 * <li>如果 <code>values</code> 为 <code>null</code>，什么也不会发生</li>
	 * <li>如果 <code>key</code> 不存在，将创建新的集合</li>
	 * <li>如果 <code>key</code> 已存在，将添加到已有的集合中</li>
	 * </ul>
	 * 
	 * @param key
	 * @param values
	 */
	public <T> void addValues(String key, Iterable<T> values) {
		if (values == null)
			return;
		addValues(key, values.iterator());
	}

	/**
	 * 添加一组对象。
	 * 
	 * <ul>
	 * <li>如果 <code>values</code> 为 <code>null</code>，什么也不会发生</li>
	 * <li>如果 <code>key</code> 不存在，将创建新的集合</li>
	 * <li>如果 <code>key</code> 已存在，将添加到已有的集合中</li>
	 * </ul>
	 * 
	 * @param key
	 * @param values
	 */
	public <T> void addValues(String key, Iterator<T> values) {
		if (values == null || !values.hasNext())
			return;
		Collection<T> items = findOrNew(key);
		do {
			T value = values.next();
			if (value != null)
				items.add(value);
		} while (values.hasNext());
	}

	public <S, T> void addValues(String key, Collection<S> col, Function<S, T> func) {
		if (col == null || col.isEmpty())
			return;
		addValues(key, col.iterator(), func);
	}

	public <S, T> void addValues(String key, Iterator<S> it, Function<S, T> func) {
		if (it == null || !it.hasNext())
			return;
		Collection<T> items = findOrNew(key);
		do {
			S s = it.next();
			T value = func.apply(s);
			if (value != null)
				items.add(value);
		} while (it.hasNext());
	}

	@SuppressWarnings("unchecked")
	private <T> Collection<T> findOrNew(String key) {
		Collection<T> items = (Collection<T>) map.get(key);
		if (items == null) {
			items = new HashSet<>();
			map.put(key, items);
		}
		return items;
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	@Override
	public Object get(Object key) {
		return map.get(key);
	}

	@Override
	public Object put(String key, Object value) {
		return map.put(key, value);
	}

	@Override
	public Object remove(Object key) {
		return map.remove(key);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		map.putAll(m);
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public Set<String> keySet() {
		return map.keySet();
	}

	@Override
	public Collection<Object> values() {
		return map.values();
	}

	@Override
	public Set<Entry<String, Object>> entrySet() {
		return map.entrySet();
	}

}

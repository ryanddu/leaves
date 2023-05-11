
package io.github.ryanddu.leaves.base.core.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 键值对对象
 *
 * @author: ryan
 * @date: 2023/4/11 17:56
 **/
public class Kv extends HashMap {

	public Kv() {
	}

	public static Kv by(Object key, Object value) {
		return (new Kv()).set(key, value);
	}

	public static Kv create() {
		return new Kv();
	}

	public Kv set(Object key, Object value) {
		super.put(key, value);
		return this;
	}

	public Kv setIfNotBlank(Object key, String value) {
		if (StringUtils.isNotEmpty(value)) {
			this.set(key, value);
		}

		return this;
	}

	public Kv setIfNotNull(Object key, Object value) {
		if (value != null) {
			this.set(key, value);
		}

		return this;
	}

	public Kv set(Map map) {
		super.putAll(map);
		return this;
	}

	public Kv set(Kv kv) {
		super.putAll(kv);
		return this;
	}

	public Kv delete(Object key) {
		super.remove(key);
		return this;
	}

	public String getStr(Object key) {
		Object s = this.get(key);
		return s != null ? s.toString() : null;
	}

	public Integer getInt(Object key) {
		Number n = (Number) this.get(key);
		return n != null ? n.intValue() : null;
	}

	public Long getLong(Object key) {
		Number n = (Number) this.get(key);
		return n != null ? n.longValue() : null;
	}

	public Number getNumber(Object key) {
		return (Number) this.get(key);
	}

	public Boolean getBoolean(Object key) {
		return (Boolean) this.get(key);
	}

	public boolean notNull(Object key) {
		return this.get(key) != null;
	}

	public boolean isNull(Object key) {
		return this.get(key) == null;
	}

	public boolean isTrue(Object key) {
		Object value = this.get(key);
		return value instanceof Boolean && (Boolean) value;
	}

	public boolean isFalse(Object key) {
		Object value = this.get(key);
		return value instanceof Boolean && !(Boolean) value;
	}

	public String toJson() {
		return JacksonUtils.mapToJson(this);
	}

	public boolean equals(Object kv) {
		return kv instanceof Kv && super.equals(kv);
	}

}

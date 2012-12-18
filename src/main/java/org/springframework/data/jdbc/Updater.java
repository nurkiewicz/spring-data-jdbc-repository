package org.springframework.data.jdbc;

import java.util.Map;

public interface Updater<T> {
	Map<String, Object> mapColumns(T t);
}


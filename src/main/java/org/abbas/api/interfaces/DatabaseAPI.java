package org.abbas.api.interfaces;

import java.util.List;
import java.util.Map;

public interface DatabaseAPI {

    void execute(String sql, Object... params);
    <T> T query(String sql, ResultMapper<T> mapper, Object... parameters);
    List<Map<String, Object>> query(String sql, Object... parameters);
    int update(String sql, Object... parameters);
    void close();
}

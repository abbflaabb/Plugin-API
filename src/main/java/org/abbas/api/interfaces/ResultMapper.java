package org.abbas.api.interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;
@FunctionalInterface
public interface ResultMapper<T> {
    T map(ResultSet rs) throws SQLException;
}

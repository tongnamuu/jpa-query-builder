package persistence.sql.dialect;

import persistence.sql.dml.insert.InsertQuery;

public interface Dialect {
    String insertBuilder(InsertQuery insertQuery);
}

package persistence.sql.ddl.assembler;

import persistence.sql.ddl.DataDefinitionLanguageGenerator;
import persistence.sql.ddl.TableCreator;
import persistence.sql.ddl.TableRemover;
import persistence.sql.ddl.vo.DatabaseField;

public class DataDefinitionLanguageAssembler {
    private final DataDefinitionLanguageGenerator dataDefinitionLanguageGenerator;

    public DataDefinitionLanguageAssembler(DataDefinitionLanguageGenerator dataDefinitionLanguageGenerator) {
        this.dataDefinitionLanguageGenerator = dataDefinitionLanguageGenerator;
    }

    public String assembleCreateTableQuery(Class<?> cls) {
        TableCreator tableCreator = dataDefinitionLanguageGenerator.generateTableCreatorWithClass(cls);
        StringBuilder sb = new StringBuilder();
        sb.append("create table ");
        sb.append(tableCreator.getTableName());
        sb.append('(');
        sb.append(System.lineSeparator());
        int fieldSize = tableCreator.getFields().getDatabaseFields().size();
        for (int i = 0; i < fieldSize; i++) {
            fillQueryWithField(sb, tableCreator.getFields().getDatabaseFields().get(i), i == fieldSize - 1);
        }
        sb.append(");");
        return sb.toString();
    }

    public String assembleDropTableQuery(Class<?> cls) {
        TableRemover tableRemover = dataDefinitionLanguageGenerator.generateTableRemoverWithClass(cls);
        StringBuilder sb = new StringBuilder();
        sb.append("drop table");
        sb.append(" ");
        sb.append(tableRemover.getTableName());
        sb.append(" if exists;");
        return sb.toString();
    }

    private void fillQueryWithField(StringBuilder sb, DatabaseField databaseField, boolean isLast) {
        sb.append(databaseField);
        if(databaseField.isPrimary()) {
            sb.append(" ");
            sb.append(getPrimaryKeyStrategy(databaseField));
        }
        if (!isLast) {
            sb.append(',');
        }
        sb.append(System.lineSeparator());
    }


    private String getPrimaryKeyStrategy(DatabaseField databaseField) {
        if(databaseField.isPrimary() && databaseField.getPrimaryKeyGenerationType() == null) {
            return "primary key";
        }
        // Todo Mysql 등 database dialect 에 따라 바뀌도록 수정
        return "generated by default as identity";
    }
}

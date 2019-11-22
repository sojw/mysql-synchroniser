package com.jbuncle.mysqlsynchroniser;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import com.jbuncle.mysqlsynchroniser.structure.MySQL;
import com.jbuncle.mysqlsynchroniser.structure.diff.DatabaseDiff;
import com.jbuncle.mysqlsynchroniser.structure.diff.TableDiff;
import com.jbuncle.mysqlsynchroniser.structure.objects.Database;
import com.jbuncle.mysqlsynchroniser.structure.objects.Table;

import lombok.experimental.UtilityClass;

/**
 *
 * @author James Buncle
 */
@UtilityClass
public class ScriptGenerator {

    /**
     * Compare table.
     *
     * @param source the source
     * @param target the target
     * @param table the table
     * @return the list
     * @throws SQLException the SQL exception
     */
    public static List<String> compareTable(
        final DataSource source,
        final DataSource target,
        final String table)
        throws SQLException {

        final Table sourceTable = new MySQL(source).loadTable(table);
        final Table targetTable = new MySQL(target).loadTable(table);

        return TableDiff.diff(sourceTable, targetTable);
    }

    /**
     * Generates a List of MySQL Statements to update/synchronise the given target database structure based on the
     * source database.
     *
     * @param source the datasource used as the source
     * @param target the target datasource to create update statements for
     * @return a list MySQL statements created by comparing the source schema to the target schema
     * @throws SQLException the SQL exception
     */
    public static List<String> compareSchema(
        final DataSource source,
        final DataSource target)
        throws SQLException {

        final Database sourceDatabase = new MySQL(source).loadDatabase();
        final Database targetDatabase = new MySQL(target).loadDatabase();

        final DatabaseDiff diff = new DatabaseDiff(sourceDatabase, targetDatabase);
        return diff.diff(source);
    }
}
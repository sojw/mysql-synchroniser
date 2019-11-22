/*
 * The MIT License
 *
 * Copyright 2016 James Buncle <jbuncle@hotmail.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.jbuncle.mysqlsynchroniser;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.jbuncle.mysqlsynchroniser.connection.ArrayRowMapper;
import com.jbuncle.mysqlsynchroniser.connection.ConnectionStrategy;
import com.jbuncle.mysqlsynchroniser.connection.RowMapper;
import com.mysql.cj.jdbc.MysqlDataSource;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author James Buncle <jbuncle@hotmail.com>
 */
@Slf4j
@Ignore
public class ScriptGeneratorTest {
    private ConnectionStrategy source;
    private ConnectionStrategy target;

    private static MysqlDataSource createDataSource(final String host, final String user, final String password,
        final int port) {
        final MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUser(user);
        dataSource.setPassword(password);
        dataSource.setServerName(host);
        dataSource.setPort(port);
        return dataSource;
    }

    private static MysqlDataSource createDataSource(final String host, final String schema, final String user,
        final String password, final int port) {
        final MysqlDataSource dataSource = createDataSource(host, user, password, port);
        dataSource.setDatabaseName(schema);
        return dataSource;
    }

    @Before
    public void setUp() throws Exception {
        this.source = new ConnectionStrategy(createDataSource("zzz", "zzz", "zz", "zz", 20306));
        this.target = new ConnectionStrategy(createDataSource("zzzz", "zzz", "zz", "zz", 20306));
    }

    //    /**
    //     * Test of compareTable method, of class ScriptGenerator.
    //     *
    //     * @throws java.lang.Exception
    //     */
    //    public void testCompareTable() throws Exception {
    //        System.out.println("compareTable");
    //        source.update("CREATE TABLE pet ("
    //            + "name VARCHAR(20) NOT NULL COMMENT 'Pet name', "
    //            + "owner VARCHAR(20) NULL COMMENT 'The original owner' DEFAULT 'Unknown', "
    //            + "legs INT DEFAULT 4, "
    //            + "species VARCHAR(20), "
    //            + "sex CHAR(1), "
    //            + "birth DATE, "
    //            + "death DATE NOT NULL COMMENT 'Date of death',"
    //            + "CONSTRAINT pk_PersonID PRIMARY KEY (name),"
    //            + "UNIQUE KEY `mykey` (`owner`, `species`),"
    //            + "UNIQUE KEY `updatedkey` (`owner`, `death`)"
    //            + ");");
    //        target.update("CREATE TABLE pet ("
    //            + "name VARCHAR(20) NOT NULL COMMENT 'Pet name' DEFAULT '', "
    //            + "owner VARCHAR(20) COMMENT 'The current owner' DEFAULT 'Unknown', "
    //            + "legs INT, "
    //            + "species VARCHAR(20) NOT NULL, "
    //            + "type VARCHAR(20) NOT NULL, "
    //            + "death DATE COMMENT 'Date of death', "
    //            + "UNIQUE KEY `somekey` (`death`),"
    //            + "UNIQUE KEY `updatedkey` (`owner`)"
    //            + ");");
    //
    //        final String table = "pet";
    //        final List<String> result = ScriptGenerator.compareTable(source.getDataSource(), target.getDataSource(), table);
    ////        target.update(result);
    //
    //        compareQueries("DESCRIBE " + table, false);
    //        compareQueries("SHOW FULL COLUMNS IN " + table, false);
    //        compareQueries("SHOW INDEXES FROM " + table, true);
    //    }

    private void compareQueries(final String query, final boolean ignoreOrder)
        throws SQLException {
        final RowMapper<Object[]> rowMapper = new ArrayRowMapper();
        if (ignoreOrder) {
            final LinkedHashSet<Object[]> sourceData = new LinkedHashSet<>(this.source.query(query, rowMapper));
            final LinkedHashSet<Object[]> targetData = new LinkedHashSet<>(this.target.query(query, rowMapper));
            if (sourceData.size() != targetData.size()) {
                Assert.fail("Results count differs in length");
            }

            for (Object[] objects : targetData) {
                if (!setContainsArray(objects, sourceData)) {
                    Assert.fail("Target data has extra entry: '" + Arrays.toString(objects) + "'");
                }
            }
            for (Object[] objects : sourceData) {
                if (!setContainsArray(objects, targetData)) {
                    Assert.fail("Target data missing entry: '" + Arrays.toString(objects) + "'");
                }
            }
        } else {
            final List<Object[]> sourceData = this.source.query(query, rowMapper);
            final List<Object[]> targetData = this.target.query(query, rowMapper);
            for (int i = 0; i < sourceData.size(); i++) {
                if (!Arrays.equals(sourceData.get(i), targetData.get(i))) {
                    Assert.fail("Source data "
                        + "'" + Arrays.toString(sourceData.get(i)) + "'"
                        + " is not equal to "
                        + "'" + Arrays.toString(targetData.get(i)) + "'");
                }
            }
        }
    }

    private static boolean setContainsArray(final Object[] arr, final Set<Object[]> set) {
        for (final Object[] objects : set) {
            if (Arrays.equals(objects, arr)) {
                return true;
            }
        }
        return false;
    }

    //    /**
    //     * Test of compareSchema method, of class ScriptGenerator.
    //     *
    //     * @throws java.lang.Exception
    //     */
    //    @Test
    //    public void test_compareSchema() throws Exception {
    //        source.update("DROP TABLE table1;");
    //        source.update("DROP TABLE table2;");
    //        source.update("CREATE TABLE table1 (name VARCHAR(20));");
    //        source.update("CREATE TABLE table2 (name VARCHAR(20));");
    //
    //        target.update("DROP TABLE table1;");
    //        target.update("DROP TABLE table3;");
    //        target.update("CREATE TABLE table1 (owner VARCHAR(20));");
    //        target.update("CREATE TABLE table3 (owner VARCHAR(20));");
    //
    //        final List<String> result = ScriptGenerator.compareSchema(source.getDataSource(), target.getDataSource());
    //        log.debug("result : {}", String.join("\n", result));
    //        //        target.update(result);
    //
    //        compareQueries("DESCRIBE table1;", false);
    //        compareQueries("DESCRIBE table2;", false);
    //        compareQueries("SHOW FULL TABLES;", false);
    //
    //        source.update("DROP TABLE table1;");
    //        source.update("DROP TABLE table2;");
    //        target.update("DROP TABLE table1;");
    //        target.update("DROP TABLE table3;");
    //    }

    @Test
    public void compareSchema() throws Exception {
        final List<String> result = ScriptGenerator.compareSchema(source.getDataSource(), target.getDataSource());
        if (CollectionUtils.isNotEmpty(result)) {
            log.debug("result : {}", String.join("\n", result));
        }
    }
}

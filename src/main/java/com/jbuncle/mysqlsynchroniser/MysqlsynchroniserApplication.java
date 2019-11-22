package com.jbuncle.mysqlsynchroniser;

import java.sql.SQLException;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;

/**
 * BuffettBatchApplication
 *
 * @author gun-pyo.park<fp10050@linecorp.com>
 * @since 2018. 08. 08.
 */
@Slf4j
//@SpringBootApplication
public class MysqlsynchroniserApplication {
    //    public static void main(final String[] args) throws SQLException {
    //        SpringApplication springApplication = new SpringApplication(MysqlsynchroniserApplication.class);
    //        springApplication.setWebApplicationType(WebApplicationType.NONE);
    //
    //        DBSchemaComparator db = new DBSchemaComparator();
    //        db.compareSchema();
    //
    //        System.exit(SpringApplication.exit(springApplication.run()));
    //    }

    public static void main(final String[] args) throws SQLException {
        log.info("MysqlsynchroniserApplication start.");

        String propertiyFileName = "application-beta.properties";
        if (Objects.nonNull(args) && args.length > 0) {
            propertiyFileName = args[0];
        }

        log.info("propertiyFileName: {}", propertiyFileName);
        DBSchemaComparator db = new DBSchemaComparator(propertiyFileName);
        db.compareSchema();
    }
}
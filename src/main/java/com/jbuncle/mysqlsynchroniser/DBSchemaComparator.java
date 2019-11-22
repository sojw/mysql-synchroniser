package com.jbuncle.mysqlsynchroniser;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.jbuncle.mysqlsynchroniser.connection.ConnectionStrategy;
import com.jbuncle.mysqlsynchroniser.util.MysqlSyncConstants;
import com.mysql.cj.jdbc.MysqlDataSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DBSchemaComparator {
    private ConnectionStrategy source;
    private ConnectionStrategy target;
    private Properties properties = new Properties();

    public DBSchemaComparator(final String propertiyFileName) {

        try (InputStream inputStream = getClass().getResourceAsStream("/" + propertiyFileName)) {
            properties.load(inputStream);
        } catch (IOException e) {
            log.error("", e, e);
            System.exit(-1);
        }
        log.info("properties loaded.");

        this.source = new ConnectionStrategy(createDataSource(properties.getProperty("source.host"),
            properties.getProperty("source.schema"), properties.getProperty("source.user"),
            properties.getProperty("source.password"), Integer.parseInt(properties.getProperty("source.port"))));
        log.info("source datasource loaded.");

        this.target = new ConnectionStrategy(createDataSource(properties.getProperty("target.host"),
            properties.getProperty("target.schema"), properties.getProperty("target.user"),
            properties.getProperty("target.password"), Integer.parseInt(properties.getProperty("target.port"))));
        log.info("target datasource loaded.");
    }

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

    public void compareSchema() throws SQLException {
        final List<String> result = ScriptGenerator.compareSchema(source.getDataSource(), target.getDataSource());
        if (CollectionUtils.isNotEmpty(result)) {
            final String contents = String.join("\n", result);
            log.debug("\n{}", contents);
            sendEmail(contents);
        }
    }

    private void sendEmail(final String contents) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(MysqlSyncConstants.HOST);
        mailSender.setPort(MysqlSyncConstants.PORT);

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(MysqlSyncConstants.MAIL_FROM);
        msg.setTo(MysqlSyncConstants.MAIL_TO);
        msg.setSubject(MysqlSyncConstants.MAIL_SUBJECT);
        msg.setText(contents);

        mailSender.send(msg);
    }
}
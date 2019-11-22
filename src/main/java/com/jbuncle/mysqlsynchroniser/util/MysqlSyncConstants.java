package com.jbuncle.mysqlsynchroniser.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MysqlSyncConstants {
    public static final String HOST = "smtp.com";
    public static final int PORT = 25;

    public static final String MAIL_FROM = "dummy@dummy-test.com";
    public static final String[] MAIL_TO = {"dummy1@dummy-test.com", "dummy2@dummy-test.com"};
    public static final String MAIL_SUBJECT = "Warning, change occurred, DB schema.";
}

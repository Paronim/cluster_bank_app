package com.don.bank.config.hibernate;

import org.hibernate.resource.jdbc.spi.StatementInspector;

public class StatusQueryInterceptor implements StatementInspector {

    @Override
    public String inspect(String sql) {
        sql = sql.trim();

        if (sql.toLowerCase().startsWith("select")) {

            String lowerSql = sql.toLowerCase();
            int orderIndex = lowerSql.indexOf(" order by");
            int groupIndex = lowerSql.indexOf(" group by");
            int limitIndex = lowerSql.indexOf(" limit");

            int splitIndex = sql.length();

            if (orderIndex != -1) splitIndex = Math.min(splitIndex, orderIndex);
            if (groupIndex != -1) splitIndex = Math.min(splitIndex, groupIndex);
            if (limitIndex != -1) splitIndex = Math.min(splitIndex, limitIndex);

            String mainPart = sql.substring(0, splitIndex).trim();
            String endingPart = sql.substring(splitIndex).trim();

            if (mainPart.toLowerCase().contains("where")) {
                mainPart = mainPart.replaceFirst("(?i)where", "WHERE (") + ") AND status = 'active'";
            } else {
                mainPart = mainPart + " WHERE status = 'active'";
            }

            sql = mainPart + " " + endingPart;
        }

        return sql;
    }
}

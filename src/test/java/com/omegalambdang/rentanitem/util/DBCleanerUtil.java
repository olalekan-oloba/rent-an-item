package com.omegalambdang.rentanitem.util;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class DBCleanerUtil {
    @Autowired
    JdbcTemplate jdbcTemplate;
    public static void clearDb(JdbcTemplate jdbcTemplate){

        List<String> tables = List.of(

        );

        tables=toLowerCase(tables);

        String[] excludes={};
        List<String> excludesList = Arrays.asList(excludes);
        if (CollectionUtils.isNotEmpty(excludesList)) {
            tables.removeAll(excludesList);
        }
        String[] tablesArr = new String[tables.size()];
        tablesArr = tables.toArray(tablesArr);

         JdbcTestUtils.deleteFromTables(jdbcTemplate,
                tablesArr);
    }

    private static List<String> toLowerCase(List<String> tables) {
      return   tables.stream().map(String::toLowerCase).collect(Collectors.toList());
    }
}

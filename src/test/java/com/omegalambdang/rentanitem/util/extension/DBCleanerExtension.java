package com.omegalambdang.rentanitem.util.extension;

import com.omegalambdang.rentanitem.util.DBCleanerUtil;
import org.junit.jupiter.api.extension.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class DBCleanerExtension implements BeforeAllCallback, AfterAllCallback, BeforeEachCallback, AfterEachCallback {


    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        DBCleanerUtil.clearDb(SpringExtension.getApplicationContext(extensionContext).getBean(JdbcTemplate.class));
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {

    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        DBCleanerUtil.clearDb(SpringExtension.getApplicationContext(extensionContext).getBean(JdbcTemplate.class));
    }

}

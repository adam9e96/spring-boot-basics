package com.adam9e96.chapter054querydslsearch;


import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootTest
@Log4j2
public class DataSourceTest {
    @Autowired
    private DataSource dataSource;

    @Test
    public void testDataSource() throws SQLException {
        @Cleanup Connection connection = dataSource.getConnection();


        log.info("connection 테스트 ");
        log.info(connection.toString());
        Assertions.assertNotNull(connection);

    }
}

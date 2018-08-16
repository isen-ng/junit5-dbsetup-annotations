package com.iseninc.junit5;

import com.ninja_squad.dbsetup.operation.Operation;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.NotExtensible;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static com.ninja_squad.dbsetup.Operations.deleteAllFrom;
import static com.ninja_squad.dbsetup.Operations.insertInto;
import static org.assertj.core.api.Assertions.assertThat;

@DbSetup
class DbSetupTest {
    @DbSetupSourceFactory
    private static final DataSource DATA_SOURCE;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:mem:TestAppService;MODE=MYSQL;IGNORECASE=TRUE;INIT=RUNSCRIPT FROM 'classpath:h2-database-create.sql'");
        DATA_SOURCE = new HikariDataSource(config);
    }

    @DbSetupOperation
    Operation deleteAll() {
        return deleteAllFrom("My_Table");
    }

    @DbSetupOperation
    Operation insert = insertInto("My_Table").columns("primary_key", "my_value").values(1, "2").build();

    @Test
    void shouldHave1RowAtStart() throws Exception {
        // arrange
        Connection connection = DATA_SOURCE.getConnection();
        Statement statement = connection.createStatement();

        // act
        boolean result = statement.execute("select count(*) as Total from My_Table");
        ResultSet resultSet = statement.getResultSet();

        // assert
        assertThat(result).isTrue();

        resultSet.next();
        assertThat(resultSet.getInt("total")).isEqualTo(1);
    }

    @Nested
    class Inner {
        @DbSetupOperation
        Operation insert = insertInto("My_Table").columns("primary_key", "my_value").values(2, "3").build();

        @Test
        void shouldHave2RowsAfter() throws Exception {
            // arrange
            Connection connection = DATA_SOURCE.getConnection();
            Statement statement = connection.createStatement();

            // act
            boolean result = statement.execute("select count(*) as Total from My_Table");
            ResultSet resultSet = statement.getResultSet();

            // assert
            assertThat(result).isTrue();

            resultSet.next();
            assertThat(resultSet.getInt("total")).isEqualTo(2);
        }
    }
}
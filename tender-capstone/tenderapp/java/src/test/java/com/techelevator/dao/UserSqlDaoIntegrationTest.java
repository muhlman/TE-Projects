package com.techelevator.dao;

import com.techelevator.model.User;
import com.techelevator.model.UserDetails;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class UserSqlDaoIntegrationTest extends DAOIntegrationTest {

    private UserSqlDAO userSqlDAO;

    @Before
    public void setup() {
        DataSource dataSource = this.getDataSource();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Test
    public void createNewUser() {
    	
      /*  boolean userCreated = userSqlDAO.create("TEST_USER","test_password","user", "test", "user", "user@user.com");
        Assert.assertTrue(userCreated);
        User user = userSqlDAO.findByUsername("TEST_USER");
        Assert.assertEquals("TEST_USER", user.getUsername());*/
    } 

}

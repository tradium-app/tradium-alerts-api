package com.tradiumapp.swingtradealerts.query

import com.tradiumapp.swingtradealerts.auth.BuiltInRoleDefinitions
import com.tradiumapp.swingtradealerts.models.Stock
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;

/// still auth context not working

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WatchListQueryTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private WatchListQuery watchListQuery

    @Test(groups = "integration")
//    @WithMockUser(username = "Y2jSiFgU01PZvZDJpL0KLNfdxqG3", roles = ["ROLE_USER"], authorities = ["permission.watchlist.admin"])
    public void testWatchListQuery() {
//        Authentication auth = Mockito.mock(Authentication.class);
//        auth.authorities.add(BuiltInRoleDefinitions.getAuthoritiesForRole("ROLE_USER"))
//        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
//        Mockito.when(securityContext.getAuthentication()).thenReturn(auth);
//        SecurityContextHolder.setContext(securityContext);

        List<Stock> watchList = watchListQuery.getWatchList()

        assertTrue(watchList.size() > 1)
    }
}
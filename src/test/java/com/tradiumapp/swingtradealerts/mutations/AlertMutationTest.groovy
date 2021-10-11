package com.tradiumapp.swingtradealerts.mutations

import com.tradiumapp.swingtradealerts.auth.BuiltInRoleDefinitions
import com.tradiumapp.swingtradealerts.auth.firebase.FirebaseAuthenticationProvider
import com.tradiumapp.swingtradealerts.models.Alert
import com.tradiumapp.swingtradealerts.models.Response
import com.tradiumapp.swingtradealerts.repositories.AlertRepository
import org.junit.Assert
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.context.support.WithSecurityContext
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.springframework.test.context.web.WebAppConfiguration
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

@DataMongoTest
@WebAppConfiguration
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AlertMutationTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private AlertMutation alertMutation

    @Autowired
    private AlertRepository alertRepository

    @BeforeClass
    public void setUp() {
//        Authentication auth = Mockito.mock(Authentication.class);
//        auth.authorities.add(BuiltInRoleDefinitions.getAuthoritiesForRole("ROLE_USER"))
//        auth.setAuthenticated(true);
//
//        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
////        securityContext.setAuthentication(auth);
//
//        Mockito.when(securityContext.getAuthentication()).thenReturn(auth);
////        SecurityContextHolder.getContext().setAuthentication(auth);
//        SecurityContextHolder.setContext(securityContext);
    }

    @Test(groups = "integration")
//    @WithMockUser(username = "testUser", roles = ["ROLE_USER"], authorities = ["permission.alert.admin"])
    @WithUserDetails("syuraj@gmail.com")
    public void updateAlertTest() {
        Alert alert = new Alert();
        alert.status = Alert.AlertStatus.Off;
        alert.title = "test alert";
        Response response = alertMutation.updateAlert()

        Assert.assertTrue(response.success)

        Alert savedAlert = alertRepository.find();
        Assert.assertEquals(savedAlert.status, Alert.AlertStatus.Off)
        Assert.assertEquals(savedAlert.title, "test alert")
    }

}

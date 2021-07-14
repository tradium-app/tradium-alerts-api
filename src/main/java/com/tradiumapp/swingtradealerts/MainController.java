package com.tradiumapp.swingtradealerts;

import com.rollbar.notifier.Rollbar;
import graphql.GraphQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class MainController {
    @Autowired
    private static Rollbar rollbar;

    private GraphQL graphQL;

    @Autowired
    private SampleClass sampleClass;

//    private GraphQlUtility graphQlUtility;

    //    @Autowired
//    MainController(GraphQlUtility graphQlUtility) throws IOException {
//        this.graphQlUtility = graphQlUtility;
//        graphQL = graphQlUtility.createGraphQlObject();
//    }

    MainController() throws IOException {
        System.out.println("sssssssss-sss");
    }

    @GetMapping("/query")
    public ResponseEntity query() {
        sampleClass.doSomething();
//        rollbar.debug("some msg from controller");
        return ResponseEntity.ok().build();
//        ExecutionResult result = graphQL.execute(query);
//        System.out.println("errors: "+result.getErrors());
//        return ResponseEntity.ok(result.getData());
    }
}

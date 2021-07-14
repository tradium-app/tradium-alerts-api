package com.tradiumapp.swingtradealerts.graphql_utilities;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import com.tradiumapp.swingtradealerts.dataFetchers.AllUsersDataFetcher;
import com.tradiumapp.swingtradealerts.dataFetchers.ArticlesDataFetcher;
import com.tradiumapp.swingtradealerts.dataFetchers.UserDataFetcher;

@Component
public class GraphQlUtility {

    @Value("classpath:schemas.graphql")
    private Resource schemaResource;
    private GraphQL graphQL;
    private AllUsersDataFetcher allUsersDataFetcher;
    private UserDataFetcher userDataFetcher;
    private ArticlesDataFetcher articlesDataFetcher;

    @Autowired
    GraphQlUtility(
            AllUsersDataFetcher allUsersDataFetcher, UserDataFetcher userDataFetcher,
            ArticlesDataFetcher articlesDataFetcher) throws
            IOException {
        this.allUsersDataFetcher = allUsersDataFetcher;
        this.userDataFetcher = userDataFetcher;
        this.articlesDataFetcher = articlesDataFetcher;
    }

    @PostConstruct
    public GraphQL createGraphQlObject() throws IOException {
        InputStream schemaStream = schemaResource.getInputStream();
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(new InputStreamReader(schemaStream));
        RuntimeWiring wiring = buildRunTimeWiring();
        GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);
        return GraphQL.newGraphQL(schema).build();
    }

    private RuntimeWiring buildRunTimeWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type("Query", (typewiring) -> typewiring
                        .dataFetcher("users", allUsersDataFetcher)
                        .dataFetcher("user", userDataFetcher))
                .type("User", typeWiring -> typeWiring
                        .dataFetcher("articles", articlesDataFetcher)
                        .dataFetcher("friends", allUsersDataFetcher))
                .build();
    }
}

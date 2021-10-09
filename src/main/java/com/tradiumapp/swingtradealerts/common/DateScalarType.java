package com.tradiumapp.swingtradealerts.common;

import graphql.schema.*;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DateScalarType {

    @Bean
    public GraphQLScalarType dateType() {
        return GraphQLScalarType.newScalar()
                .name("UtcDate").description("Built-in Date").coercing(new Coercing() {
                    @Override
                    public Object serialize(Object o) throws CoercingSerializeException {
                        return ((Date)o).getTime();
                    }

                    @Override
                    public Object parseValue(Object o) throws CoercingParseValueException {
                        return o;
                    }

                    @Override
                    public Object parseLiteral(Object o) throws CoercingParseLiteralException {
                        if (o == null) {
                            return null;
                        }
                        return o.toString();
                    }
                }).build();
    }


}

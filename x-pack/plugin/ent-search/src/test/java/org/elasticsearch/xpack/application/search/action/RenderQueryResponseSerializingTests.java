/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.application.search.action;

import org.elasticsearch.TransportVersion;
import org.elasticsearch.common.io.stream.NamedWriteableRegistry;
import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregator;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.xcontent.XContentParser;
import org.elasticsearch.xpack.core.ml.AbstractBWCSerializationTestCase;

import java.io.IOException;
import java.util.Arrays;

public class RenderQueryResponseSerializingTests extends AbstractBWCSerializationTestCase<RenderSearchApplicationQueryAction.Response> {

    private SearchSourceBuilder searchSourceBuilder;

    @Override
    protected RenderSearchApplicationQueryAction.Response createTestInstance() {
        return new RenderSearchApplicationQueryAction.Response(randomSearchSourceBuilder());
    }

    @Override
    protected RenderSearchApplicationQueryAction.Response mutateInstance(RenderSearchApplicationQueryAction.Response instance)
        throws IOException {
        return randomValueOtherThan(instance, this::createTestInstance);
    }

    protected SearchSourceBuilder randomSearchSourceBuilder() {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        if (randomBoolean()) {
            searchSourceBuilder.query(new TermQueryBuilder(randomAlphaOfLengthBetween(1, 10), randomAlphaOfLengthBetween(1, 10)));
        }
        if (randomBoolean()) {
            searchSourceBuilder.aggregation(
                new TermsAggregationBuilder(randomAlphaOfLengthBetween(1, 10)).field(randomAlphaOfLengthBetween(1, 10))
                    .collectMode(randomFrom(Aggregator.SubAggCollectionMode.values()))
            );
        }
        this.searchSourceBuilder = searchSourceBuilder;
        return searchSourceBuilder;
    }

    @Override
    protected NamedWriteableRegistry getNamedWriteableRegistry() {
        return new NamedWriteableRegistry(
            Arrays.asList(
                new NamedWriteableRegistry.Entry(
                    RenderSearchApplicationQueryAction.Response.class,
                    RenderSearchApplicationQueryAction.NAME,
                    RenderSearchApplicationQueryAction.Response::new
                ),
                new NamedWriteableRegistry.Entry(AggregationBuilder.class, TermsAggregationBuilder.NAME, TermsAggregationBuilder::new),
                new NamedWriteableRegistry.Entry(QueryBuilder.class, TermQueryBuilder.NAME, TermQueryBuilder::new)
            )
        );
    }

    // @Override
    // protected Class<RenderSearchApplicationQueryAction.Response> categoryClass() {
    // return RenderSearchApplicationQueryAction.Response.class;
    // }

    @Override
    protected Writeable.Reader<RenderSearchApplicationQueryAction.Response> instanceReader() {
        return RenderSearchApplicationQueryAction.Response::new;
    }

    @Override
    protected RenderSearchApplicationQueryAction.Response doParseInstance(XContentParser parser) throws IOException {
        return RenderSearchApplicationQueryAction.Response.fromXContent(parser);
    }

    @Override
    protected RenderSearchApplicationQueryAction.Response mutateInstanceForVersion(
        RenderSearchApplicationQueryAction.Response instance,
        TransportVersion version
    ) {
        return new RenderSearchApplicationQueryAction.Response(instance.getSearchSourceBuilder());
    }
}

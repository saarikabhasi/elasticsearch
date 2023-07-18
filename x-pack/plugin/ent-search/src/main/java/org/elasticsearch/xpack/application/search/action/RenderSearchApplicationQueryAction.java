/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.application.search.action;

import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.ActionType;
import org.elasticsearch.common.io.stream.NamedWriteable;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.xcontent.ConstructingObjectParser;
import org.elasticsearch.xcontent.ObjectParser;
import org.elasticsearch.xcontent.ParseField;
import org.elasticsearch.xcontent.ToXContentObject;
import org.elasticsearch.xcontent.XContentBuilder;
import org.elasticsearch.xcontent.XContentParser;

import java.io.IOException;
import java.util.Objects;

import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

public class RenderSearchApplicationQueryAction extends ActionType<RenderSearchApplicationQueryAction.Response> {

    public static final RenderSearchApplicationQueryAction INSTANCE = new RenderSearchApplicationQueryAction();
    public static final String NAME = "cluster:admin/xpack/application/search_application/render_query";

    public static ParseField SEARCH_SOURCE_BUILDER = new ParseField("search_source_builder");
    public static ParseField NAME_FIELD = new ParseField("name");
    public static ParseField AGGREGATION = new ParseField("aggregations");
    public static ParseField QUERY_FIELD = new ParseField("query");

    public RenderSearchApplicationQueryAction() {
        super(NAME, RenderSearchApplicationQueryAction.Response::new);
    }

    public static class Response extends ActionResponse implements ToXContentObject, NamedWriteable {

        private final SearchSourceBuilder searchSourceBuilder;

        public Response(StreamInput in) throws IOException {
            super(in);
            this.searchSourceBuilder = new SearchSourceBuilder(in);
        }

        public Response(SearchSourceBuilder searchSourceBuilder) {
            this.searchSourceBuilder = searchSourceBuilder;
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            searchSourceBuilder.writeTo(out);
        }

        public SearchSourceBuilder getSearchSourceBuilder() {
            return searchSourceBuilder;
        }

        @Override
        public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
            builder.startObject();
            if (searchSourceBuilder.aggregations() != null) {
                builder.field(AGGREGATION.getPreferredName(), searchSourceBuilder.aggregations());
            }
            if (searchSourceBuilder.query() != null) {
                builder.field(QUERY_FIELD.getPreferredName(), searchSourceBuilder.query());
            }

            builder.endObject();
            return searchSourceBuilder.toXContent(builder, params);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Response response = (Response) o;
            return searchSourceBuilder.equals(response.searchSourceBuilder);
        }

        @Override
        public int hashCode() {
            return Objects.hash(searchSourceBuilder);
        }

        @Override
        public String getWriteableName() {
            return NAME;
        }

        @SuppressWarnings("unchecked")
        private static final ConstructingObjectParser<Response, String> PARSER = new ConstructingObjectParser<>(
            "render_search_application_query_action",
            (p) -> new Response((SearchSourceBuilder) p[0])
        );
        static {
            PARSER.declareObject(constructorArg(), (p, c) -> new SearchSourceBuilder().parseXContent(p, true), SEARCH_SOURCE_BUILDER);
            PARSER.declareField(
                optionalConstructorArg(),
                (p, c) -> TermsAggregationBuilder.PARSER.parse(p, c),
                AGGREGATION,
                ObjectParser.ValueType.OBJECT_OR_NULL
            );
            PARSER.declareField(
                optionalConstructorArg(),
                (p, c) -> TermQueryBuilder.fromXContent(p),
                QUERY_FIELD,
                ObjectParser.ValueType.OBJECT_OR_NULL
            );

        }

        public static Response fromXContent(XContentParser parser) throws IOException {
            return PARSER.apply(parser, null);
        }

    }
}

/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.application.search.action;

import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionRequestValidationException;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.ActionType;
import org.elasticsearch.action.ValidateActions;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.xcontent.StatusToXContentObject;
import org.elasticsearch.core.Nullable;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.xcontent.ConstructingObjectParser;
import org.elasticsearch.xcontent.ParseField;
import org.elasticsearch.xcontent.ToXContent;
import org.elasticsearch.xcontent.ToXContentObject;
import org.elasticsearch.xcontent.XContentBuilder;
import org.elasticsearch.xcontent.XContentParser;
import org.elasticsearch.xpack.application.search.SearchApplicationListItem;
import org.elasticsearch.xpack.core.action.util.PageParams;
import org.elasticsearch.xpack.core.action.util.QueryPage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;

public class ListSearchApplicationAction extends ActionType<ListSearchApplicationAction.Response> {

    public static final ListSearchApplicationAction INSTANCE = new ListSearchApplicationAction();
    public static final String NAME = "cluster:admin/xpack/application/search_application/list";

    public ListSearchApplicationAction() {
        super(NAME, ListSearchApplicationAction.Response::new);
    }

    public static class Request extends ActionRequest implements ToXContentObject {

        private static final String DEFAULT_QUERY = "*";
        private final String query;
        private final PageParams pageParams;

        public static final ParseField QUERY_FIELD = new ParseField("query");
        public static final ParseField PAGE_PARAMS_FIELD = new ParseField("pageParams");

        public Request(StreamInput in) throws IOException {
            super(in);
            this.query = in.readString();
            this.pageParams = new PageParams(in);
        }

        public Request(@Nullable String query, PageParams pageParams) {
            this.query = Objects.requireNonNullElse(query, DEFAULT_QUERY);
            this.pageParams = pageParams;
        }

        public String query() {
            return query;
        }

        public PageParams pageParams() {
            return pageParams;
        }

        @Override
        public ActionRequestValidationException validate() {
            // Pagination validation is done as part of PageParams constructor
            ActionRequestValidationException validationException = null;
            if (Strings.isEmpty(query())) {
                validationException = ValidateActions.addValidationError("Search Application query is missing", validationException);
            }
            return validationException;
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            super.writeTo(out);
            out.writeString(query);
            pageParams.writeTo(out);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Request that = (Request) o;
            return Objects.equals(query, that.query) && Objects.equals(pageParams, that.pageParams);
        }

        @Override
        public int hashCode() {
            return Objects.hash(query, pageParams);
        }

        private static final ConstructingObjectParser<Request, Void> PARSER = new ConstructingObjectParser<>(
            "list_search_application_action_request",
            p -> new Request((String) p[0], (PageParams) p[1])
        );
        static {
            PARSER.declareString(optionalConstructorArg(),QUERY_FIELD);
            PARSER.declareObject(constructorArg(), (p,c) -> PageParams.parse(p), PAGE_PARAMS_FIELD);
        }
        public static Request parse(XContentParser parser) {
            return PARSER.apply(parser, null);
        }
//        public static Request fromXContent(XContentParser parser) throws IOException {
//            return PARSER.parse(parser, null);
//        }

        @Override
        public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
            builder.startObject();
            if (query != null){
                builder.field(QUERY_FIELD.getPreferredName(), query);
            }


            builder.field(PAGE_PARAMS_FIELD.getPreferredName(), pageParams);

            builder.endObject();
            return builder;
        }

    }

    public static class Response extends ActionResponse implements StatusToXContentObject {

        public static final ParseField RESULT_FIELD = new ParseField("results");

        public static final ParseField QUERY_PAGE = new ParseField("queryPage");
        final QueryPage<SearchApplicationListItem> queryPage;

        public Response(StreamInput in) throws IOException {
            super(in);
            this.queryPage = new QueryPage<>(in, SearchApplicationListItem::new);
        }
        public Response(QueryPage queryPageObj) {
            Objects.requireNonNull(queryPageObj, "Query page cannot be null");
            this.queryPage = queryPageObj;
        }
        public Response(List<SearchApplicationListItem> items, Long totalResults) {
            this.queryPage = new QueryPage<>(items, totalResults, RESULT_FIELD);
        }
        private static final ConstructingObjectParser<Response, Void> PARSER = new ConstructingObjectParser<>(
            "list_search_application_action_response",
            p -> new Response((QueryPage) p[0])
        );

        static {

//            PARSER.declareObject(constructorArg(), (p,c) -> QueryPage.parse(p), RESULT_FIELD);
        }
        public static Response parse(XContentParser parser) {
            return PARSER.apply(parser, null);
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            queryPage.writeTo(out);
        }

        @Override
        public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
            builder.startObject();
            builder.field(QUERY_PAGE.getPreferredName(), queryPage );
            builder.endObject();
            return builder;

            //            return queryPage.toXContent(builder, params);
        }

        @Override
        public RestStatus status() {
            return RestStatus.OK;
        }

        public QueryPage<SearchApplicationListItem> queryPage() {
            return queryPage;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Response that = (Response) o;
            return queryPage.equals(that.queryPage);
        }

        @Override
        public int hashCode() {
            return queryPage.hashCode();
        }
    }
}

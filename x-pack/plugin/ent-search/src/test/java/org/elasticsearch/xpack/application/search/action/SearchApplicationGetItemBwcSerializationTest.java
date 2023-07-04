/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.application.search.action;

import org.elasticsearch.TransportVersion;
import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.xcontent.XContentParser;
import org.elasticsearch.xpack.application.search.SearchApplication;
import org.elasticsearch.xpack.application.search.SearchApplicationTestUtils;
import org.elasticsearch.xpack.core.ml.AbstractBWCSerializationTestCase;

import java.io.IOException;

public class SearchApplicationGetItemBwcSerializationTest extends AbstractBWCSerializationTestCase<SearchApplication> {

    @Override
    protected Writeable.Reader<SearchApplication> instanceReader() {
        return SearchApplication::new;
    }

    @Override
    protected SearchApplication createTestInstance() {
        return new SearchApplication(
            randomAlphaOfLengthBetween(1, 10),
            generateRandomStringArray(10, 10, false, false),
            randomAlphaOfLengthBetween(1, 10),
            randomLong(),
            SearchApplicationTestUtils.getRandomSearchApplicationTemplate());
    }

    @Override
    protected SearchApplication mutateInstance(SearchApplication instance) throws IOException {
        return randomValueOtherThan(instance, this::createTestInstance);
    }

    @Override
    protected SearchApplication doParseInstance(XContentParser parser) throws IOException {
        return  SearchApplication.parse(parser);
    }

    @Override
    protected SearchApplication mutateInstanceForVersion(SearchApplication instance, TransportVersion version) {
        return new SearchApplication(instance.name(), instance.indices(), instance.analyticsCollectionName(), instance.updatedAtMillis(), instance.searchApplicationTemplate());
    }
}

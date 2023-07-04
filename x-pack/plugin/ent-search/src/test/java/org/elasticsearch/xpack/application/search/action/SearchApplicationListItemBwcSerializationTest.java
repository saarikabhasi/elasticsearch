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
import org.elasticsearch.xpack.application.search.SearchApplicationListItem;
import org.elasticsearch.xpack.core.ml.AbstractBWCSerializationTestCase;

import java.io.IOException;

public class SearchApplicationListItemBwcSerializationTest extends AbstractBWCSerializationTestCase<SearchApplicationListItem> {
    @Override
    protected Writeable.Reader<SearchApplicationListItem> instanceReader() {
        return SearchApplicationListItem::new;
    }

    @Override
    protected SearchApplicationListItem createTestInstance() {
        return new SearchApplicationListItem(randomAlphaOfLengthBetween(1,10), generateRandomStringArray(10,10, false,false), randomAlphaOfLengthBetween(1,10),randomLong());
    }

    @Override
    protected SearchApplicationListItem mutateInstance(SearchApplicationListItem instance) throws IOException {
        return randomValueOtherThan(instance, this::createTestInstance);
    }

    @Override
    protected SearchApplicationListItem doParseInstance(XContentParser parser) throws IOException {
        return SearchApplicationListItem.parse(parser);
    }

    @Override
    protected SearchApplicationListItem mutateInstanceForVersion(SearchApplicationListItem instance, TransportVersion version) {
        return new SearchApplicationListItem(instance.name(), instance.indices(), instance.analyticsCollectionName(),instance.updatedAtMillis());
    }
}

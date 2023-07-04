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
import org.elasticsearch.xpack.application.search.SearchApplicationTemplate;
import org.elasticsearch.xpack.application.search.SearchApplicationTestUtils;
import org.elasticsearch.xpack.core.ml.AbstractBWCSerializationTestCase;

import java.io.IOException;

public class SearchApplicationTemplateBwcSerializationTest extends AbstractBWCSerializationTestCase<SearchApplicationTemplate> {
    @Override
    protected Writeable.Reader<SearchApplicationTemplate> instanceReader() {
        return SearchApplicationTemplate::new;
    }

    @Override
    protected SearchApplicationTemplate createTestInstance() {
        return SearchApplicationTestUtils.getRandomSearchApplicationTemplate();
    }

    @Override
    protected SearchApplicationTemplate mutateInstance(SearchApplicationTemplate instance) throws IOException {
        return randomValueOtherThan(instance, this::createTestInstance);
    }

    @Override
    protected SearchApplicationTemplate doParseInstance(XContentParser parser) throws IOException {
        return SearchApplicationTemplate.parse(parser);
    }

    @Override
    protected SearchApplicationTemplate mutateInstanceForVersion(SearchApplicationTemplate instance, TransportVersion version) {
        return new SearchApplicationTemplate(instance.script(),instance.templateParamValidator());
    }
}

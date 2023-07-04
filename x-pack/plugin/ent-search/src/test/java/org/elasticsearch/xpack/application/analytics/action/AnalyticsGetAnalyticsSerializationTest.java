/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.application.analytics.action;

import org.elasticsearch.TransportVersion;
import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.xcontent.ToXContent;
import org.elasticsearch.xcontent.XContentParser;
import org.elasticsearch.xpack.application.analytics.AnalyticsCollection;
import org.elasticsearch.xpack.core.ml.AbstractBWCSerializationTestCase;

import java.io.IOException;

public class AnalyticsGetAnalyticsSerializationTest extends AbstractBWCSerializationTestCase<AnalyticsCollection> {


    @Override
    protected Writeable.Reader<AnalyticsCollection> instanceReader(){
        return AnalyticsCollection::new;
    }

    @Override
    protected AnalyticsCollection createTestInstance() {
        return new AnalyticsCollection( randomAlphaOfLengthBetween(1, 10));
    }

    @Override
    protected AnalyticsCollection mutateInstance(AnalyticsCollection instance) throws IOException {
        return randomValueOtherThan(instance, this::createTestInstance);
    }

    @Override
    protected AnalyticsCollection doParseInstance(XContentParser parser) {
        return AnalyticsCollection.parse(parser);
    }

    @Override
    protected AnalyticsCollection mutateInstanceForVersion(AnalyticsCollection instance, TransportVersion version) {
        return new AnalyticsCollection(instance.getName());
    }
}

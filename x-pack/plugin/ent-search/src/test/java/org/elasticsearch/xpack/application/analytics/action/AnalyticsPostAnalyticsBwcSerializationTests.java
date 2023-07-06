/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.application.analytics.action;

import org.elasticsearch.TransportVersion;
import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.xcontent.XContentParser;
import org.elasticsearch.xpack.core.ml.AbstractBWCSerializationTestCase;

import java.io.IOException;

public class AnalyticsPostAnalyticsBwcSerializationTests extends AbstractBWCSerializationTestCase<PostAnalyticsEventAction.Response> {

    @Override
    protected Writeable.Reader<PostAnalyticsEventAction.Response> instanceReader() {
        return null;
    }

    @Override
    protected PostAnalyticsEventAction.Response createTestInstance() {
        return null;
    }

    @Override
    protected PostAnalyticsEventAction.Response mutateInstance(PostAnalyticsEventAction.Response instance) throws IOException {
        return null;
    }

    @Override
    protected PostAnalyticsEventAction.Response doParseInstance(XContentParser parser) throws IOException {
        return null;
    }

    @Override
    protected PostAnalyticsEventAction.Response mutateInstanceForVersion(
        PostAnalyticsEventAction.Response instance,
        TransportVersion version
    ) {
        return null;
    }
}

/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.application.analytics;

import org.elasticsearch.ElasticsearchParseException;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.common.xcontent.XContentHelper;
import org.elasticsearch.xcontent.ConstructingObjectParser;
import org.elasticsearch.xcontent.ObjectParser;
import org.elasticsearch.xcontent.ParseField;
import org.elasticsearch.xcontent.ToXContentObject;
import org.elasticsearch.xcontent.XContentBuilder;
import org.elasticsearch.xcontent.XContentParser;
import org.elasticsearch.xcontent.XContentParserConfiguration;
import org.elasticsearch.xcontent.XContentType;
import org.elasticsearch.xpack.application.search.SearchApplication;
import org.elasticsearch.xpack.application.search.SearchApplicationTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static org.elasticsearch.xcontent.ConstructingObjectParser.constructorArg;
import static org.elasticsearch.xcontent.ConstructingObjectParser.optionalConstructorArg;
import static org.elasticsearch.xpack.application.analytics.AnalyticsConstants.EVENT_DATA_STREAM_INDEX_PREFIX;
import static org.elasticsearch.xpack.application.search.SearchApplicationListItem.NAME_FIELD;

/**
 * The {@link AnalyticsCollection} model.
 */
public class AnalyticsCollection implements Writeable, ToXContentObject {


    private static final ConstructingObjectParser<AnalyticsCollection, String> PARSER = new ConstructingObjectParser<>(
        "analytics_collection",
        false,
        (params) -> {
            return new AnalyticsCollection((String) params[0]);
        }
    );


    public static final ParseField NAME_FIELD = new ParseField("name");
    static {
        PARSER.declareString(optionalConstructorArg(), NAME_FIELD);
    }

    private final String name;

    /**
     * Default public constructor.
     *
     * @param name Name of the analytics collection.
     */
    public AnalyticsCollection(String name) {
        if (Strings.isNullOrEmpty(name)) {
            throw new IllegalArgumentException("Analytics name cannot be null or blank");
        }
        this.name = Objects.requireNonNull(name);
    }

    /**
     * Build a new {@link AnalyticsCollection} from a stream.
     */
    public AnalyticsCollection(StreamInput in) throws IOException {
        this(in.readString());
    }

    /**
     * Getter for the {@link AnalyticsCollection} name.
     *
     * @return {@link AnalyticsCollection} name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * The event data stream used by the {@link AnalyticsCollection} to store events.
     * For now, it is a computed property because we have no real storage for the Analytics collection.
     *
     * @return Event data stream name/
     */
    public String getEventDataStream() {
        return EVENT_DATA_STREAM_INDEX_PREFIX + name;
    }

    public static AnalyticsCollection parse(XContentParser parser) {
        return PARSER.apply(parser, null);
    }


    /**
     * Serialize the {@link AnalyticsCollection} to a XContent.
     *
     * @return Serialized {@link AnalyticsCollection}
     */
    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        if (name!=null) {
            builder.field(NAME_FIELD.getPreferredName(), name);
        }
        builder.endObject();

        return builder;
    }

    /**
     * Parses an {@link AnalyticsCollection} from its {@param xContentType} representation in bytes.
     *
     * @param resourceName The name of the resource (must match the {@link AnalyticsCollection} name).
     * @param source The bytes that represents the {@link AnalyticsCollection}.
     * @param xContentType The format of the representation.
     *
     * @return The parsed {@link AnalyticsCollection}.
     */
    public static AnalyticsCollection fromXContentBytes(String resourceName, BytesReference source, XContentType xContentType) {
        try (XContentParser parser = XContentHelper.createParser(XContentParserConfiguration.EMPTY, source, xContentType)) {
            return AnalyticsCollection.fromXContent(resourceName, parser);
        } catch (IOException e) {
            throw new ElasticsearchParseException("Failed to parse: " + source.utf8ToString(), e);
        }
    }

    /**
     * Parses an {@link AnalyticsCollection} through the provided {@param parser}.
     *
     * @param resourceName The name of the resource (must match the {@link AnalyticsCollection} name).
     * @param parser The {@link XContentType} parser.
     *
     * @return The parsed {@link AnalyticsCollection}.
     */
    public static AnalyticsCollection fromXContent(String resourceName, XContentParser parser) throws IOException {
        return PARSER.parse(parser, resourceName);
    }

    public static AnalyticsCollection fromDataStreamName(String dataStreamName) {
        if (dataStreamName.startsWith(EVENT_DATA_STREAM_INDEX_PREFIX) == false) {
            throw new IllegalArgumentException(
                "Data stream name (" + dataStreamName + " must start with " + EVENT_DATA_STREAM_INDEX_PREFIX
            );
        }

        return new AnalyticsCollection(dataStreamName.replaceFirst(EVENT_DATA_STREAM_INDEX_PREFIX, ""));
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeString(name);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnalyticsCollection other = (AnalyticsCollection) o;
        return name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return Strings.toString(this);
    }
}

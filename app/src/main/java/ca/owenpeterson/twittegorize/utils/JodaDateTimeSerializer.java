package ca.owenpeterson.twittegorize.utils;

import com.activeandroid.serializer.TypeSerializer;

import org.joda.time.DateTime;

/**
 * Created by owen on 7/19/15.
 */
public final class JodaDateTimeSerializer extends TypeSerializer {
    public Class<?> getDeserializedType() {
        return DateTime.class;
    }

    public Class<?> getSerializedType() {
        return long.class;
    }

    public Long serialize(Object data) {
        if (data == null) {
            return null;
        }

        return ((DateTime) data).getMillis();
    }

    public DateTime deserialize(Object data) {
        if (data == null) {
            return null;
        }

        return new DateTime().withMillis((Long) data);

    }
}


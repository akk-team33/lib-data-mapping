package net.team33.mapping;

import net.team33.mapping.pattern.Classes;
import net.team33.mapping.reflect.Fields;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.unmodifiableMap;

public class FieldMapping implements Function<Class<?>, Map<String, Field>> {

    public static final FieldMapping DEFAULT = builder().build();

    private final Map<Class<?>, Map<String, Field>> cache = new ConcurrentHashMap<>();
    private final Function<Class<?>, Stream<Field>> streaming;
    private final Predicate<Field> filter;
    private final BiFunction<Class<?>, Field, String> naming;

    private FieldMapping(final Builder builder) {
        streaming = builder.streaming;
        filter = builder.filter;
        naming = builder.naming;
    }

    /**
     * Provides {@link Builder}s for {@link FieldMapping}s.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Returns a field map for an underlying class.
     */
    @Override
    public final Map<String, Field> apply(final Class<?> underlying) {
        return Optional.ofNullable(cache.get(underlying)).orElseGet(() -> {
            final Map<String, Field> result = unmodifiableMap(streaming.apply(underlying)
                    .filter(filter)
                    .peek(field -> field.setAccessible(true))
                    .collect(TreeMap::new, addField(underlying), Map::putAll));
            cache.put(underlying, result);
            return result;
        });
    }

    private BiConsumer<TreeMap<String, Field>, Field> addField(final Class<?> underlying) {
        return (map, field) -> map.put(naming.apply(underlying, field), field);
    }

    /**
     * Specifies a Builder for {@link FieldMapping}s.
     */
    public static final class Builder {

        private static final String DOT = ".";

        private Function<Class<?>, Stream<Field>> streaming = Fields.DEEP;
        private Predicate<Field> filter = Fields.Filter.SIGNIFICANT;
        private BiFunction<Class<?>, Field, String> naming = (covering, field) -> Stream.generate(() -> DOT)
                .limit(Classes.distance(field.getDeclaringClass(), covering))
                .collect(Collectors.joining())
                + field.getName();

        private Builder() {
        }

        /**
         * Builds a new {@link FieldMapping}.
         */
        public final FieldMapping build() {
            return new FieldMapping(this);
        }

        /**
         * Determines how a stream of fields is determined for a given class. By default, {@link Fields#DEEP} is used.
         *
         * @return The builder itself.
         */
        public final Builder setStreaming(final Function<Class<?>, Stream<Field>> streaming) {
            this.streaming = streaming;
            return this;
        }

        /**
         * Determines which filter to apply to fields to decide whether or not they should be included in a resulting
         * FieldMap. By default, {@link Fields.Filter#SIGNIFICANT} is used.
         *
         * @return The builder itself.
         */
        public final Builder setFilter(final Predicate<Field> filter) {
            this.filter = filter;
            return this;
        }

        /**
         * Determines how the name of a field should be determined based on the underlying class.
         * The underlying class is not necessarily the class in which the field was declared.
         *
         * @return The builder itself.
         */
        public final Builder setNaming(final BiFunction<Class<?>, Field, String> naming) {
            this.naming = naming;
            return this;
        }
    }
}

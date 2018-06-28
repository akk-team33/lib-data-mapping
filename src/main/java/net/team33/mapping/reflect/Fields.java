package net.team33.mapping.reflect;

import java.lang.reflect.Field;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public enum Fields implements Function<Class<?>, Stream<Field>> {

    /**
     * Delivers all {@link Field}s straightly declared by a given {@link Class}
     */
    FLAT {
        @Override
        public Stream<Field> apply(final Class<?> subject) {
            return (null == subject)
                    ? Stream.empty()
                    : Stream.of(subject.getDeclaredFields());
        }
    },

    /**
     * Delivers all {@link Field}s declared by a given {@link Class} or any of its superclasses.
     */
    DEEP {
        @Override
        public Stream<Field> apply(final Class<?> subject) {
            return (null == subject)
                    ? Stream.empty()
                    : Stream.concat(apply(subject.getSuperclass()), Stream.of(subject.getDeclaredFields()));
        }
    };

    public enum Filter implements Predicate<Field> {

        /**
         * Defines a filter accepting all fields (including static fields).
         */
        ANY(Modifiers.TRUE),

        /**
         * Defines a filter accepting all public fields.
         */
        PUBLIC(Modifiers.PUBLIC),

        /**
         * Defines a filter accepting all private fields.
         */
        PRIVATE(Modifiers.PRIVATE),

        /**
         * Defines a filter accepting all protected fields.
         */
        PROTECTED(Modifiers.PROTECTED),

        /**
         * Defines a filter accepting all static fields.
         */
        STATIC(Modifiers.STATIC),

        /**
         * Defines a filter accepting all final fields.
         */
        FINAL(Modifiers.FINAL),

        /**
         * Defines a filter accepting all transient fields.
         */
        TRANSIENT(Modifiers.TRANSIENT),

        /**
         * Defines a filter accepting all instance-fields (non-static fields).
         */
        INSTANCE(Modifiers.STATIC.negate()),

        /**
         * Defines a filter accepting all but static or transient fields.
         * Those fields should be significant for a type with value semantics.
         */
        SIGNIFICANT(Modifiers.STATIC.or(Modifiers.TRANSIENT).negate());

        private final IntPredicate filter;

        Filter(final IntPredicate filter) {
            this.filter = filter;
        }

        @Override
        public final boolean test(final Field field) {
            return filter.test(field.getModifiers());
        }
    }
}

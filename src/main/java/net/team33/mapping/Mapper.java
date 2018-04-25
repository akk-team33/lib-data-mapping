package net.team33.mapping;

import net.team33.mapping.Type.Compound;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

public class Mapper {

    private static final Map<Type, Type> NORMAL = Init.newNormal();

    private final Map<Compound, Map<Compound, BiFunction>> methods;

    private Mapper(final Builder builder) {
        this.methods = builder.methods.entrySet().stream()
                .collect(ConcurrentHashMap::new, Mapper::add, Map::putAll);
    }

    private static void add(final Map<Compound, Map<Compound, BiFunction>> map,
                            final Map.Entry<Compound, Map<Compound, BiFunction>> entry) {
        map.put(entry.getKey(), new ConcurrentHashMap<>(entry.getValue()));
    }

    public static Builder builder() {
        return new Builder();
    }

    public final <S, T> Function<S, T> map(final Class<S> sourceClass, final Class<T> targetClass) {
        return map(Type.of(sourceClass), Type.of(targetClass));
    }

    public final <S, T> Function<S, T> map(final Type<S> sourceType, final Class<T> targetClass) {
        return map(sourceType, Type.of(targetClass));
    }

    public final <S, T> Function<S, T> map(final Class<S> sourceClass, final Type<T> targetType) {
        return map(Type.of(sourceClass), targetType);
    }

    public final <S, T> Function<S, T> map(final Type<S> sourceType, final Type<T> targetType) {
        final BiFunction<Mapper, S, T> method =
                method(normal(sourceType).getCompound(), normal(targetType).getCompound());
        return source -> (null == source) ? null : method.apply(this, source);
    }

    @SuppressWarnings("unchecked")
    private <S, T> BiFunction<Mapper, S, T> method(final Compound srcCompound, final Compound tgtCompound) {
        final Map<Compound, BiFunction> srcMethods = Optional.ofNullable(methods.get(srcCompound)).orElseGet(() -> {
            final Map<Compound, BiFunction> result = new ConcurrentHashMap<>();
            methods.put(srcCompound, result);
            return result;
        });
        return Optional.ofNullable(srcMethods.get(tgtCompound)).orElseGet(() -> {
            final BiFunction result = defaultMethod(srcCompound, tgtCompound);
            srcMethods.put(tgtCompound, result);
            return result;
        });
    }

    private BiFunction defaultMethod(final Compound srcCompound, final Compound tgtCompound) {
        if (String.class.equals(tgtCompound.getRawClass())) {
            return (map, src) -> src.toString();
        }
        return (map, src) -> {
            throw new IllegalArgumentException("no method specified for " + srcCompound + " -> " + tgtCompound);
        };
    }

    @SuppressWarnings("unchecked")
    private static <T> Type<T> normal(final Type<T> type) {
        return Optional.ofNullable(NORMAL.get(type))
                .orElse(type);
    }

    public static class Builder {

        private final Map<Compound, Map<Compound, BiFunction>> methods = new HashMap<>(0);

        private Builder() {
//            put(Boolean.class, String.class, (map, src) -> Objects.toString(src));
//            put(Byte.class, String.class, (map, src) -> Objects.toString(src));
//            put(Short.class, String.class, (map, src) -> Objects.toString(src));
//            put(Integer.class, String.class, (map, src) -> Objects.toString(src));
//            put(Long.class, String.class, (map, src) -> Objects.toString(src));
//            put(Float.class, String.class, (map, src) -> Objects.toString(src));
//            put(Double.class, String.class, (map, src) -> Objects.toString(src));
//            put(Character.class, String.class, (map, src) -> Objects.toString(src));
            put(String.class, Boolean.class, (map, src) -> Boolean.valueOf(src));
            put(String.class, Byte.class, (map, src) -> Byte.valueOf(src));
            put(String.class, Short.class, (map, src) -> Short.valueOf(src));
            put(String.class, Integer.class, (map, src) -> Integer.valueOf(src));
            put(String.class, Long.class, (map, src) -> Long.valueOf(src));
            put(String.class, Float.class, (map, src) -> Float.valueOf(src));
            put(String.class, Double.class, (map, src) -> Double.valueOf(src));
            put(String.class, Character.class, (map, src) -> src.charAt(0));
        }

        public final <S, T> Builder put(final Class<S> sourceClass,
                                        final Class<T> targetClass,
                                        final BiFunction<Mapper, S, T> method) {
            return put(Type.of(sourceClass), Type.of(targetClass), method);
        }

        public final <S, T> Builder put(final Class<S> sourceClass,
                                        final Type<T> targetType,
                                        final BiFunction<Mapper, S, T> method) {
            return put(Type.of(sourceClass), targetType, method);
        }

        public final <S, T> Builder put(final Type<S> sourceType,
                                        final Class<T> targetClass,
                                        final BiFunction<Mapper, S, T> method) {
            return put(sourceType, Type.of(targetClass), method);
        }

        public final <S, T> Builder put(final Type<S> sourceType,
                                        final Type<T> targetType,
                                        final BiFunction<Mapper, S, T> method) {
            return put(normal(sourceType).getCompound(), normal(targetType).getCompound(), method);
        }

        private <S, T> Builder put(final Compound srcCompound,
                                   final Compound tgtCompound,
                                   final BiFunction<Mapper, S, T> method) {
            Optional.ofNullable(methods.get(srcCompound)).orElseGet(() -> {
                final Map<Compound, BiFunction> result = new HashMap<>();
                methods.put(srcCompound, result);
                return result;
            }).put(tgtCompound, method);
            return this;
        }

        public final Mapper build() {
            return new Mapper(this);
        }
    }

    private static class Init {

        private static final Class[][] NORMAL = {
                {Boolean.TYPE, Boolean.class},
                {Byte.TYPE, Byte.class},
                {Short.TYPE, Short.class},
                {Integer.TYPE, Integer.class},
                {Long.TYPE, Long.class},
                {Float.TYPE, Float.class},
                {Double.TYPE, Double.class},
                {Character.TYPE, Character.class}
        };

        public static Map<Type, Type> newNormal() {
            return Collections.unmodifiableMap(
                    Stream.of(NORMAL)
                            .collect(HashMap::new, Init::add, Map::putAll));
        }

        @SuppressWarnings("unchecked")
        private static void add(final Map<Type, Type> map, final Class[] classes) {
            map.put(Type.of(classes[0]), Type.of(classes[1]));
        }
    }
}

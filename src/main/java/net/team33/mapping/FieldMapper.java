package net.team33.mapping;

import net.team33.mapping.pattern.Classes;
import net.team33.mapping.reflect.Fields;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FieldMapper {

    private static final Map<Class<?>, Map<String, Field>> CACHE = new ConcurrentHashMap<>();
    private static final String DOT = ".";

    private final Supplier<Map<String, Object>> newMap = TreeMap::new;
    private final Function<Object, Object> subMapper = subject -> subject;

    public FieldMapper(final Builder builder) {
    }

    private static Map<String, Field> fieldMap(final Class<?> originClass) {
        return Optional
                .ofNullable(CACHE.get(originClass))
                .orElseGet(() -> newFieldMap(originClass));
    }

    private static Map<String, Field> newFieldMap(final Class<?> originClass) {
        final Map<String, Field> result = Fields.DEEP.apply(originClass)
                .filter(Fields.Filter.SIGNIFICANT)
                .peek(field -> field.setAccessible(true))
                .collect(TreeMap::new, (map, field) -> map.put(name(field, originClass), field), Map::putAll);
        CACHE.put(originClass, result);
        return result;
    }

    private static String name(final Field field, final Class<?> originClass) {
        return Stream.generate(() -> DOT)
                .limit(Classes.distance(field.getDeclaringClass(), originClass))
                .collect(Collectors.joining())
                + field.getName();
    }

    public static Builder builder() {
        return new Builder();
    }

    public Map<String, Object> map(final Object origin) {
        return fieldMap(origin.getClass()).entrySet().stream()
                .collect(newMap, (map, entry) -> put(map, entry, origin), Map::putAll);
    }

    private void put(final Map<String, Object> map, final Map.Entry<String, Field> entry, final Object origin) {
        map.put(entry.getKey(), value(entry.getValue(), origin));
    }

    private Object value(final Field field, final Object origin) {
        try {
            return subMapper.apply(field.get(origin));
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public <T> T map(final T target, final Map<String, Object> origin) {
        fieldMap(origin.getClass()).entrySet().stream();
        throw new UnsupportedOperationException("not yet implemented");
    }

    public static class Builder {

        public final FieldMapper build() {
            return new FieldMapper(this);
        }
    }
}

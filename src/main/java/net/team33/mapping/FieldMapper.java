package net.team33.mapping;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class FieldMapper {


    private final Function<Class<?>, Map<String, Field>> fieldMapFunction;
    private final Supplier<Map<String, Object>> newMap;

    public FieldMapper(final Builder builder) {
        fieldMapFunction = builder.fieldMapFunction;
        newMap = builder.newMap;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Map<String, Object> map(final Object origin) {
        return fieldMapFunction.apply(origin.getClass())
                .entrySet().stream()
                .collect(newMap, put(origin), Map::putAll);
    }

    private BiConsumer<Map<String, Object>, Map.Entry<String, Field>> put(final Object origin) {
        return (map, entry) -> {
            try {
                map.put(entry.getKey(), entry.getValue().get(origin));
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        };
    }

    public <T> T map(final T target, final Map<String, Object> origin) {
        fieldMapFunction.apply(target.getClass())
                .entrySet().stream()
                .forEach(get(target, origin));
        return target;
    }

    private Consumer<Map.Entry<String, Field>> get(final Object target, final Map<String, Object> origin) {
        return entry -> {
            try {
                entry.getValue().set(target, origin.get(entry.getKey()));
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        };
    }

    public static class Builder {

        private Function<Class<?>, Map<String, Field>> fieldMapFunction = FieldMapFunction.DEFAULT;
        private Supplier<Map<String, Object>> newMap = TreeMap::new;

        public final FieldMapper build() {
            return new FieldMapper(this);
        }

        public final Builder setFieldMapFunction(final Function<Class<?>, Map<String, Field>> fieldMapFunction) {
            this.fieldMapFunction = fieldMapFunction;
            return this;
        }

        public final Builder setNewMap(final Supplier<Map<String, Object>> newMap) {
            this.newMap = newMap;
            return this;
        }
    }
}

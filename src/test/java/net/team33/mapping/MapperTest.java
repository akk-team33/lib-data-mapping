package net.team33.mapping;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class MapperTest<S, T> {

    private static final Mapper MAPPER = Mapper.builder().build();

    private final Class<S> sourceClass;
    private final Class<T> targetClass;
    private final S source;
    private final T expected;

    public MapperTest(final Class<S> sourceClass, final Class<T> targetClass, final S source, final T expected) {
        this.sourceClass = sourceClass;
        this.targetClass = targetClass;
        this.source = source;
        this.expected = expected;
    }

    @Parameters(name = "{index}: map({0}, {1}).apply({2}) -> {3}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {Boolean.TYPE, String.class, true, String.valueOf(true)},
                {Boolean.class, String.class, false, String.valueOf(false)},
                {Byte.TYPE, String.class, (byte) 29, String.valueOf(29)},
                {Byte.class, String.class, (byte) -29, String.valueOf(-29)},
                {Short.TYPE, String.class, (short) 1000, String.valueOf(1000)},
                {Short.class, String.class, (short) -1000, String.valueOf(-1000)},
                {Integer.TYPE, String.class, 278, String.valueOf(278)},
                {Integer.class, String.class, -278, String.valueOf(-278)},
                {Long.TYPE, String.class, 278L, String.valueOf(278)},
                {Long.class, String.class, -278L, String.valueOf(-278)},
                {Float.TYPE, String.class, (float) 278.1415, String.valueOf(278.1415)},
                {Float.class, String.class, (float) -278.1415, String.valueOf(-278.1415)},
                {Double.TYPE, String.class, 278.987456, String.valueOf(278.987456)},
                {Double.class, String.class, -278.987456, String.valueOf(-278.987456)},
                {Character.TYPE, String.class, 'A', String.valueOf('A')},
                {Character.class, String.class, 'x', String.valueOf('x')},

                {String.class, Boolean.TYPE, "true", true},
                {String.class, Boolean.class, "false", false},
                {String.class, Byte.TYPE, "29", (byte) 29},
                {String.class, Byte.class, "-29", (byte) -29},
                {String.class, Short.TYPE, "753", (short) 753},
                {String.class, Short.class, "-753", (short) -753},
                {String.class, Integer.TYPE, "765432", 765432},
                {String.class, Integer.class, "-765432", -765432},
                {String.class, Long.TYPE, "1234567890", 1234567890L},
                {String.class, Long.class, "-1234567890", -1234567890L},
                {String.class, Float.TYPE, "3.141592654", (float) 3.141592654},
                {String.class, Float.class, "-3.141592654", (float) -3.141592654},
                {String.class, Double.TYPE, "3.141592654", 3.141592654},
                {String.class, Double.class, "-3.141592654", -3.141592654},
                {String.class, Character.TYPE, "A", 'A'},
                {String.class, Character.class, "x", 'x'}
        });
    }

    @Test
    public void map() {
        assertEquals(
                expected,
                MAPPER.map(sourceClass, targetClass).apply(source)
        );
    }

    @Test
    public void mapNull() {
        assertNull(MAPPER.map(sourceClass, targetClass).apply(null));
    }
}
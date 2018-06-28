package net.team33.mapping;

import net.team33.mapping.reflect.Fields;
import net.team33.mapping.testing.SampleA;
import net.team33.mapping.testing.SampleB;
import net.team33.mapping.testing.SampleC;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class FieldMappingTest {

    private static final FieldMapping DEFAULT_FIELD_MAPPING = FieldMapping.DEFAULT;
    private static final FieldMapping INSTANCE_FIELD_MAPPING = FieldMapping.builder()
            .setFilter(Fields.Filter.INSTANCE)
            .build();

    @Test
    public void testDate() {
        final Map<String, Field> fieldMap = INSTANCE_FIELD_MAPPING.apply(Date.class);
        assertEquals(new HashSet<>(Arrays.asList("cdate", "fastTime")), fieldMap.keySet());
    }

    @Test
    public void testArrayList() {
        final Map<String, Field> fieldMap = INSTANCE_FIELD_MAPPING.apply(ArrayList.class);
        assertEquals(
                new HashSet<>(Arrays.asList(
                        ".modCount",
                        "elementData",
                        "size")),
                fieldMap.keySet());
    }

    @Test
    public void testSampleA() {
        final Map<String, Field> fieldMap = DEFAULT_FIELD_MAPPING.apply(SampleA.class);
        assertEquals(new HashSet<>(Arrays.asList("privateFinalInt", "privateString")), fieldMap.keySet());
    }

    @Test
    public void testSampleB() {
        final Map<String, Field> fieldMap = DEFAULT_FIELD_MAPPING.apply(SampleB.class);
        assertEquals(
                new HashSet<>(Arrays.asList(
                        ".privateFinalInt",
                        ".privateString",
                        "privateFinalInt",
                        "privateString")),
                fieldMap.keySet());
    }

    @Test
    public void testSampleC() {
        final Map<String, Field> fieldMap = DEFAULT_FIELD_MAPPING.apply(SampleC.class);
        assertEquals(
                new HashSet<>(Arrays.asList(
                        "..privateFinalInt",
                        "..privateString",
                        ".privateFinalInt",
                        ".privateString",
                        "privateFinalInt",
                        "privateString")),
                fieldMap.keySet());
    }
}
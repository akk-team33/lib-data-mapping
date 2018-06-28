package net.team33.mapping;

import net.team33.mapping.testing.SampleA;
import net.team33.mapping.testing.SampleB;
import net.team33.mapping.testing.SampleC;
import net.team33.random.SmartRandom;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class FieldMapperTest {

    private static final SmartRandom RANDOM = SmartRandom.builder()
            .put(SampleA.class, rnd -> rnd.setAllFields(new SampleA(0)))
            .put(SampleB.class, rnd -> rnd.setAllFields(new SampleB(0, -1)))
            .put(SampleC.class, rnd -> rnd.setAllFields(new SampleC(0, -1, -2)))
            .build();

    private final FieldMapper mapper = FieldMapper.builder().build();

    @Test
    public void mapToSampleA() {
        final SampleA sample = RANDOM.any(SampleA.class);
        final Map<String, Object> intermediate = mapper.map(sample);
        final SampleA result = mapper.map(new SampleA(0), intermediate);
        Assert.assertEquals(sample, result);
    }

    @Test
    public void mapSampleAToMap() {
        final Map<String, Object> sample = mapper.map(RANDOM.any(SampleA.class));
        final SampleA intermediate = mapper.map(new SampleA(0), sample);
        final Map<String, Object> result = mapper.map(intermediate);
        Assert.assertEquals(sample, result);
    }

    @Test
    public void mapToSampleB() {
        final SampleB sample = RANDOM.any(SampleB.class);
        final Map<String, Object> intermediate = mapper.map(sample);
        final SampleB result = mapper.map(new SampleB(0, 1), intermediate);
        Assert.assertEquals(sample, result);
    }

    @Test
    public void mapSampleBToMap() {
        final Map<String, Object> sample = mapper.map(RANDOM.any(SampleB.class));
        final SampleB intermediate = mapper.map(new SampleB(0, 1), sample);
        final Map<String, Object> result = mapper.map(intermediate);
        Assert.assertEquals(sample, result);
    }

    @Test
    public void mapToSampleC() {
        final SampleC sample = RANDOM.any(SampleC.class);
        final Map<String, Object> intermediate = mapper.map(sample);
        final SampleC result = mapper.map(new SampleC(0, 1, 2), intermediate);
        Assert.assertEquals(sample, result);
    }

    @Test
    public void mapSampleCToMap() {
        final Map<String, Object> sample = mapper.map(RANDOM.any(SampleC.class));
        final SampleC intermediate = mapper.map(new SampleC(0, 1, 2), sample);
        final Map<String, Object> result = mapper.map(intermediate);
        Assert.assertEquals(sample, result);
    }
}
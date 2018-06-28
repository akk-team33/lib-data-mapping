package net.team33.mapping;

import net.team33.mapping.testing.SampleA;
import net.team33.random.SmartRandom;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class FieldMapperTest {

    private final SmartRandom random = SmartRandom.builder()
            .put(SampleA.class, rnd -> rnd.setAllFields(new SampleA(0)))
            .build();
    private final FieldMapper subject = FieldMapper.builder().build();

    @Test
    public void mapToSampleA() {
        final SampleA sample = random.any(SampleA.class);
        final Map<String, Object> intermediate = subject.map(sample);
        final SampleA result = subject.map(new SampleA(0), intermediate);
        Assert.assertEquals(sample, result);
    }

    @Test
    public void mapToMap() {
        final Map<String, Object> sample = subject.map(random.any(SampleA.class));
        final SampleA intermediate = subject.map(new SampleA(0), sample);
        final Map<String, Object> result = subject.map(intermediate);
        Assert.assertEquals(sample, result);
    }
}
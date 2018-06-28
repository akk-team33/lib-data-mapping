package net.team33.mapping.testing;

import java.util.Arrays;
import java.util.List;

public class SampleA {

    private final int privateFinalInt;

    private String privateString;

    public SampleA(final int privateFinalInt) {
        this.privateFinalInt = privateFinalInt;
    }

    protected List<Object> toList() {
        return Arrays.asList(privateFinalInt, privateString);
    }

    @Override
    public int hashCode() {
        return toList().hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return (this == obj) || ((SampleA.class == obj.getClass()) && equals_((SampleA) obj));
    }

    private boolean equals_(final SampleA other) {
        return toList().equals(other.toList());
    }

    @Override
    public String toString() {
        return toList().toString();
    }
}

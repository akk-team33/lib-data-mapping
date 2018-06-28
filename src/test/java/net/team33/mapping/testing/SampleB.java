package net.team33.mapping.testing;

import java.util.Arrays;
import java.util.List;

public class SampleB extends SampleA {

    private final int privateFinalInt;

    private String privateString;

    public SampleB(final int superPrivateFinalInt, final int privateFinalInt) {
        super(superPrivateFinalInt);
        this.privateFinalInt = privateFinalInt;
    }

    public String getPrivateString() {
        return privateString;
    }

    public SampleB setPrivateString(final String privateString) {
        this.privateString = privateString;
        return this;
    }

    protected List<Object> toList() {
        return Arrays.asList(super.toList(), privateFinalInt, privateString);
    }

    @Override
    public int hashCode() {
        return toList().hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return (this == obj) || ((SampleB.class == obj.getClass()) && equals_((SampleB) obj));
    }

    private boolean equals_(final SampleB other) {
        return toList().equals(other.toList());
    }

    @Override
    public String toString() {
        return toList().toString();
    }
}

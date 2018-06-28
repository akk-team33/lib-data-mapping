package net.team33.mapping.testing;

import java.util.Arrays;
import java.util.List;

public class SampleC extends SampleB {

    private final int privateFinalInt;

    private String privateString;

    public SampleC(final int superSuperPrivateFinalInt, final int superPrivateFinalInt, final int privateFinalInt) {
        super(superSuperPrivateFinalInt, superPrivateFinalInt);
        this.privateFinalInt = privateFinalInt;
    }

    public int getPrivateFinalInt() {
        return privateFinalInt;
    }

    @Override
    public String getPrivateString() {
        return privateString;
    }

    @Override
    public SampleC setPrivateString(final String privateString) {
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
        return (this == obj) || ((SampleC.class == obj.getClass()) && equals_((SampleC) obj));
    }

    private boolean equals_(final SampleC other) {
        return toList().equals(other.toList());
    }

    @Override
    public String toString() {
        return toList().toString();
    }
}

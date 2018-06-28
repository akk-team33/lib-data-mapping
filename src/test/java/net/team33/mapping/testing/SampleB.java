package net.team33.mapping.testing;

public class SampleB extends SampleA {

    private final int privateFinalInt;

    private String privateString;

    public SampleB(final int superPrivateFinalInt, final int privateFinalInt) {
        super(superPrivateFinalInt);
        this.privateFinalInt = privateFinalInt;
    }
}

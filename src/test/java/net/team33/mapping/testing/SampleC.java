package net.team33.mapping.testing;

public class SampleC extends SampleB {

    private final int privateFinalInt;

    private String privateString;

    public SampleC(final int superSuperPrivateFinalInt, final int superPrivateFinalInt, final int privateFinalInt) {
        super(superSuperPrivateFinalInt, superPrivateFinalInt);
        this.privateFinalInt = privateFinalInt;
    }
}

package tech.pegasys.pantheon.util.bytes;

public class DelegatingBytes32 extends BaseDelegatingBytesValue<Bytes32> implements Bytes32 {
    protected DelegatingBytes32(final Bytes32 wrapped) {
        super(unwrap(wrapped));
    }

    // Make sure we don't end-up with giant chains of delegating through wrapping.
    private static Bytes32 unwrap(final Bytes32 v) {
        Bytes32 wrapped = v;

        if (wrapped instanceof DelegatingBytes32) {
            wrapped = ((DelegatingBytes32) wrapped).wrapped;
        }
        return wrapped;
    }

    @Override
    public Bytes32 copy() {
        return wrapped.copy();
    }

    @Override
    public MutableBytes32 mutableCopy() {
        return wrapped.mutableCopy();
    }
}
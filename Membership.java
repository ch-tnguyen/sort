package com.collectivehealth.bizcore.model.helpers.sort;

// This class is used to define membership for elements of type T.
public interface Membership<T> {
    // Return true iff candidate is included in this membership.
    public boolean includes(T candidate);
}

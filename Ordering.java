package com.collectivehealth.bizcore.model.helpers.sort;

import java.util.List;

// Class representing an ordering for elements of type T.
public interface Ordering<T> {
    // Sort the list according to internal ordering.
    public void sort(List<T> list);
}

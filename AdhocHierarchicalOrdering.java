package com.collectivehealth.bizcore.model.helpers.sort;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;

public class AdhocHierarchicalOrdering<T> implements Ordering<T> {

    private List<Pair<Membership<T>, Comparator<T>>> rules;

    /**
     * Construct an AdhocHierarchicalOrdering with ordering defined by rules.
     * 
     * @param rules
     *            List of pairs of Range and Comparators which define the
     *            overall sort order.
     */
    public AdhocHierarchicalOrdering(List<Pair<Membership<T>, Comparator<T>>> rules) {
        this.rules = rules;
    }

    /**
     * Sort the list according to the rules. Each rule consists of a pair of
     * comparators. The left comparator defines the range over which the rule
     * applies. The right comparator defines the sort order for the rule.
     * 
     * This method allows one to sort a list as though it were a tree where each
     * set of siblings can have a different sort order.
     * 
     * @param <T>
     *            The type of object to be sorted.
     * @param list
     *            List of items to be sorted.
     */
    public void sort(List<T> list) {

        for (Pair<Membership<T>, Comparator<T>> rule : this.rules) {

            Membership<T> membership = rule.getLeft();
            Comparator<T> sortOrder = rule.getRight();

            Pair<Integer, Integer> range = firstRange(list, membership);

            List<T> sublist = list.subList(range.getLeft(), range.getRight());

            if (sublist != null) {
                Collections.sort(sublist, sortOrder);
            }
        }
    }

    /**
     * Return the first range where for all elements e in range [start, end)
     * criteria.equals(e) == true.
     * 
     * @param <T>
     *            The type of object to be evaluated.
     * @param list
     *            List over which the range is to be determined.
     * @param membership
     *            Range criteria.
     * @return Pair of integers describing the start and end. End exclusive.
     */
    private static <T> Pair<Integer, Integer> firstRange(List<T> list, Membership<T> membership) {

        int start = 0, end = 0;

        boolean foundStart = false;
        boolean foundEnd = false;
        for (int i = 0; i < list.size(); i++) {
            if (!foundStart) {
                // look for the start
                if (membership.includes(list.get(i))) {
                    start = i;
                    foundStart = true;
                }
            } else {
                // look for the end
                if (!membership.includes(list.get(i))) {
                    end = i;
                    foundEnd = true;
                    break;
                }
            }
        }

        if (!foundEnd) {
            end = list.size();
        }

        return Pair.of(start, end);
    }
}

package com.collectivehealth.bizcore.model.helpers.sort;

import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.Test;

public class AdhocHierarchicalOrderingTest {

    private <T> void reorder(Ordering<T> order, List<T> list) {
        order.sort(list);
    }

    /**
     * Given: ["red", "green", "red"]
     * 
     * Return: ["red", "red", "green"]
     */
    @Test
    public void testSort_SingleTier() {
        List<String> list = new ArrayList<String>();
        list.add("red");
        list.add("green");
        list.add("red");

        List<Pair<Membership<String>, Comparator<String>>> rules = new ArrayList<Pair<Membership<String>, Comparator<String>>>();

        // sort the whole list based on color, red before green
        rules.add(Pair.of(
                new Membership<String>() {
                    public boolean includes(String str) {
                        return true; // apply rule to entire list
                    }
                },
                new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        if (o1.equals("red")) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                }));

        reorder(new AdhocHierarchicalOrdering<String>(rules), list);

        assertTrue(list.get(0).equals("red"));
        assertTrue(list.get(1).equals("red"));
        assertTrue(list.get(2).equals("green"));
    }

    /**
     * Given: [("red", 1), ("green", 1), ("red", 0), ("green", 0)]
     * 
     * Return: [("red", 0), ("red", 1), ("green", 1), ("green", 0)]
     */
    @Test
    public void testSort_TwoTiers() {
        List<Pair<String, Integer>> list = new ArrayList<Pair<String, Integer>>();
        list.add(Pair.of("red", 1));
        list.add(Pair.of("green", 1));
        list.add(Pair.of("red", 0));
        list.add(Pair.of("green", 0));

        List<Pair<Membership<Pair<String, Integer>>, Comparator<Pair<String, Integer>>>> rules = new ArrayList<Pair<Membership<Pair<String, Integer>>, Comparator<Pair<String, Integer>>>>();

        // sort the whole list based on color, red before green
        rules.add(Pair.of(
                new Membership<Pair<String, Integer>>() {
                    public boolean includes(Pair<String, Integer> o) {
                        return true; // apply rule to entire list
                    }
                },
                new Comparator<Pair<String, Integer>>() {
                    @Override
                    public int compare(Pair<String, Integer> o1, Pair<String, Integer> o2) {
                        if (o1.getLeft().equals("red")) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                }));

        // sort the red portion by ascending integer
        rules.add(Pair.of(
                new Membership<Pair<String, Integer>>() {
                    public boolean includes(Pair<String, Integer> o) {
                        return o.getLeft().equals("red");
                    }
                },
                new Comparator<Pair<String, Integer>>() {
                    @Override
                    public int compare(Pair<String, Integer> o1, Pair<String, Integer> o2) {
                        int result = 0;
                        if (o1.getRight() < o2.getRight()) {
                            result = -1;
                        }
                        if (o1.getRight() == o2.getRight()) {
                            result = 0;
                        }
                        if (o1.getRight() > o2.getRight()) {
                            result = 1;
                        }
                        return result;
                    }
                }));

        // sort the green portion by descending integer
        rules.add(Pair.of(
                new Membership<Pair<String, Integer>>() {
                    public boolean includes(Pair<String, Integer> o) {
                        return o.getLeft().equals("green");
                    }
                },
                new Comparator<Pair<String, Integer>>() {
                    @Override
                    public int compare(Pair<String, Integer> o1, Pair<String, Integer> o2) {
                        int result = 0;
                        if (o1.getRight() < o2.getRight()) {
                            result = 1;
                        }
                        if (o1.getRight() == o2.getRight()) {
                            result = 0;
                        }
                        if (o1.getRight() > o2.getRight()) {
                            result = -1;
                        }
                        return result;
                    }
                }));

        reorder(new AdhocHierarchicalOrdering<Pair<String, Integer>>(rules), list);

        assertTrue(list.get(0).getLeft().equals("red"));
        assertTrue(list.get(0).getRight().equals(0));
        assertTrue(list.get(1).getLeft().equals("red"));
        assertTrue(list.get(1).getRight().equals(1));
        assertTrue(list.get(2).getLeft().equals("green"));
        assertTrue(list.get(2).getRight().equals(1));
        assertTrue(list.get(3).getLeft().equals("green"));
        assertTrue(list.get(3).getRight().equals(0));
    }

    /**
     * Given: [("red", 1, false), ("green", 1, false), ("red", 0, true),
     * ("green", 0, true), ("red", 1, true), ("green", 1, true)]
     * 
     * Return: [("red", 0, true), ("red", 1, true), ("red", 1, false), ("green",
     * 1, true), ("green", 1, false), ("green", 0, true)]
     */
    @Test
    public void testSort_ThreeTiers() {
        List<Triple<String, Integer, Boolean>> list = new ArrayList<Triple<String, Integer, Boolean>>();
        list.add(Triple.of("red", 1, false));
        list.add(Triple.of("green", 1, false));
        list.add(Triple.of("red", 0, true));
        list.add(Triple.of("green", 0, true));
        list.add(Triple.of("red", 1, true));
        list.add(Triple.of("green", 1, true));

        List<Pair<Membership<Triple<String, Integer, Boolean>>, Comparator<Triple<String, Integer, Boolean>>>> rules = new ArrayList<Pair<Membership<Triple<String, Integer, Boolean>>, Comparator<Triple<String, Integer, Boolean>>>>();

        // sort the whole list based on color, red before green
        rules.add(Pair.of(
                new Membership<Triple<String, Integer, Boolean>>() {
                    public boolean includes(Triple<String, Integer, Boolean> o) {
                        return true; // apply rule to entire list
                    }
                },
                new Comparator<Triple<String, Integer, Boolean>>() {
                    @Override
                    public int compare(Triple<String, Integer, Boolean> o1, Triple<String, Integer, Boolean> o2) {
                        if (o1.getLeft().equals("red")) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                }));

        // sort the red portion by ascending integer
        rules.add(Pair.of(
                new Membership<Triple<String, Integer, Boolean>>() {
                    public boolean includes(Triple<String, Integer, Boolean> o) {
                        return o.getLeft().equals("red");
                    }
                },
                new Comparator<Triple<String, Integer, Boolean>>() {
                    @Override
                    public int compare(Triple<String, Integer, Boolean> o1, Triple<String, Integer, Boolean> o2) {
                        int result = 0;
                        if (o1.getMiddle() < o2.getMiddle()) {
                            result = -1;
                        }
                        if (o1.getMiddle() == o2.getMiddle()) {
                            result = 0;
                        }
                        if (o1.getMiddle() > o2.getMiddle()) {
                            result = 1;
                        }
                        return result;
                    }
                }));

        // sort the green portion by descending integer
        rules.add(Pair.of(
                new Membership<Triple<String, Integer, Boolean>>() {
                    public boolean includes(Triple<String, Integer, Boolean> o) {
                        return o.getLeft().equals("green");
                    }
                },
                new Comparator<Triple<String, Integer, Boolean>>() {
                    @Override
                    public int compare(Triple<String, Integer, Boolean> o1, Triple<String, Integer, Boolean> o2) {
                        int result = 0;
                        if (o1.getMiddle() < o2.getMiddle()) {
                            result = 1;
                        }
                        if (o1.getMiddle() == o2.getMiddle()) {
                            result = 0;
                        }
                        if (o1.getMiddle() > o2.getMiddle()) {
                            result = -1;
                        }
                        return result;
                    }
                }));

        // sort each (color, number) by truth value, true first
        Comparator<Triple<String, Integer, Boolean>> booleanOrdering = new Comparator<Triple<String, Integer, Boolean>>() {
            @Override
            public int compare(Triple<String, Integer, Boolean> o1, Triple<String, Integer, Boolean> o2) {
                return o1.getRight() ? -1 : 1;
            }
        };
        rules.add(Pair.of(
                new Membership<Triple<String, Integer, Boolean>>() {
                    public boolean includes(Triple<String, Integer, Boolean> o) {
                        return o.getLeft().equals("red") && o.getMiddle() == 0;
                    }
                },
                booleanOrdering));
        rules.add(Pair.of(
                new Membership<Triple<String, Integer, Boolean>>() {
                    public boolean includes(Triple<String, Integer, Boolean> o) {
                        return o.getLeft().equals("red") && o.getMiddle() == 1;
                    }
                },
                booleanOrdering));
        rules.add(Pair.of(
                new Membership<Triple<String, Integer, Boolean>>() {
                    public boolean includes(Triple<String, Integer, Boolean> o) {
                        return o.getLeft().equals("green") && o.getMiddle() == 1;
                    }
                },
                booleanOrdering));
        rules.add(Pair.of(
                new Membership<Triple<String, Integer, Boolean>>() {
                    public boolean includes(Triple<String, Integer, Boolean> o) {
                        return o.getLeft().equals("green") && o.getMiddle() == 0;
                    }
                },
                booleanOrdering));

        reorder(new AdhocHierarchicalOrdering<Triple<String, Integer, Boolean>>(rules), list);

        assertTrue(list.get(0).getLeft().equals("red"));
        assertTrue(list.get(0).getMiddle().equals(0));
        assertTrue(list.get(0).getRight().equals(true));
        assertTrue(list.get(1).getLeft().equals("red"));
        assertTrue(list.get(1).getMiddle().equals(1));
        assertTrue(list.get(1).getRight().equals(true));
        assertTrue(list.get(2).getLeft().equals("red"));
        assertTrue(list.get(2).getMiddle().equals(1));
        assertTrue(list.get(2).getRight().equals(false));
        assertTrue(list.get(3).getLeft().equals("green"));
        assertTrue(list.get(3).getMiddle().equals(1));
        assertTrue(list.get(3).getRight().equals(true));
        assertTrue(list.get(4).getLeft().equals("green"));
        assertTrue(list.get(4).getMiddle().equals(1));
        assertTrue(list.get(4).getRight().equals(false));
        assertTrue(list.get(5).getLeft().equals("green"));
        assertTrue(list.get(5).getMiddle().equals(0));
        assertTrue(list.get(5).getRight().equals(true));
    }
}

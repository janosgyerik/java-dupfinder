package com.janosgyerik.dupfinder.dupfinder;

import org.junit.Test;

import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.*;

public class DuplicateTrackerTest {
    static class Item {
        // Inherits default .equals and .hashCode
        // -> instances will not be equal, with distinct hash code
    }

    private DuplicateTracker<Item> newTracker() {
        return new DuplicateTracker<>();
    }

    @Test
    public void should_create_common_pool_for_2_items() {
        DuplicateTracker<Item> tracker = newTracker();
        tracker.add(new Item(), new Item());

        Set<Set<Item>> duplicates = tracker.getDuplicates();
        assertEquals(1, duplicates.size());
        assertEquals(2, duplicates.iterator().next().size());
    }

    @Test
    public void should_create_common_pool_for_3_items() {
        DuplicateTracker<Item> tracker = newTracker();
        Item item = new Item();
        tracker.add(item, new Item());
        tracker.add(item, new Item());

        Set<Set<Item>> duplicates = tracker.getDuplicates();
        assertEquals(1, duplicates.size());
        assertEquals(3, duplicates.iterator().next().size());
    }

    @Test
    public void should_find_pool_of_second_when_exists() {
        DuplicateTracker<Item> tracker = newTracker();
        Item item = new Item();
        tracker.add(item, new Item());
        tracker.add(new Item(), item);

        Set<Set<Item>> duplicates = tracker.getDuplicates();
        assertEquals(1, duplicates.size());
        assertEquals(3, duplicates.iterator().next().size());
    }

    @Test
    public void should_create_distinct_pools_for_2_pairs_of_items() {
        DuplicateTracker<Item> tracker = newTracker();
        tracker.add(new Item(), new Item());
        tracker.add(new Item(), new Item());

        Set<Set<Item>> duplicates = tracker.getDuplicates();
        assertEquals(2, duplicates.size());

        Iterator<Set<Item>> iterator = duplicates.iterator();
        assertEquals(2, iterator.next().size());
        assertEquals(2, iterator.next().size());
    }

    @Test
    public void should_merge_pools() {
        DuplicateTracker<Item> tracker = newTracker();

        Item ofPool1 = new Item();
        Item ofPool2 = new Item();

        tracker.add(ofPool1, new Item());
        tracker.add(ofPool2, new Item());
        tracker.add(ofPool1, ofPool2);

        Set<Set<Item>> duplicates = tracker.getDuplicates();
        assertEquals(1, duplicates.size());
        assertEquals(4, duplicates.iterator().next().size());
    }
}

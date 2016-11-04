package seedu.taskscheduler.commons.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

//@@author A0138696L

public class CollectionUtilTest {

    @Test
    public void isAnyNull_nullElement_trueReturned() {
        Object nullElement = null;
        assertTrue(CollectionUtil.isAnyNull("one", "two", nullElement, "three"));
    }
    
    @Test
    public void isAllNull_emptyList_trueReturned() {
        assertTrue(CollectionUtil.isAllNull());
    }
    
    @Test
    public void elementsAreUnique_duplicateElements_falseReturned() {
        final ArrayList<String> duplicateList = new ArrayList<>();
        duplicateList.add("duplicate");
        duplicateList.add("one");
        duplicateList.add("two");
        duplicateList.add("duplicate");
        duplicateList.add("three");
        assertFalse(CollectionUtil.elementsAreUnique(duplicateList));
    }
}

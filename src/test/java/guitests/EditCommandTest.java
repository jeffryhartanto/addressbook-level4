package guitests;

import static org.junit.Assert.*;

import org.junit.Test;

//@@author A0148145E

public class EditCommandTest extends TaskSchedulerGuiTest {

    @Test
    public void edit() {

        int indexToEdit = 1;
        edit_name_success(indexToEdit, "change name");

        edit_address_success(indexToEdit, "a changed location");

        edit_period_success(indexToEdit, "05-Oct-2016", "15-Nov-2016");

        edit_noIndex_success(indexToEdit);
    }

    private void edit_noIndex_success(int prevIndex) {
        String changedName = "new name";
        commandBox.runCommand("edit " + changedName);
        assertEquals(taskListPanel.navigateToTask(prevIndex - 1).getFullName(), changedName);
    }
    
    private void edit_period_success(int indexToEdit, String changedStartDate, String changedEndDate) {
        //edit the date
        commandBox.runCommand("edit " + indexToEdit + " from " + changedStartDate + " to " + changedEndDate);
        
        //confirm the editted card contains the right data
        assertTrue(taskListPanel.navigateToTask(indexToEdit - 1).getStartDate().contains(changedStartDate));
        assertTrue(taskListPanel.navigateToTask(indexToEdit - 1).getEndDate().contains(changedEndDate));
    }

    private void edit_address_success(int indexToEdit, String changedAddress) {
        //edit the address
        commandBox.runCommand("edit " + indexToEdit + " at " + changedAddress);
        
        //confirm the editted card contains the right data
        assertEquals(taskListPanel.navigateToTask(indexToEdit - 1).getLocation(), changedAddress);
    }

    private void edit_name_success(int indexToEdit, String changedName) {
        //edit the name
        commandBox.runCommand("edit " + indexToEdit + " " + changedName);
        
        //confirm the editted card contains the right data
        assertEquals(taskListPanel.navigateToTask(indexToEdit - 1).getFullName(), changedName);
    }
}
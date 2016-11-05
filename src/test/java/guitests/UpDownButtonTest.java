package guitests;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

//@@author A0140007B

public class UpDownButtonTest extends TaskSchedulerGuiTest {

    @Test
    public void buttonTest() {

        final String firstCommand = td.event.getAddCommand();
        final String rubbishCommand = "23r23r23534423";
        commandBox.runCommand(firstCommand);

        //retrieve previous entered command
        pressUp_retrievePreviousCommand_success(firstCommand); 
        
        commandBox.runCommand(rubbishCommand);
        pressUp_retrievePreviousCommand_success(rubbishCommand); 
        
        //holds at first ever command
        pressUp_multipleTimes_returnFirstCommand(firstCommand); 
        
        //retrieve the entered rubbish command
        pressDown_retrieveNextCommand_success(rubbishCommand); 
        
        //goes back to empty
        pressDown_multipleTimes_commandBoxEmpty(); 
        
        pressUp_retrievePreviousCommand_success(rubbishCommand); 
    }

    private void pressUp_multipleTimes_returnFirstCommand(String command) {
        for (int i = 0; i < 3; i++) {
            commandBox.pressUp();
        }
        assertEquals(commandBox.getCommandInput(), command);
    }

    private void pressDown_retrieveNextCommand_success(String rubbishCommand) {
        commandBox.pressDown();
        assertEquals(commandBox.getCommandInput(), rubbishCommand);
    }

    private void pressDown_multipleTimes_commandBoxEmpty() {
        for (int i = 0; i < 3; i++) {
            commandBox.pressDown();
        }
        assertEquals(commandBox.getCommandInput(),"");
    }

    private void pressUp_retrievePreviousCommand_success(String command) {
        commandBox.pressUp();
        assertEquals(commandBox.getCommandInput(), command);
    }

}
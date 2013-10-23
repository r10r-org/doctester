package org.doctester.rendermachine;
import org.doctester.testbrowser.TestBrowser;

public interface RenderMachine extends RenderMachineCommands {

    public void setTestBrowser(TestBrowser testBrowser);
    
    public void setFileName(String fileName);    
    
    public void finishAndWriteOut();

}
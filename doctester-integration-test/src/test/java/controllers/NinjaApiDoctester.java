package controllers;

import ninja.utils.NinjaConstant;
import ninja.utils.NinjaTestServer;

import org.doctester.DocTester;
import org.junit.After;
import org.junit.Before;

public abstract class NinjaApiDoctester extends DocTester {
	
	public NinjaTestServer ninjaTestServer;

    public NinjaApiDoctester() {
    }

    @Before
    public void startServerInTestMode() {
        System.setProperty(NinjaConstant.MODE_KEY_NAME, NinjaConstant.MODE_TEST);
        ninjaTestServer = new NinjaTestServer();
    }

    @After
    public void shutdownServer() {
    	System.clearProperty(NinjaConstant.MODE_KEY_NAME);
        ninjaTestServer.shutdown();
    }
    
    public String getTestServerUrl() {
   	
    	return ninjaTestServer.getServerAddress();
    	
    }

}

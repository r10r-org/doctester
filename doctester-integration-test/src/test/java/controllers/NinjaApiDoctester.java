package controllers;

import ninja.utils.NinjaConstant;
import ninja.utils.NinjaTestServer;

import org.doctester.DocTester;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public abstract class NinjaApiDoctester extends DocTester {
	
	public static NinjaTestServer ninjaTestServer;

    public NinjaApiDoctester() {
    }

    @BeforeClass
    public static void startServerInTestMode() {
        System.setProperty(NinjaConstant.MODE_KEY_NAME, NinjaConstant.MODE_TEST);
        ninjaTestServer = new NinjaTestServer();
    }

    @AfterClass
    public static void shutdownServer() {
    	System.clearProperty(NinjaConstant.MODE_KEY_NAME);
        ninjaTestServer.shutdown();
    }
    
    public String getTestServerUrl() {
   	
    	return ninjaTestServer.getServerAddress();
    	
    }

}

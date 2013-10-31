/**
 * Copyright (C) 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.doctester;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import static org.hamcrest.CoreMatchers.equalTo;

import org.doctester.testbrowser.Request;
import org.doctester.testbrowser.Url;
import org.junit.Assert;
import org.junit.Test;


public class DocTesterTest extends DocTester {
    
    @Test
    public void testThatIndexFileWritingWorks() throws Exception {

        doCreateSomeTestOuputForDoctest();
        
        finishDocTest();
        
        File expectedIndex = new File("target/site/doctester/index.html");
        
        Assert.assertTrue(expectedIndex.exists());
        
        assertThatFileContainsText(expectedIndex, "index");
        
        
    }
    
    @Test
    public void testThatIndexWritingOutDoctestFileWorks() throws Exception {
        
        String EXPECTED_FILENAME = DocTesterTest.class.getSimpleName() + ".html";
        
        doCreateSomeTestOuputForDoctest();
        
        finishDocTest();  

        File expectedDoctestfile = new File("target/site/doctester/" + EXPECTED_FILENAME);
        File expectedIndexFile = new File("target/site/doctester/index.html");
        
        // just a simple test to make sure the name is written somewhere in the file.
        assertThatFileContainsText(expectedDoctestfile, DocTesterTest.class.getSimpleName());
        
        // just a simple test to make sure that index.html contains a "link" to the doctest file.
        assertThatFileContainsText(expectedIndexFile, EXPECTED_FILENAME);

    }
    
    @Override
    public String getName() {
        return DocTesterTest.class.getSimpleName();
    }

    @Override
    public String getTestServerUrl() {
   
        return "NOTUSED";
    }
    
    
    public void doCreateSomeTestOuputForDoctest() {
    
            
        sayNextSection("another fun heading!");
        say("and a very long text...!");
       
    }
    
    public void assertThatFileContainsText(File file, String text) throws IOException {
    
        String content = Files.toString(file, Charsets.UTF_8);
        Assert.assertTrue(content.contains(text));
        
    }
    
}

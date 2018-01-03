/**
 * Copyright (C) 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.r10r.doctester;

import org.r10r.doctester.DocTester;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import org.r10r.doctester.rendermachine.RenderMachine;
import org.hamcrest.CoreMatchers;

import static org.hamcrest.CoreMatchers.is;
import org.junit.AfterClass;
import static org.junit.Assert.assertThat;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

public class DocTesterLifecycleTest extends DocTester {

    @Mock // A static mock to test lifecycle of class
    public static RenderMachine renderMachineMock = Mockito.mock(RenderMachine.class);

    @BeforeClass
    public void asserThatRenderEngineHasBeenInitialized() {
        Assert.assertNotNull(renderMachine);
        assertThat(renderMachine, CoreMatchers.equalTo(renderMachineMock));
    }

    @AfterClass
    public void asserThatRenderEngineHasBeenShutDownCorrectly() {
        Mockito.verify(renderMachineMock).finishAndWriteOut();
        Assert.assertNull(renderMachine);
    }

    @Override
    public RenderMachine getRenderMachine() {
        // to verify that something on renderMachineMock has been called
        return renderMachineMock;
    }

}

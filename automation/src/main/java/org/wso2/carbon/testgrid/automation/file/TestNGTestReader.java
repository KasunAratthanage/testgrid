/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.testgrid.automation.file;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.testgrid.automation.beans.JMeterTest;
import org.wso2.carbon.testgrid.automation.beans.Test;
import org.wso2.carbon.testgrid.automation.beans.TestNGTest;
import org.wso2.carbon.testgrid.automation.exceptions.TestReaderException;
import org.wso2.carbon.testgrid.automation.file.common.TestReader;
import org.wso2.carbon.testgrid.common.constants.TestGridConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This class is responsible for reading testNG tests for TestGrid framework.
 */
public class TestNGTestReader implements TestReader {

    private static final Log log = LogFactory.getLog(JMeterTestReader.class);
    private static final  String TESTNG_TEST_PATH = /*"src" + File.separator + "test" + File.separator +*/ "testng";

    /**
     * This method goes through the file structure and create an object model of the tests.
     *
     * @param file File object for the test folder.
     * @return a List of Test objects.
     */
    private List<Test> processTestStructure(File file) {
        List<Test> testsList = new ArrayList<>();
        String[] list = file.list();
        for (String solution : Arrays.asList(list)) {
            File tests = new File(file.getAbsolutePath() + File.separator + solution +
                    File.separator + TESTNG_TEST_PATH);
            TestNGTest test = new TestNGTest();

            test.setTestName(solution);
            List<String> testNGList = new ArrayList<>();
            if (tests.exists()) {
                for (String testFile : Arrays.asList(tests.list())) {
                    if (testFile.endsWith(TestGridConstants.TESTNG_SUFFIX)) {
                        testNGList.add(tests.getAbsolutePath() + File.separator + testFile);
                    }
                }
            }
            Collections.sort(testNGList);
            test.setTestNGJars(testNGList);
            testsList.add(test);

        }
        return testsList;
    }

    @Override
    public List<Test> readTests(String testLocation) throws TestReaderException {
        return processTestStructure(new File(testLocation));
    }
}

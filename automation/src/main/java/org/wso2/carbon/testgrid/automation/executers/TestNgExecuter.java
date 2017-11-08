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

package org.wso2.carbon.testgrid.automation.executers;

import org.testng.TestNG;
import org.wso2.carbon.testgrid.automation.exceptions.TestGridExecuteException;
import org.wso2.carbon.testgrid.automation.executers.common.TestExecuter;
import org.wso2.carbon.testgrid.common.Deployment;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * This class is responsible for Executing TestNG tests.
 */
public class TestNgExecuter implements TestExecuter {
    private String testGridFolder;
    private String testName;

    @Override
    public void execute(String jarFilePath, Deployment deployment) {
        File jarFile = new File(jarFilePath);
        String jarName = jarFile.getName().substring(0, jarFile.getName().lastIndexOf("."));

        loadJarToClasspath(jarFile);

        TestNG testng = new TestNG();
        testng.setTestJar(jarFilePath);
        testng.setOutputDirectory(testGridFolder + "/Results/TestNG/" + jarName);
        testng.run();
    }

    @Override
    public void init(String testGridFolder, String testName) throws TestGridExecuteException {
        this.testGridFolder = testGridFolder;
        this.testName = testName;
    }

    public static void loadJarToClasspath(File jarFile) {
        if (jarFile.isFile()) {
            URL url = null;
            try {
                url = jarFile.toURL();
                URL[] urls = new URL[] { url };
                ClassLoader cl = new URLClassLoader(urls);
                Thread.currentThread().setContextClassLoader(cl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method returns the jar file name given the absolute path.
     *
     * @param file the path of the file file.
     * @return name of the file file.
     */
    private String getJarName(String file) {
        String[] split = file.split("/");
        return split[split.length - 1];
    }

}

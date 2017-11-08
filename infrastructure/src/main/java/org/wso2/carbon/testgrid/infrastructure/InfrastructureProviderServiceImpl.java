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

package org.wso2.carbon.testgrid.infrastructure;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.testgrid.common.TestPlan;

import org.wso2.carbon.testgrid.utils.Util;

/**
 * This class creates the infrastructure for running tests
 */
public class InfrastructureProviderServiceImpl implements InfrastructureProviderService {

    private static final Log log = LogFactory.getLog(InfrastructureProviderServiceImpl.class);

    @Override
    public boolean createTestEnvironment(TestPlan testPlan) throws TestGridInfrastructureException {
        String testPlanLocation = testPlan.getHome() +"/test-grid-is-resources/DeploymentPatterns/" + testPlan.getDeploymentPattern();

        log.info("Initializing terraform...");
        Util.executeCommand("terraform init " + testPlanLocation + "/OpenStack", null);

        log.info("Creating the Kubernetes cluster...");
        Util.executeCommand("bash " + testPlanLocation + "/OpenStack/infra.sh", null);
        testPlan.setStatus(TestPlan.Status.INFRASTRUCTURE_READY);
        return true;
    }

    @Override
    public boolean removeTestEnvironment(TestPlan testPlan) throws TestGridInfrastructureException {
        String testPlanLocation = testPlan.getHome() +"/test-grid-is-resources/DeploymentPatterns/" + testPlan.getDeploymentPattern();
        System.out.println("Destroying test environment...");
        if(Util.executeCommand("sh " + testPlanLocation + "/OpenStack/cluster-destroy.sh", null)) {
            return true;
        }
        return false;
    }
}

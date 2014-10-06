/**
 * Copyright 2014 Antonio Robirosa

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package org.areco.ecommerce.deploymentscripts.core.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.tenant.MockTenant;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import junit.framework.Assert;

import org.areco.ecommerce.deploymentscripts.core.TenantDetector;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * It checks that the script configuration reader is handling correctly the conversion of the tenants.
 * 
 * @author arobirosa
 * 
 */
@UnitTest
public class TenantConversionInScriptConfigurationTest {

    private static final String JUNIT_TENANT_SCRIPT_NAME = "20141004_JUNIT_TENANT";

    private static final String MASTER_TENANT_SCRIPT_NAME = "20141004_MASTER_TENANT";

    private static final String DEPLOYMENT_SCRIPTS_FOLDER = "test/tenant-conversion-script-configuration/";

    @InjectMocks
    private final PropertyFileDeploymentScriptConfigurationReader configurationReader = new PropertyFileDeploymentScriptConfigurationReader() {

        @Override
        protected PropertyFileDeploymentScriptConfiguration createConfiguration() {
            return new PropertyFileDeploymentScriptConfiguration();
        }
    };

    @Mock
    private TenantDetector tenantDetector;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testJunitTenantInSingleTenantEnvironment() throws URISyntaxException {
        Mockito.when(tenantDetector.areWeInATestSystemWithOneSingleTenant()).thenReturn(Boolean.TRUE);
        Mockito.when(tenantDetector.getCurrentTenant()).thenReturn(new MockTenant("master"));
        Mockito.when(tenantDetector.getTenantByID(Mockito.eq("junit"))).thenReturn(null);
        assertTenantConversion("master", JUNIT_TENANT_SCRIPT_NAME);
    }

    @Test
    public void testMasterTenantInSingleTenantEnvironment() throws URISyntaxException {
        Mockito.when(tenantDetector.areWeInATestSystemWithOneSingleTenant()).thenReturn(Boolean.TRUE);
        Mockito.when(tenantDetector.getCurrentTenant()).thenReturn(new MockTenant("master"));
        Mockito.when(tenantDetector.getTenantByID(Mockito.eq("master"))).thenReturn(new MockTenant("master"));
        assertTenantConversion("master", MASTER_TENANT_SCRIPT_NAME);
    }

    @Test
    public void testJunitTenantInMultiTenantEnvironment() throws URISyntaxException {
        Mockito.when(tenantDetector.areWeInATestSystemWithOneSingleTenant()).thenReturn(Boolean.FALSE);
        Mockito.when(tenantDetector.getCurrentTenant()).thenReturn(new MockTenant("junit"));
        Mockito.when(tenantDetector.getTenantByID(Mockito.eq("junit"))).thenReturn(new MockTenant("junit"));
        assertTenantConversion("junit", JUNIT_TENANT_SCRIPT_NAME);
    }

    @Test
    public void testMasterTenantInMultiTenantEnvironment() throws URISyntaxException {
        Mockito.when(tenantDetector.areWeInATestSystemWithOneSingleTenant()).thenReturn(Boolean.FALSE);
        Mockito.when(tenantDetector.getCurrentTenant()).thenReturn(new MockTenant("master"));
        Mockito.when(tenantDetector.getTenantByID(Mockito.eq("master"))).thenReturn(new MockTenant("master"));
        assertTenantConversion("master", MASTER_TENANT_SCRIPT_NAME);
    }

    private void assertTenantConversion(final String expectedTenantID, final String deploymentScriptNameD) throws URISyntaxException {
        final URL scriptUrl = this.getClass().getClassLoader().getResource(DEPLOYMENT_SCRIPTS_FOLDER + deploymentScriptNameD);
        final PropertyFileDeploymentScriptConfiguration actualConfiguration = configurationReader.loadConfiguration(new File(scriptUrl.toURI()));
        Assert.assertEquals("The must be one tenant", 1, actualConfiguration.getAllowedTenants().size());
        Assert.assertEquals("The tenant has the wrong ID", expectedTenantID, actualConfiguration.getAllowedTenants().iterator().next().getTenantID());
    }
}

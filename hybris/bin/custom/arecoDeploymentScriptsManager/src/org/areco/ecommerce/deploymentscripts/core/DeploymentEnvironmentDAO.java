/**
 * Copyright 2014 Antonio Robirosa
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.areco.ecommerce.deploymentscripts.core;

import org.areco.ecommerce.deploymentscripts.model.DeploymentEnvironmentModel;

import java.util.Set;

/**
 * It manages the instances of the deployment environments.
 *
 * @author arobirosa
 */
public interface DeploymentEnvironmentDAO {
    /**
     * Return the environments with the given names.
     *
     * @param environmentNames
     *            Required
     * @return Never null.
     */
    Set<DeploymentEnvironmentModel> loadEnvironments(Set<String> environmentNames);

    /**
     * Returns the deployment environment where this server is running
     *
     * @return Never null
     */
    DeploymentEnvironmentModel getCurrent();
}

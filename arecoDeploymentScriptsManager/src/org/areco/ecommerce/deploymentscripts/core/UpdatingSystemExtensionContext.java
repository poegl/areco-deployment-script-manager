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
package org.areco.ecommerce.deploymentscripts.core;

import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.util.JspContext;

import org.apache.log4j.Logger;


/**
 * It represents the extension being update or initialize during the update system process. It is an immutable object.
 * 
 * @author arobirosa
 * 
 */
public class UpdatingSystemExtensionContext
{
	private static final Logger LOG = Logger.getLogger(UpdatingSystemExtensionContext.class);

	private final String extensionName;

	private final SystemSetup.Process process;

	/*
	 * It is used to log in HAC.
	 */
	private final JspContext jspContext;

	public UpdatingSystemExtensionContext(final String pExtensionName, final SystemSetup.Process pProcess,
			final JspContext pJspContext)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("pExtensionName", pExtensionName);
		ServicesUtil.validateParameterNotNullStandardMessage("pProcess", pProcess);
		this.extensionName = pExtensionName;
		this.process = pProcess;
		this.jspContext = pJspContext;
	}

	public UpdatingSystemExtensionContext(final String pExtensionName, final SystemSetup.Process pProcess)
	{
		this(pExtensionName, pProcess, null);
	}

	public String getExtensionName()
	{
		return extensionName;
	}

	public SystemSetup.Process getProcess()
	{
		return process;
	}

	protected JspContext getJspContext()
	{
		return jspContext;
	}

	@Override
	public String toString()
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("UpdatingSystemExtensionContext [extensionName=");
		builder.append(extensionName);
		builder.append(", process=");
		builder.append(process);
		builder.append(", jspContext=");
		builder.append(jspContext);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((extensionName == null) ? 0 : extensionName.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final UpdatingSystemExtensionContext other = (UpdatingSystemExtensionContext) obj;
		if (extensionName == null)
		{
			if (other.extensionName != null)
			{
				return false;
			}
		}
		else if (!extensionName.equals(other.extensionName))
		{
			return false;
		}
		return true;
	}

}

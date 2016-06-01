package org.fantom;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IExecutableExtensionFactory;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.osgi.util.NLS;
import org.fantom.internal.core.Pod;

public class FanExtension implements IExecutableExtension,
		IExecutableExtensionFactory {

	private String type;

	public Object create() throws CoreException {
		if (type == null) {
			throw new CoreException(new Status(IStatus.ERROR,
					FantomVM.PLUGIN_ID, "Type not specified in "
							+ describeConfigurationElement()));
		}
		try {
			if (!type.contains("::")) {
				List<String> pods = Pod.findInBundle(configurationElement
						.getContributor().getName());
				if (pods.size() != 1) {
					throw new CoreException(new Status(IStatus.ERROR,
							FantomVM.PLUGIN_ID, "Can't detect pod for "
									+ describeConfigurationElement()));
				}
				type = pods.get(0) + "::" + type;
			}
			final Object result = FantomVM.create(type);
			if (FantomVM.DEBUG) {
				System.out.println("FanExtension created " + type + " result -> " + result);
			}
			if (result instanceof IExecutableExtension) {
				((IExecutableExtension) result).setInitializationData(
						configurationElement, propertyName, data);
			}
			return result;
		} catch (FantomException t) {
			if (FantomVM.DEBUG) {
				t.printStackTrace();
			}
			throw new CoreException(new Status(IStatus.ERROR,
					FantomVM.PLUGIN_ID, NLS.bind(
							"Error instantiating \"{0}\": {1}", type, t
									.getCause().toString()), t.getCause()));
		}
	}

	private IConfigurationElement configurationElement;
	private String propertyName;
	private Object data;

	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {
		this.configurationElement = config;
		this.propertyName = propertyName;
		this.data = data;
		if (data instanceof String) {
			// data is pod::class string
			type = (String) data;
			if (FantomVM.DEBUG) {
				System.out.println("FanExtension for \"" + type + "\"");
			}
		} else {
			if (FantomVM.DEBUG) {
				System.out.println("FanExtension in "
						+ describeConfigurationElement() + " without type");
			}
		}
	}

	private String describeConfigurationElement() {
		return configurationElement.getContributor().getName() + "<"
				+ configurationElement.getName() + ">";
	}

}

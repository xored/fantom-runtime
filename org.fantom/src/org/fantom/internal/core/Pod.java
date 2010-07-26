package org.fantom.internal.core;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.fantom.FantomVM;
import org.osgi.framework.Bundle;

public class Pod extends ClassLoader {

	private static final String ATTR_NAME = "name"; //$NON-NLS-1$
	private static final String ATTR_LOCATION = "location"; //$NON-NLS-1$
	private static final String ATTR_ACTIVATOR = "activator"; //$NON-NLS-1$

	private static final String PODS_EXTPT = FantomVM.PLUGIN_ID + ".pod"; //$NON-NLS-1$

	private static final String SYS = "sys"; //$NON-NLS-1$

	public static List<Pod> load() {
		final List<Pod> pods = new ArrayList<Pod>();
		final IConfigurationElement[] elements = Platform
				.getExtensionRegistry().getConfigurationElementsFor(PODS_EXTPT);
		for (IConfigurationElement element : elements) {
			String name = element.getAttribute(ATTR_NAME);
			if (name == null || SYS.equals(name)) {
				continue;
			}
			String location = element.getAttribute(ATTR_LOCATION);
			String activator = element.getAttribute(ATTR_ACTIVATOR);
			String bundleId = element.getContributor().getName();
			Bundle bundle = Platform.getBundle(bundleId);
			pods.add(new Pod(name, bundle, location, activator));
		}
		return pods;
	}

	private final String name;
	private final Bundle bundle;
	private final String path;
	private final String activator;

	public Pod(String name, Bundle bundle, String path, String activator) {
		this.name = name;
		this.bundle = bundle;
		this.path = path;
		this.activator = activator;
	}

	public String getName() {
		return name;
	}

	public String getActivator() {
		return activator;
	}

	public File toFile() {
		final java.io.File javaFile;
		try {
			URL fileUrl = FileLocator.toFileURL(bundle.getEntry(path));
			javaFile = new java.io.File(fileUrl.getPath());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return javaFile;
	}

	@Override
	protected synchronized Class<?> loadClass(String name, boolean resolve)
			throws ClassNotFoundException {
		return loadClass(name);
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		return bundle.loadClass(name);
	}

	/**
	 * @param contributorName
	 * @return
	 */
	public static List<String> findInBundle(String contributorName) {
		final List<String> podNames = new ArrayList<String>();
		final IConfigurationElement[] elements = Platform
				.getExtensionRegistry().getConfigurationElementsFor(PODS_EXTPT);
		for (IConfigurationElement element : elements) {
			if (!contributorName.equals(element.getContributor().getName()))
				continue;
			String name = element.getAttribute(ATTR_NAME);
			if (name == null || SYS.equals(name)) {
				continue;
			}
			podNames.add(name);
		}
		return podNames;
	}

}

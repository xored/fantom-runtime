package org.fantom;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.osgi.util.NLS;
import org.fantom.internal.core.IFantomInstall;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class FantomVM extends Plugin {

	public static final boolean DEBUG = false;

	// The plug-in ID
	public static final String PLUGIN_ID = "org.fantom";

	// The shared instance
	private static FantomVM plugin;

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		final IConfigurationElement[] elements = Platform
				.getExtensionRegistry().getConfigurationElementsFor(
						PLUGIN_ID + ".install");
		for (IConfigurationElement element : elements) {
			IFantomInstall install;
			try {
				install = (IFantomInstall) element
						.createExecutableExtension("class");
			} catch (Exception e) {
				log("Error instantiating IFantomInstall", e);
				continue;
			}
			try {
				install.init();
			} catch (CoreException e) {
				log("Error initializing IFantomInstall", e);
				continue;
			}
			this.install = install;
			break;
		}
	}

	private IFantomInstall install = null;

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static FantomVM getDefault() {
		return plugin;
	}

	/**
	 * Creates the specified Fantom type.
	 * 
	 * @param typeName
	 * @return created instance
	 * @throws FantomException
	 */
	public static Object create(String typeName, Object... args)
			throws FantomException {
		return getDefault().install.createObject(typeName, args);
	}

	/**
	 * Creates the specified Fantom type. Possible exceptions are caught and
	 * <code>null</code> is returned.
	 * 
	 * @param typeName
	 * @return created instance or <code>null</code> if an exceptions occurs
	 */
	public static Object makeObject(String typeName, Object... args) {
		try {
			return create(typeName, args);
		} catch (FantomException t) {
			FantomVM.log(NLS.bind("Error instantiating \"{0}\": {1}", typeName,
					t.getCause().toString()), t.getCause());
		}
		return null;
	}

	public static void log(String message, Throwable th) {
		log(new Status(IStatus.ERROR, PLUGIN_ID, IStatus.ERROR, message, th));
	}

	public static void log(IStatus status) {
		getDefault().getLog().log(status);
	}

}

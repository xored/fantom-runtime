package org.fantom.internal.sys;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;

public class Activator extends Plugin
{

  // The plug-in ID
  public static final String PLUGIN_ID = "org.fantom.sys";

  // The shared instance
  private static Activator plugin;

  @Override
  public void start(BundleContext context) throws Exception
  {
    super.start(context);
    plugin = this;
  }

  @Override
  public void stop(BundleContext context) throws Exception
  {
    plugin = null;
    super.stop(context);
  }

  /**
   * Returns the shared instance
   * 
   * @return the shared instance
   */
  public static Activator getDefault()
  {
    return plugin;
  }

  public static void error(String message)
  {
    plugin.getLog().log(
        new Status(IStatus.ERROR, PLUGIN_ID, IStatus.ERROR, message, null));
  }

  public static void error(String message, Throwable t)
  {
    plugin.getLog().log(
        new Status(IStatus.ERROR, PLUGIN_ID, IStatus.ERROR, message, t));
  }

}

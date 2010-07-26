package org.fantom.internal.sys;

import java.io.File;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.osgi.util.NLS;
import org.fantom.FantomVM;
import org.fantom.internal.core.Pod;

import fan.sys.Env;
import fan.sys.List;
import fan.sys.Method;
import fan.sys.PodEvent;
import fan.sys.Sys;
import fan.sys.Type;
import fan.sys.UnknownPodErr;
import fan.sys.UnresolvedErr;

public class EquinoxEnv extends Env
{

  private final Map<String, Pod> pods = new HashMap<String, Pod>();

  public EquinoxEnv()
  {
    super(Sys.bootEnv);
    for (Pod pod : Pod.load())
    {
      pods.put(pod.getName(), pod);
    }
  }

  @Override
  public fan.sys.File findPodFile(String name)
  {
    final Pod pod = pods.get(name);
    if (pod == null)
    {
      throw UnknownPodErr.make(name).val;
    }
    final File file = pod.toFile();
    if (file == null)
    {
      throw UnresolvedErr.make("Pod file not found " + name).val;
    }
    return new BundleFile(file);
  }

  @Override
  public List findAllPodNames()
  {
    List acc = new List(Sys.StrType);
    for (String podName : pods.keySet())
    {
      acc.add(podName);
    }
    return acc;
  }

  @Override
  public ClassLoader getExtClassLoader(String podName)
  {
    final Pod pod = pods.get(podName);
    if (pod != null)
    {
      return pod;
    }
    else
    {
      return super.getExtClassLoader(podName);
    }
  }

  @Override
  public void fireEvent(EventObject event)
  {
    super.fireEvent(event);
    if (FantomVM.DEBUG)
      System.out.println(event);
    if (event instanceof PodEvent)
    {
      final PodEvent podEvent = (PodEvent) event;
      if (podEvent.kind() == PodEvent.STARTED)
      {
        callActivator(podEvent);
      }
    }
  }

  private void callActivator(final PodEvent podEvent)
  {
    final Pod pod = pods.get(podEvent.podName());
    if (pod == null || pod.getActivator() == null)
    {
      return;
    }
    final Type activatorType = podEvent.pod().type(pod.getActivator(), false);
    if (activatorType == null)
    {
      Activator.error(NLS.bind("Activator \"{0}\" not found in pod {1}", pod
          .getActivator(), podEvent.podName()));
      return;
    }
    final Method startMethod = activatorType.method("start", false);
    if (startMethod == null)
    {
      Activator.error(NLS.bind(
          "Method start() not found in activator \"{0}\" of {1}", pod
              .getActivator(), podEvent.podName()));
      return;
    }
    try
    {
      if (startMethod.isStatic())
      {
        startMethod.call();
      }
      else
      {
        Object activator = activatorType.make();
        startMethod.callOn(activator, null);
      }
    }
    catch (Exception e)
    {
      Activator.error(NLS.bind("Error executing activator \"{0}\" of {1}", pod
          .getActivator(), podEvent.podName()), e);
      return;
    }
  }
}

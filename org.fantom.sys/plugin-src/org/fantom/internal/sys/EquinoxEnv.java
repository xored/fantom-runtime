package org.fantom.internal.sys;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.fantom.internal.core.Pod;

import fan.sys.Env;
import fan.sys.List;
import fan.sys.Sys;
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
  public ClassLoader getJavaClassLoader(String callingPod)
  {
    final Pod pod = pods.get(callingPod);
    if (pod != null)
    {
      return pod;
    }
    else
    {
      return super.getJavaClassLoader(callingPod);
    }
  }
  
  @Override
  public Class loadJavaClass(String className, String loadingPod) throws Exception
  {
    return getJavaClassLoader(loadingPod).loadClass(className);
  }
}

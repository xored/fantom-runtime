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

public class EquinoxEnv extends Env {
	private final Map<String, Pod>           pods      = new HashMap<String, Pod>();
	private final Map<String, fan.sys.File>  jstub     = new HashMap<String, fan.sys.File>();

	public EquinoxEnv() {
		super(Sys.bootEnv);
		for (Pod pod : Pod.load()) {
			pods.put(pod.getName(), pod); //gets all pods from plugins
		}
	}

	@Override
	public fan.sys.File findPodFile(String name) {
		if (jstub != null && jstub.containsKey(name))
			return jstub.get(name);
		
		Pod pod = pods.get(name);
		if (pod == null) {
		  String str = "";
		  
		  if (jstub.size() > 0) {
        for (String p : pods.keySet())
          str = str + p + " ";
        throw UnknownPodErr.make(name + " : choose from " + str);
		  }
		  
		  for (String p : pods.keySet()) {
		    str = str + p + " ";
		  }
			throw UnknownPodErr.make(name + " : choose from " + str);
		}

		final File file = pod.toFile();
		if (file == null)
			throw UnresolvedErr.make("Pod file not found " + name);

		return new BundleFile(file);
	}

	@Override
	public List findAllPodNames() {
		List acc = new List(Sys.StrType);
		for (String podName : pods.keySet()) {
			acc.add(podName);
		}
		return acc;
	}

	//these problems - env.java applyaptch
	@Override
	public ClassLoader getJavaClassLoader(String callingPod) {
		//return Pod:ClassLoader 
		final Pod pod = pods.get(callingPod);
		if (pod != null)
			return pod;
		
		return super.getJavaClassLoader(callingPod);
	}
	
	@Override
	public Class loadJavaClass(String className, String loadingPod) throws Exception {
		return getJavaClassLoader(loadingPod).loadClass(className); //load class from own classloader
	}
	
	public void addJStubPod(String name, fan.sys.File pod) {
		jstub.put(name, pod);
	}
	public void removeJStubPod() {
		jstub.clear();
	}
}

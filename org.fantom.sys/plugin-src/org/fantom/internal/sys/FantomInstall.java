package org.fantom.internal.sys;

import org.eclipse.core.runtime.CoreException;
import org.fantom.FantomException;
import org.fantom.internal.core.FanHome;
import org.fantom.internal.core.IFantomInstall;
import org.osgi.framework.Bundle;

import fan.sys.Err;
import fan.sys.Sys;
import fan.sys.Type;

public class FantomInstall implements IFantomInstall {
  
	public Object createObject(String typeName, Object... args) throws FantomException {
		if (typeName.indexOf("::") < 0)
			typeName += "::Main";
	
		try {
			Type type = Type.find(typeName, true);
			if (args != null && args.length != 0) {
				return type.make(new fan.sys.List(Sys.ObjType, args));
			}
			else {
				return type.make();
			}
		}
		catch (Err err) {
			while (err.cause() != null) {
				err = err.cause();
			}
			if (err.toJava() != err) {
				throw new FantomException(err.toJava());
			}
			throw new FantomException(err);
		}
		catch (RuntimeException e) {
			throw new FantomException(e);
		}
	}

	public void init() throws CoreException {
		Activator plugin = Activator.getDefault();
		FanHome home = new FanHome(plugin.getStateLocation().toFile());
		Bundle self = plugin.getBundle();
		home.init(self, "etc");
		home.init(self, "lib");
		System.setProperty("fan.home", home.getAbsolutePath());
		final Class<EquinoxEnv> envClass = EquinoxEnv.class;
		System.setProperty("FAN_ENV", "[java]" + envClass.getPackage().getName() + "::" + envClass.getSimpleName());
		Sys.boot();
	}
}

package org.fantom.fwt.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.eclipse.swt.widgets.Widget;

public class FwtUtil {

	public static void addToSwt(Object fanControl, Object swtParent)
			throws Exception {
		Object peer = getPeer(fanControl);
		findMethod(peer).invoke(peer, fanControl, swtParent);
	}

	private static Object getPeer(Object fanControl) throws Exception {
		return fanControl.getClass().getField("peer").get(fanControl);
	}

	private static Method findMethod(Object peer) {
		for (Method method : peer.getClass().getMethods()) {
			if (method.getName() == "attach"
					&& method.getParameterTypes().length == 2)
				return method;
		}
		return null;
	}

	public static Widget widget(Object fanControl) throws Exception {
		Object peer = getPeer(fanControl);
		Field field = findField(peer.getClass(), "control");
		field.setAccessible(true);
		return (Widget) field.get(peer);
	}

	private static Field findField(Class c, String name) {
		if (c == null)
			return null;
		for (Field f : c.getDeclaredFields()) {
			// System.out.println(f.getName());
			if (f.getName().equals(name))
				return f;
		}
		return findField(c.getSuperclass(), name);
	}

	public static void dispose(Object object) {
		try {
			Object fwt = getFwt(object);
			Class<?> imageClass = object.getClass().getClassLoader().loadClass(
					"fan.gfx.Image");
			Method dispose = fwt.getClass().getMethod("dispose", imageClass);
			dispose.invoke(fwt, object);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Object getFwt(Object object) {
		if (fwt == null) {
			try {
				Class<?> clazz = object.getClass().getClassLoader().loadClass(
						"fan.fwt.Fwt");
				Method method = clazz.getMethod("get");
				fwt = method.invoke(null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return fwt;
	}

	private static Object fwt;

}

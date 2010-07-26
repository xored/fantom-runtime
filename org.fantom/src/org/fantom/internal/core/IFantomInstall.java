package org.fantom.internal.core;

import org.eclipse.core.runtime.CoreException;
import org.fantom.FantomException;

public interface IFantomInstall {

	void init() throws CoreException;

	Object createObject(String typeName, Object... args) throws FantomException;

}

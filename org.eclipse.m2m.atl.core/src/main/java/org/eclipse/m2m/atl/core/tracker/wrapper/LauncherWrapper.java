package org.eclipse.m2m.atl.core.tracker.wrapper;

import java.util.Map;

import org.eclipse.m2m.atl.core.launch.ILauncher;

public class LauncherWrapper extends ExtensionWrapper<ILauncher>{

	public LauncherWrapper(ILauncher iClass, Map<String, Object> properties) {
		super(iClass, properties);
	}

	public String getMode() {
		Object o = getProperty("mode");
		return (o==null) ? null : (String)o;
	}

	public String getDescription() {
		Object o = getProperty("description");
		return (o==null) ? null : (String)o;
	}

	public String getParent() {
		Object o = getProperty("parent");
		return (o==null) ? null : (String)o;
	}

}

package org.eclipse.m2m.atl.core.tracker.wrapper;

import java.util.Map;

import org.eclipse.m2m.atl.core.IInjector;

public class InjectorWrapper extends ExtensionWrapper<IInjector> {

	public InjectorWrapper(IInjector iClass, Map<String, Object> properties) {
		super(iClass, properties);
	}

}

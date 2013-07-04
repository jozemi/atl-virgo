package org.eclipse.m2m.atl.core.tracker.wrapper;

import java.util.Map;

import org.eclipse.m2m.atl.core.IExtractor;

public class ExtractorWrapper extends ExtensionWrapper<IExtractor>{

	public ExtractorWrapper(IExtractor iClass, Map<String, Object> properties) {
		super(iClass, properties);
	}


}

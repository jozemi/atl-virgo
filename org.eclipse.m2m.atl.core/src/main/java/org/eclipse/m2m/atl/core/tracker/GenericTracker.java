package org.eclipse.m2m.atl.core.tracker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.m2m.atl.core.IExtractor;
import org.eclipse.m2m.atl.core.IInjector;
import org.eclipse.m2m.atl.core.launch.ILauncher;
import org.eclipse.m2m.atl.core.tracker.wrapper.ExtensionWrapper;
import org.eclipse.m2m.atl.core.tracker.wrapper.ExtractorWrapper;
import org.eclipse.m2m.atl.core.tracker.wrapper.LauncherWrapper;
import org.eclipse.m2m.atl.core.tracker.wrapper.InjectorWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericTracker {

	private static final String KEY = "epoint";
	
	private static final String LAUNCHERS_EXTENSION_POINT = "org.eclipse.m2m.atl.core.launcher"; //$NON-NLS-1$

	private static final String INJECTORS_EXTENSION_POINT = "org.eclipse.m2m.atl.core.injector"; //$NON-NLS-1$

	private static final String EXTRACTORS_EXTENSION_POINT = "org.eclipse.m2m.atl.core.extractor"; //$NON-NLS-1$

	private static final String MODELS_EXTENSION_POINT = "org.eclipse.m2m.atl.core.model"; //$NON-NLS-1$
	
	private Map<String,ExtensionWrapper<?>> launcherTracker = new HashMap<String,ExtensionWrapper<?>>();
	
	private Map<String,ExtensionWrapper<?>> injectorTracker =  new HashMap<String,ExtensionWrapper<?>>();
	
	private Map<String,ExtensionWrapper<?>> extractorTracker = new HashMap<String,ExtensionWrapper<?>>();
	
	private Map<String,ExtensionWrapper<?>> modelTracker = new HashMap<String,ExtensionWrapper<?>>();
	
	
	// LOG
	private static final Logger LOG = LoggerFactory.getLogger(GenericTracker.class);
	
	/**
	 * Gets the properly map tracker from his Extension Point name
	 * @param epoint
	 * @return
	 */
	private Map<String,ExtensionWrapper<?>> getTracker(String epoint){
		
		if (epoint.equals(LAUNCHERS_EXTENSION_POINT)){
			return launcherTracker;
		}
		if (epoint.equals(INJECTORS_EXTENSION_POINT)){
			return injectorTracker;
		}
		if (epoint.equals(EXTRACTORS_EXTENSION_POINT)){
			return extractorTracker;
		}
		if (epoint.equals(MODELS_EXTENSION_POINT)){
			return modelTracker;
		}
		
		LOG.error("Extension point type {1} not found ",epoint);
		
		return null;
	}
	
	/**
	 * Register a IExtractor
	 * 
	 * @param l
	 * @param properties
	 */
	public void register(IExtractor l, Map<String, Object> properties){
		
		ExtractorWrapper ew = new ExtractorWrapper(l,properties);		
		Map<String,ExtensionWrapper<?>> tracker = getTracker((String)properties.get(KEY));
		
		doRegister(ew, tracker);
	}
	
	/**
	 * Register a IInjector
	 * 
	 * @param l
	 * @param properties
	 */
	public void register(IInjector i, Map<String, Object> properties){
		
		InjectorWrapper iw = new InjectorWrapper(i,properties);		
		Map<String,ExtensionWrapper<?>> tracker = getTracker((String)properties.get(KEY));
		
		doRegister(iw, tracker);
	}
	
	/**
	 * Register a IInjector
	 * 
	 * @param l
	 * @param properties
	 */
	public void register(ILauncher il, Map<String, Object> properties){
		
		LauncherWrapper ilw = new LauncherWrapper(il,properties);		
		Map<String,ExtensionWrapper<?>> tracker = getTracker((String)properties.get(KEY));
		
		doRegister(ilw, tracker);
	}

	//TODO: Model tracker
	
	
	/**
	 * Unregister 
	 * @param l
	 * @param properties
	 */
	// FIXME: Try
	public void unRegister(Object l, Map<String, Object> properties){
		
		String name = (String)properties.get("name");
		Map<String,ExtensionWrapper<?>> tracker = getTracker((String)properties.get(KEY));
		
		doUnregister(name, tracker);
		
	}
	
	

	/**
	 * Effective unregister of the element
	 * @param name
	 * @param tracker
	 */
	private void doUnregister(String name, Map<String, ExtensionWrapper<?>> tracker) {
		if (tracker.containsKey(name)){
			tracker.remove(name);
			LOG.debug("Element {1} unregistered",name);
		}else{
			LOG.error("Element {1} was not previously registered",name);
		}
	}


	/** 
	 * Do the registration
	 * @param ew
	 * @param tracker
	 */
	private void doRegister(ExtensionWrapper<?> ew, Map<String, ExtensionWrapper<?>> tracker) {
		if (!tracker.containsKey(ew.getName())){
			tracker.put(ew.getName(), ew);
			LOG.debug("Element {1} registered",ew.getiClass());
		}else{
			LOG.error("Element {1} was not registered",ew.getiClass());
		}
	}

	/**
	 * 
	 * @param extensionId
	 * @param executableExtensionName
	 * @param extensionName
	 * @return
	 */
	public Object getClass(String extensionId, String executableExtensionName, String extensionName) {
		Map<String,ExtensionWrapper<?>> tracker = getTracker(extensionId);
		ExtensionWrapper<?> ew = tracker.get(extensionName);
		if (ew == null){
			LOG.error("Extension name {1} not found", extensionName);
			return null;
		}
		return ew.getiClass();
	}

	public String[] getExtensionsNames(String extensionId) {
		Map<String,ExtensionWrapper<?>> tracker = getTracker(extensionId);
		return (String[])tracker.keySet().toArray();
	}

	private  Map<String,ExtensionWrapper<?>> getLauncherTracker(){
		return launcherTracker;
	}
	
	/**
	 * Returns the available launchers names.
	 * 
	 * @param mode
	 *            the launch mode restriction
	 * @return the available launchers names
	 */
	public String[] getLaunchersNames(String mode) {
		
		List<String> names = new ArrayList<String>();
		Map<String,ExtensionWrapper<?>> lt = getLauncherTracker();
		
		for (String name : lt.keySet()){
						
			LauncherWrapper lw = (LauncherWrapper)lt.get(name);
			String launcherMode = lw.getMode();
			
			if (mode == null || (mode != null && (mode.equals(launcherMode) || launcherMode == null))) {
				names.add(name);
			}
		}
		
		Collections.<String> sort(names);
		return names.toArray(new String[] {});

	}

	public Map<String, String> getLauncherOptions(String launcherName, String mode) {
		
		Map<String,ExtensionWrapper<?>> lt = getLauncherTracker();
		LauncherWrapper lw = (LauncherWrapper)lt.get(launcherName);
		
		Map<String, String> optionsMap = new HashMap<String, String>();
		
		String optionMode = lw.getMode();
		if (mode == null
				|| (mode != null && (mode.equals(optionMode) || optionMode == null))) {
			optionsMap.put(lw.getName(), lw.getDescription()); 
		}
		String parentLauncher = lw.getParent();
		if (parentLauncher != null) {
			optionsMap.putAll(getLauncherOptions(parentLauncher, mode));
		}
		return optionsMap;
		
		
	}
	
	
}

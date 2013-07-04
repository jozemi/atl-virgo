/*******************************************************************************
 * Copyright (c) 2008, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Intelliment Security - Adapted for use Eclipse Blueprint services 
 *******************************************************************************/
package org.eclipse.m2m.atl.core.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.m2m.atl.core.ATLCoreException;
import org.eclipse.m2m.atl.core.IExtractor;
import org.eclipse.m2m.atl.core.IInjector;
import org.eclipse.m2m.atl.core.ModelFactory;
import org.eclipse.m2m.atl.core.launch.ILauncher;
import org.eclipse.m2m.atl.core.tracker.GenericTracker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The CoreService utility class provides a way to use extension points in order to retrieve the needed
 * extensions:
 * <ul>
 * <li>{@link ILauncher}</li>
 * <li>{@link IInjector}</li>
 * <li>{@link IExtractor}</li>
 * <li>{@link ModelFactory}</li>
 * </ul>
 * For each extension class type, a method returns the list of all detected extensions names.
 * 
 * @author <a href="mailto:william.piers@obeo.fr">William Piers</a>
 * Modified by <a href="jmperez@intellimentsec.com">Jose Miguel Perez</a>
 */
public final class CoreService {

	private static final String LAUNCHERS_EXTENSION_POINT = "org.eclipse.m2m.atl.core.launcher"; //$NON-NLS-1$

	private static final String INJECTORS_EXTENSION_POINT = "org.eclipse.m2m.atl.core.injector"; //$NON-NLS-1$

	private static final String EXTRACTORS_EXTENSION_POINT = "org.eclipse.m2m.atl.core.extractor"; //$NON-NLS-1$

	private static final String MODELS_EXTENSION_POINT = "org.eclipse.m2m.atl.core.model"; //$NON-NLS-1$

	private static Map<String, Class<? extends ILauncher>> launcherRegistry = new HashMap<String, Class<? extends ILauncher>>();

	private static Map<String, Class<? extends IInjector>> injectorRegistry = new HashMap<String, Class<? extends IInjector>>();

	private static Map<String, Class<? extends IExtractor>> extractorRegistry = new HashMap<String, Class<? extends IExtractor>>();

	private static Map<String, Class<? extends ModelFactory>> factoryRegistry = new HashMap<String, Class<? extends ModelFactory>>();

	private static GenericTracker tracker;
	
	public static GenericTracker getTracker() {
		return tracker;
	}

	public static void setTracker(GenericTracker tracker) {
		CoreService.tracker = tracker;
	}

	// LOG
	private static final Logger LOG = LoggerFactory.getLogger(CoreService.class);
	

	
	private CoreService() {
		super();
	}

	/**
	 * Registers a launcher in the launcherRegistry.
	 * 
	 * @param launcher
	 *            the launcher
	 * @deprecated Use {@link #registerLauncher(String, Class)} instead.
	 */
	public static void registerLauncher(ILauncher launcher) {
		registerLauncher(launcher.getName(), launcher.getClass());
	}

	

	/**
	 * Registers a launcher in the launcherRegistry.
	 * 
	 * @param launcherName
	 *            the launcher name
	 * @param launcherClass
	 *            the launcher class
	 */
	public static void registerLauncher(String launcherName, Class<? extends ILauncher> launcherClass) {
		launcherRegistry.put(launcherName, launcherClass);
	}

	/**
	 * Registers an injector in the injectorRegistry.
	 * 
	 * @param name
	 *            the injector name
	 * @param injector
	 *            the injector
	 * @deprecated Use {@link #registerInjector(String, Class)} instead.
	 */
	public static void registerInjector(String name, IInjector injector) {
		registerInjector(name, injector.getClass());
	}

	/**
	 * Registers an injector in the injectorRegistry.
	 * 
	 * @param injectorName
	 *            the injector name
	 * @param injectorClass
	 *            the injector class
	 */
	public static void registerInjector(String injectorName, Class<? extends IInjector> injectorClass) {
		injectorRegistry.put(injectorName, injectorClass);
	}

	/**
	 * Registers an extractor in the extractorRegistry.
	 * 
	 * @param name
	 *            the extractor name
	 * @param extractor
	 *            the extractor
	 * @deprecated Use {@link #registerExtractor(String, Class)} instead.
	 */
	public static void registerExtractor(String name, IExtractor extractor) {
		registerExtractor(name, extractor.getClass());
	}

	/**
	 * Registers an extractor in the extractorRegistry.
	 * 
	 * @param extractorName
	 *            the extractor name
	 * @param extractorClass
	 *            the extractor class
	 */
	public static void registerExtractor(String extractorName, Class<? extends IExtractor> extractorClass) {
		extractorRegistry.put(extractorName, extractorClass);
	}

	/**
	 * Registers a factory in the factoryRegistry.
	 * 
	 * @param name
	 *            the factory name
	 * @param factoryClass
	 *            the factory class
	 */
	public static void registerFactory(String name, Class<? extends ModelFactory> factoryClass) {
		factoryRegistry.put(name, factoryClass);
	}

	/**
	 * Creates a new {@link ModelFactory} with the given name.
	 * 
	 * @param name
	 *            the factory name
	 * @return the new ModelFactory
	 * @deprecated Use {@link #getModelFactory(String)} instead.
	 */
	public static ModelFactory createModelFactory(String name) throws ATLCoreException {
		return getModelFactory(name);
	}

	/**
	 * Creates a new {@link ModelFactory} with the given name.
	 * 
	 * @param name
	 *            the factory name
	 * @return the new ModelFactory
	 */
	public static ModelFactory getModelFactory(String name) throws ATLCoreException {
		if (factoryRegistry.containsKey(name)) {
			try {
				return factoryRegistry.get(name).newInstance();
			} catch (IllegalAccessException e) {
				throw new ATLCoreException(e.getMessage(), e);
			} catch (InstantiationException e) {
				throw new ATLCoreException(e.getMessage(), e);
			}
		} else {
			ModelFactory factory = (ModelFactory)getExtensionClass(MODELS_EXTENSION_POINT,
					"modelFactory", name); //$NON-NLS-1$
			if (factory != null) {
				factoryRegistry.put(name, factory.getClass());
				return factory;
			} else {
				throw new ATLCoreException(MODELS_EXTENSION_POINT + " " + name //$NON-NLS-1$
						+ " not found, check the spelling or register it manually"); //$NON-NLS-1$
			}
		}
	}

	/**
	 * Returns the launcher matching the given name.
	 * 
	 * @param name
	 *            the launcher name
	 * @return the launcher matching the given name
	 * @throws ATLCoreException
	 */
	public static ILauncher getLauncher(String name) throws ATLCoreException {
		String realName = getCompatibleLauncherName(name);
		if (launcherRegistry.containsKey(realName)) {
			try {
				return launcherRegistry.get(realName).newInstance();
			} catch (IllegalAccessException e) {
				throw new ATLCoreException(e.getMessage(), e);
			} catch (InstantiationException e) {
				throw new ATLCoreException(e.getMessage(), e);
			}
		} else {
			ILauncher launcher = (ILauncher)getExtensionClass(LAUNCHERS_EXTENSION_POINT, "class", realName); //$NON-NLS-1$
			if (launcher != null) {
				launcherRegistry.put(realName, launcher.getClass());
				return launcher;
			} else {
				throw new ATLCoreException(LAUNCHERS_EXTENSION_POINT + " " + realName //$NON-NLS-1$
						+ " not found, check the spelling or register it manually"); //$NON-NLS-1$
			}
		}
	}

	/**
	 * Returns the injector matching the given name.
	 * 
	 * @param name
	 *            the injector name
	 * @return the injector matching the given name
	 * @throws ATLCoreException
	 */
	public static IInjector getInjector(String name) throws ATLCoreException {
		if (injectorRegistry.containsKey(name)) {
			try {
				return injectorRegistry.get(name).newInstance();
			} catch (IllegalAccessException e) {
				throw new ATLCoreException(e.getMessage(), e);
			} catch (InstantiationException e) {
				throw new ATLCoreException(e.getMessage(), e);
			}
		} else {
			IInjector injector = (IInjector)getExtensionClass(INJECTORS_EXTENSION_POINT, "class", name); //$NON-NLS-1$
			if (injector != null) {
				injectorRegistry.put(name, injector.getClass());
				return injector;
			} else {
				throw new ATLCoreException(INJECTORS_EXTENSION_POINT + " " + name //$NON-NLS-1$
						+ " not found, check the spelling or register it manually"); //$NON-NLS-1$
			}
		}
	}

	/**
	 * Returns the extractors matching the given name.
	 * 
	 * @param name
	 *            the extractors name
	 * @return the extractors matching the given name
	 * @throws ATLCoreException
	 */
	public static IExtractor getExtractor(String name) throws ATLCoreException {
		if (extractorRegistry.containsKey(name)) {
			try {
				return extractorRegistry.get(name).newInstance();
			} catch (IllegalAccessException e) {
				throw new ATLCoreException(e.getMessage(), e);
			} catch (InstantiationException e) {
				throw new ATLCoreException(e.getMessage(), e);
			}
		} else {
			IExtractor extractor = (IExtractor)getExtensionClass(EXTRACTORS_EXTENSION_POINT, "class", name); //$NON-NLS-1$
			if (extractor != null) {
				extractorRegistry.put(name, extractor.getClass());
				return extractor;
			} else {
				throw new ATLCoreException(EXTRACTORS_EXTENSION_POINT + " " + name //$NON-NLS-1$
						+ " not found, check the spelling or register it manually"); //$NON-NLS-1$
			}
		}
	}
	

	private static Object getExtensionClass(String extensionId, String executableExtensionName, String extensionName) throws ATLCoreException {
		return getTracker().getClass(extensionId,executableExtensionName,extensionName);
	}

	private static String[] getExtensionsNames(String extensionId) {
		return getTracker().getExtensionsNames(extensionId);
	}

	/**
	 * Returns the available launchers names.
	 * 
	 * @return the available launchers names
	 */
	public static String[] getLaunchersNames() {
		return getLaunchersNames(null);
	}

	/**
	 * Returns the available launchers names.
	 * 
	 * @param mode
	 *            the launch mode restriction
	 * @return the available launchers names
	 */
	public static String[] getLaunchersNames(String mode) {
		
		return getTracker().getLaunchersNames(mode);
		
	}

	/**
	 * Returns the available injectors names.
	 * 
	 * @return the available injectors names
	 */
	public static String[] getInjectorsNames() {
		return getExtensionsNames(INJECTORS_EXTENSION_POINT);
	}

	/**
	 * Returns the available extractors names.
	 * 
	 * @return the available extractors names
	 */
	public static String[] getExtractorsNames() {
		return getExtensionsNames(EXTRACTORS_EXTENSION_POINT);
	}

	/**
	 * Returns the available model factories names.
	 * 
	 * @return the available model factories names
	 */
	public static String[] getModelFactoriesNames() {
		return getExtensionsNames(MODELS_EXTENSION_POINT);
	}

	/**
	 * Returns the options Map (id, description) for the given launcher.
	 * 
	 * @param launcherName
	 *            the launcher name
	 * @return the options Map
	 */
	public static Map<String, String> getLauncherOptions(String launcherName) {
		return getLauncherOptions(launcherName, null);
	}

	/**
	 * Returns the options Map (id, description) for the given launcher, in the given mode.
	 * 
	 * @param launcherName
	 *            the launcher name
	 * @param mode
	 *            the launch mode
	 * @return the options Map
	 */
	public static Map<String, String> getLauncherOptions(String launcherName, String mode) {
		
		return getTracker().getLauncherOptions(launcherName, mode);
		
			
	}

	/**
	 * Converts the old Regular-VM name.
	 * 
	 * @param name
	 *            the launcher name
	 * @return the converted name
	 */
	public static String getCompatibleLauncherName(String name) {
		if (name.equals("Regular VM (with debugger)")) { //$NON-NLS-1$
			return "Regular VM"; //$NON-NLS-1$
		}
		return name;
	}

}

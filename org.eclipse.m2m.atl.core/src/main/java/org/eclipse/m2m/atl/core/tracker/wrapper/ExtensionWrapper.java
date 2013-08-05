package org.eclipse.m2m.atl.core.tracker.wrapper;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author José Miguel Pérez <jmperez@intellimentsec.com>
 *
 * @param <E>
 */
public abstract class ExtensionWrapper<E> {
	
	private E iClass;
	
	private Map<String,Object> properties;
	
	private String name;
	
	// LOG
	private static final Logger LOG = LoggerFactory.getLogger(ExtensionWrapper.class);

	
	public ExtensionWrapper(E iClass, Map<String,Object> properties){
		this.iClass = iClass;
		this.properties = properties;
		name = (String)properties.get("name");
		if (name == null){
			LOG.error("No name found in properties for {1}",iClass);
		}
	}
	
	public void setProperties(Map<String,Object> properties){
		this.properties = properties;
	}
	
	public Object getProperty(String name){
		return properties.get(name);
	}
	
	public String getName(){
		return name;
	}
	
	public E getiClass() {
		return iClass;
	}

	public void setiClass(E iClass) {
		this.iClass = iClass;
	}
	
}

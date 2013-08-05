package org.eclipse.m2m.atl.core.tracker.wrapper;

import org.eclipse.m2m.atl.core.IModelWrapper;


public class ModelWrapper implements IModelWrapper{

	private String name,model,modelFactory,referenceModel;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getModelFactory() {
		return modelFactory;
	}

	public void setModelFactory(String modelFactory) {
		this.modelFactory = modelFactory;
	}

	public String getReferenceModel() {
		return referenceModel;
	}

	public void setReferenceModel(String referenceModel) {
		this.referenceModel = referenceModel;
	}

	
	
}

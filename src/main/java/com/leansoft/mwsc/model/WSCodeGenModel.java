package com.leansoft.mwsc.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Data model for code generation
 * 
 * @author bulldog
 *
 */
public class WSCodeGenModel {
	
	private final List<SEIInfo> SEIs = new ArrayList<SEIInfo>();
	
	private final List<FaultInfo> faults = new ArrayList<FaultInfo>();

    /**
     *  SEI model for codegen
     * @return List<SEIInfo>
     */
	public List<SEIInfo> getServiceEndpointInterfaces() {
		return SEIs;
	}
	
    /**
     *  Fault model for codegen
     * @return List<FaultInfo>
     */
	public List<FaultInfo> getFaults() {
		return faults;
	}
}

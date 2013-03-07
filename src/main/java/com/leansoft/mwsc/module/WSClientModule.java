package com.leansoft.mwsc.module;

import java.util.Set;

import com.leansoft.mwsc.model.WSCodeGenModel;
import com.leansoft.mxjc.model.CGConfig;
import com.leansoft.mxjc.model.FileInfo;
import com.leansoft.mxjc.module.ModuleName;
import com.sun.tools.ws.wscompile.ErrorReceiver;

/**
 * Interface for a web service client module.  A client module for a specific platform 
 * implements platform specific logic for code generation.
 *
 * @author bulldog
 */
public interface WSClientModule {

	/**
	 * Get the name of the client module
	 * 
	 * @return The name of the client module
	 */
	public ModuleName getName();
	
	/**
	 * Set ErrorReceiver to be used for error reporting
	 * 
	 * @param errorReceiver
	 */
	public void setErrorReceiver(ErrorReceiver errorReceiver);
	
	/**
	 * Initialize the module
	 * 
	 */
	public void init() throws WscModuleException;
	
	
	/**
	 * Generate target code according to platform specific logic
	 * 
	 * @param cgModel, code generation model
	 * @param config for code generation
	 * @return a set of generated file model
	 * @throws WscModuleException
	 */
	public Set<FileInfo> generate(WSCodeGenModel cgModel, CGConfig config) throws WscModuleException;
}

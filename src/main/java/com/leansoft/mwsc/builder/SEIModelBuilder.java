package com.leansoft.mwsc.builder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import com.leansoft.mwsc.model.FaultInfo;
import com.leansoft.mwsc.model.MethodInfo;
import com.leansoft.mwsc.model.ParameterInfo;
import com.leansoft.mwsc.model.SEIInfo;
import com.leansoft.mwsc.model.WSCodeGenModel;
import com.leansoft.mwsc.model.annotation.SOAPBindingAnnotation;
import com.leansoft.mwsc.model.annotation.WebMethodAnnotation;
import com.leansoft.mwsc.model.annotation.WebParamAnnotation;
import com.leansoft.mwsc.model.annotation.WebResultAnnotation;
import com.leansoft.mwsc.model.annotation.WebServiceAnnotation;
import com.leansoft.mxjc.model.TypeInfo;
import com.leansoft.mxjc.util.ClassNameUtil;
import com.sun.codemodel.JType;
import com.sun.tools.ws.processor.generator.Names;
import com.sun.tools.ws.processor.model.Block;
import com.sun.tools.ws.processor.model.Fault;
import com.sun.tools.ws.processor.model.Message;
import com.sun.tools.ws.processor.model.Model;
import com.sun.tools.ws.processor.model.ModelProperties;
import com.sun.tools.ws.processor.model.Operation;
import com.sun.tools.ws.processor.model.Parameter;
import com.sun.tools.ws.processor.model.Port;
import com.sun.tools.ws.processor.model.Request;
import com.sun.tools.ws.processor.model.Response;
import com.sun.tools.ws.processor.model.Service;
import com.sun.tools.ws.processor.model.java.JavaInterface;
import com.sun.tools.ws.processor.model.java.JavaMethod;
import com.sun.tools.ws.processor.model.java.JavaParameter;
import com.sun.tools.ws.processor.model.jaxb.JAXBType;
import com.sun.tools.ws.wscompile.ErrorReceiver;
import com.sun.tools.ws.wsdl.document.soap.SOAPStyle;

public class SEIModelBuilder {
	
    private String serviceNS;
	
    private boolean isDocStyle = true;
    private boolean sameParamStyle = true;
    
    private Map<String, FaultInfo> faults = new HashMap<String, FaultInfo>();
    private Set<String> interfaceClassNames = new HashSet<String>();
    
    private Model wsModel;
    
    private ErrorReceiver errorReceiver;
    
    public SEIModelBuilder(Model wsModel, ErrorReceiver errorReceiver) {
    	this.wsModel = wsModel;
    	this.errorReceiver = errorReceiver;
    }
    
    /**
	 * 
	 * Build service endpoint interface model, also build fault model if any.
	 * 
	 */
	public WSCodeGenModel buildSEIModel() {
		WSCodeGenModel cgModel = new WSCodeGenModel();
		for (Service service : wsModel.getServices()) {
			for (Port port : service.getPorts()) {
				if (port.isProvider()) {
					continue; // Not generating for Provider based endpoint
				}
				SEIInfo seiInfo = new SEIInfo();

				JavaInterface intf = port.getJavaInterface();
				// fqn class name
				String className = intf.getName();
				// If the interface has already been defined, just skip it.
				if (interfaceClassNames.contains(className)) {
					continue;
				}
				interfaceClassNames.add(className);
				
				// package name of this interface
				String packageName = ClassNameUtil.getPackageName(className);
				seiInfo.setPackageName(packageName);
				// simple class name
				// String simpleName = intf.getSimpleName();
				String simpleName = ClassNameUtil.stripQualifier(className);
				seiInfo.setName(simpleName);
				// java doc of this interface
				String ptDoc = intf.getJavaDoc();
				seiInfo.setDocComment(ptDoc);
				
				// @WebService
				seiInfo.setWebServiceAnnotation(getWebServiceAnnotation(port));
				
				// @SOAPBinding
				seiInfo.setSoapBindingAnnotation(getSOAPBindingAnnotation(port));
				
		        for (Operation operation: port.getOperations()) {
		            JavaMethod method = operation.getJavaMethod();
		            
					MethodInfo methodInfo = new MethodInfo();
					// method name
					methodInfo.setName(method.getName());
					
					// method doc comment
					String methodJavaDoc = operation.getJavaDoc();
					if (methodJavaDoc == null) {
						methodJavaDoc = "public method";
					}
					
					// method return type
		            String returnTypeName;
					if(method.getReturnType().getName().equals("void")){
						returnTypeName = "void";
		            } else {
		            	returnTypeName = method.getReturnType().getType()
						.getName();
		            }
					if (this.isWrapped(operation)) { // wrapped response
				        Response response = operation.getResponse();
		                Block resBlock = response.getBodyBlocks().next();
		                returnTypeName = resBlock.getType().getJavaType().getName();
					}
					TypeInfo returnType = new TypeInfo();
					returnType.setFullName(returnTypeName);
					returnType.setName(ClassNameUtil
							.stripQualifier(returnTypeName));
					methodInfo.setReturnType(returnType);
					
					methodInfo.setWebMethodAnnotation(getWebMethodAnnotation(operation, method.getName()));
					methodInfo.setWebResultAnnotation(getWebResultAnnotation(operation));
					methodInfo.setSoapBindingAnnotation(getSOAPBindingAnnotation(operation));
					
					if (this.isWrapped(operation)) { // wrapped request parameter
			            Block reqBlock = operation.getRequest().getBodyBlocks().next();
			            String parameterTypeName = reqBlock.getType().getJavaType().getName();
			            
						ParameterInfo paramInfo = new ParameterInfo();
			            paramInfo.setName("request");
						// param type
						TypeInfo paramType = new TypeInfo();
						paramType.setFullName(parameterTypeName);
						paramType.setName(ClassNameUtil
								.stripQualifier(parameterTypeName));
						paramInfo.setType(paramType);
						
						// add this param in method definition
						methodInfo.getParameters().add(paramInfo);
					} else {
						for (JavaParameter parameter : method.getParametersList()) {
							ParameterInfo paramInfo = new ParameterInfo();
							
							// param name
							paramInfo.setName(parameter.getName());
							// param type
							TypeInfo paramType = new TypeInfo();
							paramType.setFullName(parameter.getType().getName());
							paramType.setName(ClassNameUtil
									.stripQualifier(parameter.getType().getName()));
							paramInfo.setType(paramType);
							
							// @WebParam
							paramInfo.setWebParamAnnotation(getWebParamAnnotation(operation, parameter));
							
							// add this param in method definition
							methodInfo.getParameters().add(paramInfo);
							//methodJavaDoc += NEW_LINE + "@param " + parameter.getName();
						}
					}
					
					// Fault
					for(Fault fault : operation.getFaultsSet()) {
				        String faultClassName = Names.customExceptionClassName(fault);
				        FaultInfo faultInfo = null;
				        if (faults.containsKey(faultClassName)) {
				        	faultInfo = faults.get(faultClassName);
				        } else {
				        	faultInfo = getFaultInfo(fault);
				        	// also put the fault model into the codegen model
				        	cgModel.getFaults().add(faultInfo);
				        	faults.put(faultClassName, faultInfo);
				        }
				        methodInfo.getThrows().add(faultInfo);
					}
					
					methodInfo.setDocComment(methodJavaDoc);
					seiInfo.getMethods().add(methodInfo);
		        }
				
				
				// add this endpoint interface in the codegen
				// model
				cgModel.getServiceEndpointInterfaces().add(seiInfo);
			}
		}
		return cgModel;
	}
	
	public boolean isWrapped(Operation operation) {
        if (operation.isWrapped() && operation.getStyle().equals(SOAPStyle.DOCUMENT)) {
        	return true;
        } else {
        	return false;
        }
	}
	
    private WebServiceAnnotation getWebServiceAnnotation(Port port) {
        QName name = (QName) port.getProperty(ModelProperties.PROPERTY_WSDL_PORT_TYPE_NAME);
        WebServiceAnnotation wsa = new WebServiceAnnotation();
        wsa.setName(name.getLocalPart());
        wsa.setTargetNamespace(name.getNamespaceURI());
        
        return wsa;
    }
    
    private SOAPBindingAnnotation getSOAPBindingAnnotation(Port port) {
        SOAPBindingAnnotation soapBindingAnn = null;
        isDocStyle = port.getStyle() == null || port.getStyle().equals(SOAPStyle.DOCUMENT);
        if(!isDocStyle){
            soapBindingAnn = new SOAPBindingAnnotation();
            soapBindingAnn.setStyle("SOAPBinding.Style.RPC");
            port.setWrapped(true);
        }
        if(isDocStyle){
            boolean first = true;
            boolean isWrapper = true;
            for(Operation operation:port.getOperations()){
                if(first){
                    isWrapper = operation.isWrapped();
                    first = false;
                    continue;
                }
                sameParamStyle = (isWrapper == operation.isWrapped());
                if(!sameParamStyle)
                    break;
            }
            if(sameParamStyle)
                port.setWrapped(isWrapper);
        }
        if(sameParamStyle && !port.isWrapped()){
            if(soapBindingAnn == null)
                soapBindingAnn = new SOAPBindingAnnotation();
            soapBindingAnn.setParameterStyle("SOAPBinding.ParameterStyle.BARE");
        }
        
        return soapBindingAnn;
    }
    
    private WebMethodAnnotation getWebMethodAnnotation(Operation operation, String methodName) {
    	WebMethodAnnotation webMethodAnn = new WebMethodAnnotation();
    	
    	String operationName = operation.getName().getLocalPart();
    	if(!methodName.equals(operationName)) {
    		webMethodAnn.setOperationName(operationName);
    	}
    	
        if (operation.getSOAPAction() != null && operation.getSOAPAction().length() > 0){
            webMethodAnn.setAction(operation.getSOAPAction());
        }
        
        return webMethodAnn;
    }
    
    private WebResultAnnotation getWebResultAnnotation(Operation operation) {
    	WebResultAnnotation webResultAnno = null;
    	
        Block block;
        String resultName = null;
        String nsURI = null;
        if (operation.getResponse().getBodyBlocks().hasNext()) {
            block = operation.getResponse().getBodyBlocks().next();
            resultName = block.getName().getLocalPart();
            if(isDocStyle || block.getLocation() == Block.HEADER){
                nsURI = block.getName().getNamespaceURI();
            }
        }

        for (Parameter parameter : operation.getResponse().getParametersList()) {
            if (parameter.getParameterIndex() == -1) {
                if(operation.isWrapped()||!isDocStyle){
                    if(parameter.getBlock().getLocation() == Block.HEADER){
                        resultName = parameter.getBlock().getName().getLocalPart();
                    }else{
                        resultName = parameter.getName();
                    }
                    if (isDocStyle || (parameter.getBlock().getLocation() == Block.HEADER)) {
                        nsURI = parameter.getType().getName().getNamespaceURI();
                    }
                }else if(isDocStyle){
                    JAXBType t = (JAXBType)parameter.getType();
                    resultName = t.getName().getLocalPart();
                    nsURI = t.getName().getNamespaceURI();
                }

                if(!resultName.equals("return")){
                	webResultAnno = new WebResultAnnotation();
                    webResultAnno.setName(resultName);
                }
                if((nsURI != null) && (!nsURI.equals(serviceNS) || (isDocStyle && operation.isWrapped()))){
                    if(webResultAnno == null)
                    	webResultAnno = new WebResultAnnotation();
                    webResultAnno.setTargetNamespace(nsURI);
                }
                //doclit wrapped could have additional headers
                if(!(isDocStyle && operation.isWrapped()) ||
                        (parameter.getBlock().getLocation() == Block.HEADER)){
                    if(webResultAnno == null)
                    	webResultAnno = new WebResultAnnotation();
                    webResultAnno.setPartName(parameter.getName());
                }
                if(parameter.getBlock().getLocation() == Block.HEADER){
                    if(webResultAnno == null)
                    	webResultAnno = new WebResultAnnotation();
                    webResultAnno.setHeader(true);
                }
            }

        }
        
//        if (operation.isWrapped() && operation.getStyle().equals(SOAPStyle.DOCUMENT)) {
//			LocatorImpl locator = new LocatorImpl();
//			locator.setLineNumber(-1);
//			locator.setSystemId("SEIModelBuilder");
//        	errorReceiver.warning(locator, "javax.xml.ws.RequestWrapper is needed, but is not supprted yet");
//        }
        
        return webResultAnno;
    }
    
    private SOAPBindingAnnotation getSOAPBindingAnnotation(Operation operation) {
        //DOC/BARE
    	
    	SOAPBindingAnnotation soapBindingAnnotation = null;
    	
        if (!sameParamStyle) {
            if(!operation.isWrapped()) {
            	soapBindingAnnotation = new SOAPBindingAnnotation();
            	soapBindingAnnotation.setParameterStyle("SOAPBinding.ParameterStyle.BARE");
            	
            }
        }
        return soapBindingAnnotation;
    }
    
    private WebParamAnnotation getWebParamAnnotation(Operation operation, JavaParameter javaParameter) {
    	WebParamAnnotation webParamAnnotation = new WebParamAnnotation();
    	
        Parameter param = javaParameter.getParameter();
        Request req = operation.getRequest();
        Response res = operation.getResponse();

        boolean header = isHeaderParam(param, req) ||
            (res != null && isHeaderParam(param, res));

        String name;
        boolean isWrapped = operation.isWrapped();

        if((param.getBlock().getLocation() == Block.HEADER) || (isDocStyle && !isWrapped))
            name = param.getBlock().getName().getLocalPart();
        else
            name = param.getName();

        webParamAnnotation.setName(name);

        String ns= null;

        if (isDocStyle) {
            ns = param.getBlock().getName().getNamespaceURI(); // its bare nsuri
            if(isWrapped){
                ns = param.getType().getName().getNamespaceURI();
            }
        }else if(header){
            ns = param.getBlock().getName().getNamespaceURI();
        }

        if((ns != null) && (!ns.equals(serviceNS) || (isDocStyle && isWrapped)))
            webParamAnnotation.setTargetNamespace(ns);

        if (header) {
            webParamAnnotation.setHeader(true);
        }

        if (param.isINOUT()){
            webParamAnnotation.setMode("javax.jws.WebParam.Mode.INOUT");
        }else if ((res != null) && (isMessageParam(param, res) || isHeaderParam(param, res) || isAttachmentParam(param, res) ||
                isUnboundParam(param,res) || param.isOUT())){
            webParamAnnotation.setMode("javax.jws.WebParam.Mode.OUT");
        }

        //doclit wrapped could have additional headers
        if(!(isDocStyle && isWrapped) || header)
        	webParamAnnotation.setPartName(javaParameter.getParameter().getName());
        
        return webParamAnnotation;
    }
    
    private boolean isAttachmentParam(Parameter param, Message message){
        if (message.getAttachmentBlockCount() == 0)
            return false;

        for (Block attBlock : message.getAttachmentBlocksMap().values())
            if (param.getBlock().equals(attBlock))
                return true;

        return false;
    }
    
    private boolean isMessageParam(Parameter param, Message message) {
        Block block = param.getBlock();

        return (message.getBodyBlockCount() > 0 && block.equals(message.getBodyBlocks().next())) ||
               (message.getHeaderBlockCount() > 0 &&
               block.equals(message.getHeaderBlocks().next()));
    }
    
    private boolean isUnboundParam(Parameter param, Message message){
        if (message.getUnboundBlocksCount() == 0)
            return false;

        for (Block unboundBlock : message.getUnboundBlocksMap().values())
            if (param.getBlock().equals(unboundBlock))
                return true;

        return false;
    }
    
    private boolean isHeaderParam(Parameter param, Message message) {
        if (message.getHeaderBlockCount() == 0)
            return false;

        for (Block headerBlock : message.getHeaderBlocksMap().values())
            if (param.getBlock().equals(headerBlock))
                return true;

        return false;
    }
    
    // fault
    private FaultInfo getFaultInfo(Fault fault) {
    	FaultInfo faultInfo = new FaultInfo();
    	
        String className = Names.customExceptionClassName(fault);
        faultInfo.setFullName(className);
        faultInfo.setPackageName(ClassNameUtil.getPackageName(className));
        faultInfo.setName(ClassNameUtil.stripQualifier(className));
        
        if (fault.getJavaDoc() != null) {
        	faultInfo.setDocComment(fault.getJavaDoc());
        }
        
        String localPart = fault.getBlock().getName().getLocalPart();
        String targetNamespace = fault.getBlock().getName().getNamespaceURI();
        QName schemaName = new QName(localPart, targetNamespace);
        faultInfo.setSchemaName(schemaName);
        
        JType faultBean = fault.getBlock().getType().getJavaType().getType().getType();
        TypeInfo faultBeanTypeInfo = new TypeInfo();
        faultBeanTypeInfo.setFullName(faultBean.fullName());
        faultBeanTypeInfo.setName(faultBean.name());
        faultInfo.setFaultBeanTypeInfo(faultBeanTypeInfo);
        
        return faultInfo;
    }

}

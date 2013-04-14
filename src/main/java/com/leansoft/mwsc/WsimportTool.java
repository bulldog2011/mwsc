/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */


package com.leansoft.mwsc;

import com.leansoft.mwsc.builder.SEIModelBuilder;
import com.leansoft.mwsc.model.WSCodeGenModel;
import com.leansoft.mwsc.module.WSClientModule;
import com.leansoft.mwsc.module.WSModuleFactory;
import com.leansoft.mwsc.module.WscModuleException;
import com.leansoft.mwsc.resources.MwscompileMessages;
import com.leansoft.mxjc.CodeBuilder;
import com.leansoft.mxjc.builder.ModelBuilder;
import com.leansoft.mxjc.model.CGConfig;
import com.leansoft.mxjc.model.CGModel;
import com.leansoft.mxjc.model.FileInfo;
import com.leansoft.mxjc.module.ClientModule;
import com.leansoft.mxjc.module.ModuleFactory;
import com.leansoft.mxjc.module.ModuleName;
import com.leansoft.mxjc.module.XjcModuleException;
import com.leansoft.mxjc.writer.FileCodeWriter;
import com.leansoft.mxjc.writer.ICodeWriter;
import com.leansoft.mxjc.writer.ProgressCodeWriter;
import com.sun.tools.ws.ToolVersion;
import com.sun.tools.ws.processor.model.Model;
import com.sun.tools.ws.processor.modeler.wsdl.ConsoleErrorReporter;
import com.sun.tools.ws.processor.modeler.wsdl.WSDLModeler;
import com.sun.tools.ws.resources.WscompileMessages;
import com.sun.tools.ws.resources.WsdlMessages;
import com.sun.tools.ws.wscompile.AbortException;
import com.sun.tools.ws.wscompile.BadCommandLineException;
import com.sun.tools.ws.wscompile.DefaultAuthenticator;
import com.sun.tools.ws.wscompile.ErrorReceiverFilter;
import com.sun.tools.ws.wscompile.Options;
import com.sun.tools.ws.wscompile.WsimportListener;
import com.sun.tools.xjc.XJCListener;
import com.sun.tools.xjc.api.ErrorListener;
import com.sun.tools.xjc.api.impl.s2j.SchemaCompilerEx;
import com.sun.tools.xjc.outline.Outline;
import com.sun.tools.xjc.util.NullStream;
import com.sun.xml.ws.api.server.Container;
import org.xml.sax.EntityResolver;
import org.xml.sax.SAXParseException;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Authenticator;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Vivek Pandey, adapted by bulldog
 */
public class WsimportTool {
    private static final String MWSC = "mwsc";
    private final PrintStream out;

    /**
     * Wsimport specific options
     */
    private final WsimportOptionsEx options = new WsimportOptionsEx();

    public WsimportTool(OutputStream out) {
        this(out, null);
    }

    public WsimportTool(OutputStream logStream, Container container) {
        this.out = (logStream instanceof PrintStream)?(PrintStream)logStream:new PrintStream(logStream);
    }

    public boolean run(String[] args) {
        class Listener extends WsimportListener {
            ConsoleErrorReporter cer = new ConsoleErrorReporter(out == null ? new PrintStream(new NullStream()) : out);

            @Override
            public void generatedFile(String fileName) {
                message(fileName);
            }

            @Override
            public void message(String msg) {
                out.println(msg);
            }

            @Override
            public void error(SAXParseException exception) {
                cer.error(exception);
            }

            @Override
            public void fatalError(SAXParseException exception) {
                cer.fatalError(exception);
            }

            @Override
            public void warning(SAXParseException exception) {
                cer.warning(exception);
            }

            @Override
            public void debug(SAXParseException exception) {
                cer.debug(exception);
            }

            @Override
            public void info(SAXParseException exception) {
                cer.info(exception);
            }

            public void enableDebugging(){
                cer.enableDebugging();
            }
        }
        final Listener listener = new Listener();
        ErrorReceiverFilter receiver = new ErrorReceiverFilter(listener) {
            public void info(SAXParseException exception) {
                if (options.verbose)
                    super.info(exception);
            }

            public void warning(SAXParseException exception) {
                if (!options.quiet)
                    super.warning(exception);
            }

            @Override
            public void pollAbort() throws AbortException {
                if (listener.isCanceled())
                    throw new AbortException();
            }

            @Override
            public void debug(SAXParseException exception){
                if(options.debugMode){
                    listener.debug(exception);
                }
            }
        };

        for (String arg : args) {
            if (arg.equals("-version")) {
                listener.message(ToolVersion.VERSION.BUILD_VERSION);
                return true;
            }
        }
        try {
            options.parseArguments(args);
            options.validate();
            if(options.debugMode)
                listener.enableDebugging();
            options.parseBindings(receiver);

            try {
                if( !options.quiet )
                    listener.message(WscompileMessages.WSIMPORT_PARSING_WSDL());

                //set auth info
                //if(options.authFile != null)
                    Authenticator.setDefault(new DefaultAuthenticator(receiver, options.authFile));


                WSDLModeler wsdlModeler = new WSDLModeler(options, receiver);
                Model wsdlModel = wsdlModeler.buildModel();
                if (wsdlModel == null) {
                    listener.message(WsdlMessages.PARSING_PARSE_FAILED());
                    return false;
                }
        		SchemaCompilerEx schemaCompilerEx = (SchemaCompilerEx) (options
        				.getSchemaCompiler());
        		Outline outline = schemaCompilerEx.getOutline();
        		if (outline == null) {
                    listener.message(WsdlMessages.PARSING_PARSE_FAILED());
                    return false;
        		}
        		
        		XjcErrorReceiverAdapter xjcErrorReceiver = new XjcErrorReceiverAdapter(receiver);
        		CGModel jaxbCodeGenModel = ModelBuilder.buildCodeGenModel(outline, xjcErrorReceiver);
        		
        		SEIModelBuilder seiModelBuilder = new SEIModelBuilder(wsdlModel, receiver);
        		WSCodeGenModel wsCodeGenModel = seiModelBuilder.buildSEIModel();
        		
                CGConfig cgConfig = new CGConfig();
                cgConfig.picoPrefix = options.prefix;
                cgConfig.picoServiceGroup = wsCodeGenModel.getServiceEndpointInterfaces().get(0).getName();
                // demo only configs
                cgConfig.eBaySOAService = options.eBaySOAPService;
                cgConfig.eBayShoppingAPI = options.eBayShoppingAPI;
                cgConfig.eBayTradingAPI = options.eBayTradingAPI;
                
                // for nano binding
                cgConfig.nanoPrivateField = options.privateField;
        		
                // use specific client module to generate code
                Set<FileInfo> targetFiles = new HashSet<FileInfo>();
                try {
                    // generate types first
                    ClientModule clientModule = ModuleFactory.getModule(options.module);
                    clientModule.setErrorReceiver(xjcErrorReceiver);// enable reporting
                    clientModule.init();
                    
					Set<FileInfo> typeFiles = clientModule.generate(jaxbCodeGenModel, cgConfig);
					
					// then generate ws client
					WSClientModule wsClientModule = WSModuleFactory.getModule(options.module);
					wsClientModule.setErrorReceiver(receiver);
					wsClientModule.init();
					
					Set<FileInfo> wsFiles = wsClientModule.generate(wsCodeGenModel, cgConfig);
					
					targetFiles.addAll(typeFiles);
					targetFiles.addAll(wsFiles);
				} catch (XjcModuleException e1) {
					receiver.error(e1);
					return false;
				} catch (WscModuleException e1) {
					receiver.error(e1);
					return false;
				}
                
                
                //generated code
                if( !options.quiet )
                    listener.message(WscompileMessages.WSIMPORT_GENERATING_CODE());
                
                try {
                    ICodeWriter cw = new FileCodeWriter(options.destDir, false);

                    if( !options.quiet ) {
                    	XjcErrorListenerAdapter xjcErrorListenerAdapter = new XjcErrorListenerAdapter(listener);
                        cw = new ProgressCodeWriter(cw,xjcErrorListenerAdapter, targetFiles.size());
                    }
                    
                    // then print them out
                    CodeBuilder.build(targetFiles, cw);
                } catch (IOException e) {
                	//e.printStackTrace();
                    receiver.error(e);
                    return false;
                }
                
                listener.message("done.");
                
            } catch(AbortException e){
                //error might have been reported
            }

        } catch (Options.WeAreDone done) {
            usage(done.getOptions());
        } catch (BadCommandLineException e) {
            if (e.getMessage() != null) {
                System.out.println(e.getMessage());
                System.out.println();
            }
            usage(e.getOptions());
            return false;
        } finally{
            if(!options.keep){
                options.removeGeneratedFiles();
            }
        }
        return true;
    }

    public void setEntityResolver(EntityResolver resolver){
        this.options.entityResolver = resolver;
    }

    protected void usage(Options options) {
        System.out.println(MwscompileMessages.MWSC_HELP(MWSC));
        System.out.println(MwscompileMessages.MWSC_USAGE_EXAMPLES());
    }
    
    static class XjcErrorReceiverAdapter extends com.sun.tools.xjc.ErrorReceiver {
    	
    	public XjcErrorReceiverAdapter(ErrorListener errorListener) {
    		this.errorListener = errorListener;
    	}
    	
        /**
         * User-specified error receiver.
         * This field can be null, in which case errors need to be discarded.
         */
        private ErrorListener errorListener;

        public void info(SAXParseException exception) {
            if(errorListener!=null)
                errorListener.info(exception);
        }
        public void warning(SAXParseException exception) {
            if(errorListener!=null)
                errorListener.warning(exception);
        }
        public void error(SAXParseException exception) {
            if(errorListener!=null)
                errorListener.error(exception);
        }
        public void fatalError(SAXParseException exception) {
            if(errorListener!=null)
                errorListener.fatalError(exception);
        }

    }
    
    static class XjcErrorListenerAdapter extends XJCListener {
    	
    	public XjcErrorListenerAdapter(WsimportListener wsimportListener) {
    		this.wsimportListener = wsimportListener;
    	}
    	
        /**
         * User-specified error listener.
         * This field can be null, in which case errors need to be discarded.
         */
    	private WsimportListener wsimportListener;

		@Override
		public void error(SAXParseException exception) {
			if (wsimportListener != null) {
				wsimportListener.error(exception);
			}
		}

		@Override
		public void fatalError(SAXParseException exception) {
			if (wsimportListener != null) {
				wsimportListener.fatalError(exception);
			}
		}

		@Override
		public void warning(SAXParseException exception) {
			if (wsimportListener != null) {
				wsimportListener.warning(exception);
			}	
		}

		@Override
		public void info(SAXParseException exception) {
			if (wsimportListener != null) {
				wsimportListener.info(exception);
			}
		}
    	
    }
}

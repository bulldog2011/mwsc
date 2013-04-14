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

import com.leansoft.mxjc.module.ModuleName;
import com.sun.tools.ws.wscompile.BadCommandLineException;
import com.sun.tools.ws.wscompile.WsimportOptions;
import com.sun.tools.xjc.api.SchemaCompiler;
import com.sun.tools.xjc.api.SpecVersion;
import com.sun.tools.xjc.api.impl.s2j.SchemaCompilerEx;


/**
 * 
 * An Extension to JAX-WS's {@link WsimportOptions}, will use an
 * extended JAXB's {@link SchemaCompiler}
 * 
 * @author bulldog
 *
 */
public class WsimportOptionsEx extends WsimportOptions {
	
    public ModuleName module = ModuleName.NANO;
    
    // for demo
    public boolean eBaySOAPService = false;;
    public boolean eBayShoppingAPI = false;;
    public boolean eBayTradingAPI = false;
    
    public String prefix;
    
    public boolean privateField;
	
    /**
     * Extended JAXB's {@link SchemaCompiler} to be used for handling the schema portion.
     * This object is also configured through options.
     */
    private SchemaCompiler schemaCompilerEx = new SchemaCompilerEx();
    
    @Override
    public SchemaCompiler getSchemaCompiler() {
        schemaCompilerEx.setTargetVersion(SpecVersion.parse(target.getVersion()));
        schemaCompilerEx.setEntityResolver(entityResolver);
        return schemaCompilerEx;
    }
    
    /** Parse Mwsc-specific options. */
    @Override
    public int parseArguments(String[] args, int i) throws BadCommandLineException {
        
        if (args[i].equals("-nano")) {
        	module = ModuleName.NANO;
        	return 1;
        }
        
        if (args[i].equals("-pico")) {
        	module = ModuleName.PICO;
        	return 1;
        }
        
        if (args[i].equals("-ebaysoa")) {
        	this.eBaySOAPService = true;
        	return 1;
        }
        
        if (args[i].equals("-ebayshopping")) {
        	this.eBayShoppingAPI = true;
        	return 1;
        }
        
        if (args[i].equals("-ebaytrading")) {
        	this.eBayTradingAPI = true;
        	return 1;
        }
        
        if (args[i].equals("-privateField")) {
        	this.privateField = true;
        	return 1;
        }
        
        if (args[i].equals("-prefix")) {
        	prefix = super.requireArgument("-prefix", args, ++i);
        	return 2;
        }
        

        return super.parseArguments(args, i);
    }
	
}

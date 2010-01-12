/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.ui.examples.css.rcp;

import java.io.InputStream;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.e4.ui.css.core.engine.CSSErrorHandler;
import org.eclipse.e4.ui.css.swt.engine.CSSSWTEngineImpl;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

/**
 * This workbench advisor creates the window advisor, and specifies
 * the perspective id for the initial window.
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {
	
	private final static String STYLE_SHEET_PATH = "css/default.css";
	public static ApplicationWorkbenchAdvisor INSTANCE;

	public CSSEngine engine;
	
	public ApplicationWorkbenchAdvisor() {
		super();
		INSTANCE = this;
	}
	
    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
    	return new ApplicationWorkbenchWindowAdvisor(configurer);
    }

	public String getInitialWindowPerspectiveId() {
		return Perspective.ID;
	} 
	
	public void initialize(IWorkbenchConfigurer configurer) {
		super.initialize(configurer);
		engine = initializeStyling();
	}
	
	private CSSEngine initializeStyling() {
		// Instantiate SWT CSS Engine
		Display display = Display.getDefault();
		CSSEngine engine = new CSSSWTEngineImpl(display, true);
		
		// CRITICAL: this happens for free post 0.9 but this setData needed so CSSPresentation can find the engine
		display.setData("org.eclipse.e4.ui.css.core.engine", engine);
		
		engine.setErrorHandler(new CSSErrorHandler() {
			public void error(Exception e) {
				e.printStackTrace();
			}
		});
		
		try {
			URL url = FileLocator.find(Activator.getDefault().getBundle(), new Path(STYLE_SHEET_PATH), null);
			InputStream stream = url.openStream();
			engine.parseStyleSheet(stream);	
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return engine;
	}
}
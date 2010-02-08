/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.e4.ui.tests.reconciler;

import java.util.Collection;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.MApplicationFactory;
import org.eclipse.e4.ui.model.application.MWindow;
import org.eclipse.e4.workbench.modeling.IModelReconcilingService;
import org.eclipse.e4.workbench.modeling.ModelDelta;
import org.eclipse.e4.workbench.modeling.ModelReconciler;
import org.eclipse.e4.workbench.ui.internal.ModelReconcilingService;

public class E4XMIResourceFactoryTest extends ModelReconcilerTest {

	public void testNonConflictingIds() {
		MApplication application = createApplication();

		saveModel();

		application = createApplication();

		MWindow window = MApplicationFactory.eINSTANCE.createWindow();
		application.getChildren().add(window);

		assertFalse(getId(application).equals(getId(window)));
	}

	public void testNonConflictingIds2() {
		MApplication application = createApplication();

		saveModel();

		ModelReconciler reconciler = createModelReconciler();
		reconciler.recordChanges(application);

		MWindow window1 = MApplicationFactory.eINSTANCE.createWindow();
		application.getChildren().add(window1);

		Object state = reconciler.serialize();

		application = createApplication();

		MWindow window2 = MApplicationFactory.eINSTANCE.createWindow();
		application.getChildren().add(window2);

		Collection<ModelDelta> deltas = constructDeltas(application, state);
		applyAll(deltas);

		window1 = application.getChildren().get(0);
		window2 = application.getChildren().get(1);

		String applicationId = getId(application);
		String window1Id = getId(window1);
		String window2Id = getId(window2);

		assertFalse(applicationId.equals(window1Id));
		assertFalse(applicationId.equals(window2Id));
		assertFalse(window1Id.equals(window2Id));
	}

	@Override
	protected IModelReconcilingService getModelReconcilingService() {
		return new ModelReconcilingService();
	}

}
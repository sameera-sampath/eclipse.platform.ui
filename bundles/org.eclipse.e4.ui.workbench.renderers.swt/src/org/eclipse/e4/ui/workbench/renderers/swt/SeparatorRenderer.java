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
package org.eclipse.e4.ui.workbench.renderers.swt;

import java.util.List;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuSeparator;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBarSeparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Widget;

/**
 * Create a contribute part.
 */
public class SeparatorRenderer extends SWTPartRenderer {

	public Object createWidget(final MUIElement element, Object parent) {
		Widget newSep = null;
		if (!element.isVisible()) {
			return null;
		}
		int objIndex = calcIndex(element);
		int addIndex = calcVisibleIndex(element);
		MUIElement nextVisibleChild = getNextVisibleChild(element, objIndex);
		if (addIndex == 0) {
			return null;
		}
		if (nextVisibleChild == null) {
			return null;
		}
		if (nextVisibleChild.isVisible()
				&& (nextVisibleChild instanceof MMenuSeparator || nextVisibleChild instanceof MToolBarSeparator)) {
			return null;
		}
		if (element instanceof MMenuSeparator) {
			Menu menu = null;
			Object widget = element.getParent().getWidget();
			if (widget instanceof Menu) {
				menu = (Menu) widget;
			} else if (widget instanceof MenuItem) {
				menu = ((MenuItem) widget).getMenu();
			}
			if (menu != null) {
				newSep = new MenuItem(menu, SWT.SEPARATOR, addIndex);
			}
		} else if (element instanceof MToolBarSeparator) {
			ToolBar tb = parent instanceof ToolBar ? (ToolBar) parent
					: (ToolBar) element.getParent().getWidget();
			newSep = new ToolItem(tb, SWT.SEPARATOR, addIndex);
		}

		return newSep;
	}

	MUIElement getNextVisibleChild(final MUIElement element, int objIndex) {
		List<MUIElement> children = element.getParent().getChildren();
		for (int i = objIndex + 1; i < children.size(); i++) {
			MUIElement child = children.get(i);
			if (child.isVisible()) {
				return child;
			}
		}
		return null;
	}
}
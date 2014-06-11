/*******************************************************************************
 * Copyright (c) 2014 Agnie Technologies.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Agnie Technologies - initial API and implementation
 ******************************************************************************/
package com.agnie.useradmin.main.client;

import com.agnie.gwt.common.client.mvp.Place;
import com.agnie.useradmin.main.client.mvp.PlaceToken;
import com.agnie.useradmin.main.client.ui.ViewFactory;

public interface Presenter extends com.agnie.gwt.common.client.mvp.Presenter {
	public void setViewFactory(ViewFactory viewFactory);

	public void setPlace(Place<PlaceToken> place);
}

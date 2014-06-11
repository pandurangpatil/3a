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
package com.agnie.useradmin.common.client.helper;

import com.agnie.common.gwt.serverclient.client.renderer.Title;
import com.agnie.useradmin.persistance.client.enums.RequestStatus;

public class RequestStatusWrapper implements Title {
	
	String title;
	RequestStatus reqStatus;
	
	public RequestStatusWrapper(String title) {
		this.title=title;
	}
	
	public RequestStatusWrapper(RequestStatus reqStatus) {
		this.reqStatus=reqStatus;
	}
	
	public RequestStatus getReqStatus (){
		return reqStatus;
	}

	@Override
	public String getTitle() {
		if(reqStatus!=null){
			return reqStatus.getLocalized();
		}else{
			return title;
		}
	}

}

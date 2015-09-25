/*
 * Copyright 2013-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.apsadmin.common.currentuser;

import com.agiletec.apsadmin.system.entity.IApsEntityAction;

/**
 * Interfaccia delle classi action per la gestione del Profilo Utente corrente
 * @author E.Santoboni
 */
public interface ICurrentUserProfileAction extends IApsEntityAction {
	
	public static final String SESSION_PARAM_NAME_CURRENT_PROFILE = "currentProfileOnSession";
	
}

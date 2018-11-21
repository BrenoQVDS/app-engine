/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jacms.aps.system.services.contentmodel;

import java.util.List;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.ContentModelReference;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.ContentModelDto;
import org.entando.entando.aps.system.services.dataobjectmodel.model.IEntityModelDictionary;
import org.entando.entando.plugins.jacms.web.contentmodel.EntandoCrudService;

public interface ContentModelService extends EntandoCrudService<ContentModelDto, Long> {
    
    String BEAN_NAME = "ContentModelService";
    
    ContentModelDto getContentModel(Long modelId);
    
    List<ContentModelReference> getContentModelReferences(Long modelId);
    
    IEntityModelDictionary getContentModelDictionary(String typeCode);
}

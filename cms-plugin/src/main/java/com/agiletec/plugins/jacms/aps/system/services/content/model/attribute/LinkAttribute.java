/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package com.agiletec.plugins.jacms.aps.system.services.content.model.attribute;

import com.agiletec.aps.system.common.entity.model.AttributeFieldError;
import com.agiletec.aps.system.common.entity.model.AttributeTracer;
import com.agiletec.aps.system.common.entity.model.FieldError;
import com.agiletec.aps.system.common.entity.model.attribute.AbstractJAXBAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.TextAttribute;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.CmsAttributeReference;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SymbolicLink;
import com.agiletec.plugins.jacms.aps.system.services.content.model.attribute.util.SymbolicLinkValidator;
import com.agiletec.plugins.jacms.aps.system.services.linkresolver.ILinkResolverManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.ent.exception.EntRuntimeException;
import org.entando.entando.ent.util.EntLogging.EntLogFactory;
import org.entando.entando.ent.util.EntLogging.EntLogger;
import org.jdom.Element;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

/**
 * Rappresenta una informazione di tipo "link". La destinazione del link è la
 * stessa per tutte le lingue, ma il testo associato varia con la lingua.
 *
 * @author W.Ambu - S.Didaci
 */
public class LinkAttribute extends TextAttribute implements IReferenceableAttribute {

    private static final EntLogger logger = EntLogFactory.getSanitizedLogger(LinkAttribute.class);

    @Override
    public Object getAttributePrototype() {
        LinkAttribute prototype = (LinkAttribute) super.getAttributePrototype();
        prototype.setContentManager(this.getContentManager());
        prototype.setLinkResolverManager(this.getLinkResolverManager());
        prototype.setPageManager(this.getPageManager());
        prototype.setResourceManager(this.getResourceManager());
        return prototype;
    }

    @Override
    public Element getJDOMElement() {
        Element attributeElement = this.createRootElement("attribute");
        if (null != this.getSymbolicLink()) {
            Element linkElement = new Element("link");
            attributeElement.addContent(linkElement);
            Element dest;
            int type = this.getSymbolicLink().getDestType();
            switch (type) {
                case SymbolicLink.URL_TYPE:
                    linkElement.setAttribute("type", "external");
                    dest = new Element("urldest");
                    dest.addContent(this.getSymbolicLink().getUrlDest());
                    linkElement.addContent(dest);
                    break;
                case SymbolicLink.PAGE_TYPE:
                    linkElement.setAttribute("type", "page");
                    dest = new Element("pagedest");
                    dest.addContent(this.getSymbolicLink().getPageDest());
                    linkElement.addContent(dest);
                    break;
                case SymbolicLink.CONTENT_TYPE:
                    linkElement.setAttribute("type", "content");
                    dest = new Element("contentdest");
                    dest.addContent(this.getSymbolicLink().getContentDest());
                    linkElement.addContent(dest);
                    break;
                case SymbolicLink.CONTENT_ON_PAGE_TYPE:
                    linkElement.setAttribute("type", "contentonpage");
                    dest = new Element("pagedest");
                    dest.addContent(this.getSymbolicLink().getPageDest());
                    linkElement.addContent(dest);
                    dest = new Element("contentdest");
                    dest.addContent(this.getSymbolicLink().getContentDest());
                    linkElement.addContent(dest);
                    break;
                case SymbolicLink.RESOURCE_TYPE:
                    linkElement.setAttribute("type", "resource");
                    dest = new Element("resourcedest");
                    dest.addContent(this.getSymbolicLink().getResourceDest());
                    linkElement.addContent(dest);
                    break;
                default:
                    linkElement.setAttribute("type", "");
            }
        }
        super.addTextElements(attributeElement);
        if (null != this.getLinkProperties()) {
            Element propertiesElement = new Element("properties");
            Iterator<String> keyIter = this.getLinkProperties().keySet().iterator();
            while (keyIter.hasNext()) {
                String key = keyIter.next();
                String value = this.getLinkProperties().get(key);
                if (!StringUtils.isBlank(value)) {
                    Element propertyElement = new Element("property");
                    propertyElement.setAttribute("key", key);
                    propertyElement.setText(value.trim());
                    propertiesElement.addContent(propertyElement);
                }
            }
            attributeElement.addContent(propertiesElement);
        }
        return attributeElement;
    }

    /**
     * Restituisce la stringa rappresentante la destinazione simbolica. Il
     * metodo è atto ad essere utilizzato nel modello di renderizzazione e la
     * stringa restituita sarà successivamente risolta in fase di
     * renderizzazione dal servizio linkResolver.
     *
     * @return La stringa rappresentante la destinazione simbolica.
     */
    public String getDestination() {
        String destination = "";
        if (null != this.getSymbolicLink()) {
            destination = this.getSymbolicLink().getSymbolicDestination();
            if (this.getSymbolicLink().getDestType() == SymbolicLink.URL_TYPE) {
                destination = destination.replaceAll("&(?![a-z]+;)", "&amp;");
            }
        }
        return destination;
    }

    @Override
    public boolean isSearchableOptionSupported() {
        return false;
    }

    @Override
    public List<CmsAttributeReference> getReferences(List<Lang> systemLangs) {
        List<CmsAttributeReference> refs = new ArrayList<>();
        SymbolicLink symbLink = this.getSymbolicLink();
        if (null != symbLink && (symbLink.getDestType() != SymbolicLink.URL_TYPE)) {
            CmsAttributeReference ref = new CmsAttributeReference(symbLink.getPageDest(),
                    symbLink.getContentDest(), null);
            refs.add(ref);
        }
        return refs;
    }

    @Override
    public Object getValue() {
        return this.getSymbolicLink();
    }

    @Override
    protected AbstractJAXBAttribute getJAXBAttributeInstance() {
        return new JAXBLinkAttribute();
    }

    @Override
    public AbstractJAXBAttribute getJAXBAttribute(String langCode) {
        JAXBLinkAttribute jaxbAttribute = (JAXBLinkAttribute) super.createJAXBAttribute(langCode);
        if (null == jaxbAttribute || null == this.getSymbolicLink()) {
            return jaxbAttribute;
        }
        JAXBLinkValue value = new JAXBLinkValue();
        String text = this.getTextForLang(langCode);
        value.setText(text);
        value.setSymbolicLink(this.getSymbolicLink());
        jaxbAttribute.setLinkValue(value);
        return jaxbAttribute;
    }

    @Override
    public void valueFrom(AbstractJAXBAttribute jaxbAttribute, String langCode) {
        super.valueFrom(jaxbAttribute, langCode);
        JAXBLinkValue value = ((JAXBLinkAttribute) jaxbAttribute).getLinkValue();
        if (null == value) {
            return;
        }
        this.setSymbolicLink(value.getSymbolicLink());
        String textValue = value.getText();
        if (null == textValue) {
            return;
        }
        this.getTextMap().put(this.getDefaultLangCode(), textValue);
    }

    @Override
    public Status getStatus() {
        Status linkStatus =
                (null != this.getSymbolicLink() && this.getSymbolicLink().getDestType() != 0) ? Status.VALUED : Status.EMPTY;
        if (linkStatus.equals(Status.EMPTY)) {
            return Status.EMPTY;
        }
        Status textStatus = super.getStatus();
        if (!textStatus.equals(linkStatus)) {
            return Status.INCOMPLETE;
        }
        if (textStatus.equals(linkStatus) && textStatus.equals(Status.VALUED)) {
            return Status.VALUED;
        }
        return Status.EMPTY;
    }

    @Override
    @Deprecated
    public List<AttributeFieldError> validate(AttributeTracer tracer, ILangManager langManager) {
        return this.validate(tracer, langManager, null);
    }

    @Override
    public List<AttributeFieldError> validate(AttributeTracer tracer, ILangManager langManager, BeanFactory beanFactory) {
        List<AttributeFieldError> errors = super.validate(tracer, langManager, beanFactory);
        try {
            SymbolicLink symbolicLink = this.getSymbolicLink();
            if (null == symbolicLink) {
                return errors;
            }
            SymbolicLinkValidator sler = this.getSymbolicLinkValidator(beanFactory);
            AttributeFieldError attributeError = sler.scan(symbolicLink, (Content) this.getParentEntity());
            if (null != attributeError) {
                AttributeFieldError error = new AttributeFieldError(this, attributeError.getErrorCode(), tracer);
                if (attributeError.getMessage() == null) {
                    attributeError.setMessage("Invalid link - page " + symbolicLink.getPageDest()
                            + " - content " + symbolicLink.getContentDest() + " - Error code " + attributeError.getErrorCode());
                }
                error.setMessage(attributeError.getMessage());
                errors.add(error);
            }
        } catch (Exception t) {
            logger.error("Error validating link attribute", t);
            throw new EntRuntimeException("Error validating link attribute", t);
        }
        for (AttributeFieldError error : errors) {
            if (FieldError.INVALID.equals(error.getErrorCode()) && Status.INCOMPLETE.equals(this.getStatus())) {
                error.setMessage("The Link attribute is invalid or incomplete");
            }
        }
        return errors;
    }

    private SymbolicLinkValidator getSymbolicLinkValidator(BeanFactory beanFactory) {
        return new SymbolicLinkValidator(
                beanFactory == null ? this.contentManager : beanFactory.getBean(IContentManager.class),
                beanFactory == null ? this.pageManager : beanFactory.getBean(IPageManager.class),
                beanFactory == null ? this.resourceManager : beanFactory.getBean(IResourceManager.class)
        );
    }

    /**
     * Setta il link simbolico caratterizzante l'attributo.
     *
     * @param symbolicLink Il link simbolico.
     */
    public void setSymbolicLink(SymbolicLink symbolicLink) {
        this.symbolicLink = symbolicLink;
    }

    /**
     * Restituisce il link simbolico caratterizzante l'attributo.
     *
     * @return Il link simbolico.
     */
    public SymbolicLink getSymbolicLink() {
        return symbolicLink;
    }

    public Map<String, String> getLinkProperties() {
        return linkProperties;
    }

    public void setLinkProperties(Map<String, String> linkProperties) {
        this.linkProperties = linkProperties;
    }

    @Deprecated
    protected IContentManager getContentManager() {
        return contentManager;
    }

    @Deprecated
    public void setContentManager(IContentManager contentManager) {
        this.contentManager = contentManager;
    }

    @Deprecated
    protected IPageManager getPageManager() {
        return pageManager;
    }

    @Deprecated
    public void setPageManager(IPageManager pageManager) {
        this.pageManager = pageManager;
    }

    @Deprecated
    protected ILinkResolverManager getLinkResolverManager() {
        return linkResolverManager;
    }

    @Deprecated
    public void setLinkResolverManager(ILinkResolverManager linkResolverManager) {
        this.linkResolverManager = linkResolverManager;
    }

    @Deprecated
    public IResourceManager getResourceManager() {
        return resourceManager;
    }

    @Deprecated
    public void setResourceManager(IResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    private SymbolicLink symbolicLink;
    private Map<String, String> linkProperties = new HashMap<>();
    private transient IContentManager contentManager;
    private transient IPageManager pageManager;
    private transient ILinkResolverManager linkResolverManager;
    private transient IResourceManager resourceManager;

    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        WebApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
        if (ctx == null) {
            logger.warn("Null WebApplicationContext during deserialization");
            return;
        }
        this.contentManager = ctx.getBean(IContentManager.class);
        this.pageManager = ctx.getBean(IPageManager.class);
        this.linkResolverManager = ctx.getBean(ILinkResolverManager.class);
        this.resourceManager = ctx.getBean(IResourceManager.class);
        this.setLangManager(ctx.getBean(ILangManager.class));
    }
}
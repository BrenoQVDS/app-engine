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
package org.entando.entando.aps.system.services.controller.executor;

import com.agiletec.aps.system.EntThreadLocal;
import com.agiletec.aps.util.ApsTenantApplicationUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.util.Objects;
import java.util.Optional;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.entando.entando.aps.system.services.guifragment.GuiFragment;
import org.entando.entando.aps.system.services.guifragment.IGuiFragmentManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.tags.util.IFrameDecoratorContainer;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.ApsWebApplicationUtils;

import freemarker.template.Template;
import java.util.Arrays;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author E.Santoboni
 */
public abstract class AbstractWidgetExecutorService {

	private static final Logger _logger = LoggerFactory.getLogger(AbstractWidgetExecutorService.class);

	@Value("${PARALLEL_WIDGET_RENDER:false}")
	private boolean parallelWidgetRender;

	protected void buildWidgetsOutput(RequestContext reqCtx, IPage page, String[] widgetOutput) throws ApsSystemException {
		try {
			List<IFrameDecoratorContainer> decorators = this.extractDecorators(reqCtx);
			Widget[] widgets = page.getWidgets();
			if (this.parallelWidgetRender) {
				List<Widget> widgetList = Arrays.asList(widgets);
				Optional<String> tenantCode = ApsTenantApplicationUtils.extractCurrentTenantCode(reqCtx.getRequest());
				widgetList.parallelStream().forEach(w -> {
					int frame = widgetList.indexOf(w);
					try {
						reqCtx.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_FRAME, frame);

						tenantCode.ifPresent(ApsTenantApplicationUtils::setTenant);

						Widget widget = widgets[frame];
						widgetOutput[frame] = this.buildWidgetOutput(reqCtx, widget, decorators);
					} catch (Exception e) {
						_logger.error("Error extracting output for frame " + frame, e);
					} finally {
						reqCtx.removeExtraParam(SystemConstants.EXTRAPAR_CURRENT_FRAME);
					}
				});
			} else {
				for (int frame = 0; frame < widgets.length; frame++) {
					reqCtx.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_FRAME, frame);
					Widget widget = widgets[frame];
					widgetOutput[frame] = this.buildWidgetOutput(reqCtx, widget, decorators);
					reqCtx.removeExtraParam(SystemConstants.EXTRAPAR_CURRENT_FRAME);
				}
			}
		} catch (Throwable t) {
			String msg = "Error detected during widget preprocessing";
			_logger.error(msg, t);
			throw new ApsSystemException(msg, t);
		}
	}

	protected String buildWidgetOutput(RequestContext reqCtx, Widget widget, List<IFrameDecoratorContainer> decorators)
			throws ApsSystemException {
		StringBuilder buffer = new StringBuilder();
		try {
            IWidgetTypeManager widgetTypeManager = (IWidgetTypeManager) ApsWebApplicationUtils.getBean(SystemConstants.WIDGET_TYPE_MANAGER, reqCtx.getRequest());
			WidgetType type = (null != widget) ? widgetTypeManager.getWidgetType(widget.getTypeCode()) : null;
            if (null != widget && this.isUserAllowed(reqCtx, type)) {
				reqCtx.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET, CurrentLogicWidget.extractCurrentWidget(widget, widgetTypeManager));
			} else {
				reqCtx.removeExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET);
			}
			buffer.append(this.extractDecoratorsOutput(reqCtx, widget, decorators, false, true));
			if (null != widget && this.isUserAllowed(reqCtx, type)) {
				String widgetOutput = extractWidgetOutput(reqCtx, type);
				// String widgetJspPath = widget.getType().getJspPath();
				buffer.append(this.extractDecoratorsOutput(reqCtx, widget, decorators, true, true));
				// buffer.append(this.extractJspOutput(reqCtx, widgetJspPath));
				buffer.append(widgetOutput);
				buffer.append(this.extractDecoratorsOutput(reqCtx, widget, decorators, true, false));
			}
			buffer.append(this.extractDecoratorsOutput(reqCtx, widget, decorators, false, false));
		} catch (Throwable t) {
			String msg = "Error creating widget output";
			_logger.error(msg, t);
			throw new RuntimeException(msg, t);
		}
		return buffer.toString();
	}

	public static String extractWidgetOutput(RequestContext reqCtx, WidgetType type) throws ApsSystemException {
		if (null == type) {
			return "";
		}
		String widgetTypeCode = (type.isLogic()) ? type.getParentType().getCode() : type.getCode();
		try {
			IGuiFragmentManager guiFragmentManager = (IGuiFragmentManager) ApsWebApplicationUtils.getBean(
					SystemConstants.GUI_FRAGMENT_MANAGER, reqCtx.getRequest());
			GuiFragment guiFragment = guiFragmentManager.getUniqueGuiFragmentByWidgetType(widgetTypeCode);
			if (null != guiFragment) {
				String fragmentOutput = extractFragmentOutput(guiFragment, reqCtx);
				if (StringUtils.isBlank(fragmentOutput)) {
					_logger.info("The fragment '{}' of widget '{}' is not available", guiFragment.getCode(), widgetTypeCode);
					return "";
				}
				return fragmentOutput;
			} else {
				String widgetJspPath = type.getJspPath();
				return extractJspWidgetOutput(widgetTypeCode, reqCtx, widgetJspPath);
			}
		} catch (Throwable t) {
			String msg = "Error creating widget output - Type '" + widgetTypeCode + "'";
			_logger.error(msg, t);
			throw new ApsSystemException(msg, t);
		}
	}

	protected List<IFrameDecoratorContainer> extractDecorators(RequestContext reqCtx) throws ApsSystemException {
		HttpServletRequest request = reqCtx.getRequest();
		WebApplicationContext wac = ApsWebApplicationUtils.getWebApplicationContext(request);
		List<IFrameDecoratorContainer> containters = new ArrayList<IFrameDecoratorContainer>();
		try {
			String[] beanNames = wac.getBeanNamesForType(IFrameDecoratorContainer.class);
			for (int i = 0; i < beanNames.length; i++) {
				IFrameDecoratorContainer container = (IFrameDecoratorContainer) wac.getBean(beanNames[i]);
				containters.add(container);
			}
			BeanComparator comparator = new BeanComparator("order");
			Collections.sort(containters, comparator);
		} catch (Throwable t) {
			_logger.error("Error extracting widget decorators", t);
			throw new ApsSystemException("Error extracting widget decorators", t);
		}
		return containters;
	}

	protected String extractDecoratorsOutput(RequestContext reqCtx, Widget widget, List<IFrameDecoratorContainer> decorators,
			boolean isWidgetDecorator, boolean includeHeader) throws Throwable {
		if (null == decorators || decorators.isEmpty()) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < decorators.size(); i++) {
			IFrameDecoratorContainer decoratorContainer = (includeHeader) ? decorators.get(i) : decorators.get(decorators.size() - i - 1);
			if ((isWidgetDecorator != decoratorContainer.isWidgetDecorator()) || !decoratorContainer.needsDecoration(widget, reqCtx)) {
				continue;
			}
			String fragmentCode = (includeHeader) ? decoratorContainer.getHeaderFragmentCode() : decoratorContainer.getFooterFragmentCode();
			String fragmentOutput = extractFragmentOutput(fragmentCode, reqCtx);
			if (StringUtils.isNotBlank(fragmentCode)) {
				builder.append(fragmentOutput);
			} else {
				String jspPath = (includeHeader) ? decoratorContainer.getHeaderJspPath() : decoratorContainer.getFooterJspPath();
				if (StringUtils.isNotBlank(jspPath)) {
					String output = extractJspOutput(reqCtx, jspPath);
					builder.append(output);
				}
			}
		}
		return builder.toString();
	}

	protected String extractFragmentOutput(String fragmentCode, RequestContext reqCtx) throws Throwable {
		if (StringUtils.isBlank(fragmentCode)) {
			return null;
		}
		IGuiFragmentManager guiFragmentManager = (IGuiFragmentManager) ApsWebApplicationUtils.getBean(SystemConstants.GUI_FRAGMENT_MANAGER,
				reqCtx.getRequest());
		GuiFragment fragment = guiFragmentManager.getGuiFragment(fragmentCode);
		return extractFragmentOutput(fragment, reqCtx);
	}

	protected static String extractFragmentOutput(GuiFragment fragment, RequestContext reqCtx) throws Throwable {
		if (null == fragment) {
			return null;
		}
		try {
			String currentGui = fragment.getCurrentGui();
			if (StringUtils.isBlank(currentGui)) {
				_logger.info("The fragment '{}' is not available", fragment.getCode());
				return "";
			}
			ExecutorBeanContainer ebc = reqCtx.getExecutorBeanContainer();
			Integer frame = (Integer) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_FRAME);
			Widget currentWidget = (Widget) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET);
			StringBuilder templateName = new StringBuilder(String.valueOf(frame)).append("_").append(fragment.getCode());
			if (null != currentWidget && null != currentWidget.getTypeCode()) {
				templateName.append("_").append(currentWidget.getTypeCode());
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Writer out = new OutputStreamWriter(baos);
			Template template = new Template(templateName.toString(), new StringReader(currentGui), ebc.getConfiguration());
			template.process(ebc.getTemplateModel(), out);
			out.flush();
			return baos.toString().trim();
		} catch (Throwable t) {
			String msg = "Error creating fragment output - code '" + fragment.getCode() + "'";
			_logger.error(msg, t);
			throw new ApsSystemException(msg, t);
		}
	}

	protected boolean isUserAllowed(RequestContext reqCtx, WidgetType widgetType) {
		if (null == widgetType) {
			return false;
		}
		String widgetTypeGroup = widgetType.getMainGroup();
		try {
			if (null == widgetTypeGroup || widgetTypeGroup.equals(Group.FREE_GROUP_NAME)) {
				return true;
			}
			IAuthorizationManager authorizationManager = (IAuthorizationManager) ApsWebApplicationUtils.getBean(
					SystemConstants.AUTHORIZATION_SERVICE, reqCtx.getRequest());
			UserDetails currentUser = (UserDetails) reqCtx.getRequest().getSession().getAttribute(
					SystemConstants.SESSIONPARAM_CURRENT_USER);
			return authorizationManager.isAuthOnGroup(currentUser, widgetTypeGroup);
		} catch (Throwable t) {
			_logger.error("Error checking user authorities", t);
		}
		return false;
	}

	protected static String extractJspWidgetOutput(String widgetTypeCode, RequestContext reqCtx, String jspPath) throws Throwable {
		try {
			return extractJspOutput(reqCtx, jspPath);
		} catch (IOException e) {
			_logger.error("The widget '{}' is unavailable. Expected jsp path '{}'", widgetTypeCode, jspPath, e);
			return "The widget '" + widgetTypeCode + "' is unavailable";
		} catch (Throwable t) {
			_logger.error("Error extracting jsp output", t);
			throw t;
		}
	}

	protected static String extractJspOutput(RequestContext reqCtx, String jspPath) throws ServletException, IOException {
		HttpServletRequest request = reqCtx.getRequest();
		HttpServletResponse response = reqCtx.getResponse();
		BufferedHttpResponseWrapper wrapper = new BufferedHttpResponseWrapper(response);
		ServletContext context = request.getSession().getServletContext();
		String url = response.encodeRedirectURL(jspPath);
		RequestDispatcher dispatcher = context.getRequestDispatcher(url);
		dispatcher.include(request, wrapper);
		return wrapper.getOutput();
	}
    
    public static class CurrentLogicWidget extends Widget {
        
        private ApsProperties config;
        private Widget concrete;
        
        public CurrentLogicWidget(Widget concrete, ApsProperties logicParameters) {
            this.concrete = concrete;
            this.config = logicParameters;
        }
        
        public static Widget extractCurrentWidget(Widget concrete, IWidgetTypeManager widgetTypeManager) {
            WidgetType type = widgetTypeManager.getWidgetType(concrete.getTypeCode());
            if (type.isLogic()) {
                return new CurrentLogicWidget(concrete, type.getConfig());
            }
            return concrete;
        }

        @Override
        public String getTypeCode() {
            return this.concrete.getTypeCode();
        }

        @Override
        public ApsProperties getConfig() {
            return this.config;
        }

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (!(o instanceof CurrentLogicWidget)) {
				return false;
			}
			if (!super.equals(o)) {
				return false;
			}
			CurrentLogicWidget that = (CurrentLogicWidget) o;
			return Objects.equals(config, that.config) && Objects.equals(concrete, that.concrete);
		}

		@Override
		public int hashCode() {
			return Objects.hash(super.hashCode(), config, concrete);
		}
	}

}

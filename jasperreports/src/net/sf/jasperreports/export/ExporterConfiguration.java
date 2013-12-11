/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2013 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.jasperreports.export;

import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.export.ExporterFilter;
import net.sf.jasperreports.engine.export.JRExportProgressMonitor;
import net.sf.jasperreports.engine.export.JRHyperlinkProducerFactory;
import net.sf.jasperreports.export.annotations.ExporterParameter;


/**
 * Instances of this class are used for configuring the behavior of exporters.
 * <p>
 * see {@link Exporter#setConfiguration(ExporterConfiguration)}
 * see {@link ExporterInputItem#getConfiguration()}
 *
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public interface ExporterConfiguration
{
	/**
	 * A global (per context) property that serves as default value for the {@link ExporterConfiguration#isOverrideHints()} setting 
	 * and establishes the priority of export configuration settings against report hints.
	 * 
	 * If the property is true, export configuration settings override report hints; this is the
	 * default behavior.
	 * 
	 * This property cannot be used as a report export hint.
	 */
	public static final String PROPERTY_EXPORT_CONFIGURATION_OVERRIDE_REPORT_HINTS = 
		JRPropertiesUtil.PROPERTY_PREFIX + "export.configuration.override.report.hints";
	
	/**
	 * This property serves as default value for the {@link XlsExporterConfiguration#isIgnorePageMargins()} 
	 * and {@link HtmlExporterConfiguration#isIgnorePageMargins()} exporter configuration settings.
	 * <p>
	 * The property itself defaults to <code>false</code>.
	 * </p>
	 * @see JRPropertiesUtil
	 */
	public static final String PROPERTY_IGNORE_PAGE_MARGINS = JRPropertiesUtil.PROPERTY_PREFIX + "export.ignore.page.margins";

	/**
	 * Specifies whether the settings provided by this exporter configuration object are supposed to override the equivalent exporter 
	 * hints specified in the reports themselves using configuration properties.
	 * @see #PROPERTY_EXPORT_CONFIGURATION_OVERRIDE_REPORT_HINTS
	 */
	public Boolean isOverrideHints();
	

	/**
	 * Returns an integer value representing the start index of the page range to be exported. 
	 * This is useful when only a range of pages is needed for export.
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		type=net.sf.jasperreports.engine.JRExporterParameter.class, 
		name="START_PAGE_INDEX"
		)
	public Integer getStartPageIndex();
	

	/**
	 * Returns an integer value representing the end index of the page range to be exported. 
	 * This is useful when only a range of pages is needed for export.
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		type=net.sf.jasperreports.engine.JRExporterParameter.class, 
		name="END_PAGE_INDEX"
		)
	public Integer getEndPageIndex();
	

	/**
	 * Returns an integer value representing the index of the page to be exported. 
	 * This is useful when only one page of the entire report is needed for export.
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		type=net.sf.jasperreports.engine.JRExporterParameter.class, 
		name="PAGE_INDEX"
		)
	public Integer getPageIndex();
	

	/**
	 * Return a {@link net.sf.jasperreports.engine.export.JRExportProgressMonitor JRExportProgressMonitor} instance for monitoring export status. 
	 * This is useful for users who need to be notified after each page is exported (a GUI tool that shows a progress bar might need this feature).
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		type=net.sf.jasperreports.engine.JRExporterParameter.class, 
		name="PROGRESS_MONITOR"
		)
	public JRExportProgressMonitor getProgressMonitor();
	

	/**
	 * Returns an instance of the {@link net.sf.jasperreports.engine.export.ExporterFilter ExporterFilter} interface to be used by the exporter 
	 * to filter the elements to be exported.
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		type=net.sf.jasperreports.engine.JRExporterParameter.class,
		name="FILTER"
		)
	public ExporterFilter getExporterFilter();
	

	/**
	 * A setting that allows users to move the entire content of each page horizontally. 
	 * It is mostly useful for printing, when the report data does not fit inside the page margins.
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		type=net.sf.jasperreports.engine.JRExporterParameter.class,
		name="OFFSET_X"
		)
	public Integer getOffsetX();
	

	/**
	 * A setting that allows users to move the entire content of each page vertically. 
	 * It is mostly useful for printing, when the report data does not fit inside the page margins.
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		type=net.sf.jasperreports.engine.JRExporterParameter.class,
		name="OFFSET_Y"
		)
	public Integer getOffsetY();
	

	/**
	 * Returns a {@link net.sf.jasperreports.engine.export.JRHyperlinkProducerFactory JRHyperlinkProducerFactory} which should be used for custom 
	 * hyperlink types during export.
	 */
	@SuppressWarnings("deprecation")
	@ExporterParameter(
		type=net.sf.jasperreports.engine.JRExporterParameter.class,
		name="HYPERLINK_PRODUCER_FACTORY"
		)
	public JRHyperlinkProducerFactory getHyperlinkProducerFactory();
}
/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */

/*
 * Contributors:
 * Gaganis Giorgos - gaganis@users.sourceforge.net
 * Peter Severin - peter_p_s@users.sourceforge.net
 */
package net.sf.jasperreports.engine.design;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionChunk;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.util.JRStringUtil;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRClassGenerator
{
	
	
	/**
	 *
	 */
	private static final int EXPR_MAX_COUNT_PER_METHOD = 100;
	
	/**
	 *
	 */
	private JasperDesign jasperDesign = null;

	private static Map fieldPrefixMap = null;
	private static Map variablePrefixMap = null;
	private static Map methodSuffixMap = null;


	/**
	 *
	 */
	protected JRClassGenerator(JasperDesign jrDesign)
	{
		jasperDesign = jrDesign;

		fieldPrefixMap = new HashMap();
		fieldPrefixMap.put(new Byte(JRExpression.EVALUATION_OLD),       "Old");
		fieldPrefixMap.put(new Byte(JRExpression.EVALUATION_ESTIMATED), "");
		fieldPrefixMap.put(new Byte(JRExpression.EVALUATION_DEFAULT),   "");
		
		variablePrefixMap = new HashMap();
		variablePrefixMap.put(new Byte(JRExpression.EVALUATION_OLD),       "Old");
		variablePrefixMap.put(new Byte(JRExpression.EVALUATION_ESTIMATED), "Estimated");
		variablePrefixMap.put(new Byte(JRExpression.EVALUATION_DEFAULT),   "");
		
		methodSuffixMap = new HashMap();
		methodSuffixMap.put(new Byte(JRExpression.EVALUATION_OLD),       "Old");
		methodSuffixMap.put(new Byte(JRExpression.EVALUATION_ESTIMATED), "Estimated");
		methodSuffixMap.put(new Byte(JRExpression.EVALUATION_DEFAULT),   "");
	}


	/**
	 *
	 */
	public static String generateClass(JasperDesign jrDesign) throws JRException
	{
		JRClassGenerator generator = new JRClassGenerator(jrDesign);
		return generator.generateClass();
	}


	/**
	 *
	 */
	protected String generateClass() throws JRException
	{
		StringBuffer sb = new StringBuffer();

		/*   */
		sb.append("/*\n");
		sb.append(" * Generated by JasperReports - ");
		sb.append((new SimpleDateFormat()).format(new java.util.Date()));
		sb.append("\n");
		sb.append(" */\n");
		sb.append("import net.sf.jasperreports.engine.*;\n");
		sb.append("import net.sf.jasperreports.engine.fill.*;\n");
		sb.append("\n");
		sb.append("import java.util.*;\n");
		sb.append("import java.math.*;\n");
		sb.append("import java.text.*;\n");
		sb.append("import java.io.*;\n");
		sb.append("import java.net.*;\n");
		sb.append("\n");
		
		/*   */
		String[] imports = jasperDesign.getImports();
		if (imports != null && imports.length > 0)
		{
			for (int i = 0; i < imports.length; i++)
			{
				sb.append("import ");
				sb.append(imports[i]);
				sb.append(";\n");
			}
		}

		/*   */
		sb.append("\n");
		sb.append("\n");
		sb.append("/**\n");
		sb.append(" *\n");
		sb.append(" */\n");
		sb.append("public class ");
		sb.append(jasperDesign.getName());
		sb.append(" extends JRCalculator\n");
		sb.append("{\n"); 
		sb.append("\n");
		sb.append("\n");
		sb.append("    /**\n");
		sb.append("     *\n");
		sb.append("     */\n");

		/*   */
		Map parametersMap = jasperDesign.getParametersMap();
		if (parametersMap != null && parametersMap.size() > 0)
		{
			Collection parameterNames = parametersMap.keySet();
			for (Iterator it = parameterNames.iterator(); it.hasNext();)
			{
				sb.append("    private JRFillParameter parameter_");
				sb.append(JRStringUtil.getLiteral((String)it.next()));
				sb.append(" = null;\n");
			}
		}
		
		/*   */
		sb.append("\n");

		/*   */
		Map fieldsMap = jasperDesign.getFieldsMap();
		if (fieldsMap != null && fieldsMap.size() > 0)
		{
			Collection fieldNames = fieldsMap.keySet();
			for (Iterator it = fieldNames.iterator(); it.hasNext();)
			{
				sb.append("    private JRFillField field_");
				sb.append(JRStringUtil.getLiteral((String)it.next()));
				sb.append(" = null;\n");
			}
		}
		
		/*   */
		sb.append("\n");

		/*   */
		JRVariable[] variables = jasperDesign.getVariables();
		if (variables != null && variables.length > 0)
		{
			for (int i = 0; i < variables.length; i++)
			{
				sb.append("    private JRFillVariable variable_");
				sb.append(JRStringUtil.getLiteral((String)variables[i].getName()));
				sb.append(" = null;\n");
			}
		}

		/*   */
		this.generateInitMethod(sb);

		sb.append(this.generateMethod(JRExpression.EVALUATION_DEFAULT));
		sb.append(this.generateMethod(JRExpression.EVALUATION_OLD));
		sb.append(this.generateMethod(JRExpression.EVALUATION_ESTIMATED));

		sb.append("}\n");

		return sb.toString();
	}		


	/**
	 *
	 */
	protected void generateInitMethod(StringBuffer sb) throws JRException
	{
		/*   */
		sb.append("\n");
		sb.append("\n");
		sb.append("    /**\n");
		sb.append("     *\n");
		sb.append("     */\n");
		sb.append("    public void customizedInit(\n"); 
		sb.append("        Map pm,\n");
		sb.append("        Map fm,\n"); 
		sb.append("        Map vm\n");
		sb.append("        )\n");
		sb.append("    {\n");
		sb.append("        initParams(pm);\n");
		sb.append("        initFields(fm);\n");
		sb.append("        initVars(vm);\n");
		sb.append("    }\n");
		sb.append("\n");
		sb.append("\n");

		/*   */
		Map parametersMap = jasperDesign.getParametersMap();
		Iterator parIt = null;
		if (parametersMap != null && parametersMap.size() > 0) 
		{
			parIt = parametersMap.keySet().iterator();
		}
		else
		{
			parIt = Collections.EMPTY_SET.iterator();
		}
		generateInitParamsMethod(sb, parIt, 0);

		/*   */
		Map fieldsMap = jasperDesign.getFieldsMap();
		Iterator fieldIt = null;
		if (fieldsMap != null && fieldsMap.size() > 0) 
		{
			fieldIt = fieldsMap.keySet().iterator();
		}
		else
		{
			fieldIt = Collections.EMPTY_SET.iterator();
		}
		generateInitFieldsMethod(sb, fieldIt, 0);

		/*   */
		JRVariable[] variables = jasperDesign.getVariables();
		Iterator varIt = null;
		if (variables != null && variables.length > 0) 
		{
			varIt = Arrays.asList(variables).iterator();
		}
		else
		{
			varIt = Collections.EMPTY_LIST.iterator();
		}
		generateInitVarsMethod(sb, varIt, 0);
	}		


	/**
	 *
	 */
	private void generateInitParamsMethod(StringBuffer sb, Iterator it, int index) throws JRException
	{
		sb.append("    /**\n");
		sb.append("     *\n");
		sb.append("     */\n");
		sb.append("    private void initParams");
		if(index > 0)
		{
			sb.append(index);
		}
		sb.append("(Map pm)\n");
		sb.append("    {\n");
		for (int i = 0; i < EXPR_MAX_COUNT_PER_METHOD && it.hasNext(); i++)
		{
			String parameterName = (String)it.next();
			sb.append("        parameter_");
			sb.append(JRStringUtil.getLiteral(parameterName));
			sb.append(" = (JRFillParameter)pm.get(\"");
			sb.append(parameterName);
			sb.append("\");\n");
		}
		if(it.hasNext())
		{
			sb.append("        initParams");
			sb.append(index + 1);
			sb.append("(pm);\n");
		}
		sb.append("    }\n");
		sb.append("\n");
		sb.append("\n");

		if(it.hasNext())
		{
			generateInitParamsMethod(sb, it, index + 1);
		}
	}		


	/**
	 *
	 */
	private void generateInitFieldsMethod(StringBuffer sb, Iterator it, int index) throws JRException
	{
		sb.append("    /**\n");
		sb.append("     *\n");
		sb.append("     */\n");
		sb.append("    private void initFields");
		if(index > 0)
		{
			sb.append(index);
		}
		sb.append("(Map fm)\n");
		sb.append("    {\n");
		for (int i = 0; i < EXPR_MAX_COUNT_PER_METHOD && it.hasNext(); i++)
		{
			String fieldName = (String)it.next();
			sb.append("        field_");
			sb.append(JRStringUtil.getLiteral(fieldName));
			sb.append(" = (JRFillField)fm.get(\"");
			sb.append(fieldName);
			sb.append("\");\n");
		}
		if(it.hasNext())
		{
			sb.append("        initFields");
			sb.append(index + 1);
			sb.append("(fm);\n");
		}
		sb.append("    }\n");
		sb.append("\n");
		sb.append("\n");

		if(it.hasNext())
		{
			generateInitFieldsMethod(sb, it, index + 1);
		}
	}		


	/**
	 *
	 */
	private void generateInitVarsMethod(StringBuffer sb, Iterator it, int index) throws JRException
	{
		sb.append("    /**\n");
		sb.append("     *\n");
		sb.append("     */\n");
		sb.append("    private void initVars");
		if(index > 0)
		{
			sb.append(index);
		}
		sb.append("(Map vm)\n");
		sb.append("    {\n");
		for (int i = 0; i < EXPR_MAX_COUNT_PER_METHOD && it.hasNext(); i++)
		{
			String variableName = ((JRVariable) it.next()).getName();
			sb.append("        variable_");
			sb.append(JRStringUtil.getLiteral(variableName));
			sb.append(" = (JRFillVariable)vm.get(\"");
			sb.append(variableName);
			sb.append("\");\n");
		}
		if(it.hasNext())
		{
			sb.append("        initVars");
			sb.append(index + 1);
			sb.append("(vm);\n");
		}
		sb.append("    }\n");
		sb.append("\n");
		sb.append("\n");

		if(it.hasNext())
		{
			generateInitVarsMethod(sb, it, index + 1);
		}
	}		


	/**
	 *
	 */
	private String generateMethod(byte evaluationType) throws JRException
	{
		StringBuffer sb = new StringBuffer();

		Collection expressions = jasperDesign.getExpressions();
		if (expressions != null && expressions.size() > 0)
		{
			sb.append(generateMethod(expressions.iterator(), 0, evaluationType));
		}
		else
		{
			/*   */
			sb.append("    /**\n");
			sb.append("     *\n");
			sb.append("     */\n");
			sb.append("    public Object evaluate");
			sb.append((String)methodSuffixMap.get(new Byte(evaluationType)));
			sb.append("(int id) throws Throwable\n");
			sb.append("    {\n");
			sb.append("        return null;\n");
			sb.append("    }\n");
			sb.append("\n");
			sb.append("\n");
		}
		
		return sb.toString();
	}


	/**
	 *
	 */
	private String generateMethod(Iterator it, int index, byte evaluationType) throws JRException
	{
		StringBuffer sb = new StringBuffer();

		/*   */
		sb.append("    /**\n");
		sb.append("     *\n");
		sb.append("     */\n");
		if (index > 0)
		{
			sb.append("    private Object evaluate");
			sb.append((String)methodSuffixMap.get(new Byte(evaluationType)));
			sb.append(index);
		}
		else
		{
			sb.append("    public Object evaluate");
			sb.append((String)methodSuffixMap.get(new Byte(evaluationType)));
		}
		sb.append("(int id) throws Throwable\n");
		sb.append("    {\n");
		sb.append("        Object value = null;\n");
		sb.append("\n");
		sb.append("        switch (id)\n");
		sb.append("        {\n");

		JRExpression expression = null;
		for (int i = 0; it.hasNext() && i < EXPR_MAX_COUNT_PER_METHOD; i++)
		{
			expression = (JRExpression)it.next();
			
			sb.append("            case "); 
			sb.append(expression.getId()); 
			sb.append(" : // ");
			sb.append(expression.getName()); 
			sb.append("\n");
			sb.append("            {\n");
			sb.append("                value = (");
			sb.append(expression.getValueClassName());
			sb.append(")(");
			sb.append(this.generateExpression(expression, evaluationType));
			sb.append(");\n");
			sb.append("                break;\n");
			sb.append("            }\n");
		}

		/*   */
		sb.append("           default :\n");
		sb.append("           {\n");
		if (it.hasNext())
		{
			sb.append("               value = evaluate");
			sb.append((String) methodSuffixMap.get(new Byte(evaluationType)));
			sb.append(index + 1);
			sb.append("(id);\n");
		}
		sb.append("           }\n");
		sb.append("        }\n");
		sb.append("        \n");
		sb.append("        return value;\n");
		sb.append("    }\n");
		sb.append("\n");
		sb.append("\n");
		
		if (it.hasNext())
		{
			sb.append(generateMethod(it, index + 1, evaluationType));
		}
		
		return sb.toString();
	}


	/**
	 *
	 */
	private String generateExpression(
		JRExpression expression,
		byte evaluationType
		) throws JRException
	{
		Map parametersMap = jasperDesign.getParametersMap();
		Map fieldsMap = jasperDesign.getFieldsMap();
		Map variablesMap = jasperDesign.getVariablesMap();

		JRParameter jrParameter = null;
		JRField jrField = null;
		JRVariable jrVariable = null;

		StringBuffer sb = new StringBuffer();

		JRExpressionChunk[] chunks = expression.getChunks();
		JRExpressionChunk chunk = null;
		String chunkText = null;
		if (chunks != null && chunks.length > 0)
		{
			for(int i = 0; i < chunks.length; i++)
			{
				chunk = chunks[i];

				chunkText = chunk.getText();
				if (chunkText == null)
				{
					chunkText = "";
				}
				
				switch (chunk.getType())
				{
					case JRExpressionChunk.TYPE_TEXT :
					{
						sb.append(chunkText);
						break;
					}
					case JRExpressionChunk.TYPE_PARAMETER :
					{
						jrParameter = (JRParameter)parametersMap.get(chunkText);
	
						sb.append("((");
						sb.append(jrParameter.getValueClassName());
						sb.append(")parameter_");
						sb.append(JRStringUtil.getLiteral(chunkText));
						sb.append(".getValue())");
	
						break;
					}
					case JRExpressionChunk.TYPE_FIELD :
					{
						jrField = (JRField)fieldsMap.get(chunkText);
	
						sb.append("((");
						sb.append(jrField.getValueClassName());
						sb.append(")field_");
						sb.append(JRStringUtil.getLiteral(chunkText)); 
						sb.append(".get");
						sb.append((String)fieldPrefixMap.get(new Byte(evaluationType))); 
						sb.append("Value())");
	
						break;
					}
					case JRExpressionChunk.TYPE_VARIABLE :
					{
						jrVariable = (JRVariable)variablesMap.get(chunkText);
	
						sb.append("((");
						sb.append(jrVariable.getValueClassName());
						sb.append(")variable_"); 
						sb.append(JRStringUtil.getLiteral(chunkText)); 
						sb.append(".get");
						sb.append((String)variablePrefixMap.get(new Byte(evaluationType))); 
						sb.append("Value())");
	
						break;
					}
					case JRExpressionChunk.TYPE_RESOURCE :
					{
						sb.append("str(\"");
						sb.append(chunkText);
						sb.append("\")");
	
						break;
					}
				}
			}
		}
		
		if (sb.length() == 0)
		{
			sb.append("null");
		}

		return sb.toString();
	}


}

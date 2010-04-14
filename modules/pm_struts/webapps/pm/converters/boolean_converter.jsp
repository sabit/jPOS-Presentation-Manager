<%--
 * jPOS Presentation Manager [http://jpospm.blogspot.com]
 * Copyright (C) 2010 Jeronimo Paoletti [jeronimo.paoletti@gmail.com]
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *--%>
<%@page contentType="text/html; charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<logic:equal value="checked" parameter="checked">
<input type="radio" value="true"  id="f_${param.f}" name="f_${param.f}" checked /><bean:message key="pm.converter.boolean_converter.yes" />
<input type="radio" value="false" id="f_${param.f}" name="f_${param.f}" /> <bean:message key="pm.converter.boolean_converter.no" />
</logic:equal>
<logic:notEqual value="checked" parameter="checked">
<input type="radio" value="true"  id="f_${param.f}" name="f_${param.f}" /><bean:message key="pm.converter.boolean_converter.yes" />
<input type="radio" value="false" id="f_${param.f}" name="f_${param.f}" checked /><bean:message key="pm.converter.boolean_converter.no" />
</logic:notEqual>

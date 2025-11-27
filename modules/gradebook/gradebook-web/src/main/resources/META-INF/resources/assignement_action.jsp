<%@ include file="/init.jsp" %>

<liferay-ui:icon-menu
	direction="left-side"
	icon="<%= StringPool.BLANK %>"
	markupView="lexicon"
	message="<%= StringPool.BLANK %>"
	showWhenSingleIcon="<%= true %>"
>
		<portlet:renderURL var="editURL">
			<portlet:param name="mvcRenderCommandName" value="/assignment/edit_assignment" />
		</portlet:renderURL>
      <liferay-ui:icon
      			message="edit"
      			url="<%= editURL %>"
      		/>

       <portlet:renderURL var="subURL">
			<portlet:param name="mvcRenderCommandName" value="/assignment/view_submissions"/>
		</portlet:renderURL>
        <liferay-ui:icon
             			message="submission"
             			url="<%= subURL %>"
             		/>

       <portlet:renderURL var="permissionURL">
			<portlet:param name="mvcRenderCommandName" value="/assignment/view_permission" />
	</portlet:renderURL>
 <liferay-ui:icon
      			message="permission"
      			url="<%= permissionURL %>"
      		/>


</liferay-ui:icon-menu>
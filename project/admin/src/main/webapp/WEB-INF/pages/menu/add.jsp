<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<%@ taglib  prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div id="title">添加菜单</div>
<form role="form" action="/menu" class="form-horizontal ajax" method="post" redirect="/menu">
   <div class="form-group">
       <label for="name" class="col-sm-2 control-label">名称:</label>
       <div class="col-sm-10">
           <input type="text" class="form-control" name="name">
       </div>
   </div>
   <div class="form-group">
       <label for="type" class="col-sm-2 control-label">类型:</label>
       <div class="col-sm-10">
	       <select class="form-control" name="type">
	       	   <option value="URL">链接</option>
	       	   <option value="CONTAINER">子菜单</option>
	       </select>
       </div>
   </div>
   <div class="form-group">
       <label for="parentId" class="col-sm-2 control-label">上级菜单:</label>
       <div class="col-sm-10">
	       <select class="form-control" name="parentId">
	       	<option value="">无</option>
	       	   <c:forEach items="${menus }" var="menu">
	       	   	<option <c:if test="${menu.id eq parentId }">selected</c:if> value="${menu.id }">${menu.name }</option>
	       	   </c:forEach>
	       </select>
       </div>
   </div>
   <div class="form-group">
       <label for="ordernum" class="col-sm-2 control-label">序号:</label>
       <div class="col-sm-10">
           <input type="number" class="form-control" name="ordernum">
       </div>
   </div>
   <div class="form-group">
       <label for="url" class="col-sm-2 control-label">地址:</label>
       <div class="col-sm-10">
           <input type="text" class="form-control" name="url">
       </div>
   </div>
   <div class="col-sm-offset-2 col-sm-10">
      <button type="submit" class="btn btn-default">提交</button>
   </div>
</form>

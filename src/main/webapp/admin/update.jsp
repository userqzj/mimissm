<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>

	<head>
		<meta charset="UTF-8">
		<title></title>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.css" />
		<link rel="stylesheet" href="${pageContext.request.contextPath}/css/addBook.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.3.1.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/ajaxfileupload.js"></script>
	</head>

	<body>
		<div id="addAll">
			<div id="nav">
				<p>商品管理>更新商品</p>
			</div>
			<script type="text/javascript">
				function fileChange(){//注意：此处不能使用jQuery中的change事件，因此仅触发一次，因此使用标签的：onchange属性
					//alert("change");
					$.ajaxFileUpload({
						url:"${pageContext.request.contextPath}/prod/ajaxImg.action",
						secureuri: false,
						fileElementId: 'pimage',
						dataType:"json",
						success:function (data) {
							$("#imgDiv").empty();
							//创建一个图片的标签
							var imgData=$("<img>");
							imgData.attr("src","/mimissm/image_big/"+data.imgurl);
							imgData.attr("width","100px");
							imgData.attr("height","100px");
							//将图片追加到imgDiv上
							$("#imgDiv").append(imgData);
						}
					})
				}
			</script>
<script type="text/javascript">
	function myclose(ispage) {
		window.location="${pageContext.request.contextPath}/admin/product?flag=split&ispage="+ispage;
		//window.close();
	}
</script>
			<div id="table">
				<form action="${pageContext.request.contextPath}/prod/update.action" enctype="multipart/form-data" method="post" id="myform">
					<input type="hidden" value="${prod.pId}" name="pId">
					<input type="hidden" value="${prod.pImage}" name="pImage">
					<input type="hidden" value="${page}" name="page">
					<table>
						<tr>
							<td class="one">商品名称</td>
							<td><input type="text" name="pName" class="two" value="${prod.pName}"></td>
						</tr>
						<!--错误提示-->
						<tr class="three">
							<td class="four"></td>
							<td><span id="pnameerr"></span></td>
						</tr>
						<tr>
							<td class="one">商品介绍</td>
							<td><input type="text" name="pContent" class="two" value="${prod.pContent}"></td>
						</tr>
						<!--错误提示-->
						<tr class="three">
							<td class="four"></td>
							<td><span id="pcontenterr"></span></td>
						</tr>
						<tr>
							<td class="one">定价</td>
							<td><input type="number" name="pPrice" class="two" value="${prod.pPrice}"></td>
						</tr>
						<!--错误提示-->
						<tr class="three">
							<td class="four"></td>
							<td><span id="priceerr"></span></td>
						</tr>
						
						<tr>
							<td class="one">图片介绍</td>
							<td> <br><div id="imgDiv" style="display:block; width: 40px; height: 50px;"><img src="/mimissm/image_big/${prod.pImage}" width="100px" height="100px" ></div><br><br><br><br>
								<input type="file" id="pimage" name="pimage" onchange="fileChange()">
								<span id="imgName"></span><br>

							</td>
						</tr>
						<tr class="three">
							<td class="four"></td>
							<td><span></span></td>
						</tr>
						
						<tr>
							<td class="one">总数量</td>
							<td><input type="number" name="pNumber" class="two"  value="${prod.pNumber}"></td>
						</tr>
						<!--错误提示-->
						<tr class="three">
							<td class="four"></td>
							<td><span id="numerr"></span></td>
						</tr>
						
						
						<tr>
							<td class="one">类别</td>
							<td>
								<select name="typeId">
									<c:forEach items="${typeList}" var="type">
										<option value="${type.typeId}"
												<c:if test="${type.typeId==prod.typeId}">
													selected="selected"
												</c:if>
										>${type.typeName}</option>

									</c:forEach>
								</select>
							</td>
						</tr>
						<!--错误提示-->
						<tr class="three">
							<td class="four"></td>
							<td><span></span></td>
						</tr>

						<tr>
							<td>
								<input type="submit" value="提交" class="btn btn-success">
							</td>
							<td>
								<input type="reset" value="取消" class="btn btn-default" onclick="myclose(1)">
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>

	</body>

</html>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.productCategory.edit")} </title>
<meta name="author" content="" />
<meta name="copyright" content="" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<style type="text/css">
.brands label, .promotions label {
	width: 150px;
	display: block;
	float: left;
	padding-right: 6px;
}
</style>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	
	[@flash_message /]
	
	// 表单验证
	$inputForm.validate({
		rules: {
			name: "required",
			generalRate: {
				required: true,
				min: 0,
				decimal: {
					integer: 3,
					fraction: 3
				}
			},
			selfRate: {
				required: true,
				min: 0,
				decimal: {
					integer: 3,
					fraction: 3
				}
			},
			order: "digits"
		}
	});

});
</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/index">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.productCategory.edit")}
	</div>
	<form id="inputForm" action="update" method="post">
		<input type="hidden" name="id" value="${productCategory.id}" />
		<table class="input">
			<tr>
				<th>
					<span class="requiredField">*</span>${message("ProductCategory.name")}:
				</th>
				<td>
					<input type="text" id="name" name="name" class="text" value="${productCategory.name}" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					${message("ProductCategory.parent")}:
				</th>
				<td>
					<select name="parentId">
						<option value="">${message("admin.productCategory.root")}</option>
						[#list productCategoryTree as category]
							[#if category != productCategory && !children?seq_contains(category)]
								<option value="${category.id}"[#if category == productCategory.parent] selected="selected"[/#if]>
									[#if category.grade != 0]
										[#list 1..category.grade as i]
											&nbsp;&nbsp;
										[/#list]
									[/#if]
									${category.name}
								</option>
							[/#if]
						[/#list]
					</select>
				</td>
			</tr>
			[#--<tr>
				<th>
					<span class="requiredField">*</span>${message("ProductCategory.generalRate")}:
				</th>
				<td>
					<input type="text" id="generalRate" name="generalRate" class="text" value="${productCategory.generalRate}" maxlength="200"/>
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("ProductCategory.selfRate")}:
				</th>
				<td>
					<input type="text" id="selfRate" name="selfRate" class="text" value="${productCategory.selfRate}" maxlength="200"/>
				</td>
			</tr>--]
			<input type="hidden" id="generalRate" name="generalRate" class="text" value="${productCategory.generalRate}" maxlength="200"/>
			<input type="hidden" id="selfRate" name="selfRate" class="text" value="${productCategory.selfRate}" maxlength="200"/>



			<tr class="brands">
				<th>
					${message("ProductCategory.brands")}:
				</th>
				<td>
					[#list brands as brand]
						<label>
							<input type="checkbox" name="brandIds" value="${brand.id}"[#if productCategory.brands?seq_contains(brand)] checked="checked"[/#if] />${brand.name}
						</label>
					[/#list]
				</td>
			</tr>
			<tr class="promotions">
				<th>
					${message("ProductCategory.promotions")}:
				</th>
				<td>
					[#list promotions as promotion]
						<label title="${promotion.title}">
							<input type="checkbox" name="promotionIds" value="${promotion.id}"[#if productCategory.promotions?seq_contains(promotion)] checked="checked"[/#if] />${promotion.name}
						</label>
					[/#list]
				</td>
			</tr>
			<tr>
				<th>
					${message("ProductCategory.seoTitle")}:
				</th>
				<td>
					<input type="text" name="seoTitle" class="text" value="${productCategory.seoTitle}" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					${message("ProductCategory.seoKeywords")}:
				</th>
				<td>
					<input type="text" name="seoKeywords" class="text" value="${productCategory.seoKeywords}" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					${message("ProductCategory.seoDescription")}:
				</th>
				<td>
					<input type="text" name="seoDescription" class="text" value="${productCategory.seoDescription}" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					${message("admin.common.order")}:
				</th>
				<td>
					<input type="text" name="order" class="text" value="${productCategory.order}" maxlength="9" />
				</td>
			</tr>
			<tr>
				<th>
					&nbsp;
				</th>
				<td>
					<input type="submit" class="button" value="${message("admin.common.submit")}" />
					<input type="button" class="button" value="${message("admin.common.back")}" onclick="history.back(); return false;" />
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
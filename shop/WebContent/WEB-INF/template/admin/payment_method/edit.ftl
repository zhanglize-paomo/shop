<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.paymentMethod.edit")} </title>
<meta name="author" content="" />
<meta name="copyright" content="" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/webuploader.js"></script>
<script type="text/javascript" src="${base}/resources/admin/ueditor/ueditor.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	var $filePicker = $("#filePicker");
	var $content = $("#content");
	
	[@flash_message /]
	
	$filePicker.uploader();
	
	$content.editor();
	
	// 表单验证
	$inputForm.validate({
		rules: {
			name: "required",
			timeout: {
				integer: true,
				min: 1
			},
			icon: {
				pattern: /^(http:\/\/|https:\/\/|\/).*$/i
			},
			order: "digits"
		}
	});

});
</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/index">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.paymentMethod.edit")}
	</div>
	<form id="inputForm" action="update" method="post">
		<input type="hidden" name="id" value="${paymentMethod.id}" />
		<table class="input">
			<tr>
				<th>
					<span class="requiredField">*</span>${message("PaymentMethod.name")}:
				</th>
				<td>
					<input type="text" name="name" class="text" value="${paymentMethod.name}" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					${message("PaymentMethod.type")}:
				</th>
				<td>
					<select name="type">
						[#list types as type]
							<option value="${type}"[#if type == paymentMethod.type] selected="selected"[/#if]>${message("PaymentMethod.Type." + type)}</option>
						[/#list]
					</select>
				</td>
			</tr>
			<tr>
				<th>
					${message("PaymentMethod.method")}:
				</th>
				<td>
					<select name="method">
						[#list methods as method]
							<option value="${method}"[#if method == paymentMethod.method] selected="selected"[/#if]>${message("PaymentMethod.Method." + method)}</option>
						[/#list]
					</select>
				</td>
			</tr>
			<tr>
				<th>
					${message("PaymentMethod.timeout")}:
				</th>
				<td>
					<input type="text" name="timeout" class="text" value="${paymentMethod.timeout}" maxlength="9" title="${message("admin.paymentMethod.timeoutTitle")}" />
				</td>
			</tr>
			<tr>
				<th>
					${message("PaymentMethod.icon")}:
				</th>
				<td>
					<span class="fieldSet">
						<input type="text" name="icon" class="text" value="${paymentMethod.icon}" maxlength="200" />
						<a href="javascript:;" id="filePicker" class="button">${message("admin.upload.filePicker")}</a>
						[#if paymentMethod.icon??]
							<a href="${paymentMethod.icon}" target="_blank">${message("admin.common.view")}</a>
						[/#if]
					</span>
				</td>
			</tr>
			<tr>
				<th>
					${message("PaymentMethod.description")}:
				</th>
				<td>
					<input type="text" name="description" class="text" value="${paymentMethod.description}" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					${message("admin.common.order")}:
				</th>
				<td>
					<input type="text" name="order" class="text" value="${paymentMethod.order}" maxlength="9" />
				</td>
			</tr>
			<tr>
				<th>
					${message("PaymentMethod.content")}:
				</th>
				<td>
					<textarea id="content" name="content" class="editor">${paymentMethod.content}</textarea>
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
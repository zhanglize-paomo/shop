<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.review.edit")} </title>
<meta name="author" content="" />
<meta name="copyright" content="" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	
	[@flash_message /]
	
});
</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/index">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.review.edit")}
	</div>
	<form id="inputForm" action="update" method="post">
		<input type="hidden" name="id" value="${review.id}" />
		<table class="input">
			<tr>
				<th>
					${message("Review.product")}:
				</th>
				<td>
					<a href="${base}${review.product.path}" target="_blank">${review.product.name}</a>
				</td>
			</tr>
			<tr>
				<th>
					${message("Review.member")}:
				</th>
				<td>
					[#if review.member??]
						<a href="../member/view?id=${review.member.id}">${review.member.username}</a>
					[#else]
						${message("admin.review.anonymous")}
					[/#if]
				</td>
			</tr>
			<tr>
				<th>
					${message("Review.ip")}:
				</th>
				<td>
					${review.ip}
				</td>
			</tr>
			<tr>
				<th>
					${message("Review.score")}:
				</th>
				<td>
					${review.score}
				</td>
			</tr>
			<tr>
				<th>
					${message("Review.content")}:
				</th>
				<td>
					${review.content}
				</td>
			</tr>
			<tr>
				<th>
					${message("Review.isShow")}:
				</th>
				<td>
					<input type="checkbox" name="isShow" value="true"[#if review.isShow] checked="checked"[/#if] />
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
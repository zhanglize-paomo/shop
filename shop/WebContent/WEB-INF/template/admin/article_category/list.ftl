<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.articleCategory.list")} </title>
<meta name="author" content="" />
<meta name="copyright" content="" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $delete = $("#listTable a.delete");
	
	[@flash_message /]
	
	// 删除
	$delete.click(function() {
		var $this = $(this);
		$.dialog({
			type: "warn",
			content: "${message("admin.dialog.deleteConfirm")}",
			onOk: function() {
				$.ajax({
					url: "delete",
					type: "POST",
					data: {id: $this.attr("val")},
					dataType: "json",
					cache: false,
					success: function(message) {
						$.message(message);
						if (message.type == "success") {
							$this.closest("tr").remove();
						}
					}
				});
			}
		});
		return false;
	});

});
</script>
</head>
<body>
	<div class="breadcrumb">
		<a href="${base}/admin/index">${message("admin.breadcrumb.home")}</a> &raquo; ${message("admin.articleCategory.list")}
	</div>
	<div class="bar">
		<a href="add" class="iconButton">
			<span class="addIcon">&nbsp;</span>${message("admin.common.add")}
		</a>
		<a href="javascript:;" id="refreshButton" class="iconButton">
			<span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
		</a>
	</div>
	<table id="listTable" class="list">
		<tr>
			<th>
				<span>${message("ArticleCategory.name")}</span>
			</th>
			<th>
				<span>${message("admin.common.order")}</span>
			</th>
			<th>
				<span>${message("admin.common.action")}</span>
			</th>
		</tr>
		[#list articleCategoryTree as articleCategory]
			<tr>
				<td>
					<span style="margin-left: ${articleCategory.grade * 20}px;[#if articleCategory.grade == 0] color: #000000;[/#if]">
						${articleCategory.name}
					</span>
				</td>
				<td>
					${articleCategory.order}
				</td>
				<td>
					<a href="${base}${articleCategory.path}" target="_blank">[${message("admin.common.view")}]</a>
					<a href="edit?id=${articleCategory.id}">[${message("admin.common.edit")}]</a>
					<a href="javascript:;" class="delete" val="${articleCategory.id}">[${message("admin.common.delete")}]</a>
				</td>
			</tr>
		[/#list]
	</table>
</body>
</html>
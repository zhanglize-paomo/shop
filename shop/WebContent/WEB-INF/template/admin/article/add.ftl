<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html;charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("admin.article.add")} </title>
<meta name="author" content="" />
<meta name="copyright" content="" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin/ueditor/ueditor.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	var $content = $("#content");
	
	[@flash_message /]
	
	$content.editor();
	
	// 表单验证
	$inputForm.validate({
		rules: {
			title: "required",
			articleCategoryId: "required"
		}
	});

});
</script>
</head>
<body>
	<div class="breadcrumb">
		${message("admin.article.add")}
	</div>
	<form id="inputForm" action="save" method="post">
		<table class="input">
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Article.title")}:
				</th>
				<td>
					<input type="text" name="title" class="text" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					<span class="requiredField">*</span>${message("Article.articleCategory")}:
				</th>
				<td>
					<select name="articleCategoryId">
						[#list articleCategoryTree as articleCategory]
							<option value="${articleCategory.id}">
								[#if articleCategory.grade != 0]
									[#list 1..articleCategory.grade as i]
										&nbsp;&nbsp;
									[/#list]
								[/#if]
								${articleCategory.name}
							</option>
						[/#list]
					</select>
				</td>
			</tr>
			<tr>
				<th>
					${message("Article.author")}:
				</th>
				<td>
					<input type="text" name="author" class="text" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					${message("admin.common.setting")}:
				</th>
				<td>
					<label>
						<input type="checkbox" name="isPublication" value="true" checked="checked" />${message("Article.isPublication")}
						<input type="hidden" name="_isPublication" value="false" />
					</label>
					<label>
						<input type="checkbox" name="isTop" value="true" />${message("Article.isTop")}
						<input type="hidden" name="_isTop" value="false" />
					</label>
				</td>
			</tr>
			<tr>
				<th>
					${message("Article.articleTags")}:
				</th>
				<td>
					[#list articleTags as articleTag]
						<label>
							<input type="checkbox" name="articleTagIds" value="${articleTag.id}" />${articleTag.name}
						</label>
					[/#list]
				</td>
			</tr>
			<tr>
				<th>
					${message("Article.content")}:
				</th>
				<td>
					<textarea id="content" name="content" class="editor"></textarea>
				</td>
			</tr>
			<tr>
				<th>
					${message("Article.seoTitle")}:
				</th>
				<td>
					<input type="text" name="seoTitle" class="text" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					${message("Article.seoKeywords")}:
				</th>
				<td>
					<input type="text" name="seoKeywords" class="text" maxlength="200" />
				</td>
			</tr>
			<tr>
				<th>
					${message("Article.seoDescription")}:
				</th>
				<td>
					<input type="text" name="seoDescription" class="text" maxlength="200" />
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
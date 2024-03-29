<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html;charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>${message("member.productFavorite.list")}[#if showPowered] [/#if]</title>
<meta name="author" content="" />
<meta name="copyright" content="" />
<link href="${base}/resources/member/css/animate.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/member/css/common.css" rel="stylesheet" type="text/css" />
<link href="${base}/resources/member/css/member.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/member/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/member/js/common.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $delete = $("#listTable a.delete");
	
	// 删除
	$delete.click(function() {
		if (confirm("${message("member.dialog.deleteConfirm")}")) {
			var $element = $(this);
			var productFavoriteId = $element.data("product-favorite-id");
			$.ajax({
				url: "delete",
				type: "POST",
				data: {productFavoriteId: productFavoriteId},
				dataType: "json",
				success: function() {
					var $item = $element.closest("tr");
					if ($item.siblings("tr").size() < 2) {
						setTimeout(function() {
							location.reload(true);
						}, 3000);
					}
					$item.remove();
				}
			});
		}
		return false;
	});

});
</script>
</head>
<body>
	[#assign current = "productFavoriteList" /]
	[#include "/shop/include/header.ftl" /]
	<div class="container member">
		<div class="row">
			[#include "/member/include/navigation.ftl" /]
			<div class="span10">
				<div class="list">
					<div class="title">${message("member.productFavorite.list")}</div>
					<table id="listTable" class="list">
						<tr>
							<th>
								${message("member.productFavorite.productImage")}
							</th>
							<th>
								${message("Product.sn")}
							</th>
							<th>
								${message("Product.name")}
							</th>
							<th>
								${message("Product.price")}
							</th>
							<th>
								${message("member.common.action")}
							</th>
						</tr>
						[#list page.content as productFavorite]
							<tr[#if !productFavorite_has_next] class="last"[/#if]>
								<td>
									<img src="${productFavorite.product.thumbnail!setting.defaultThumbnailProductImage}" class="productThumbnail" alt="${productFavorite.product.name}" />
								</td>
								<td>
									${productFavorite.product.sn}
								</td>
								<td>
									<a href="${base}${productFavorite.product.path}" title="${productFavorite.product.name}" target="_blank">${abbreviate(productFavorite.product.name, 30)}</a>
								</td>
								<td>
									${currency(productFavorite.product.price, true)}
								</td>
								<td>
									<a href="javascript:;" class="delete" data-product-favorite-id="${productFavorite.id}">[${message("member.common.delete")}]</a>
								</td>
							</tr>
						[/#list]
					</table>
					[#if !page.content?has_content]
						<p>${message("member.common.noResult")}</p>
					[/#if]
				</div>
				[@pagination pageNumber = page.pageNumber totalPages = page.totalPages pattern = "?pageNumber={pageNumber}"]
					[#include "/shop/include/pagination.ftl"]
				[/@pagination]
			</div>
		</div>
	</div>
	[#include "/shop/include/footer.ftl" /]
</body>
</html>
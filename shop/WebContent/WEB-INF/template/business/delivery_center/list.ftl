<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="author" content="">
	<meta name="copyright" content="">
	<title>${message("business.deliveryCenter.list")} </title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/business/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/business/css/font-awesome.css" rel="stylesheet">
	<link href="${base}/resources/business/css/animate.css" rel="stylesheet">
	<link href="${base}/resources/business/css/adminLTE.css" rel="stylesheet">
	<link href="${base}/resources/business/css/common.css" rel="stylesheet">
	<!--[if lt IE 9]>
		<script src="${base}/resources/business/js/html5shiv.js"></script>
		<script src="${base}/resources/business/js/respond.js"></script>
	<![endif]-->
	<script src="${base}/resources/business/js/jquery.js"></script>
	<script src="${base}/resources/business/js/bootstrap.js"></script>
	<script src="${base}/resources/business/js/velocity.js"></script>
	<script src="${base}/resources/business/js/velocity.ui.js"></script>
	<script src="${base}/resources/business/js/icheck.js"></script>
	<script src="${base}/resources/business/js/adminLTE.js"></script>
	<script src="${base}/resources/business/js/common.js"></script>
	<script type="text/javascript">
	$().ready(function() {
	
		[#if flashMessage?has_content]
			$.alert("${flashMessage}");
		[/#if]
	
	});
	</script>
</head>
<body class="hold-transition sidebar-mini">
	<div class="wrapper">
		[#include "/business/include/main_header.ftl" /]
		[#include "/business/include/main_sidebar.ftl" /]
		<div class="content-wrapper">
			<div class="container-fluid">
				<section class="content-header">
					<h1>${message("business.deliveryCenter.list")}</h1>
					<ol class="breadcrumb">
						<li>
							<a href="${base}/business/index">
								<i class="fa fa-home"></i>
								${message("business.common.index")}
							</a>
						</li>
						<li class="active">${message("business.deliveryCenter.list")}</li>
					</ol>
				</section>
				<section class="content">
					<div class="row">
						<div class="col-xs-12">
							<form action="${base}/business/delivery_center/list" method="get">
								<input name="pageSize" type="hidden" value="${page.pageSize}">
								<input name="pageNumber" type="hidden" value="${page.pageNumber}">
								<input name="searchProperty" type="hidden" value="${page.searchProperty}">
								<input name="orderProperty" type="hidden" value="${page.orderProperty}">
								<input name="orderDirection" type="hidden" value="${page.orderDirection}">
								<div class="box">
									<div class="box-header">
										<div class="row">
											<div class="col-xs-9">
												<div class="btn-group">
													<a class="btn btn-default" href="${base}/business/delivery_center/add">
														<i class="fa fa-plus"></i>
														${message("business.common.add")}
													</a>
													<button class="btn btn-default" type="button" data-toggle="delete" disabled>
														<i class="fa fa-times"></i>
														${message("business.common.delete")}
													</button>
													<button class="btn btn-default" type="button" data-toggle="refresh">
														<i class="fa fa-refresh"></i>
														${message("business.common.refresh")}
													</button>
													<div class="btn-group">
														<button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown">
															${message("business.common.pageSize")}
															<span class="caret"></span>
														</button>
														<ul class="dropdown-menu">
															<li[#if page.pageSize == 10] class="active"[/#if] data-page-size="10">
																<a href="javascript:;">10</a>
															</li>
															<li[#if page.pageSize == 20] class="active"[/#if] data-page-size="20">
																<a href="javascript:;">20</a>
															</li>
															<li[#if page.pageSize == 50] class="active"[/#if] data-page-size="50">
																<a href="javascript:;">50</a>
															</li>
															<li[#if page.pageSize == 100] class="active"[/#if] data-page-size="100">
																<a href="javascript:;">100</a>
															</li>
														</ul>
													</div>
												</div>
											</div>
											<div class="col-xs-3">
												<div class="box-tools">
													<div id="search" class="input-group">
														<div class="input-group-btn">
															<button class="btn btn-default" type="button" data-toggle="dropdown">
																[#switch page.searchProperty]
																	[#case "contact"]
																		<span>${message("DeliveryCenter.contact")}</span>
																		[#break]
																	[#case "phone"]
																		<span>${message("DeliveryCenter.phone")}</span>
																		[#break]
																	[#case "mobile"]
																		<span>${message("DeliveryCenter.mobile")}</span>
																		[#break]
																	[#case "areaName"]
																		<span>${message("DeliveryCenter.areaName")}</span>
																		[#break]
																	[#case "address"]
																		<span>${message("DeliveryCenter.address")}</span>
																		[#break]
																	[#default]
																		<span>${message("DeliveryCenter.name")}</span>
																[/#switch]
																<i class="caret"></i>
															</button>
															<ul class="dropdown-menu">
																<li[#if !page.searchProperty?? || page.searchProperty == "name"] class="active"[/#if] data-search-property="name">
																	<a href="javascript:;">${message("DeliveryCenter.name")}</a>
																</li>
																<li[#if page.searchProperty == "contact"] class="active"[/#if] data-search-property="contact">
																	<a href="javascript:;">${message("DeliveryCenter.contact")}</a>
																</li>
																<li[#if page.searchProperty == "phone"] class="active"[/#if] data-search-property="phone">
																	<a href="javascript:;">${message("DeliveryCenter.phone")}</a>
																</li>
																<li[#if page.searchProperty == "mobile"] class="active"[/#if] data-search-property="mobile">
																	<a href="javascript:;">${message("DeliveryCenter.mobile")}</a>
																</li>
																<li[#if page.searchProperty == "areaName"] class="active"[/#if] data-search-property="areaName">
																	<a href="javascript:;">${message("DeliveryCenter.areaName")}</a>
																</li>
																<li[#if page.searchProperty == "address"] class="active"[/#if] data-search-property="address">
																	<a href="javascript:;">${message("DeliveryCenter.address")}</a>
																</li>
															</ul>
														</div>
														<input name="searchValue" class="form-control" type="text" value="${page.searchValue}" placeholder="${message("business.common.search")}">
														<div class="input-group-btn">
															<button class="btn btn-default" type="submit">
																<i class="fa fa-search"></i>
															</button>
														</div>
													</div>
												</div>
											</div>
										</div>
									</div>
									<div class="box-body table-responsive no-padding">
										<table class="table table-hover">
											<thead>
												<tr>
													<th>
														<i class="check-all fa fa-square-o" data-toggle="checkAll"></i>
													</th>
													<th>
														<a href="javascript:;" data-order-property="name">
															${message("DeliveryCenter.name")}
															<i class="fa fa-sort"></i>
														</a>
													</th>
													<th>
														<a href="javascript:;" data-order-property="contact">
															${message("DeliveryCenter.contact")}
															<i class="fa fa-sort"></i>
														</a>
													</th>
													<th>
														<a href="javascript:;" data-order-property="areaName">
															${message("DeliveryCenter.areaName")}
															<i class="fa fa-sort"></i>
														</a>
													</th>
													<th>
														<a href="javascript:;" data-order-property="address">
															${message("DeliveryCenter.address")}
															<i class="fa fa-sort"></i>
														</a>
													</th>
													<th>
														<a href="javascript:;" data-order-property="zipCode">
															${message("DeliveryCenter.zipCode")}
															<i class="fa fa-sort"></i>
														</a>
													</th>
													<th>
														<a href="javascript:;" data-order-property="phone">
															${message("DeliveryCenter.phone")}
															<i class="fa fa-sort"></i>
														</a>
													</th>
													<th>
														<a href="javascript:;" data-order-property="mobile">
															${message("DeliveryCenter.mobile")}
															<i class="fa fa-sort"></i>
														</a>
													</th>
													<th>
														<a href="javascript:;" data-order-property="isDefault">
															${message("DeliveryCenter.isDefault")}
															<i class="fa fa-sort"></i>
														</a>
													</th>
													<th>${message("business.common.action")}</th>
												</tr>
											</thead>
											<tbody>
												[#list page.content as deliveryCenter]
													<tr>
														<td>
															<input name="ids" type="checkbox" value="${deliveryCenter.id}">
														</td>
														<td>${deliveryCenter.name}</td>
														<td>${deliveryCenter.contact}</td>
														<td>${deliveryCenter.areaName}</td>
														<td>
															<span title="${deliveryCenter.address}">${abbreviate(deliveryCenter.address, 50, "...")}</span>
														</td>
														<td>${deliveryCenter.zipCode}</td>
														<td>${deliveryCenter.phone}</td>
														<td>${deliveryCenter.mobile}</td>
														<td>
															[#if deliveryCenter.isDefault]
																<i class="fa fa-check"></i>
															[#else]
																<i class="fa fa-times"></i>
															[/#if]
														</td>
														<td>
															<a href="${base}/business/delivery_center/edit?deliveryCenterId=${deliveryCenter.id}">[${message("business.common.edit")}]</a>
														</td>
													</tr>
												[/#list]
											</tbody>
										</table>
										[#if !page.content?has_content]
											<p class="no-result">${message("business.common.noResult")}</p>
										[/#if]
									</div>
									[@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
										[#if totalPages > 1]
											<div class="box-footer clearfix">
												[#include "/business/include/pagination.ftl"]
											</div>
										[/#if]
									[/@pagination]
								</div>
							</form>
						</div>
					</div>
				</section>
			</div>
		</div>
		[#include "/business/include/main_footer.ftl" /]
	</div>
</body>
</html>
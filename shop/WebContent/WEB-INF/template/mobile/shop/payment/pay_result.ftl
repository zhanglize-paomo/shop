[#if message?has_content]
	[#noautoesc]
		${message}
	[/#noautoesc]
[#else]
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no">
	<meta name="author" content="">
	<meta name="copyright" content="">
	<title>${message("shop.payment.payResult")}[#if showPowered] [/#if]</title>
	<link href="${base}/favicon.ico" rel="icon">
	<link href="${base}/resources/mobile/shop/css/bootstrap.css" rel="stylesheet">
	<link href="${base}/resources/mobile/shop/css/common.css" rel="stylesheet">
	<link href="${base}/resources/mobile/shop/css/payment.css" rel="stylesheet">
	<!--[if lt IE 9]>
		<script src="${base}/resources/mobile/shop/js/html5shiv.js"></script>
		<script src="${base}/resources/mobile/shop/js/respond.js"></script>
	<![endif]-->
	<script src="${base}/resources/mobile/shop/js/jquery.js"></script>
	<script src="${base}/resources/mobile/shop/js/bootstrap.js"></script>
	<script src="${base}/resources/mobile/shop/js/common.js"></script>
</head>
<style>
	.payment {
		margin-top: 40px;
	}
</style>
<body>
	<header class="header-fixed">${message("shop.payment.payResult")}</header>
	<main>
		<div class="container payment">
			<div class="row">
				<div class="col-xs-12">
					[#if paymentTransaction?has_content]
						<div class="alert alert-success text-center">
							[#if paymentTransaction.isSuccess]
								${message("shop.payment.payCompleted")}
							[#else]
								${message("shop.payment.wait")}
							[/#if]
						</div>
						<div class="text-center">
							<a href="${base}/">${message("shop.payment.index")}</a>
						</div>
					[#else]
						<div class="alert alert-danger text-center">${message("shop.payment.failure")}</div>
					[/#if]
				</div>
			</div>
		</div>
	</main>
	<footer class="footer-fixed">
		<div class="container-fluid">
			<div class="row">
				<div class="col-xs-3 text-center active">
					<span class="glyphicon glyphicon-home"></span>
					<a href="${base}/">${message("shop.common.index")}</a>
				</div>
				<div class="col-xs-3 text-center">
					<span class="glyphicon glyphicon-th-list"></span>
					<a href="${base}/product_category">${message("shop.common.productCategory")}</a>
				</div>
				<div class="col-xs-3 text-center">
					<span class="glyphicon glyphicon-shopping-cart"></span>
					<a href="${base}/cart/list">${message("shop.common.cart")}</a>
				</div>
				<div class="col-xs-3 text-center">
					<span class="glyphicon glyphicon-user"></span>
					<a href="${base}/member/index">${message("shop.common.member")}</a>
				</div>
			</div>
		</div>
	</footer>
</body>
</html>
[/#if]
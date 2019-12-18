/*
 * Copyright 2005-2017 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.controller.admin;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;

import net.shopxx.Results;
import net.shopxx.entity.*;
import net.shopxx.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import net.shopxx.Page;
import net.shopxx.Pageable;
import net.shopxx.Setting;
import net.shopxx.util.SystemUtils;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 订单
 * 
 * @author SHOP++ Team
 * @version 5.0
 */
@Controller("adminOrderController")
@RequestMapping("/admin/order")
public class OrderController extends BaseController {

	@Inject
	private StoreService storeService;
	@Inject
	private AreaService areaService;
	@Inject
	private OrderService orderService;
	@Inject
	private ShippingMethodService shippingMethodService;
	@Inject
	private PaymentMethodService paymentMethodService;
	@Inject
	private DeliveryCorpService deliveryCorpService;
	@Inject
	private OrderShippingService orderShippingService;
	@Inject
	private MemberService memberService;

	/**
	 * 物流动态
	 */
	@GetMapping("/transit_step")
	public @ResponseBody Map<String, Object> transitStep(Long shippingId) {
		Map<String, Object> data = new HashMap<>();
		OrderShipping orderShipping = orderShippingService.find(shippingId);
		if (orderShipping == null) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		Setting setting = SystemUtils.getSetting();
		if (StringUtils.isEmpty(setting.getKuaidi100Key()) || StringUtils.isEmpty(orderShipping.getDeliveryCorpCode()) || StringUtils.isEmpty(orderShipping.getTrackingNo())) {
			data.put("message", ERROR_MESSAGE);
			return data;
		}
		data.put("message", SUCCESS_MESSAGE);
		data.put("transitSteps", orderShippingService.getTransitSteps(orderShipping));
		return data;
	}

	/**
	 * 查看
	 */
	@GetMapping("/view")
	public String view(Long id, ModelMap model) {
		Setting setting = SystemUtils.getSetting();
		model.addAttribute("methods", OrderPayment.Method.values());
		model.addAttribute("refundsMethods", OrderRefunds.Method.values());
		model.addAttribute("paymentMethods", paymentMethodService.findAll());
		model.addAttribute("shippingMethods", shippingMethodService.findAll());
		model.addAttribute("deliveryCorps", deliveryCorpService.findAll());
		model.addAttribute("isKuaidi100Enabled", StringUtils.isNotEmpty(setting.getKuaidi100Key()));
		model.addAttribute("order", orderService.find(id));
		return "admin/order/view";
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(Order.Type type, Order.Status status, String memberUsername, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable, ModelMap model) {
		model.addAttribute("types", Order.Type.values());
		model.addAttribute("statuses", Order.Status.values());
		model.addAttribute("type", type);
		model.addAttribute("status", status);
		model.addAttribute("memberUsername", memberUsername);
		model.addAttribute("isPendingReceive", isPendingReceive);
		model.addAttribute("isPendingRefunds", isPendingRefunds);
		model.addAttribute("isAllocatedStock", isAllocatedStock);
		model.addAttribute("hasExpired", hasExpired);

		Member member = memberService.findByUsername(memberUsername);
		if (StringUtils.isNotEmpty(memberUsername) && member == null) {
			model.addAttribute("page", Page.emptyPage(pageable));
		} else {
			model.addAttribute("page", orderService.findPage(type, status, null, member, null, isPendingReceive, isPendingRefunds, null, null, isAllocatedStock, hasExpired, pageable));
		}
		return "admin/order/list";
	}

	/**
	 * 获取订单锁
	 */
	@PostMapping("/acquire_lock")
	public @ResponseBody boolean acquireLock(@ModelAttribute(binding = false) Order order) {
		return true;
//		return order != null && orderService.acquireLock(order);
	}

	/**
	 * 计算
	 */
	@PostMapping("/calculate")
	public ResponseEntity<?> calculate(Long orderId, BigDecimal freight, BigDecimal tax, BigDecimal offsetAmount) {
		Order order = orderService.find(orderId);

		if (order == null) {
			return Results.UNPROCESSABLE_ENTITY;
		}

		Map<String, Object> data = new HashMap<>();
		data.put("amount", orderService.calculateAmount(order.getPrice(), order.getFee(), freight, tax, order.getPromotionDiscount(), order.getCouponDiscount(), offsetAmount));
		return ResponseEntity.ok(data);
	}

	/**
	 * 审核
	 */
	@PostMapping("/review")
	public String review(Long orderId, Boolean passed,  RedirectAttributes redirectAttributes) {
		Order order = orderService.find(orderId);

		if (order == null || order.hasExpired() || !Order.Status.pendingReview.equals(order.getStatus()) || passed == null) {
			return ERROR_VIEW;
		}

		orderService.review(order, passed);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:view?id=" + order.getId();
	}

	/**
	 * 收款
	 */
	@PostMapping("/payment")
	public String payment(OrderPayment orderPaymentForm,Long  orderId, Long paymentMethodId, RedirectAttributes redirectAttributes) {

		Order order = orderService.find(orderId);
		orderPaymentForm.setOrder(order);
		orderPaymentForm.setPaymentMethod(paymentMethodService.find(paymentMethodId));
		if (!isValid(orderPaymentForm)) {
			return ERROR_VIEW;
		}

		orderPaymentForm.setFee(BigDecimal.ZERO);
		orderService.payment(order, orderPaymentForm);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:view?id=" + order.getId();

	}

	/**
	 * 退款
	 */
	@PostMapping("/refunds")
	public String refunds(OrderRefunds orderRefundsForm, Long orderId, Long paymentMethodId,  RedirectAttributes redirectAttributes) {
		Order order = orderService.find(orderId);

		if (order == null || order.hasExpired() || order.getRefundableAmount().compareTo(BigDecimal.ZERO) <= 0) {
			return ERROR_VIEW;
		}
		orderRefundsForm.setOrder(order);
		orderRefundsForm.setPaymentMethod(paymentMethodService.find(paymentMethodId));
		if (!isValid(orderRefundsForm)) {
			return ERROR_VIEW;
		}
		if (OrderRefunds.Method.deposit.equals(orderRefundsForm.getMethod()) && orderRefundsForm.getAmount().compareTo(order.getStore().getBusiness().getBalance()) > 0) {
			return ERROR_VIEW;
		}
		orderService.refunds(order, orderRefundsForm);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:view?id=" + order.getId();
	}

	/**
	 * 发货
	 */
	@PostMapping("/shipping")
	public String shipping(OrderShipping orderShippingForm, Long orderId, Long shippingMethodId, Long deliveryCorpId, Long areaId, RedirectAttributes redirectAttributes) {
		Order order = orderService.find(orderId);

		if (order == null || order.getShippableQuantity() <= 0) {
			return ERROR_VIEW;
		}
		boolean isDelivery = false;
		for (Iterator<OrderShippingItem> iterator = orderShippingForm.getOrderShippingItems().iterator(); iterator.hasNext();) {
			OrderShippingItem orderShippingItem = iterator.next();
			if (orderShippingItem == null || StringUtils.isEmpty(orderShippingItem.getSn()) || orderShippingItem.getQuantity() == null || orderShippingItem.getQuantity() <= 0) {
				iterator.remove();
				continue;
			}
			OrderItem orderItem = order.getOrderItem(orderShippingItem.getSn());
			if (orderItem == null || orderShippingItem.getQuantity() > orderItem.getShippableQuantity()) {
				return ERROR_VIEW;
			}
			Sku sku = orderItem.getSku();
			if (sku != null && orderShippingItem.getQuantity() > sku.getStock()) {
				return ERROR_VIEW;
			}
			orderShippingItem.setName(orderItem.getName());
			orderShippingItem.setIsDelivery(orderItem.getIsDelivery());
			orderShippingItem.setSku(sku);
			orderShippingItem.setOrderShipping(orderShippingForm);
			orderShippingItem.setSpecifications(orderItem.getSpecifications());
			if (orderItem.getIsDelivery()) {
				isDelivery = true;
			}
		}
		orderShippingForm.setOrder(order);
		orderShippingForm.setShippingMethod(shippingMethodService.find(shippingMethodId));
		orderShippingForm.setDeliveryCorp(deliveryCorpService.find(deliveryCorpId));
		orderShippingForm.setArea(areaService.find(areaId));
		if (isDelivery) {
			if (!isValid(orderShippingForm, OrderShipping.Delivery.class)) {
				return ERROR_VIEW;
			}
		} else {
			orderShippingForm.setShippingMethod((String) null);
			orderShippingForm.setDeliveryCorp((String) null);
			orderShippingForm.setDeliveryCorpUrl(null);
			orderShippingForm.setDeliveryCorpCode(null);
			orderShippingForm.setTrackingNo(null);
			orderShippingForm.setFreight(null);
			orderShippingForm.setConsignee(null);
			orderShippingForm.setArea((String) null);
			orderShippingForm.setAddress(null);
			orderShippingForm.setZipCode(null);
			orderShippingForm.setPhone(null);
			if (!isValid(orderShippingForm)) {
				return ERROR_VIEW;
			}
		}

		orderService.shipping(order, orderShippingForm);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:view?id=" + order.getId();
	}

	/**
	 * 退货
	 */
	@PostMapping("/returns")
	public String returns(OrderReturns orderReturnsForm, Long orderId, Long shippingMethodId, Long deliveryCorpId, Long areaId,  RedirectAttributes redirectAttributes) {
		Order order = orderService.find(orderId);

		if (order == null || order.getReturnableQuantity() <= 0) {
			return ERROR_VIEW;
		}
		for (Iterator<OrderReturnsItem> iterator = orderReturnsForm.getOrderReturnsItems().iterator(); iterator.hasNext();) {
			OrderReturnsItem orderReturnsItem = iterator.next();
			if (orderReturnsItem == null || StringUtils.isEmpty(orderReturnsItem.getSn()) || orderReturnsItem.getQuantity() == null || orderReturnsItem.getQuantity() <= 0) {
				iterator.remove();
				continue;
			}
			OrderItem orderItem = order.getOrderItem(orderReturnsItem.getSn());
			if (orderItem == null || orderReturnsItem.getQuantity() > orderItem.getReturnableQuantity()) {
				return ERROR_VIEW;
			}
			orderReturnsItem.setName(orderItem.getName());
			orderReturnsItem.setOrderReturns(orderReturnsForm);
			orderReturnsItem.setSpecifications(orderItem.getSpecifications());
		}
		orderReturnsForm.setOrder(order);
		orderReturnsForm.setShippingMethod(shippingMethodService.find(shippingMethodId));
		orderReturnsForm.setDeliveryCorp(deliveryCorpService.find(deliveryCorpId));
		orderReturnsForm.setArea(areaService.find(areaId));
		if (!isValid(orderReturnsForm)) {
			return ERROR_VIEW;
		}

		orderService.returns(order, orderReturnsForm);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:view?id=" + order.getId();
	}

	/**
	 * 完成
	 */
	@PostMapping("/complete")
	public String complete(Long orderId,  RedirectAttributes redirectAttributes) {
		Order order = orderService.find(orderId);

		if (order == null || order.hasExpired() || !Order.Status.received.equals(order.getStatus())) {
			return ERROR_VIEW;
		}

		orderService.complete(order);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:view?id=" + order.getId();
	}

	/**
	 * 失败
	 */
	@PostMapping("/fail")
	public String fail( Long orderId, RedirectAttributes redirectAttributes) {
		Order order = orderService.find(orderId);
		if (order == null || order.hasExpired() || (!Order.Status.pendingShipment.equals(order.getStatus()) && !Order.Status.shipped.equals(order.getStatus()) && !Order.Status.received.equals(order.getStatus()))) {
			return ERROR_VIEW;
		}

		orderService.fail(order);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:view?id=" + order.getId();
	}
}
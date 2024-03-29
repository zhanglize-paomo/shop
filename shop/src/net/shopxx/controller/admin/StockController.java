/*
 * Copyright 2005-2017 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.controller.admin;

import net.shopxx.Pageable;
import net.shopxx.entity.Business;
import net.shopxx.entity.Sku;
import net.shopxx.entity.StockLog;
import net.shopxx.entity.Store;
import net.shopxx.exception.UnauthorizedException;
import net.shopxx.security.CurrentUser;
import net.shopxx.service.SkuService;
import net.shopxx.service.StockLogService;
import net.shopxx.service.StoreService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller - 库存
 * 
 * @author SHOP++ Team
 * @version 5.0
 */
@Controller("adminStockController")
@RequestMapping("/admin/stock")
public class StockController extends BaseController {

	@Inject
	private StockLogService stockLogService;
	@Inject
	private SkuService skuService;
	@Inject
	private StoreService storeService;

	/**
	 * 记录
	 */
	@GetMapping("/log")
	public String log(Pageable pageable, ModelMap model) {
		model.addAttribute("page", stockLogService.findPage(pageable));
		return "admin/stock/log";
	}

	/**
	 * 添加属性
	 */
	@ModelAttribute
	public void populateModel(String skuSn, ModelMap model) {
		Store currentStore = storeService.findDefult();

		Sku sku = skuService.findBySn(skuSn);
		/*if (sku != null && !currentStore.equals(sku.getStore())) {
			throw new UnauthorizedException();
		}*/
		model.addAttribute("sku", sku);
	}

	/**
	 * SKU选择
	 */
	@GetMapping("/sku_select")
	public @ResponseBody
	List<Map<String, Object>> skuSelect(String keyword, @CurrentUser Business currentUser) {

		List<Map<String, Object>> data = new ArrayList<>();
		if (StringUtils.isEmpty(keyword)) {
			return data;
		}
		List<Sku> skus = skuService.search(storeService.findDefult(), null, keyword, null, null);
		for (Sku sku : skus) {
			Map<String, Object> item = new HashMap<>();
			item.put("sn", sku.getSn());
			item.put("name", sku.getName());
			item.put("stock", sku.getStock());
			item.put("allocatedStock", sku.getAllocatedStock());
			item.put("specifications", sku.getSpecifications());
			data.add(item);
		}
		return data;
	}

	/**
	 * 入库
	 */
	@GetMapping("/stock_in")
	public String stockIn(Sku sku, ModelMap model) {
		model.addAttribute("sku", sku);
		return "business/stock/stock_in";
	}

	/**
	 * 入库
	 */
	@PostMapping("/stock_in")
	public String stockIn(Sku sku, Integer quantity, String memo, RedirectAttributes redirectAttributes) {
		if (sku == null) {
			return ERROR_VIEW;
		}
		if (quantity == null || quantity <= 0) {
			return ERROR_VIEW;
		}

		skuService.addStock(sku, quantity, StockLog.Type.stockIn, memo);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:log";
	}

	/**
	 * 出库
	 */
	@GetMapping("/stock_out")
	public String stockOut(Sku sku, ModelMap model) {
		model.addAttribute("sku", sku);
		return "business/stock/stock_out";
	}

	/**
	 * 出库
	 */
	@PostMapping("/stock_out")
	public String stockOut(Sku sku, Integer quantity, String memo, RedirectAttributes redirectAttributes) {
		if (sku == null) {
			return ERROR_VIEW;
		}
		if (quantity == null || quantity <= 0) {
			return ERROR_VIEW;
		}
		if (sku.getStock() - quantity < 0) {
			return ERROR_VIEW;
		}

		skuService.addStock(sku, -quantity, StockLog.Type.stockOut, memo);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:log";
	}



}
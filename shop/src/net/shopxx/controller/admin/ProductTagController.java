/*
 * Copyright 2005-2017 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.controller.admin;

import javax.inject.Inject;

import net.shopxx.entity.Store;
import net.shopxx.entity.StoreProductTag;
import net.shopxx.exception.UnauthorizedException;
import net.shopxx.security.CurrentStore;
import net.shopxx.service.StoreProductTagService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.shopxx.Message;
import net.shopxx.Pageable;
import net.shopxx.entity.BaseEntity;
import net.shopxx.entity.ProductTag;
import net.shopxx.service.ProductTagService;

/**
 * Controller - 商品标签
 * 
 * @author SHOP++ Team
 * @version 5.0
 */
@Controller("adminProductTagController")
@RequestMapping("/admin/product_tag")
public class ProductTagController extends BaseController {

	@Inject
	private ProductTagService productTagService;
	@Inject
	private StoreProductTagService storeProductTagService;

	/**
	 * 添加属性
	 */
	@ModelAttribute
	public void populateModel(Long storeProductTagId, @CurrentStore Store currentStore, ModelMap model) {
		StoreProductTag storeProductTag = storeProductTagService.find(storeProductTagId);
		if (storeProductTag != null && !currentStore.equals(storeProductTag.getStore())) {
			throw new UnauthorizedException();
		}
		model.addAttribute("storeProductTag", storeProductTag);
	}

	/**
	 * 添加
	 */
	@GetMapping("/add")
	public String add(ModelMap model) {
		return "admin/product_tag/add";
	}

	/**
	 * 保存
	 */
	@PostMapping("/save")
	public String save(ProductTag productTag, RedirectAttributes redirectAttributes) {
		if (!isValid(productTag, BaseEntity.Save.class)) {
			return ERROR_VIEW;
		}
		productTag.setProducts(null);
		productTagService.save(productTag);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list";
	}

	/**
	 * 编辑
	 */
	@GetMapping("/edit")
	public String edit(Long id, ModelMap model) {
		model.addAttribute("productTag", productTagService.find(id));
		return "admin/product_tag/edit";
	}

	/**
	 * 更新
	 */
	@PostMapping("/update")
	public String update(ProductTag productTag, RedirectAttributes redirectAttributes) {
		if (!isValid(productTag)) {
			return ERROR_VIEW;
		}
		productTagService.update(productTag, "products");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list";
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", productTagService.findPage(pageable));
		return "admin/product_tag/list";
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	public @ResponseBody Message delete(Long[] ids) {
		productTagService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}
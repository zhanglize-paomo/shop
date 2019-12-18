/*
 * Copyright 2005-2017 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.shopxx.controller.admin;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import net.shopxx.FileType;
import net.shopxx.Results;
import net.shopxx.entity.*;
import net.shopxx.security.CurrentStore;
import net.shopxx.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import net.shopxx.Message;
import net.shopxx.Pageable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

/**
 * Controller - 商品
 * 
 * @author SHOP++ Team
 * @version 5.0
 */
@Controller("adminProductController")
@RequestMapping("/admin/product")
public class ProductController extends BaseController {

	@Inject
	private ProductService productService;
	@Inject
	private ProductCategoryService productCategoryService;
	@Inject
	private BrandService brandService;
	@Inject
	private ProductTagService productTagService;
	@Inject
	private SkuService skuService;
	@Inject
	private StoreService storeService;
	@Inject
	private StoreProductCategoryService storeProductCategoryService;
	@Inject
	private PromotionService promotionService;
	@Inject
	private StoreProductTagService storeProductTagService;
	@Inject
	private ProductImageService productImageService;
	@Inject
	private ParameterValueService parameterValueService;
	@Inject
	private SpecificationItemService specificationItemService;
	@Inject
	private AttributeService attributeService;
	@Inject
	private SpecificationService specificationService;
	@Inject
	private FileService fileService;

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public String list(Product.Type type, Long productCategoryId, Long brandId, Long productTagId, Boolean isActive, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock, Boolean isStockAlert, Pageable pageable, ModelMap model) {
		ProductCategory productCategory = productCategoryService.find(productCategoryId);
		Brand brand = brandService.find(brandId);
		ProductTag productTag = productTagService.find(productTagId);

		model.addAttribute("types", Product.Type.values());
		model.addAttribute("productCategoryTree", productCategoryService.findTree());
		model.addAttribute("brands", brandService.findAll());
		model.addAttribute("productTags", productTagService.findAll());
		model.addAttribute("type", type);
		model.addAttribute("productCategoryId", productCategoryId);
		model.addAttribute("brandId", brandId);
		model.addAttribute("productTagId", productTagId);
		model.addAttribute("isMarketable", isMarketable);
		model.addAttribute("isList", isList);
		model.addAttribute("isTop", isTop);
		model.addAttribute("isActive", isActive);
		model.addAttribute("isOutOfStock", isOutOfStock);
		model.addAttribute("isStockAlert", isStockAlert);
		model.addAttribute("page", productService.findPage(type, null, productCategory, null, brand, null, productTag, null, null, null, null, isMarketable, isList, isTop, isActive, isOutOfStock, isStockAlert, null, null, pageable));
		return "admin/product/list";
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	public @ResponseBody Message delete(Long[] ids) {
		productService.delete(ids);
		return SUCCESS_MESSAGE;
	}

	/**
	 * 上架商品
	 */
	@PostMapping("/shelves")
	public @ResponseBody Message shelves(Long[] ids) {
		if (ids != null) {
			for (Long id : ids) {
				Product product = productService.find(id);
				if (product == null) {
					return ERROR_MESSAGE;
				}
				if (product.getStore().hasExpired() || !product.getStore().getIsEnabled() || !product.getStore().getProductCategories().contains(product.getProductCategory())) {
					return Message.error("admin.product.isShelvesSku", product.getName());
				}
			}
			productService.shelves(ids);
		}
		return SUCCESS_MESSAGE;
	}

	/**
	 * 下架商品
	 */
	@PostMapping("/shelf")
	public @ResponseBody Message shelf(Long[] ids) {
		productService.shelf(ids);
		return SUCCESS_MESSAGE;
	}

	/**
	 * 添加
	 */
	@GetMapping("/add")
	public String add(ModelMap model,RedirectAttributes redirectAttributes)
	{
		Store currentStore = storeService.findDefult();
		Long productCount = productService.count(null, currentStore, null, null, null, null, null, null);
		/*if (currentStore.getStoreRank() != null && currentStore.getStoreRank().getQuantity() != null && productCount >= currentStore.getStoreRank().getQuantity()) {
			addFlashMessage(redirectAttributes, "business.product.addCountNotAllowed", currentStore.getStoreRank().getQuantity());
			return "redirect:list";
		}*/

		model.addAttribute("types", Product.Type.values());
		model.addAttribute("productCategoryTree", productCategoryService.findTree());
		model.addAttribute("allowedProductCategories", productCategoryService.findAdminList());
		model.addAttribute("allowedProductCategoryParents", getAllowedProductCategoryParents());
		model.addAttribute("storeProductCategoryTree", storeProductCategoryService.findTree(currentStore));
		model.addAttribute("brands", brandService.findAll());
		model.addAttribute("promotions", promotionService.findList(currentStore, null, true));
		model.addAttribute("productTags", productTagService.findAll());
		model.addAttribute("storeProductTags", storeProductTagService.findList(currentStore, null));
		model.addAttribute("specifications", specificationService.findAll());
		return "admin/product/add";
	}
	/**
	 * 获取规格
	 */
	@GetMapping("/specifications")
	public @ResponseBody List<Map<String, Object>> specifications(@ModelAttribute(binding = false) ProductCategory productCategory) {
		List<Map<String, Object>> data = new ArrayList<>();
		if (productCategory == null || CollectionUtils.isEmpty(productCategory.getSpecifications())) {
			return data;
		}
		for (Specification specification : productCategory.getSpecifications()) {
			Map<String, Object> item = new HashMap<>();
			item.put("name", specification.getName());
			item.put("options", specification.getOptions());
			data.add(item);
		}
		return data;
	}

	/**
	 * 检查编号是否存在
	 */
	@GetMapping("/check_sn")
	public @ResponseBody boolean checkSn(String sn) {
		return StringUtils.isNotEmpty(sn) && !productService.snExists(sn);
	}

	/**
	 * 上传商品图片
	 */
	@PostMapping("/upload_product_image")
	public ResponseEntity<?> uploadProductImage(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			return Results.UNPROCESSABLE_ENTITY;
		}
		if (!fileService.isValid(FileType.image, file)) {
			return Results.unprocessableEntity("business.upload.invalid");
		}
		ProductImage productImage = productImageService.generate(file);
		if (productImage == null) {
			return Results.unprocessableEntity("business.upload.error");
		}

		return ResponseEntity.ok(productImage);
	}
	/**
	 * 删除商品图片
	 */
	@PostMapping("/delete_product_image")
	public ResponseEntity<?> deleteProductImage() {
		return Results.OK;
	}

	/**
	 * 获取属性
	 */
	@GetMapping("/attributes")
	public @ResponseBody List<Map<String, Object>> attributes(@ModelAttribute(binding = false) ProductCategory productCategory) {
		List<Map<String, Object>> data = new ArrayList<>();
		if (productCategory == null || CollectionUtils.isEmpty(productCategory.getAttributes())) {
			return data;
		}
		for (Attribute attribute : productCategory.getAttributes()) {
			Map<String, Object> item = new HashMap<>();
			item.put("id", attribute.getId());
			item.put("name", attribute.getName());
			item.put("options", attribute.getOptions());
			data.add(item);
		}
		return data;
	}
	/**
	 * 获取参数
	 */
	@GetMapping("/parameters")
	public @ResponseBody List<Map<String, Object>> parameters(@ModelAttribute(binding = false) ProductCategory productCategory) {
		List<Map<String, Object>> data = new ArrayList<>();
		if (productCategory == null || CollectionUtils.isEmpty(productCategory.getParameters())) {
			return data;
		}
		for (Parameter parameter : productCategory.getParameters()) {
			Map<String, Object> item = new HashMap<>();
			item.put("group", parameter.getGroup());
			item.put("names", parameter.getNames());
			data.add(item);
		}
		return data;
	}

	/**
	 * 保存
	 */
	@PostMapping("/save")
	public String save(@ModelAttribute(name = "productForm") Product productForm,  Long productCategoryId, SkuForm skuForm, SkuListForm skuListForm, Long brandId, Long[] promotionIds, Long[] productTagIds, Long[] storeProductTagIds,
					   Long storeProductCategoryId, HttpServletRequest request, @CurrentStore Store currentStore, RedirectAttributes redirectAttributes) {
		ProductCategory productCategory = productCategoryService.find(productCategoryId);

		productImageService.filter(productForm.getProductImages());
		parameterValueService.filter(productForm.getParameterValues());
		specificationItemService.filter(productForm.getSpecificationItems());
		skuService.filter(skuListForm.getSkuList());

		 currentStore = storeService.findDefult();
		Long productCount = productService.count(null, currentStore, null, null, null, null, null, null);
		if (currentStore.getStoreRank() != null && currentStore.getStoreRank().getQuantity() != null && productCount >= currentStore.getStoreRank().getQuantity()) {
			return ERROR_VIEW;
		}
		if (productCategory == null) {
			return ERROR_VIEW;
		}
		if (storeProductCategoryId != null) {
			StoreProductCategory storeProductCategory = storeProductCategoryService.find(storeProductCategoryId);
			if (storeProductCategory == null || !currentStore.equals(storeProductCategory.getStore())) {
				return ERROR_VIEW;
			}
			productForm.setStoreProductCategory(storeProductCategory);
		}
		productForm.setStore(currentStore);
		productForm.setProductCategory(productCategory);
		productForm.setBrand(brandService.find(brandId));
		productForm.setPromotions(new HashSet<>(promotionService.findList(promotionIds)));
		productForm.setProductTags(new HashSet<>(productTagService.findList(productTagIds)));
		productForm.setStoreProductTags(new HashSet<>(storeProductTagService.findList(storeProductTagIds)));

		productForm.removeAttributeValue();
		for (Attribute attribute : productForm.getProductCategory().getAttributes()) {
			String value = request.getParameter("attribute_" + attribute.getId());
			String attributeValue = attributeService.toAttributeValue(attribute, value);
			productForm.setAttributeValue(attribute, attributeValue);
		}

		if (!isValid(productForm, BaseEntity.Save.class)) {
			return ERROR_VIEW;
		}
		if (StringUtils.isNotEmpty(productForm.getSn()) && productService.snExists(productForm.getSn())) {
			return ERROR_VIEW;
		}
		if (productForm.hasSpecification()) {
			List<Sku> skus = skuListForm.getSkuList();
			if (CollectionUtils.isEmpty(skus) || !isValid(skus, getValidationGroup(productForm.getType()), BaseEntity.Save.class)) {
				return ERROR_VIEW;
			}
			productService.create(productForm, skus);
		} else {
			Sku sku = skuForm.getSku();
			if (sku == null || !isValid(sku, getValidationGroup(productForm.getType()), BaseEntity.Save.class)) {
				return ERROR_VIEW;
			}
			productService.create(productForm, sku);
		}

		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list";

	}


	/**
	 * 根据类型获取验证组
	 *
	 * @param type
	 *            类型
	 * @return 验证组
	 */
	private Class<?> getValidationGroup(Product.Type type) {
		Assert.notNull(type);

		switch (type) {
			case general:
				return Sku.General.class;
			case exchange:
				return Sku.Exchange.class;
			case gift:
				return Sku.Gift.class;
		}
		return null;
	}
	/**
	 * 获取允许发布商品分类上级分类
	 *
	 * @param store
	 *            店铺
	 * @return 允许发布商品分类上级分类
	 */
	private Set<ProductCategory> getAllowedProductCategoryParents() {

		Set<ProductCategory> result = new HashSet<>();
		List<ProductCategory> allowedProductCategories = productCategoryService.findAdminList();
		for (ProductCategory allowedProductCategory : allowedProductCategories) {
			result.addAll(allowedProductCategory.getParents());
		}
		return result;
	}





	/**
	 * 编辑
	 */
	@GetMapping("/edit")
	public String edit(Long id, ModelMap model) {
		Store currentStore = storeService.findDefult();

		model.addAttribute("types", Product.Type.values());
		model.addAttribute("productCategoryTree", productCategoryService.findTree());
		model.addAttribute("allowedProductCategories", productCategoryService.findAdminList());
		model.addAttribute("allowedProductCategoryParents", getAllowedProductCategoryParents());
		model.addAttribute("storeProductCategoryTree", storeProductCategoryService.findTree(currentStore));
		model.addAttribute("brands", brandService.findAll());
		model.addAttribute("promotions", promotionService.findList(currentStore, null, true));
		model.addAttribute("productTags", productTagService.findAll());
		model.addAttribute("storeProductTags", storeProductTagService.findList(currentStore, null));
		model.addAttribute("specifications", specificationService.findAll());
		model.addAttribute("product", productService.find(id));
		return "admin/product/edit";
	}

	/**
	 * 更新
	 */
	@PostMapping("/update")
	public String update(@ModelAttribute("productForm") Product productForm, @ModelAttribute(binding = false) Product product, Long productId , Long  productCategoryId, net.shopxx.controller.admin.ProductController.SkuForm skuForm, net.shopxx.controller.admin.ProductController.SkuListForm skuListForm, Long brandId, Long[] promotionIds, Long[] productTagIds,
						 Long[] storeProductTagIds, Long storeProductCategoryId, HttpServletRequest request,  RedirectAttributes redirectAttributes) {

		productImageService.filter(productForm.getProductImages());
		parameterValueService.filter(productForm.getParameterValues());
		specificationItemService.filter(productForm.getSpecificationItems());

		//默认店铺
		Store currentStore = storeService.findDefult();

		ProductCategory productCategory = productCategoryService.find(productCategoryId);

		BeanUtils.copyProperties(productForm,product);


		product.setId(productId);

		skuService.filter(skuListForm.getSkuList());
		if (product == null) {
			return ERROR_VIEW;
		}
		if (productCategory == null) {
			return ERROR_VIEW;
		}
		List<Promotion> promotions = promotionService.findList(promotionIds);
		if (CollectionUtils.isNotEmpty(promotions)) {
			if (currentStore.getPromotions() == null || !currentStore.getPromotions().containsAll(promotions)) {
				return ERROR_VIEW;
			}
		}
		if (storeProductCategoryId != null) {
			StoreProductCategory storeProductCategory = storeProductCategoryService.find(storeProductCategoryId);
			if (storeProductCategory == null || !currentStore.equals(storeProductCategory.getStore())) {
				return ERROR_VIEW;
			}
			productForm.setStoreProductCategory(storeProductCategory);
		}
		productForm.setId(product.getId());
		productForm.setType(product.getType());
		productForm.setIsActive(true);
		productForm.setProductCategory(productCategory);
		productForm.setBrand(brandService.find(brandId));
		productForm.setPromotions(new HashSet<>(promotions));
		productForm.setProductTags(new HashSet<>(productTagService.findList(productTagIds)));
		productForm.setStoreProductTags(new HashSet<>(storeProductTagService.findList(storeProductTagIds)));

		productForm.removeAttributeValue();
		for (Attribute attribute : productForm.getProductCategory().getAttributes()) {
			String value = request.getParameter("attribute_" + attribute.getId());
			String attributeValue = attributeService.toAttributeValue(attribute, value);
			productForm.setAttributeValue(attribute, attributeValue);
		}

	/*	if (!isValid(productForm, BaseEntity.Update.class)) {
			return ERROR_VIEW;
		}*/

		if (productForm.hasSpecification()) {
			List<Sku> skus = skuListForm.getSkuList();
			if (CollectionUtils.isEmpty(skus) || !isValid(skus, getValidationGroup(productForm.getType()), BaseEntity.Update.class)) {
				return ERROR_VIEW;
			}
			productService.modify(productForm, skus);
		} else {
			Sku sku = skuForm.getSku();
			if (sku == null || !isValid(sku, getValidationGroup(productForm.getType()), BaseEntity.Update.class)) {
				return ERROR_VIEW;
			}
			productService.modify(productForm, sku);
		}
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list";
	}




	/**
	 * FormBean - SKU
	 *
	 * @author SHOP++ Team
	 * @version 5.0
	 */
	public static class SkuForm {

		/**
		 * SKU
		 */
		private Sku sku;

		/**
		 * 获取SKU
		 *
		 * @return SKU
		 */
		public Sku getSku() {
			return sku;
		}

		/**
		 * 设置SKU
		 *
		 * @param sku
		 *            SKU
		 */
		public void setSku(Sku sku) {
			this.sku = sku;
		}

	}

	/**
	 * FormBean - SKU
	 *
	 * @author SHOP++ Team
	 * @version 5.0
	 */
	public static class SkuListForm {

		/**
		 * SKU
		 */
		private List<Sku> skuList;

		/**
		 * 获取SKU
		 *
		 * @return SKU
		 */
		public List<Sku> getSkuList() {
			return skuList;
		}

		/**
		 * 设置SKU
		 *
		 * @param skuList
		 *            SKU
		 */
		public void setSkuList(List<Sku> skuList) {
			this.skuList = skuList;
		}

	}

}
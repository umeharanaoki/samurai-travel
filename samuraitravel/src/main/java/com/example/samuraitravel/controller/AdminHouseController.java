package com.example.samuraitravel.controller;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.form.HouseEditForm;
import com.example.samuraitravel.form.HouseRegisterForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.service.HouseService;

@Controller
@RequestMapping("/admin/houses")
public class AdminHouseController {
	private final HouseRepository houseRepository;
	private final HouseService houseService;
	
	public AdminHouseController(HouseRepository houseRepository, HouseService houseService) {
		this.houseRepository = houseRepository;
		this.houseService = houseService;
		
	}
	
	@GetMapping
	public String index(Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable, @RequestParam(name = "keyword", required = false) String keyword) {
		Page<House> housePage;
		
		if(keyword != null && !keyword.isEmpty()) {
			housePage = houseRepository.findByNameLike("%" + keyword + "%", pageable);
		} else {
			housePage = houseRepository.findAll(pageable);
		}
		
		model.addAttribute("housePage", housePage);
		model.addAttribute("keyword", keyword);
		
		return "admin/houses/index";
	}
	
	@GetMapping("/{id}")
	public String show(@PathVariable(name = "id") Integer id, Model model) {
		House house = houseRepository.getReferenceById(id);
		
		model.addAttribute("house", house);
		
		return "admin/houses/show";
	}
	
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("houseRegisterForm", new HouseRegisterForm());
		return "admin/houses/register";
	}
	
	@PostMapping("/create")
	// ModelAttributeでフォームから送られてきたデータの割り当て、Validatedでデータの形式等に問題がないか確認
	public String create(@ModelAttribute @Validated HouseRegisterForm houseRegisterForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if(bindingResult.hasErrors()) {
			// hasErrorsメソッドでエラーを確認し、ある場合民宿登録ページを表示
			return "admin/houses/register";
		}
		// エラーがない場合、createメソッドで民宿を登録
		houseService.create(houseRegisterForm);
		// リダイレクト先にデータを渡す
		redirectAttributes.addFlashAttribute("successMessage", "民宿を登録しました。"); // 第1引数：リダイレクト先から参照する変数名　第2引数：リダイレクト先に渡すデータ
		// 民宿の登録に成功した場合民宿一覧ページにリダイレクトする
		return "redirect:/admin/houses";
	}
	
	@GetMapping("/{id}/edit")
	public String edit(@PathVariable(name = "id") Integer id, Model model) {
		House house = houseRepository.getReferenceById(id);
		String imageName = house.getImageName();
		HouseEditForm houseEditForm = new HouseEditForm(house.getId(), house.getName(), null, house.getDescription(), house.getPrice(), house.getCapacity(), house.getPostalCode(), house.getAddress(), house.getPhoneNumber());
		
		model.addAttribute("imageName", imageName);
		model.addAttribute("houseEditForm", houseEditForm);
		return "admin/houses/Edit";
	}
	
	@PostMapping("/{id}/update")
	public String update(@ModelAttribute @Validated HouseEditForm houseEditForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if(bindingResult.hasErrors()) {
			return "admin/houses/edit";
		}
		
		houseService.update(houseEditForm);
		redirectAttributes.addFlashAttribute("successMessage", "民宿情報を編集しました。");
		
		return "redirect:/admin/houses";
	}
	
	@PostMapping("/{id}/delete")
	public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
		houseRepository.deleteById(id);
		
		redirectAttributes.addFlashAttribute("successMessage", "民宿を削除しました。");
		
		return "redirect:/admin/houses";
	}
}

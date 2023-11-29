package com.example.samuraitravel.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReviewEditForm;
import com.example.samuraitravel.form.ReviewPostForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.ReviewRepository;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.ReviewService;

@RequestMapping("/houses")
@Controller
public class ReviewController {
	private final HouseRepository houseRepository;
	private final ReviewRepository reviewRepository;
	private final ReviewService reviewService;
	
	public ReviewController(HouseRepository houseRepository, ReviewRepository reviewRepository, ReviewService reviewService) {
		this.houseRepository = houseRepository;
		this.reviewRepository = reviewRepository;
		this.reviewService = reviewService;
	}
	
	// レビュー一覧用のメソッド
	@GetMapping("/{id}/reviews")
	public String index(@PathVariable(name = "id") Integer houseId,
						@PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Direction.DESC) Pageable pageable,
						Model model) 
	{
		House house = houseRepository.getReferenceById(houseId);
		// houseIdをもとにレビューを表示
		Page<Review> reviewPage = reviewRepository.findByHouseId(houseId, pageable);
		
		model.addAttribute("house", house);
		model.addAttribute("reviewPage",reviewPage);
		
		return "reviews/index";
	}
	
	// フォームクラスのインスタンスをビューに渡す
	@GetMapping("/{id}/reviews/post")
	public String post(@PathVariable(name = "id") Integer houseId, Model model) {
		// idから民宿情報をゲット
		House house = houseRepository.getReferenceById(houseId);
		model.addAttribute("house", house);
		model.addAttribute("reviewPostForm", new ReviewPostForm());
		return "reviews/post";
	}
	
	// フォームの送信先を担当
	@PostMapping("/{id}/reviews/create")
	public String create(@ModelAttribute @Validated ReviewPostForm reviewPostForm,
						 @PathVariable(name = "id") Integer houseId,
						 BindingResult bindingResult, 
						 RedirectAttributes redirectAttributes,
						 @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
						 Model model) {
		
		House house = houseRepository.getReferenceById(houseId);
		User user = userDetailsImpl.getUser();
		
		// エラーがあるときフォーム画面を返す
		if(bindingResult.hasErrors()) {
			return "reviews/post";
		}
		
		reviewService.create(reviewPostForm, house, user);
		redirectAttributes.addFlashAttribute("successMessage", "レビューを投稿しました。");

		return "redirect:/houses/{id}";
	}
	
	
	// レビュー編集用
	@GetMapping("/{id}/reviews/{reviewId}/edit")
	public String edit(@PathVariable(name = "id") Integer houseId,
					   @PathVariable(name = "reviewId") Integer reviewId,
					   @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
					   Model model) 
	{
		Review review = reviewRepository.getReferenceById(reviewId);
		House house = houseRepository.getReferenceById(houseId);
		User user = userDetailsImpl.getUser();
		
		ReviewEditForm reviewEditForm = new ReviewEditForm(reviewId, review.getEvaluation(), review.getReviewContents());
		
		model.addAttribute("house", house);
		model.addAttribute("user", user);
		model.addAttribute("reviewEditForm", reviewEditForm);
		return "reviews/edit";
	}
	
	// フォームの送信先を担当
	@PostMapping("/{id}/reviews/{reviewId}/update")
	public String update(@ModelAttribute @Validated ReviewEditForm reviewEditForm,
						 @PathVariable(name = "id") Integer houseId,
						 @PathVariable(name = "reviewId") Integer reviewId,
						 BindingResult bindingResult, 
						 RedirectAttributes redirectAttributes,
						 @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
						 Model model) 
	{
		House house = houseRepository.getReferenceById(houseId);
		User user = userDetailsImpl.getUser();
		
		// エラーがあるときフォーム画面を返す
		if(bindingResult.hasErrors()) {
			return "reviews/edit";
		}
		
		reviewService.update(reviewEditForm, house, user);
		redirectAttributes.addFlashAttribute("successMessage", "レビューを更新しました。");
		return "redirect:/houses/{id}";
	}
	
	@PostMapping("/{id}/reviews/{reviewId}/delete")
	public String delete(@PathVariable(name = "id") Integer houseId, RedirectAttributes redirectAttributes) {
		reviewRepository.deleteById(houseId);
		
		redirectAttributes.addFlashAttribute("successMessage", "レビューを削除しました。");
		return "redirect:/houses/{id}";
	}
}

package com.example.samuraitravel.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReviewEditForm;
import com.example.samuraitravel.form.ReviewPostForm;
import com.example.samuraitravel.repository.ReviewRepository;

@Service
public class ReviewService {
	private final ReviewRepository reviewRepository;
	
	public ReviewService(ReviewRepository reviewRepository) {
		this.reviewRepository = reviewRepository;
		
	}
	
	@Transactional
	public void create(ReviewPostForm reviewPostForm, House house, User user) {
		// 新規登録するレビュー用のエンティティをインスタンス化
		Review review = new Review();
		// 準備したエンティティに登録する値をセット
		review.setHouse(house);
		review.setUser(user);
		review.setEvaluation(reviewPostForm.getEvaluation());
		review.setReviewContents(reviewPostForm.getReviewContents());
		
		reviewRepository.save(review);
	}
	
	@Transactional
	public void update(ReviewEditForm reviewEditForm, House house, User user) {
		// idで更新前のレビュー情報を取得
		Review review = reviewRepository.getReferenceById(reviewEditForm.getId());
		// 準備したエンティティに登録する値をセット
		review.setHouse(house);
		review.setUser(user);
		review.setEvaluation(reviewEditForm.getEvaluation());
		review.setReviewContents(reviewEditForm.getReviewContents());
		
		reviewRepository.save(review);
	}
}

package com.example.samuraitravel.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.samuraitravel.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

	// 民宿のidで検索し、新着順に並べる（民宿詳細用）
	List<Review> findByHouseIdOrderByCreatedAtDesc(Integer id, PageRequest of);
	
	// 民宿のidで検索し、新着順に並べてページネーションを作る（レビュー一覧用）
	Page<Review> findByHouseId(Integer houseId, Pageable pageable);
}

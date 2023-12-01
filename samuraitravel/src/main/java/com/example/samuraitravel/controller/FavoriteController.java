package com.example.samuraitravel.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.repository.FavoriteRepository;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.FavoriteService;

// ページ遷移したくないときは@RestControllerを指定
@RestController
public class FavoriteController {
	private final FavoriteService favoriteService;
	private final FavoriteRepository favoriteRepository;
	private final HouseRepository houseRepository;
	
	FavoriteController(FavoriteService favoriteService, FavoriteRepository favoriteRepository, HouseRepository houseRepository) {
		this.favoriteService = favoriteService;
		this.favoriteRepository = favoriteRepository;
		this.houseRepository = houseRepository;
	}
	
	// Ajaxからのリクエストを受け取る
	@PostMapping("/houses/show/{id}/favorites/toggle")
	public Integer toggleFavorite(@PathVariable(name = "id") Integer houseId, 
							   @AuthenticationPrincipal UserDetailsImpl userDetailsImpl)
	{
		User user = userDetailsImpl.getUser();
		House house = houseRepository.getReferenceById(houseId);
		// Serviceのメソッドを使用してデータベースの操作
		favoriteService.toggleFavorite(user, house);
		
		Favorite favorite = favoriteRepository.findByUserAndHouse(user, house);
		// ビューではなくデータを返す
		return favorite.getStatus();
	}
}

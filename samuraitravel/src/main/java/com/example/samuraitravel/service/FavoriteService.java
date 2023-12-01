package com.example.samuraitravel.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.repository.FavoriteRepository;

@Service
public class FavoriteService {
	private final FavoriteRepository favoriteRepository;
		
	public FavoriteService(FavoriteRepository favoriteRepository) {
		this.favoriteRepository = favoriteRepository;
	}
	
	@Transactional
	public void toggleFavorite(User user, House house) {
		Favorite favorite = favoriteRepository.findByUserAndHouse(user, house);
		
		if(favorite != null) {
			// すでに値がある場合、statusを切り替える
			favorite.setStatus(1 - favorite.getStatus());
		} else {
			// まだ値がない（そのユーザーがその民宿のお気に入りボタンを初めて押す）とき
			favorite = new Favorite();
			favorite.setUser(user);
			favorite.setHouse(house);
			favorite.setStatus(1);
		}
		favoriteRepository.save(favorite);
	}
}

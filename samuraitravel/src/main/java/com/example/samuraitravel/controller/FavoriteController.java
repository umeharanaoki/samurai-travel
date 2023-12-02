package com.example.samuraitravel.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.repository.FavoriteRepository;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.security.UserDetailsImpl;

@Controller
public class FavoriteController {
	public final FavoriteRepository favoriteRepository;
	public final HouseRepository houseRepository;
	
	FavoriteController(FavoriteRepository favoriteRepository, HouseRepository houseRepository) {
		this.favoriteRepository = favoriteRepository;
		this.houseRepository = houseRepository;
	}
	
	@GetMapping("/favorites")
	public String index(@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, @AuthenticationPrincipal UserDetailsImpl userDetailsImpl ,Model model) {
		User user = userDetailsImpl.getUser();
		Page<Favorite> favoritePage = favoriteRepository.findByUserAndStatus(user, 1, pageable);
				
//		List<House> favoriteHouses = favorites.stream()
//            .map(favorite -> houseRepository.getReferenceById(favorite.getHouse().getId()))
//            .collect(Collectors.toList());
		model.addAttribute("favoritePage", favoritePage);
//		model.addAttribute("favoriteHouses", favoriteHouses);
		return "favorites/index";
	}
}

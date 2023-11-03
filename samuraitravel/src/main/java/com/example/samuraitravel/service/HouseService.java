package com.example.samuraitravel.service;

import java.io.IOException;
import java.nio.file.Files;
// java.ioパッケージはデータの入出力関連
// java.nioパッケージはデータのコンテナ（バッファ）関連
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.form.HouseEditForm;
import com.example.samuraitravel.form.HouseRegisterForm;
import com.example.samuraitravel.repository.HouseRepository;

// Serviceはビジネスロジックを担当
@Service
public class HouseService {
	
	private final HouseRepository houseRepository;
	
	public HouseService(HouseRepository houseRepository) {
		this.houseRepository = houseRepository;
	}
	
	@Transactional
	public void create(HouseRegisterForm houseRegisterForm) {
		House house = new House();
		// 一時的に画像ファイルを保存
		MultipartFile imageFile = houseRegisterForm.getImageFile();
		// 画像ファイルがある場合（画像アップロードされたとき）
		// 画像の名前やパスを定義して登録された画像を格納＆表示できるようにする
		if(!imageFile.isEmpty()) {
			String imageName = imageFile.getOriginalFilename();
			// メソッドは下で定義(hashedはある一定のルールで変換されたの意）
			String hashedImageName = generateNewFileName(imageName);
			Path filePath = Paths.get("src/main/resouces/static/storage/" + hashedImageName);
			copyImageFile(imageFile, filePath);
			house.setImageName(hashedImageName);
		}
		
		// フォームに入力された内容をセット
		// setterなくない？→Controllerに
		house.setName(houseRegisterForm.getName());
		house.setDescription(houseRegisterForm.getDescription());
		house.setPrice(houseRegisterForm.getPrice());
		house.setCapacity(houseRegisterForm.getCapacity());
		house.setPostalCode(houseRegisterForm.getPostalCode());
		house.setAddress(houseRegisterForm.getAddress());
		house.setPhoneNumber(houseRegisterForm.getPhoneNumber());
		// エンティティをデータベースに保存
		houseRepository.save(house);
	}
	
	// 民宿の更新機能
	@Transactional
	public void update(HouseEditForm houseEditForm) {
		House house = houseRepository.getReferenceById(houseEditForm.getId());
		MultipartFile imageFile = houseEditForm.getImageFile();
		
		if(!imageFile.isEmpty()) {
			String imageName = imageFile.getOriginalFilename();
			String hashedImageName = generateNewFileName(imageName);
			Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);
			copyImageFile(imageFile, filePath);
			house.setImageName(hashedImageName);
		}
		
		house.setName(houseEditForm.getName());
		house.setDescription(houseEditForm.getDescription());
		house.setPrice(houseEditForm.getPrice());
		house.setCapacity(houseEditForm.getCapacity());
		house.setPostalCode(houseEditForm.getPostalCode());
		house.setAddress(houseEditForm.getAddress());
		house.setPhoneNumber(houseEditForm.getPhoneNumber());
		
		houseRepository.save(house);
	}
	
	// UUIDを使って生成したファイル名を返す
	public String generateNewFileName(String fileName) {
		String[] fileNames = fileName.split("\\.");
		for(int i = 0; i < fileNames.length - 1; i++) {
			fileNames[i] = UUID.randomUUID().toString();
		}
		String hashedFileName = String.join(".", fileNames);
		return hashedFileName;	
	}
	
	// 画像ファイルを指定したファイルにコピーする
	public void copyImageFile(MultipartFile imageFile, Path filePath) {
		try {
			Files.copy(imageFile.getInputStream(), filePath);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	

}

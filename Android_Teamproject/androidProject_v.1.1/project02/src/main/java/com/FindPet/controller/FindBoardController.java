package com.FindPet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.FindPet.model.FindBoard;
import com.FindPet.model.ImgFile;
import com.FindPet.model.Member;
import com.FindPet.repository.ImgRepository;
import com.FindPet.service.FindBoardService;
import com.FindPet.service.ImgService;
import com.FindPet.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/findBoard/*")
public class FindBoardController {

	@Value("${imgLocation}")
	private String imgLocation;
	private final ImgService imgService;
	private final ImgRepository imgRepository;
	private final MemberService memberService;
	private final FindBoardService findBoardService;

	//입력
	@PostMapping("insert")
	public String insert(@RequestParam(value = "imgFile", required = false) List<MultipartFile> imgFileList,
			FindBoard findBoard, String username) {

		System.out.println("insert");
		System.out.println(username);
		
		findBoard.setMember(memberService.findmember(username.replaceAll("\"", "")));
		
		FindBoard findBoard2 = findBoardService.insert(findBoard);
		if (imgFileList != null) {
			for (int i = 0; i < imgFileList.size(); i++) {
				ImgFile img = new ImgFile();
				img.setFindboard(findBoard2);

				if (i == 0)
					img.setMainImg("Y");
				else
					img.setMainImg("N");

				try {
					imgService.saveImgFile(img, imgFileList.get(i));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return "success";
	}

	// 상세보기
	@GetMapping("view")
	public FindBoard view(@RequestParam("findId") Long findId) {
		System.out.println(findBoardService.view(findId).getImgFileList().size());
		return findBoardService.view(findId);
	}

	// 수정
	@PostMapping("update")
	public FindBoard update( 
			@RequestParam(value="imgFile", required = false) List<MultipartFile> imgFileList,
			FindBoard findBoard) {
			System.out.println("11111111");

            try {
				FindBoard findBoard0 = findBoardService.update(findBoard, imgFileList);
				System.out.println("update FindBoard : " + findBoard0.getFindId());
				return findBoard0;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
            
	}

	// 삭제
	@DeleteMapping("/delete/{findId}")
	public void deleteById(@PathVariable Long findId) {
		findBoardService.deleteById(findId);
	}

	// 전체보기 (페이지, 검색 x)
	@GetMapping("/list")
	public List<FindBoard> find_list() {
		return findBoardService.Find_list();
	}

	@GetMapping("findDog/{petcategory}")
	public List<FindBoard> findDog(@PathVariable String petcategory) {

		return findBoardService.dog_find_list(petcategory);

	}

	@GetMapping("findCat/{petcategory}")
	public List<FindBoard> findCat(@PathVariable String petcategory) {
		return findBoardService.cat_find_list(petcategory);
	}

	@GetMapping("findEtc/{petcategory}")
	public List<FindBoard> findEtc(@PathVariable String petcategory) {
		return findBoardService.Etc_find_list(petcategory);
	}

	@GetMapping("findAll/{word}")
	public List<FindBoard> findall(@PathVariable String word) {

		return findBoardService.all_find(word);
	}

}

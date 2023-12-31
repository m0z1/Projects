package com.FindPet.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FindBoard {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "find_id")
	private Long findId;
	private String breed;
	private String content;
	@CreationTimestamp
	private Date regdate;
	private String petname;
	private String petage;
	private String petgender;
	private String petcharacter;
	private String petcategory;
	private String findaddr;
	
	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;
	
	
	@OneToMany(mappedBy = "findboard") 
	private List<ImgFile> imgFileList;
	  
	@OneToMany(mappedBy = "findboard") 
	private List<Comment> comment;
	
	public void findBoardUpdate(FindBoard findBoard) {
		this.breed = findBoard.getBreed();
		this.content = findBoard.getContent();
		this.petname = findBoard.getPetname();
		this.petage = findBoard.getPetage();
		this.petgender = findBoard.getPetgender();
		this.petcharacter = findBoard.getPetcharacter();
		this.petcategory = findBoard.getPetcategory();
		this.findaddr = findBoard.getFindaddr();
	}
}

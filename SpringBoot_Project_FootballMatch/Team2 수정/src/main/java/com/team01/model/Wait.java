package com.team01.model;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter
@Entity
@Table(name = "project_wait")
public class Wait {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "wait_id")
	private Long waitId;
	
	
	private Long teamId;
	
	
	
	private Long matchNum;
}

package com.example.androidphone.config.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.androidphone.model.Member;
import com.example.androidphone.repository.MemberRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class PrincipalDetail implements UserDetailsService{

	private MemberRepository memberRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("loadUserByUsername");
		Member member = memberRepository.findByUsername(username);
		if(member == null) return null;
		
		PrincipalMember puser = new PrincipalMember(member);
		return puser;
	}

}

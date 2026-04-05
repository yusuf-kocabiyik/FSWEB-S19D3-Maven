package com.workintech.s19d2.service;

import com.workintech.s19d2.entity.Member;
import com.workintech.s19d2.entity.Role;

import com.workintech.s19d2.repository.MemberRepository;
import com.workintech.s19d2.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class AuthenticationService {

    private static final String ROLE_USER = "USER";
    private static final String ROLE_ADMIN = "ADMIN";
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public Member register(String email,String password){
        Optional<Member> memberOptional = memberRepository.findByEmail(email);
        if(memberOptional.isPresent()){
            throw new RuntimeException("User with given email already exist");
        }

        String encodedPassword = passwordEncoder.encode(password);

        List<Role> roleList = new ArrayList<>();

        //addRoleUser(roleList);
        addRoleAdmin(roleList);

        Member member = new Member();
        member.setEmail(email);
        member.setPassword(encodedPassword);
        member.setRoles(roleList);
        return memberRepository.save(member);

    }


    private void addRoleUser(List<Role> roleList){
        Optional<Role> roleUser = roleRepository.findByAuthority(ROLE_USER);
        if(!roleUser.isPresent()){
            Role roleUserEntity = new Role();
            roleUserEntity.setAuthority(ROLE_USER);
            roleList.add(roleRepository.save(roleUserEntity));
        }else {
            roleList.add(roleUser.get());
        }
    }

    private void addRoleAdmin(List<Role> roleList){
        Optional<Role> roleAdmin = roleRepository.findByAuthority(ROLE_ADMIN);
        if(!roleAdmin.isPresent()){
            Role roleUserEntity = new Role();
            roleUserEntity.setAuthority(ROLE_ADMIN);
            roleList.add(roleRepository.save(roleUserEntity));
        }else {
            roleList.add(roleAdmin.get());
        }
    }

}


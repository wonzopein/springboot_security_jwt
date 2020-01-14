package com.example.demo.member;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Collection;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@Builder
public class Member implements UserDetails {

    @Id
    private String username;
    private String name;
    private String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    // 만료여부
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    //  잠김여부
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    //  패스워드 만료여부
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    //  사용가능 여부
    @Override
    public boolean isEnabled() {
        return true;
    }
}

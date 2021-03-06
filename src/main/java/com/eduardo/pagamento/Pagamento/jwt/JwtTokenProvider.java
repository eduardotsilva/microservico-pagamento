package com.eduardo.pagamento.Pagamento.jwt;

import java.util.Base64;
import java.util.Collection;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Service
public class JwtTokenProvider {

	@Value("${security.jwt.token.secret-key}")
	private String secretKey;


	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}


	public Authentication getAuthentication(String token) {

		UserDetails userDetails = new UserDetails() {
			
			@Override
			public boolean isEnabled() {
				return true;
			}
			
			@Override
			public boolean isCredentialsNonExpired() {
				return true;
			}
			
			@Override
			public boolean isAccountNonLocked() {
				return true;
			}
			@Override
			public boolean isAccountNonExpired() {
				return true;
			}
			@Override
			public String getUsername() {
				return "";
			}
			@Override
			public String getPassword() {
				return "";
			}
			@Override
			public Collection<? extends GrantedAuthority> getAuthorities() {
				return null;
			}
		}; 
		
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

		
	}

	public String resolveToken(HttpServletRequest req) {
		String bearToken = req.getHeader("Authorization");

		if (bearToken != null && bearToken.startsWith("Bearer ")) {
			return bearToken.substring(7, bearToken.length());
		}

		return null;
	}

	public boolean validateToken(String token) {
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			if (claims.getBody().getExpiration().before(new Date())) {
				return false;
			}

			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

}

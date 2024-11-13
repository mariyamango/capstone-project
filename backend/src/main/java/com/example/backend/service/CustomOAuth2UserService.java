package com.example.backend.service;

import com.example.backend.model.AppUser;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) {
        OAuth2User oAuth2User = super.loadUser(request);
        System.out.println(oAuth2User.getAttributes());
        String registrationId = request.getClientRegistration().getRegistrationId();
        AppUser appUser;
        if ("google".equals(registrationId)) {
            appUser = processGoogleUser(oAuth2User);
        } else if ("github".equals(registrationId)) {
            appUser = processGithubUser(oAuth2User);
        } else {
            throw new IllegalArgumentException("Unsupported registration id " + registrationId);
        }
        String principalAttribute = "google".equals(registrationId) ? "sub" : "id";

        return new DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority(appUser.role())),
                oAuth2User.getAttributes(),
                principalAttribute
        );
    }

    private AppUser processGoogleUser(OAuth2User oAuth2User) {
        String googleId = oAuth2User.getAttribute("sub");
        return userRepository.findById(googleId)
                .orElseGet(() -> createAndSaveUser(googleId, oAuth2User.getAttribute("name"), oAuth2User.getAttribute("picture"), "USER"));
    }

    private AppUser processGithubUser(OAuth2User oAuth2User) {
        String githubId = oAuth2User.getName();
        return userRepository.findById(githubId)
                .orElseGet(() -> createAndSaveUser(githubId, oAuth2User.getAttribute("login"), oAuth2User.getAttribute("avatar_url"), "USER"));
    }

    private AppUser createAndSaveUser(String id, String username, String avatarUrl, String role) {
        AppUser newUser = AppUser.builder()
                .id(id)
                .username(username)
                .avatarUrl(avatarUrl)
                .role(role)
                .build();

        return userRepository.save(newUser);
    }
}
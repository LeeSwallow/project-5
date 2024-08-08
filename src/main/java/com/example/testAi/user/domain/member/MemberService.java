package com.example.testAi.user.domain.member;

import com.example.testAi.user.domain.member.entity.Member;
import com.example.testAi.user.domain.member.repository.MemberRepository;
import com.example.testAi.user.standard.dto.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public RsData<Member> join(String username, String password) {
        return join(username, password, username + "@test.com", username, "");
    }

    @Transactional
    public RsData<Member> join(String username, String password, String email, String nickname, String profileImgUrl) {
        if (findByUsername(username).isPresent()) {
            return RsData.of("400-2", "이미 존재하는 회원입니다.");
        }

        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .nickname(nickname)
                .profileImgUrl(profileImgUrl)
                .build();
        memberRepository.save(member);

        return RsData.of("회원가입이 완료되었습니다.".formatted(member.getUsername()), member);
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public long count() {
        return memberRepository.count();
    }

    @Transactional
    public RsData<Member> modifyOrJoin(String username, String providerTypeCode, String email, String nickname, String profileImgUrl) {
        Member member = findByUsername(username).orElse(null);

        if (member == null) {
            return join(
                    username, "", email, nickname, profileImgUrl
            );
        }

        return modify(member, nickname, profileImgUrl);
    }

    @Transactional
    public RsData<Member> modify(Member member, String nickname, String profileImgUrl) {
        member.setNickname(nickname);
        member.setProfileImgUrl(profileImgUrl);

        return RsData.of("회원정보가 수정되었습니다.".formatted(member.getUsername()), member);
    }



    public Optional<Member> findById(long id) {
        return memberRepository.findById(id);
    }
}
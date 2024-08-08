package com.example.testAi.User.global.initData;
import com.example.testAi.user.domain.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;


@Profile("!prod")
@Configuration
@RequiredArgsConstructor
public class NotProd {
    private final MemberService memberService;

    @Lazy
    @Autowired
    private NotProd self;

    @Bean
    public ApplicationRunner initNotProd() {
        return args -> {
            self.work1();
        };
    }

    @Transactional
    public void work1() {
        if (memberService.findByUsername("system").isPresent()) return;

        memberService.join("system", "1234");
        memberService.join("admin", "1234");
        memberService.join("user1", "1234");
        memberService.join("user2", "1234");
    }
}
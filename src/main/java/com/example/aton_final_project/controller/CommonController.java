package com.example.aton_final_project.controller;

import com.example.aton_final_project.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CommonController {
    private final MemberService memberService;

//    @GetMapping("/sidebar")
//    public String mainHeader(Model model) {
//
//        return "fragment/sidebar";
//    }
}

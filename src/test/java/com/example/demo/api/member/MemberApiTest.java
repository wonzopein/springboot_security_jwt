package com.example.demo.api.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.Charset;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class MemberApiTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ModelMapper modelMapper;


    @Test
    void getAllMembers() throws Exception {
        mockMvc.perform(
                get("/api/members")
                    .param("page", "0")
                    .param("size", "10")
                    .param("sort", "username,desc")
                    .param("sort", "name,desc")
        )
        .andDo(print())
        .andExpect(status().isOk());

    }

    MemberDto.Join createMember() throws Exception {

        MemberDto.Join member = new MemberDto.Join();
        member.setUsername("wonzopein999");
        member.setPassword("1");
        member.setName("어승현");

        mockMvc.perform(
                post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(member))
        )
                .andDo(print())
                .andExpect(status().isOk());

        return member;
    }

    @Test
    void createMemberVoid() throws Exception {

        MemberDto.Join member = new MemberDto.Join();
        member.setUsername("wonzopein999");
        member.setPassword("1");
        member.setName("어승현");

        mockMvc.perform(
                post("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(member))
                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void getMember() throws Exception {

        MemberDto.Join member = createMember();

        mockMvc.perform(get("/api/members/"+ member.getUsername()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void patchMember() throws Exception {

        MemberDto.Join member = createMember();

        MemberDto.Put memberPut = modelMapper.map(member, MemberDto.Put.class);
        memberPut.setName("Test");

        MvcResult result =
            mockMvc.perform(
                    patch("/api/members")
                            .characterEncoding("utf-8")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(memberPut))
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();



        MemberDto.Get memgerGet = objectMapper.readValue(result.getResponse().getContentAsString(), MemberDto.Get.class);
        assertTrue(memgerGet.getName().equals(memberPut.getName()));
    }

    @Test
    void deleteMember() throws Exception {
        MemberDto.Join member = createMember();

        mockMvc.perform(delete("/api/members/"+member.getUsername()))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
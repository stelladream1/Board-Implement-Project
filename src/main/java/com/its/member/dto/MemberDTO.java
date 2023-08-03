package com.its.member.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.its.member.entity.MemberEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberDTO {
    private Long id;
    private String email;

    @JsonIgnore
    private String password;



    public static MemberDTO toMemberDTO(MemberEntity memberEntity){
        MemberDTO  memberDTO = new MemberDTO();
        memberDTO.setId(memberEntity.getId());
        memberDTO.setEmail(memberEntity.getEmail());

        return memberDTO;
    }
}



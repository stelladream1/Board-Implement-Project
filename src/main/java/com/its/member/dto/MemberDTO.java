package com.its.member.dto;

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
    private String password;

    public static MemberDTO toMemberDTO(MemberEntity memberEntity){
        MemberDTO  memberDTO = new MemberDTO();
        memberDTO.setId(memberEntity.getId());
        memberDTO.setEmail(memberEntity.getEmail());
        memberDTO.setPassword(memberEntity.getPassword());

        return memberDTO;
    }
}



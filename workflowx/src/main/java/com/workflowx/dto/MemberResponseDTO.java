package com.workflowx.dto;

import com.workflowx.domain.enums.MemberRole;

public record MemberResponseDTO(
		Long id,
		String memberName,
		String memberClassification,
		String memberId,
		boolean memberStatus,
		MemberRole memberRole
		) {

}

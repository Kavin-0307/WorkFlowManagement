package com.workflowx.dto;

import com.workflowx.domain.enums.MemberRole;

import jakarta.validation.constraints.*;
//This is a Java class that is used to inject data into objects from the information received from the frontend.

public class MemberRequestDTO {
	
	@NotBlank
	private String memberName;
	
	@NotNull
	@Size(max=7,message="Member Id exceeds limit")
	private String memberId;
	
	@NotBlank
	private String memberClassification;
	
	private MemberRole memberRole;
	
	@NotNull
	private boolean memberStatus;
	
	public void setMemberId(String memberId) {
		this.memberId=memberId;
	}
	public void setMemberRole(MemberRole memberRole) {
		this.memberRole=memberRole;
	}
	public void setMemberClassification(String memberClassification) {
		this.memberClassification=memberClassification;
	}
	public void setMemberStatus(boolean memberStatus) {
		this.memberStatus=memberStatus;
	}
	public void setMemberName(String memberName) {
		this.memberName=memberName;
	}
	
	public boolean getMemberStatus() {
		return memberStatus;
	}
	public String getMemberClassification() {
		return memberClassification;
	}
	public String getMemberName() {
		return memberName;
	}
	public MemberRole getMemberRole() {
		return memberRole;
	}
	public String getMemberId() {
		return memberId;
	}
	
}

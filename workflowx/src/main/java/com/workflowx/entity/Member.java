package com.workflowx.entity;

import java.util.List;

import com.workflowx.domain.enums.MemberRole;

import jakarta.persistence.*;

@Entity
@Table(name="teamMembers")
public class Member {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Column(name="member_name",nullable=false)
	private String memberName;//To store the member name
	
	@Column(name="member_classification",nullable=true)
	private String memberClassification;//to store the role of that member in the team
	
	@Column(name="member_id",nullable=false,unique=true)
	private String memberId;
	
	@Column(name="status",nullable=false)
	private boolean memberStatus;
	
	@Enumerated(EnumType.STRING)
	@Column(name="member_role",nullable=false)
	private MemberRole memberRole;
	
	@OneToMany(mappedBy="assigned_to",fetch=FetchType.LAZY)
	private List<Work> assignedWork;
	
	
	//right now we take in memberId 
	//in the future i might generate it using ORG_NAME-YEAR-Count
	public void setMemberId(String memberId) {
		this.memberId=memberId;
	}
	public void setMemberClassification(String memberClassification) {
		this.memberClassification=memberClassification;
	}
	public void setMemberStatus(boolean memberStatus) {
		this.memberStatus=memberStatus;
	}
	public void setMemberRole(MemberRole memberRole) {
		this.memberRole=memberRole;
	}
	public void setMemberName(String memberName) {
		this.memberName=memberName;
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
	public String getMemberClassification() {
		return memberClassification;
	}
	public boolean isMemberActive() {
		return memberStatus;
	}
	public long getId() {
		return id;
	}
	
	
}

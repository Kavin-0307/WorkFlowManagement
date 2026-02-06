package com.workflowx.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.workflowx.domain.enums.MemberRole;
import com.workflowx.dto.MemberRequestDTO;
import com.workflowx.dto.MemberResponseDTO;
import com.workflowx.dto.WorkResponseDTO;
import com.workflowx.entity.Member;
import com.workflowx.entity.Work;
import com.workflowx.repository.MemberRepository;
import com.workflowx.repository.WorkRepository;

import jakarta.transaction.Transactional;

@Service
public class MemberService {
	@Autowired
	private  MemberRepository memberRepository;
	@Autowired
	private WorkRepository workRepository;
	
	
	@Transactional
	public MemberResponseDTO createMember(MemberRequestDTO memberRequestDTO){
		
		if(memberRepository.existsByMemberId(memberRequestDTO.getMemberId())) {
			throw new IllegalArgumentException("Member ID already exists");
		}
		Member member=new Member();
		member.setMemberName(memberRequestDTO.getMemberName());
		member.setMemberClassification(memberRequestDTO.getMemberClassification());
		member.setMemberId(memberRequestDTO.getMemberId());
		member.setMemberRole(MemberRole.valueOf(memberRequestDTO.getMemberRole().name()));

		member.setMemberStatus(memberRequestDTO.getMemberStatus());
		
		return convertToResponseDTO(memberRepository.save(member));
	}
	public MemberResponseDTO updateMember(MemberRequestDTO memberRequestDTO,Long id) {
		Member member=memberRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Member Not Found"));
		if(!member.isMemberActive()) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,"Cannot update an inactive member");
		}
		if (!member.getMemberId().equals(memberRequestDTO.getMemberId())
			    && memberRepository.existsByMemberId(memberRequestDTO.getMemberId())) {
			    throw new ResponseStatusException(HttpStatus.CONFLICT, "Member ID already exists");
			}

		member.setMemberName(memberRequestDTO.getMemberName());
		member.setMemberClassification(memberRequestDTO.getMemberClassification());
		member.setMemberRole(MemberRole.valueOf(memberRequestDTO.getMemberRole().name()));

		member.setMemberStatus(memberRequestDTO.getMemberStatus());
		
		return convertToResponseDTO(memberRepository.save(member));
	}
	public List<MemberResponseDTO> getAllMembers(){
		return memberRepository.findAll().stream().filter(Member::isMemberActive).map(this::convertToResponseDTO).toList();
	}
	public void deactivateMember(Long id) {
		Member member=memberRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Member not found"));
		member .setMemberStatus(false);
		memberRepository.save(member);
	}
	public List<WorkResponseDTO> getWorkByMember(Long memberIdFromPath){
		Authentication auth=SecurityContextHolder.getContext().getAuthentication();
		String memberId=auth.getName();
		Member loggedInMember=memberRepository.findByMemberId(memberId).orElseThrow(()->new ResponseStatusException(HttpStatus.FORBIDDEN,"Member not found"));
		Member effectiveMember;
		if(loggedInMember.getMemberRole()==MemberRole.EMPLOYEE) {
			effectiveMember=loggedInMember;
		}
		else
		{
			effectiveMember=memberRepository.findById(memberIdFromPath).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Manager not found"));
		}
		if(!effectiveMember.isMemberActive())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Member is inactive");
		List<Work> workList=new ArrayList<>();
		workList=workRepository.findByAssignedTo(effectiveMember);
		return workList.stream().map(this::convertToWorkResponseDTO).toList();
	}
	
	public MemberResponseDTO convertToResponseDTO(Member member) {
		return new MemberResponseDTO(
				member.getId(),
				member.getMemberName(),
				member.getMemberClassification(),
				member.getMemberId(),
				member.isMemberActive(),
				member.getMemberRole());
	}
	public WorkResponseDTO convertToWorkResponseDTO(Work work) {
	    return new WorkResponseDTO(
	        work.getWorkId(),
	        work.getAssignedTo().getMemberId(),
	        work.getWorkStatus(),
	        work.getPriorityLevel(),
	        work.getStatusNotes(),
	        work.getDependsOn() != null ? work.getDependsOn().getWorkId() : null
	  
	    );
	}

	
}

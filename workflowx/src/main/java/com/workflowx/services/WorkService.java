package com.workflowx.services;

import jakarta.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.workflowx.domain.enums.MemberRole;
import com.workflowx.domain.enums.WorkStatus;
import com.workflowx.dto.WorkRequestDTO;
import com.workflowx.dto.WorkResponseDTO;
import com.workflowx.entity.Member;
import com.workflowx.entity.Work;
import com.workflowx.repository.MemberRepository;
import com.workflowx.repository.WorkRepository;

@Service
public class WorkService {
	
	private final MemberRepository memberRepository;
	
	private final WorkRepository workRepository;
	public WorkService(WorkRepository workRepository,MemberRepository memberRepository) {
		this.workRepository=workRepository;
		this.memberRepository=memberRepository;
	}
	
	@Transactional
	public WorkResponseDTO createWork(WorkRequestDTO workRequestDTO) {
		if(workRepository.existsByWorkId(workRequestDTO.getWorkId()))//prevent work creation  if the id already exists.
				throw new IllegalArgumentException("Work ID already exists");
		Work work=new Work();
		Member member = memberRepository
			    .findByMemberId(workRequestDTO.getAssignedToMemberId())
			    .orElseThrow(() -> new IllegalArgumentException("Member not found"));
		//Member retrieved to  allow  us to set the owner of that work.
			if (!member.isMemberActive()) {
			    throw new IllegalStateException("Inactive member cannot be assigned work");
			}//terminated employees should not be allowed

			work.setAssignedTo(member);
		if(workRequestDTO.getDependsOnWorkId()!=null) {
			Work parent =workRepository.findByWorkId(workRequestDTO.getDependsOnWorkId()).orElseThrow(()->new IllegalArgumentException("Parent not found"));
			if(parent.getWorkId().equals(workRequestDTO.getWorkId()))
				throw new IllegalStateException("Work cannot depend on itself");
			work.setDependsOn(parent);
		}//If work depends on itself or if parent not found we throw an exception
	
	

		work.setPriorityLevel(workRequestDTO.getWorkPriorityLevel());
		work.setStatusNotes(workRequestDTO.getStatusNotes());
		work.setWorkId(workRequestDTO.getWorkId());
		work.setWorkStatus(WorkStatus.PENDING);
		return convertToWorkResponseDTO(workRepository.save(work));
	}
	@Transactional//It allows the process of coordinating the database ops. 
	/**/
	public WorkResponseDTO updateWork(WorkRequestDTO workRequestDTO,long id) {
		Work work=workRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Work not found"));
		Authentication auth =SecurityContextHolder.getContext().getAuthentication();
		String memberId = auth.getName();
	
		Member member=memberRepository.findByMemberId(memberId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Member not found for which you want to update"));
		if(!member.isMemberActive()) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,"Cannot update for inactive member");
		}
		if(member.getMemberRole()==MemberRole.EMPLOYEE) {
			if(!work.getAssignedTo().getMemberId().equals( memberId)) {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Work doesnt belong to member");
			}
			work.setStatusNotes(workRequestDTO.getStatusNotes());

			work.setWorkStatus(workRequestDTO.getWorkStatus());
		}
		else if(member.getMemberRole()==MemberRole.MANAGER) {// Only the manager should be allowed to reset the work assignment,reset priority level 
			if(workRequestDTO.getAssignedToMemberId()!=null) {
				Member new_Assignee=memberRepository.findByMemberId(workRequestDTO.getAssignedToMemberId()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"This is member does not exist"));
				if(new_Assignee.isMemberActive()==false)
					throw new IllegalStateException("Inactive member cannot be assigned work");
				work.setAssignedTo(new_Assignee);
			
			}
			work.setPriorityLevel(workRequestDTO.getWorkPriorityLevel());
			work.setWorkStatus(workRequestDTO.getWorkStatus());
		}
		if (workRequestDTO.getDependsOnWorkId() != null) {
		    Work parent = workRepository.findByWorkId(workRequestDTO.getDependsOnWorkId())
		        .orElseThrow(() -> new IllegalArgumentException("Parent not found"));

		    if (parent.getWorkId().equals(work.getWorkId())) {
		        throw new IllegalStateException("Work cannot depend on itself");
		    }

		    work.setDependsOn(parent);
		}

		if (work.getWorkStatus() == WorkStatus.COMPLETED
		        && work.getDependsOn() != null
		        && work.getDependsOn().getWorkStatus() != WorkStatus.COMPLETED) {

		    throw new IllegalStateException(
		        "Cannot mark work as COMPLETED until parent is COMPLETED"
		    );
		}
		
		
		// No need to reset id, dangerous work.setWorkId(workRequestDTO.getWorkId());
		
		return convertToWorkResponseDTO(workRepository.save(work));		
		
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

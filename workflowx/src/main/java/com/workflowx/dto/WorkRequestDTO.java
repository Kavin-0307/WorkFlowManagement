package com.workflowx.dto;

import com.workflowx.domain.enums.WorkStatus;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class WorkRequestDTO {
	
	
	@NotBlank
	private String assignedToMemberId;
	@NotBlank
	private String workId;
	@NotNull

	private WorkStatus workStatus;
	@NotBlank
	private String statusNotes;
	@Positive 
	@Min(1)
	@Max(5)
	private int workPriorityLevel;
	@NotBlank
	private String dependsOnWorkId;
	
	public String getWorkId() {
		return workId;
	}
	public String getStatusNotes() {
		return statusNotes;
	}
	public int getWorkPriorityLevel() {
		return workPriorityLevel;
	}
	public WorkStatus getWorkStatus() {
		return workStatus;
	}
	public String getDependsOnWorkId() {
		return dependsOnWorkId;
	}
	public String getAssignedToMemberId() {
		return assignedToMemberId;
	}
	
	public void setWorkId(String workId) {
		this.workId=workId;
	}
	public void setStatusNotes(String statusNotes) {
		this.statusNotes=statusNotes;
	}
	public void setWorkPriorityLevel(int workPriorityLevel) {
		this.workPriorityLevel=workPriorityLevel;
	}
	public void setWorkStatus(WorkStatus workStatus) {
		this.workStatus=workStatus;
	}
	public void setDependsOnWorkId(String dependsOnWorkId) {
		this.dependsOnWorkId=dependsOnWorkId;
	}
	public void setAssignedTo(String assignedToMemberId) {
		this.assignedToMemberId=assignedToMemberId;
	}
	
}

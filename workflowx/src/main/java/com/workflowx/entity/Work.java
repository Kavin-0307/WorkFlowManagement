package com.workflowx.entity;
import java.time.LocalDateTime;

import com.workflowx.domain.enums.WorkStatus;

import jakarta.persistence.*;

@Entity
@Table(name="work")
public class Work {


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Column(name="work_id",nullable=false,unique=true)
	private String workId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "assigned_to_id", nullable = false)
	private Member assignedTo;//AI
	
	
	
	
	@Column(name="work_time",nullable=false)
	private LocalDateTime workTime;
	@Enumerated(EnumType.STRING)
	@Column(name="work_status",nullable=false)
	private WorkStatus workStatus;//AI
	
	@Column(name="status_notes",nullable=false)
	private String statusNotes;
	
	
	@Column(name="work_priority_level",nullable=false)
	private int workPriorityLevel;
	
	
	 @ManyToOne(fetch = FetchType.LAZY)
	 @JoinColumn(name = "depends_on_id")
	 private Work dependsOn;
	 
	 public long getId() {
		 return id;
	 }
	 public String getWorkId() {
		 return workId;
	 }
	 public Member getAssignedTo() {
		 return assignedTo;
	 }
	 public WorkStatus getWorkStatus() {
		 return workStatus;
	 }
	 public String getStatusNotes() {
		 return statusNotes;
	 }
	 public int getPriorityLevel() {
		 return workPriorityLevel;
	 }
	 public Work getDependsOn() {
		 return dependsOn;
	 }
	 
	 
	 public void setWorkId(String workId) {
		 this.workId=workId;
	 }
	 public void setAssignedTo(Member assignedTo) {
		 this.assignedTo=assignedTo;
	 }
	 public void setWorkStatus(WorkStatus workStatus) {
		 this.workStatus=workStatus;
	 }
	 public void setStatusNotes(String statusNotes) {
		 this.statusNotes=statusNotes;
	 }
	 public void setPriorityLevel(int workPriorityLevel) {
		 this.workPriorityLevel=workPriorityLevel;
	 }
	 public void setDependsOn(Work dependsOn) {
		 this.dependsOn=dependsOn;
	 }
	
	 @PrePersist
	 protected void onCreate() {
		 this.workTime=LocalDateTime.now();
	 }
	 
}
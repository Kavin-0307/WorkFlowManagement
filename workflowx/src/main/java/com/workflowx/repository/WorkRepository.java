package com.workflowx.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workflowx.domain.enums.WorkStatus;
import com.workflowx.entity.Member;
import com.workflowx.entity.Work;

public interface WorkRepository extends JpaRepository<Work,Long> {
	List<Work> findByAssignedTo_MemberId(String memberId);

		List<Work> findByWorkStatus(WorkStatus status);
		List<Work> findByDependsOn(Work parentWork);
		Optional<Work> findByWorkId(String workId);
		List<Work> findByAssignedTo(Member effectiveMember);
		boolean existsByWorkId(String workId);
}

package com.workflowx.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.*;

import com.workflowx.entity.Member;

public interface MemberRepository extends JpaRepository<Member,Long>{
	Optional<Member> findByMemberId(String memberId);
	boolean existsByMemberId(String memberId);

	
}

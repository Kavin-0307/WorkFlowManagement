
package com.workflowx.dto;

import com.workflowx.domain.enums.WorkStatus;
//records used to provide a way to give back the response in an immutable class.It also produces equals(),hashCode(),toString() which reduces the boiler plate code
public record WorkResponseDTO(String workId,
		String assignedMemberId,
		WorkStatus status,
		int priorityLevel,
		String statusNotes,
		String dependsOnWorkId
		) {

}

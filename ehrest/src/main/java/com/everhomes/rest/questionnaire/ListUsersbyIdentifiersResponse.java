// @formatter:off
package com.everhomes.rest.questionnaire;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.user.UserDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * 
 * <ul>返回值:
 * <li>UserDtos: 用户列表，参考{@link com.everhomes.rest.questionnaire.QuestionnaireUserDTO}</li>
 * </ul>
 */
public class ListUsersbyIdentifiersResponse {

	@ItemType(QuestionnaireUserDTO.class)
	private List<QuestionnaireUserDTO> userDtos;

	public ListUsersbyIdentifiersResponse() {
	}

	public ListUsersbyIdentifiersResponse(List<QuestionnaireUserDTO> userDtos) {
		this.userDtos = userDtos;
	}

	public List<QuestionnaireUserDTO> getUserDtos() {
		return userDtos;
	}

	public void setUserDtos(List<QuestionnaireUserDTO> userDtos) {
		this.userDtos = userDtos;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}

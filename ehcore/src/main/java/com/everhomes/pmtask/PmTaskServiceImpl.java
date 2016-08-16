package com.everhomes.pmtask;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.everhomes.rest.pmtask.AssignTaskCommand;
import com.everhomes.rest.pmtask.CommunityTaskDTO;
import com.everhomes.rest.pmtask.CreateCategoryCommand;
import com.everhomes.rest.pmtask.CreateNewTaskCommand;
import com.everhomes.rest.pmtask.CreateTaskCategoryCommand;
import com.everhomes.rest.pmtask.DeleteCategoryCommand;
import com.everhomes.rest.pmtask.DeleteTaskCategoryCommand;
import com.everhomes.rest.pmtask.EvaluateTaskCommand;
import com.everhomes.rest.pmtask.GetTaskDetailCommand;
import com.everhomes.rest.pmtask.ListCategoriesCommand;
import com.everhomes.rest.pmtask.ListCategoriesResponse;
import com.everhomes.rest.pmtask.ListMyTasksCommand;
import com.everhomes.rest.pmtask.ListMyTasksResponse;
import com.everhomes.rest.pmtask.ListTaskCategoriesCommand;
import com.everhomes.rest.pmtask.ListTaskCategoriesResponse;
import com.everhomes.rest.pmtask.SearchTasksCommand;
import com.everhomes.rest.pmtask.SearchTasksResponse;
import com.everhomes.rest.pmtask.SetTaskStatusCommand;

@Component
public class PmTaskServiceImpl implements PmTaskService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PmTaskServiceImpl.class);

	@Override
	public SearchTasksResponse searchTasks(SearchTasksCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListMyTasksResponse listMyTasks(ListMyTasksCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void evaluateTask(EvaluateTaskCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTaskStatus(SetTaskStatusCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void assignTask(AssignTaskCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CommunityTaskDTO getTaskDetail(GetTaskDetailCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createNewTask(CreateNewTaskCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteCategory(DeleteCategoryCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createCategory(CreateCategoryCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ListCategoriesResponse listCategories(ListCategoriesCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteTaskCategory(DeleteTaskCategoryCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createTaskCategory(CreateTaskCategoryCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ListTaskCategoriesResponse listTaskCategories(
			ListTaskCategoriesCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void exportTasks(SearchTasksCommand cmd, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		
	}
	
	
}

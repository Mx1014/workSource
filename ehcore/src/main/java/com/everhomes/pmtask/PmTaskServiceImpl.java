package com.everhomes.pmtask;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.everhomes.rest.category.CategoryDTO;
import com.everhomes.rest.pmtask.AssignTaskCommand;
import com.everhomes.rest.pmtask.GetTaskLogCommand;
import com.everhomes.rest.pmtask.PmTaskDTO;
import com.everhomes.rest.pmtask.CreateTaskCommand;
import com.everhomes.rest.pmtask.CreateTaskCategoryCommand;
import com.everhomes.rest.pmtask.DeleteTaskCategoryCommand;
import com.everhomes.rest.pmtask.EvaluateTaskCommand;
import com.everhomes.rest.pmtask.GetStatisticsCommand;
import com.everhomes.rest.pmtask.GetStatisticsResponse;
import com.everhomes.rest.pmtask.GetTaskDetailCommand;
import com.everhomes.rest.pmtask.ListUserTasksCommand;
import com.everhomes.rest.pmtask.ListUserTasksResponse;
import com.everhomes.rest.pmtask.ListTaskCategoriesCommand;
import com.everhomes.rest.pmtask.ListTaskCategoriesResponse;
import com.everhomes.rest.pmtask.PmTaskLogDTO;
import com.everhomes.rest.pmtask.SearchTaskStatisticsCommand;
import com.everhomes.rest.pmtask.SearchTaskStatisticsResponse;
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
	public ListUserTasksResponse listUserTasks(ListUserTasksCommand cmd) {
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
	public PmTaskDTO getTaskDetail(GetTaskDetailCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PmTaskDTO createTask(CreateTaskCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteTaskCategory(DeleteTaskCategoryCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CategoryDTO createTaskCategory(CreateTaskCategoryCommand cmd) {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public SearchTaskStatisticsResponse searchTaskStatistics(
			SearchTaskStatisticsCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GetStatisticsResponse getStatistics(GetStatisticsCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PmTaskLogDTO getTaskLog(GetTaskLogCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	
}

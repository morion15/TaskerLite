package com.taskerlite.taskLogic;

import com.google.gson.GsonBuilder;

import java.util.ArrayList;


public class SceneL {

	public static enum ACTION_TYPE {TIMER, FINISHBOOT, SCREENON, SCREENOFF};
	public static enum TASK_TYPE {APP, ALARM, WIFI, MOBILEDATA};
	
	private ArrayList<Scene> sceneList = new ArrayList<Scene>();
	
	public void addNewSnene(String sceneName){
		
		sceneList.add(new Scene(sceneName));
	}
	
	public Scene getScene(int index){
		
		return sceneList.get(index);
	}
	
	public ArrayList<Scene> getSceneList() {
		return sceneList;
	}
	
	public int getSceneListSize(){
		return sceneList.size();
	}
	
	public void removeSceneFromList(int index){
		sceneList.remove(index);
		// need add save to flash
	}
	
	public class Scene{
		
		private String name;
		private ArrayList<Action> actionList = new ArrayList<Action>();
		private ArrayList<Task>   taskList = new ArrayList<Task>();
		
		public Scene(String sceneName){
			
			this.name = sceneName;
		}
		
		public void addNewAction(String actionName, mAction actionObject, ACTION_TYPE actionType, int xCoordinate, int yCoordinate){
			
			actionList.add(new Action(actionName, actionObject, actionType, xCoordinate, yCoordinate));
		}
		
		public void addNewTask(String objName, mTask obj, TASK_TYPE objType){
			
			taskList.add(new Task(objName, obj, objType));
			
			//for test
			taskList.get(0).setTaskActioId(actionList.get(0).getActionId());
		}		
		
		public String getName() {
			return name;
		}

		public ArrayList<Task> getTaskList() {
			return taskList;
		}
		
		public ArrayList<Action> getActionList() {
			return actionList;
		}
	}
	
	public class Action{
		
		private transient mAction actionObject;
		private ACTION_TYPE actionType;
		private String actionINStr;
		private String actionName;
		private long actionId;
		private int xCoordinate, yCoordinate;
		
		public Action (String actionName, mAction actionObject, ACTION_TYPE actionType, int xCoordinate,int yCoordinate){
			
			this.actionName = actionName;
			this.actionType = actionType;
			this.actionObject = actionObject;
			this.xCoordinate = xCoordinate;
			this.yCoordinate = yCoordinate;
			
			actionId = System.currentTimeMillis();
			actionINStr = new GsonBuilder().create().toJson(actionObject);
		}
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public mAction getActionObject() {
			
			if(actionObject == null){
				
				Class deserializationClass = null;
				
				switch (actionType) {
				case TIMER:
					deserializationClass = aTimer.class;
					break;
				case FINISHBOOT:
					break;
				case SCREENOFF:
					break;
				case SCREENON:
					break;
				default:
					break;
				}
				
				actionObject = new GsonBuilder().create().fromJson(actionINStr, deserializationClass);
			}
			
			return actionObject;
		}
		
		public long getActionId() {
			return actionId;
		}
	}
	
	public class Task{
		
		private transient mTask obj;
		private TASK_TYPE taskType;
		private String taskINStr;
		private String taskName; 
		private long taskActioId;
		private long taskId;
		private int xCoordinate, yCoordinate;
		
		public Task(String objName, mTask obj, TASK_TYPE objType){
			
			this.taskName = objName;
			this.obj = obj;	
			this.taskType = objType;
			
			taskId = System.currentTimeMillis();
			taskINStr = new GsonBuilder().create().toJson(obj);
		}
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public mTask getObj() {
			
			if(obj == null){
				
				Class deserializationClass = null;
				
				switch (taskType) {
				case APP:
					deserializationClass = tApp.class;
					break;
				case WIFI:
					deserializationClass = tApp.class;
					break;
				case ALARM:
					break;
				case MOBILEDATA:
					break;
				default:
					break;
				}
				
				obj = new GsonBuilder().create().fromJson(taskINStr, deserializationClass);
			}
			
			return obj;
		}
		public String getTaskName() {
			return taskName;
		}
		public boolean isMyTask(long id) {
			return id == taskId ? true : false;
		}
		public boolean isMyTaskAction(long id) {
			return id == taskActioId ? true : false;
		}		
		public void setTaskActioId(long taskActioId) {
			this.taskActioId = taskActioId;
		}
		public int getXCoordinate() {
			return xCoordinate;
		}
		public int getYCoordinate() {
			return yCoordinate;
		}
	}
}


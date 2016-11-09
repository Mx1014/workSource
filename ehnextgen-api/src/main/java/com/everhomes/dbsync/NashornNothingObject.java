package com.everhomes.dbsync;

public class NashornNothingObject implements NashornObject {

	public static NashornNothingObject Nothing = new NashornNothingObject();
	
	@Override
	public long getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setId(long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getCreateTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getTimeout() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void onError(Exception ex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onComplete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getJSFunc() {
		// TODO Auto-generated method stub
		return null;
	}

}

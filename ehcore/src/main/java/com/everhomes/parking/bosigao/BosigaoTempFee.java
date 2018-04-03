package com.everhomes.parking.bosigao;

public class BosigaoTempFee {
	private String PlateNumber;
	private String CardNO;
	private String ParkingID;
	private String ParkName;
	private String EntranceDate;
	private Long payTime;
	private Integer OutTime;
	private String PayDate;
	private Integer Result;
	private Pkorder Pkorder;

	public Long getPayTime() {
		return payTime;
	}

	public void setPayTime(Long payTime) {
		this.payTime = payTime;
	}

	public String getPlateNumber() {
		return PlateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		PlateNumber = plateNumber;
	}

	public String getCardNO() {
		return CardNO;
	}

	public void setCardNO(String cardNO) {
		CardNO = cardNO;
	}

	public String getParkingID() {
		return ParkingID;
	}

	public void setParkingID(String parkingID) {
		ParkingID = parkingID;
	}

	public String getParkName() {
		return ParkName;
	}

	public void setParkName(String parkName) {
		ParkName = parkName;
	}

	public String getEntranceDate() {
		return EntranceDate;
	}

	public void setEntranceDate(String entranceDate) {
		EntranceDate = entranceDate;
	}

	public Integer getOutTime() {
		return OutTime;
	}

	public void setOutTime(Integer outTime) {
		OutTime = outTime;
	}

	public String getPayDate() {
		return PayDate;
	}

	public void setPayDate(String payDate) {
		PayDate = payDate;
	}

	public Integer getResult() {
		return Result;
	}

	public void setResult(Integer result) {
		Result = result;
	}

	public com.everhomes.parking.bosigao.Pkorder getPkorder() {
		return Pkorder;
	}

	public void setPkorder(com.everhomes.parking.bosigao.Pkorder pkorder) {
		Pkorder = pkorder;
	}
}

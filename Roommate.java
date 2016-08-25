import java.util.Arrays;

public class Roommate {
	private String name,sex,country, programType, facebookUrl, messageToFutureRoommate;
	private String[] attitude, roomConditions;

	static final String[] countries = {"Slovakia","Germany"};
	static final int FULL_POINTS = 5;
	static final int wgtOfAttitude = 10;
	static final int wgtOfRoomConditions = 10;
	static final int wgtOfNationality = 80;
	static final int wgtOfYear = 0;

	public Roommate(String[] rawData){
		//new version
		name = rawData[0];
		country = rawData[1];
		facebookUrl = rawData[2];
		sex = rawData[3];
		programType = rawData[4];
		messageToFutureRoommate = rawData[5];
		attitude = Arrays.copyOfRange(rawData, 6, 11);
		roomConditions = Arrays.copyOfRange(rawData, 12, 21);
	}

	/**
	 * compares the attitudes of two rommate objects
	 * @param roommie
	 * @return weighted score of how much the two rommates were compatible in terms of attitude
	 */
	private double compareAttitude(Roommate roommie){
		int totalPointsAvailable = FULL_POINTS*getAttitude().length;
		double score = 0.0;
		for(int i = 0; i< getAttitude().length;i++){
			int me = Integer.valueOf(getAttitude()[i]);
			int other = Integer.valueOf(roommie.getAttitude()[i]);
			score += (FULL_POINTS - Math.abs(me - other));
		}
		return (score/totalPointsAvailable)*wgtOfAttitude;
	}
	
	
	/**
	 * compares the roomCondition preferences of two roommate objects
	 * @param roommie
	 * @return weighted score of how much the two rommates were compatible in terms of roomPreferences
	 */
	private double compareRoomConditions(Roommate roommie){
		int totalPointsAvailable = FULL_POINTS*getRoomConditions().length;
		double score = 0.0;
		for (int i = 0; i< getRoomConditions().length; i++) {
			int me = Integer.valueOf(getRoomConditions()[i]);
			int other = Integer.valueOf(roommie.getRoomConditions()[i]);
			score += (FULL_POINTS - Math.abs(me - other));
		}
		return (score/totalPointsAvailable)*wgtOfRoomConditions;
	}
	
	
	boolean isFromTheSameCountryAs(Roommate roommie){
		if(getCountry().equals(roommie.getCountry())){
			return true;
		}
		return false;
	}
	
	boolean isInTheSameClassAs(Roommate roommie){
		if(getYear().equals(roommie.getYear())){
			return true;
		}
		return false;
	}
	

	double checkCompatibility(Roommate roommie){
		double result =  compareAttitude(roommie) + 
								compareRoomConditions(roommie);

		return result;
	}

	 String getName(){
		return name;
	}
	String getSex(){
		return sex;
	}

	String getCountry(){
		return country;
	}

	String getProgramType(){
		return programType;
	}

	String getFacebookURL(){
		return facebookUrl;
	}

	String getMessageToFutureRoommate(){
		return messageToFutureRoommate;
	}
	String getYear(){
		return programType;
	}

	String[] getAttitude(){
		return attitude;
	}

	String[] getRoomConditions(){
		return roomConditions;
	}
	
	String getProfile(){
		StringBuilder profile = new StringBuilder();
		profile.append("Hi, \n My name is :").append(getName())
			.append(". I am from ").append(getCountry())
				.append(".  \n This is my messsage to you: \n  " +getMessageToFutureRoommate());
		
		return profile.toString();
	}

	public String toString(){
		return getName();
	}



}

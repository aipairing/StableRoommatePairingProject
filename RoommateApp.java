import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class RoommateApp {
	private  ArrayList<Roommate> maleY2 = new ArrayList<>();  //Contains Male Roommate Objects in Y2-Program
	private ArrayList<Roommate> femaleY2 = new ArrayList<>();  //Contains Female Roommate Objects in Y2-Program
	private ArrayList<Roommate> maleY4 = new ArrayList<>();  //Contains Male Roommate Objects in Y4-Program
	private ArrayList<Roommate> femaleY4 = new ArrayList<>();  //Contains Female Roommate Objects in Y4-Program

	private ArrayList<ArrayList<String>> maleRankingsY2 = new ArrayList<>();  //Contains ArrayLists of Male Roommate Objects in Y2-Program
	private ArrayList<ArrayList<String>> maleRankingsY4 = new ArrayList<>();
	private ArrayList<ArrayList<String>>  femaleRankingsY2 = new ArrayList<>();  //Contains ArrayLists of Female Roommate Objects in Y2-Program
	private ArrayList<ArrayList<String>>  femaleRankingsY4 = new ArrayList<>();

	private HashMap<String, Roommate> masterDB = new HashMap<>();
	private HashMap<String, String> maleFinalPairings = new HashMap<>();
	private HashMap<String, String> femaleFinalPairings = new HashMap<>();




	public void run() {
		ArrayList<Roommate> db;
		try {
			db = createRoommateDatabase();
			groupIntoGender(db);
			makeListsEqual();
			//sortBasedOnCompatibility();
			doSorting();
			String[][] finalMaleRankings_Y4 = generateMaleArrayRankings();
			String[][] finalMaleRankings_Y2 = generateMaleArrayRankings2();
			String[][] finalFemaleRankings_Y4 = generateFemaleArrayRankings();
			String[][] finalFemaleRankings_Y2 = generateFemaleArrayRankings2();

			System.out.println("---Boys---");
			PrintStableMatching(finalMaleRankings_Y2,finalMaleRankings_Y4);
			System.out.println("---Girls---");
			PrintStableMatching(finalFemaleRankings_Y2,finalFemaleRankings_Y4);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	

	}


	/**
	 * Returns an ArrayList of Roommates after database is populated from csv File
	 * @return
	 * @throws FileNotFoundException 
	 * @throws UnsupportedEncodingException 
	 */
	private ArrayList<Roommate> createRoommateDatabase() throws UnsupportedEncodingException, FileNotFoundException{
		System.out.print("Extracting from csv file...0%");
		String csvFile = "LeafDatabase.csv";
		BufferedReader br = null;

		String line = "";
		String csvSplitBy = ",";
		try {
			ArrayList<Roommate> applicants = new ArrayList<Roommate>();
			br = new BufferedReader(
					new InputStreamReader(
							new FileInputStream(csvFile), "UTF-8"));
			while ((line = br.readLine()) != null) {
				// use comma as separator
				String[] data = line.split(csvSplitBy);
				Roommate roommie = new Roommate(data);
				applicants.add(roommie);
				masterDB.put(data[0], roommie);
			}
			return applicants;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		//return empty database
		return new ArrayList<>();
	}

	/**
	 * Groups all the Roommate objects in the database into their 
	 * respective gender and year groups
	 * @param allRoommates
	 */
	private void groupIntoGender(ArrayList<Roommate> applicants){
		System.out.print("Grouping by Gender and Year...");

		for(Roommate roommie: applicants){

			if(roommie.getSex().equals("Male")){
				if(roommie.getProgramType().equals("2-Year Program")){
					maleY2.add(roommie);
				}else{
					maleY4.add(roommie);
				}
			}else{
				if(roommie.getProgramType().equals("2-Year Program")){
					femaleY2.add(roommie);
				}else{
					femaleY4.add(roommie);
				}
			}
		}
		System.out.println("Grouping Done...100%");
	}

	//@SuppressWarnings("unused")
	@SuppressWarnings("unused")
	private void makeListsEqual(){
		System.out.print("Making the Lists Equal...");

		//make sure to have an even number of boys altogether
		//make sure to have an even number of girls too
		int differenceMales = Math.abs(maleY2.size() - maleY4.size());
		int differenceFemales = Math.abs(femaleY2.size() - femaleY4.size());


		if(differenceMales%2 != 0){
			String[] fakeMalePerson = {"Male X", "Country X","No FB account", "Male", "4-Year Program","I am not a real person","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1"};
			maleY4.add(new Roommate(fakeMalePerson));
			System.out.println("Males not even");
		}
		if(differenceFemales%2 != 0){
			String[] fakeFemalePerson = {"Female Y", "Country Y","No FB account", "Female", "4-Year Program","I am not a real person","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1"};
			femaleY4.add(new Roommate(fakeFemalePerson));
			System.out.println("Females not even");
		}

		while(differenceMales > 0){
			boolean Y2IsMore = maleY2.size() > maleY4.size() ;
			Roommate excess = (Y2IsMore ? maleY2.remove(0) : maleY4.remove(0));
			boolean status = (Y2IsMore? maleY4.add(excess): maleY2.add(excess));
			differenceMales = Math.abs(maleY2.size() - maleY4.size());
		}

		while(differenceFemales > 0){
			boolean Y2IsMore = femaleY2.size() > femaleY4.size() ;
			Roommate excess = (Y2IsMore ? femaleY2.remove(0) : femaleY4.remove(0));
			boolean status = (Y2IsMore? femaleY4.add(excess): femaleY2.add(excess));
			differenceFemales = Math.abs(femaleY2.size() - femaleY4.size());
		}

		System.out.print("Done...There are now: "+maleY2.size() +" Y2 boys "
				+maleY4.size()+" Y4 boys " +femaleY2.size()+" Y2 girls "
				+femaleY4.size()+" Y4 girls ");

	}


	private void doSorting(){
		//Males in Y4 Preferences
		for (Roommate roommie4 : maleY4){
			Map<String, Double> hisRankings = new LinkedHashMap<String, Double>();
			//he ranks himself first
			hisRankings.put(roommie4.getName(),0.0);

			for (Roommate roommie2: maleY2 ){
				double value = 100 - roommie4.checkCompatibility(roommie2);
				hisRankings.put(roommie2.getName(),value);
			}
			ArrayList<String> addOn = new ArrayList<String>();
			hisRankings = doBackgroundChecks(sortByValue(hisRankings));

			addOn.addAll(hisRankings.keySet());
			maleRankingsY2.add(addOn);

		}

		//Males in Y2 Preferences
		for(Roommate roommie2: maleY2){
			Map<String, Double> hisRankings = new LinkedHashMap<String, Double>();
			hisRankings.put(roommie2.getName(),0.0);

			for(Roommate roommie4: maleY4 ){
				double value = 100 - roommie2.checkCompatibility(roommie4);
				hisRankings.put(roommie4.getName(), value);
			}
			ArrayList<String> addOn = new ArrayList<String>();
			hisRankings = doBackgroundChecks(sortByValue(hisRankings));

			addOn.addAll(hisRankings.keySet());
			maleRankingsY4.add(addOn);

		}

		//Females in Y4 Rankings
		for (Roommate roommie4 : femaleY4){
			Map<String, Double> hisRankings = new LinkedHashMap<String, Double>();
			//he ranks himself first
			hisRankings.put(roommie4.getName(), 0.0);

			for (Roommate roommie2: femaleY2 ){
				double value = 100 - roommie4.checkCompatibility(roommie2);
				hisRankings.put(roommie2.getName(), value);

			}
			ArrayList<String> addOn = new ArrayList<String>();
			hisRankings = doBackgroundChecks(sortByValue(hisRankings));

			addOn.addAll(hisRankings.keySet());
			femaleRankingsY2.add(addOn);

		}

		//For Females Y2 Preferences
		for(Roommate roommie2: femaleY2){
			Map<String, Double> hisRankings = new LinkedHashMap<String, Double>();
			hisRankings.put(roommie2.getName(),0.0);

			for(Roommate roommie4: femaleY4 ){
				double value = 100 - roommie2.checkCompatibility(roommie4);
				hisRankings.put(roommie4.getName(), value);
			}

			ArrayList<String> addOn = new ArrayList<String>();
			hisRankings = doBackgroundChecks(sortByValue(hisRankings));

			addOn.addAll(hisRankings.keySet());
			femaleRankingsY4.add(addOn);
		}
	}

	private  <K, V extends Comparable<? super V>> Map<K, V>  sortByValue( Map<K, V> map ){
		Map<K, V> result = new LinkedHashMap<>();
		Stream<Map.Entry<K, V>> st = map.entrySet().stream();

		st.sorted( Map.Entry.comparingByValue() )
		.forEachOrdered( e -> result.put(e.getKey(), e.getValue()) );

		return result;
	}

	private Map<String, Double> doBackgroundChecks(Map<String, Double> map){
		int i=0;
		Map<String, Double> copyOfMap = new LinkedHashMap<String, Double>();
		copyOfMap.putAll(map);
		Roommate me = null ;
		for (String name: map.keySet()){
			if(i==0){
				me = masterDB.get(name);
			}else{
				Roommate other = masterDB.get(name);
				if(other.isFromTheSameCountryAs(me)){
					copyOfMap.remove(name);
					copyOfMap.put(name, 0.0);
				};
			}
			i++;
		}
		return copyOfMap;
	}

	private String[][] generateMaleArrayRankings(){
		ArrayList<String[]> males = new ArrayList<>();
		for(ArrayList<String> m: maleRankingsY2){
			males.add(m.toArray(new String[m.size()]));
		}
		String[][] finalMaleRankings  = males.toArray(new String[males.size()][]);
		System.out.println("---Boys---");
		printStringArray(finalMaleRankings);
		return finalMaleRankings;
	}

	private String[][] generateMaleArrayRankings2(){
		ArrayList<String[]> males = new ArrayList<>();

		for(ArrayList<String> m: maleRankingsY4){
			males.add(m.toArray(new String[m.size()]));
		}
		String[][] finalMaleRankings2  = males.toArray(new String[males.size()][]);
		System.out.println("---Boys---");
		printStringArray(finalMaleRankings2);
		return finalMaleRankings2;
	}
	private String[][] generateFemaleArrayRankings(){
		ArrayList<String[]> females = new ArrayList<>();

		//for females
		for(ArrayList<String> f: femaleRankingsY2){
			females.add(f.toArray(new String[f.size()]));
		}

		String[][] finalFemaleRankings  = females.toArray(new String[females.size()][]);
		System.out.println("---Girls---");
		printStringArray(finalFemaleRankings);
		return finalFemaleRankings;
	}
	private String[][] generateFemaleArrayRankings2(){
		ArrayList<String[]> females = new ArrayList<>();

		//for females
		for(ArrayList<String> f: femaleRankingsY4){
			females.add(f.toArray(new String[f.size()]));
		}

		String[][] finalFemaleRankings2  = females.toArray(new String[females.size()][]);
		System.out.println("---Girls---");
		printStringArray(finalFemaleRankings2);
		return finalFemaleRankings2;
	}

	private void printStringArray(String[][] array){
		for(int i=0; i<array.length;i++){
			System.out.println(Arrays.toString(array[i]));
		}
	}

	private void PrintStableMatching(String[][] twoYearPrefs, String[][] fourYearPrefs) {
		HashMap<String, Integer> nameMap = new HashMap<>();
		HashMap<Integer, String> twoReverseMap = new HashMap<>();
		HashMap<Integer, String> fourReverseMap = new HashMap<>();
		//Creating name to number maps
		for (int i=0; i<twoYearPrefs.length; i++) {
			nameMap.put(twoYearPrefs[i][0],i+1);
			nameMap.put(fourYearPrefs[i][0],i+1);
			twoReverseMap.put(i+1,twoYearPrefs[i][0]);
			fourReverseMap.put(i+1,fourYearPrefs[i][0]);
		}
		//initiating arrays
		int n = twoYearPrefs.length;
		int[][] twoYearPrefss = new int[n+1][n];
		int[][] fourYearPrefss = new int[n+1][n];
		//filling the arrays
		for (int i=1; i<=n; i++) {
			for (int j=0; j<n; j++) {
				twoYearPrefss[i][j] = nameMap.get(twoYearPrefs[i-1][j+1]);
				fourYearPrefss[i][j] = nameMap.get(fourYearPrefs[i-1][j+1]);
			}
		}
		//initiating algorithm arrays
		int [] engaged=new int[n+1];
		int [] menpointer=new int[n+1];
		int [] mlist=new int [(n+1)*(n+1)];
		int be=0;
		int en=n;
		//random filling of mlist
		for(int i=0;i<n;i++)
			mlist[i]=i+1;
		//initiating ranking array
		int [][] wrank=new int[n+1][n+1];
		//creating 2D array for women prefs ranking
		for(int i=1;i<=n;i++)
		{
			wrank[i][0]=n+1;
			for(int j=0;j<n;j++)
			{
				wrank[i][fourYearPrefss[i][j]]=j;
			}
		}
		//iterative algorithm to match people
		while(be!=en)
		{
			int a=mlist[be];
			be++;
			while(menpointer[a]<n)
			{
				int h=twoYearPrefss[a][menpointer[a]];
				menpointer[a]++;
				if (wrank[h][a]<wrank[h][engaged[h]])
				{
					if(engaged[h]!=0)
					{
						mlist[en]=engaged[h];
						en++;
					}

					engaged[h]=a;
					break;
				}
			}
		}
		//printing results of the matching
		for(int i=1;i<=n;i++)
		{
			if(engaged[i]!=0)
			{
				String person1 = twoReverseMap.get(engaged[i]);
				String person2 = fourReverseMap.get(i);

				if(masterDB.get(person1).getSex().equals("Male")){
					maleFinalPairings.put(person1, person2);
				}else{
					femaleFinalPairings.put(person1, person2);

				}
				
				//System.out.println(person1 + " matched with " + person2 );
				if(masterDB.containsKey(person1)){

					System.out.println(masterDB.get(person1).getCountry()+" - "+masterDB.get(person2).getCountry()) ;
					//System.out.println(masterDB.get(person1).getName()+" - "+masterDB.get(person2).getName()) ;
					//System.out.println(masterDB.get(person1).getYear()+" - "+masterDB.get(person2).getYear()) ;

				}


			}
		}

		exporToCSV(maleFinalPairings, "malePairings.csv");
		exporToCSV(femaleFinalPairings, "femalePairings.csv");
	}


	public  void exporToCSV(HashMap<String, String >pairings, String name){
		PrintWriter pw;
		try {
			pw = new PrintWriter(new File( name));

			for(String word: pairings.keySet()){
				StringBuilder sb = new StringBuilder();
				sb.append(word).append(',').append(pairings.get(word));
				sb.append('\n');
				pw.write(sb.toString());
			}
			pw.close();

			System.out.println("Finished Writing to CSV File!");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

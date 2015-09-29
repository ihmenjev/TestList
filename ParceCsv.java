import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ParceCsv {
	private  ArrayList<Person> personList;
	

	public  ArrayList<Person> getPersonList() {
		return personList;
	}

	public void setPersonList(ArrayList<Person> personList) {
		this.personList = personList;
	}

	public static void main(String[] args) {

		// for (int i = 0; i <
		// getPersFromFile("e:/GoogleDrive/резюме/задане/person.csv").size();
		// i++) {
		// System.out
		// .println(getPersFromFile("e:/GoogleDrive/резюме/задане/person.csv").get(i).getEmail());
		// }
	}

	public static ArrayList<Person> getPersFromFile(String address) {
		BufferedReader reader = null;
		String line = "";
		String separator = ";";
		ArrayList<Person> personList = new ArrayList<>();

		try {
			reader = new BufferedReader(new FileReader(address));
			while ((line = reader.readLine()) != null) {
				String[] person = line.split(separator);
				personList.add(new Person(person[0], person[1], person[2],
						person[3], person[4]));

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		
		for (int i = 0; i < personList.size(); i++) {
			System.out.println(personList.get(i)
					.getEmail());
		}
		return personList;
	}
}

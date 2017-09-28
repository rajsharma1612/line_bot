package line.bot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CsvWriter {
	
	static ArrayList<String> data = new ArrayList<String>();
	
	public static void write_to_csv() {
		data.add(Constants.ad_Account_Id);
		data.add(String.valueOf(Constants.campaign_id));
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		data.add(dateFormat.format(date));
		
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(new File("Logs.csv"));
			String data_to_write = data.get(0)+";"+data.get(1)+";"+data.get(2);
			fileOutputStream.write(data_to_write.getBytes());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

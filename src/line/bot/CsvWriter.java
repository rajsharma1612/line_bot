package line.bot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CsvWriter {

	static String file_path = "Logs.csv";

	public static void write_to_csv() {

		try {

			FileOutputStream fileOutputStream = new FileOutputStream(new File(file_path), true);
			List<String> lines = Files.readAllLines(Paths.get(file_path), Charset.forName("UTF-8"));
			if (lines.size() < 1) {
				String data = "Ad Account ID,CPID,Date" + "\n";
				fileOutputStream.write(data.getBytes());
			}
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();

			String data_to_write = Constants.ad_Account_Id + "," + Constants.CPID + "," + dateFormat.format(date)
					+ "\n";
			fileOutputStream.write(data_to_write.getBytes());
			fileOutputStream.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

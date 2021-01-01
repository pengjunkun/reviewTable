import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by JackPeng(jack.peng@sap.com) on 07/31/2017.
 */

public class ProduceBlankNote
{
	private static String thisYear;

	public static String outPutTemplate =
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
					+ "<!DOCTYPE en-export SYSTEM \"http://xml.evernote.com/pub/evernote-export3.dtd\">\n"
					+ "<en-export export-date=\"20170731T050238Z\" application=\"Evernote\" version=\"Evernote Mac 6.11.1 (455059)\">\n"
					+ "<note><title>%s</title><content><![CDATA[<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
					+ "<!DOCTYPE en-note SYSTEM \"http://xml.evernote.com/pub/enml2.dtd\">\n"
					+ "<en-note></en-note>]]></content><created>20170731T041828Z</created><updated>20170731T041843Z</updated><tag>reviewNotes</tag><tag>2017</tag><tag>%s</tag><note-attributes><author>pengjunkun@gmail.com</author><source>desktop.mac</source><reminder-order>0</reminder-order></note-attributes></note>\n"
					+ "</en-export>\n";

	public static void main(String[] args) throws ParseException
	{
		thisYear = new SimpleDateFormat("yyyy")
				.format(Calendar.getInstance().getTime());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		SimpleDateFormat sdfm = new SimpleDateFormat("MMM");
		Calendar startCal = Calendar.getInstance();
		Calendar endCal = Calendar.getInstance();
		startCal.setTime(sdf.parse(thisYear + ".01.01"));
		endCal.setTime(sdf.parse(thisYear + ".12.31"));
		while (startCal.compareTo(endCal) != 1)
		{
			String currentDateString = sdf.format(startCal.getTime());
			String currentMonthString = sdfm.format(startCal.getTime());
			String fileName =
					"./records/" + thisYear + "/" + currentDateString + ".enex";
			write(fileName, String.format(outPutTemplate, currentDateString,
					currentMonthString));

			startCal.add(Calendar.DAY_OF_MONTH, 1);
		}
	}

	public static void write(String fileName, String content)
	{
		Writer writer = null;

		try
		{
			writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(fileName),
							"utf-8"));
			writer.write(content);
		} catch (IOException ex)
		{
			// report
		} finally
		{
			try
			{
				writer.close();
			} catch (Exception ex)
			{/*ignore*/}
		}

	}
}

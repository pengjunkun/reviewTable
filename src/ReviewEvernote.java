import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.Writer;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.SimpleFormatter;

public class ReviewEvernote
{
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"YYYY/MM/dd E");
	private static SimpleDateFormat reviewSimpleDateFormat = new SimpleDateFormat(
			"MM/dd");
	private static SimpleDateFormat evernoteSimpleDateFormat = new SimpleDateFormat(
			"yyyy.MM.dd");
	private static SimpleDateFormat evernoteSimpleDateFormat2017 = new SimpleDateFormat(
			"MM.dd");
	private static SimpleDateFormat monthSimpleDateFormat = new SimpleDateFormat(
			"yyyy-MM-");
	private static int[] minusDays = { -1, -3, -7, -17, -35 };
	private static Map<String, String> urls = new HashMap<>();
	private static String outPutString =
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
					+ "<!DOCTYPE en-export SYSTEM \"http://xml.evernote.com/pub/evernote-export3.dtd\">\n"
					+ "<en-export export-date=\"20170731T054807Z\" application=\"Evernote\" version=\"Evernote Mac 6.11.1 (455059)\">\n"
					+ "<note><title>%s</title><content><![CDATA[<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n"
					+ "<!DOCTYPE en-note SYSTEM \"http://xml.evernote.com/pub/enml2.dtd\">\n"
					+ "<en-note><table style=\"width: 100%%; border: none; border-collapse: collapse; table-layout: fixed;\"><tbody>";

	private static String oneTDTemplate = "<td style=\"width: 14.285714285714286%%; border: 1px solid rgb(219, 219, 219); padding: 10px; margin: 0px; min-width: 14.285714285714286%%;\"><div><en-todo checked=\"false\"/><a href=\"%s\">%s</a></div></td>";
	private static String thisYear;

	public static void main(String[] args)
	{
		//initial process
		Calendar calStep = Calendar.getInstance();
		int currentYear = calStep.get(Calendar.YEAR);
		thisYear = String.valueOf(currentYear);
		calStep.set(Calendar.DAY_OF_MONTH, 1);
		/**
		 * Field number for <code>get</code> and <code>set</code> indicating the
		 * month. This is a calendar-specific value. The first month of
		 * the year in the Gregorian and Julian calendars is
		 * <code>JANUARY</code> which is 0; the last depends on the number
		 * of months in a year.
		 **/
		calStep.set(Calendar.MONTH, 0);

		getAllUrlMaps();

		while (calStep.get(Calendar.YEAR) == currentYear)
		{
			String op = String.format(outPutString,
					monthSimpleDateFormat.format(calStep.getTime()) + "Plan");

			int currentMonth = calStep.get(Calendar.MONTH);
			while (calStep.get(Calendar.MONTH) == currentMonth)
			{

				op += getOnedayContent(calStep);

				calStep.add(Calendar.DAY_OF_MONTH, 1);
			}
			op += "</tbody></table><div><br/></div></en-note>\n"
					+ "]]></content><created>20170731T052612Z</created><updated>20170731T054743Z</updated><tag>2017</tag><tag>reviewPlan</tag><note-attributes><author>pengjunkun@gmail.com</author><source>desktop.mac</source><reminder-order>0</reminder-order></note-attributes></note>\n"
					+ "</en-export>";
			write("./plantable/" + thisYear + "/" + (currentMonth + 1)
					+ "Plan.enex", op);
		}

	}

	public static void getAllUrlMaps()
	{
		File folder = new File("./urls/");
		for (final File fileEntry : folder.listFiles())
		{
			initialOneYearUrls(fileEntry);
		}
	}

	//use this method to get the maps of [title,url] in one certain year
	public static void initialOneYearUrls(File fileEntry)
	{
		BufferedReader br = null;
		FileReader fr = null;

		try
		{

			fr = new FileReader(fileEntry.getPath());
			br = new BufferedReader(fr);

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null)
			{
				String url = getSplitedString(sCurrentLine, "href=\"", 1);
				String title = "";
				if (!url.isEmpty())
				{
					title = getSplitedString(url, "\">", 1);
					url = getSplitedString(url, "\">", 0);
				}
				if (!title.isEmpty())
				{
					title = getSplitedString(title, "</a></li>", 0);
				}
				urls.put(title, url);
			}

		} catch (IOException e)
		{

			e.printStackTrace();

		} finally
		{

			try
			{

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex)
			{

				ex.printStackTrace();

			}

		}

	}

	public static String getSplitedString(String content, String word,
			int index)
	{
		String[] result = content.split(word);
		String resultString = (result == null || result.length <= index) ?
				"" :
				result[index];
		return resultString;
	}

	private static String getOnedayContent(Calendar calendar)
	{
		//write the first column of today's date
		String onedayContent = "<tr><td style=\"width: 14.285714285714286%%; border: 1px solid rgb(219, 219, 219); padding: 10px; margin: 0px; min-width: 14.285714285714286%%;\"><div><a style=\"color: rgb(0, 0, 0); font-weight: bold;\" href=\"%s\">%s</a></div></td>";
		//second param: href,   third param:name
		onedayContent = String
				.format(onedayContent, getEvernoteUrl(calendar.getTime()),
						simpleDateFormat.format(calendar.getTime()));

		//write review days' dates
		Calendar cloneForOneday = (Calendar) calendar.clone();
		for (int j = 0; j < minusDays.length; j++)
		{
			Calendar cloneForOneColumn = (Calendar) cloneForOneday.clone();
			cloneForOneColumn.add(Calendar.DAY_OF_MONTH, minusDays[j]);
			onedayContent += String.format(oneTDTemplate,
					getEvernoteUrl(cloneForOneColumn.getTime()),
					reviewSimpleDateFormat.format(cloneForOneColumn.getTime()));
			cloneForOneColumn = null;
		}
		onedayContent += "</tr>";
		cloneForOneday = null;
		return onedayContent;
	}


	private static String getEvernoteUrl(Date date)
	{
		if (date.getYear() == 118)
		{
			return urls.get(evernoteSimpleDateFormat.format(date));
		}
		//Todo: this line is just for fiting for 2017 old enex style, will remove it next year
		return urls.get(evernoteSimpleDateFormat2017.format(date));
	}

	private static void log(String s)
	{
		System.out.println(s);
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


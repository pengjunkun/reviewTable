import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;


public class Review
    {
        private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY/MM/dd E");
        private static SimpleDateFormat reviewSimpleDateFormat = new SimpleDateFormat("MM/dd");
        private static int[] minusDays = {-1, -3, -7, -17, -35};

        public static void main(String[] args)
            {
                log("-------------------------------------------------------------------------------------------------------");
                log("    Today        |                              Review Dates                 |    Status");
                Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -2);
                for (int i = 0; i < 16; i++)
                    {
			log("-------------------------------------------------------------------------------------------------------");
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(simpleDateFormat.format(calendar.getTime()));
                        Calendar temp = (Calendar) calendar.clone();
                        List<Date> dates = getReviewDates(temp);
                        for (int k = 0; k < dates.size(); k++)
                            {
                                stringBuilder.append("   |   ");
                                stringBuilder.append(reviewSimpleDateFormat.format(dates.get(k)));
                            }
                        stringBuilder.append("   |   ");
                        log(stringBuilder.toString());
                        stringBuilder = null;

                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                    }
            }

        private static void log(String s)
            {
                System.out.println(s);
            }

        //-1,-3,-7,-10,-25
        private static List<Date> getReviewDates(Calendar cl)
            {
                ArrayList<Date> dates = new ArrayList<>();
                for (int j = 0; j < minusDays.length; j++)
                    {
                        Calendar c = (Calendar)cl.clone();
                        c.add(Calendar.DAY_OF_MONTH, minusDays[j]);
                        dates.add(c.getTime());
                    }
                return dates;
            }
    }


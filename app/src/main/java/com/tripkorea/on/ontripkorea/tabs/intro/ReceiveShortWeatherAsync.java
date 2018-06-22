package com.tripkorea.on.ontripkorea.tabs.intro;

import android.os.AsyncTask;
import android.util.Log;

import com.tripkorea.on.ontripkorea.vo.ShortWeather;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by YangHC on 2018-06-18.
 */

public class ReceiveShortWeatherAsync extends AsyncTask<URL, Integer, Long> {
    private OnPostExecuteListener onPostExecuteListener;
    public void setOnPostExecuteListener(OnPostExecuteListener onPostExecuteListener) {
        this.onPostExecuteListener = onPostExecuteListener;
    }

    ArrayList<ShortWeather> shortWeathers = new ArrayList<>();

        /*TextView celcius;
        ImageView nowW;
        TextView nowWeather;*/

    public ReceiveShortWeatherAsync(){
            /*this.celcius = celcius;
            this.nowW = nowW;
            this.nowWeather = nowWeather;*/
    }

    @Override
    protected Long doInBackground(URL... urls) {
        String url = "http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=1111060000";

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            parseXML(client.newCall(request).execute().body().string());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Long result) {
        Log.e("WIFI","Unreachable1111 : ");

        onPostExecuteListener.onPostExecute(shortWeathers.get(0).getWfKor(), shortWeathers.get(0).getTemp());
        //textView_shortWeather.setText(data);
    }

    void parseXML(String xml) {
        String tagName = "";
//            boolean onHour = false;
//            boolean onDay = false;
        boolean onTem = false;
        boolean onWfKor = false;
//            boolean onPop = false;
        boolean onEnd = false;
        boolean isItemTag1 = false;
        int i = 0;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();

            parser.setInput(new StringReader(xml));

            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    tagName = parser.getName();
                    if (tagName.equals("data")) {
                        shortWeathers.add(new ShortWeather());
                        onEnd = false;
                        isItemTag1 = true;
                    }
                } else if (eventType == XmlPullParser.TEXT && isItemTag1) {
//                        if (tagName.equals("hour") && !onHour) {
//                            shortWeathers.get(i).setHour(parser.getText());
//                            onHour = true;
//                        }
//                        if (tagName.equals("day") && !onDay) {
//                            shortWeathers.get(i).setDay(parser.getText());
//                            onDay = true;
//                        }
                    if (tagName.equals("temp") && !onTem) {
                        shortWeathers.get(i).setTemp(parser.getText());
                        onTem = true;
                    }
                    if (tagName.equals("wfKor") && !onWfKor) {
                        shortWeathers.get(i).setWfKor(parser.getText());
                        onWfKor = true;
                    }
//                        if (tagName.equals("pop") && !onPop) {
//                            shortWeathers.get(i).setPop(parser.getText());
//                            onPop = true;
//                        }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if (tagName.equals("s06") && !onEnd) {
                        i++;
//                            onHour = false;
//                            onDay = false;
                        onTem = false;
                        onWfKor = false;
//                            onPop = false;
                        isItemTag1 = false;
                        onEnd = true;
                    }
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    interface OnPostExecuteListener{
        public void onPostExecute(String weatherImg, String weatherCel);
    }
}

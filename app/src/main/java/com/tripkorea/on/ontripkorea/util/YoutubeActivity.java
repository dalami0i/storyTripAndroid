package com.tripkorea.on.ontripkorea.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.tripkorea.on.ontripkorea.tabs.around.AroundDetailActivity;
import com.tripkorea.on.ontripkorea.vo.attraction.AttrClient;
import com.tripkorea.on.ontripkorea.vo.youtube.YoutubeItems;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Edward Won on 2018-06-12.
 */

public class YoutubeActivity  extends AsyncTask<String, Void,String> {
    Context conte;
    AttrClient attrClient;
    ProgressDialog loginProgress = null;

    public YoutubeActivity(Context context, AttrClient attrClient){
        this.conte = context;
        this.attrClient = attrClient;
    }


    @Override
    protected void onPreExecute() {
        loginProgress = ProgressDialog.show(conte, "", "Move to "+attrClient.title+" ...", true);
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {

        Response response = null; //Response
        OkHttpClient toServer;  //connection
        Log.e("YoutubeActivity","item youtube"+strings[0]);
        String[] youtubeList = strings[0].split(",");
        Log.e("유투브로그","---------------------------"+attrClient.title);//+strings[0]+"https://www.googleapis.com/youtube/v3/videos?id="+strings[1]+"&key=AIzaSyBj5GoJlQ4XzebaG6H2tp_WVuQ03JEOOss&fields=items(snippet(title,description))&part=snippet"
        for(String youtubekey:youtubeList){
//        boolean timeRequest = false;
//        int youtubeStart = 0;
//        int youtubeEnd = 0;
//        for(int i=0; i<MainActivity.youtubeArrayList.size(); i++){
//            if(strings[0].equals(MainActivity.youtubeArrayList.get(i).getContentID()) && MainActivity.youtubeArrayList.get(i).getYoutubeEnd() > 0){
//                timeRequest = true;
//                youtubeStart = MainActivity.youtubeArrayList.get(i).getYoutubeStart();
//                youtubeEnd = MainActivity.youtubeArrayList.get(i).getYoutubeEnd();
//            }
//        }
            try {

                toServer = OkHttpInitSingtonManager.getOkHttpClient();
                Request request = new Request.Builder()
                        .url("https://www.googleapis.com/youtube/v3/videos?id=" + youtubekey + "&key=AIzaSyBj5GoJlQ4XzebaG6H2tp_WVuQ03JEOOss&fields=items(snippet(title,description))&part=snippet")
                        .build();
                response = toServer.newCall(request).execute();
//            if(!timeRequest) {
//                Request request = new Request.Builder()
//                        .url("https://www.googleapis.com/youtube/v3/videos?id=" + strings[1] + "&key=AIzaSyBj5GoJlQ4XzebaG6H2tp_WVuQ03JEOOss&fields=items(snippet(title,description))&part=snippet")
//                        .build();
//
//                response = toServer.newCall(request).execute();
//            }else{
//                Request request = new Request.Builder()
//                        .url("https://www.googleapis.com/youtube/v3/videos?id=" + strings[1] +
//                                "&key=AIzaSyBj5GoJlQ4XzebaG6H2tp_WVuQ03JEOOss&fields=items(snippet(title,description))&part=snippet&start="
//                                +youtubeStart+"&end="+youtubeEnd)
//                        .build();
//
//                response = toServer.newCall(request).execute();
//            }
                String responsedMessage = response.body().string();
//                Log.e("YoutubeActivity",responsedMessage);
                Gson gson = new Gson();
                if (response.isSuccessful()) {
                    YoutubeItems items = gson.fromJson(responsedMessage, YoutubeItems.class);
//                    Log.e("YoutubeActivity",items.items.get(0).snippet.title);
                    AroundDetailActivity.youtubeDetails.add(items.items.get(0).snippet);
                    AroundDetailActivity.youtubeDetails.get(AroundDetailActivity.youtubeDetails.size()-1).youtubeKey = strings[0];

//                for(int i=0; i<MainActivity.totalArrayList.size(); i++){
//                    if(strings[0].equals(MainActivity.totalArrayList.get(i).contentID)){
////                        Log.e("dump3","test2|"+items.items.get(0).snippet.title);
////                        Log.e("youtube","if i: "+i);
//                        MainActivity.totalArrayList.get(i).youtubetitle = items.items.get(0).snippet.title;
////                        Log.e("youtube",MainActivity.totalArrayList.get(i).youtubetitle);
//                        MainActivity.totalArrayList.get(i).youtubediscription = items.items.get(0).snippet.description;
////                        Log.e("Icandoit",MainActivity.totalArrayList.get(i).title+" "+MainActivity.totalArrayList.get(i).youtubeTitle);
//                        break;
//                    }
//                }

                }

            }catch (Exception e){
                Log.e("Youtube Parsing error", e.toString());
            } finally {
                if (response != null) {
                    response.close();
                }
            }
        }

        return "";
    }


    @Override
    protected void onPostExecute(String detail) {
        super.onPostExecute(detail);

        Intent intent = new Intent(conte, AroundDetailActivity.class);
        intent.putExtra("attraction", attrClient);
        conte.startActivity(intent);
        loginProgress.dismiss();

    }
}

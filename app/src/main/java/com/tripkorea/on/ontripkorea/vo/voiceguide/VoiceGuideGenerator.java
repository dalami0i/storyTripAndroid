/*
package com.tripkorea.on.ontripkorea.vo.voiceguide;

import android.util.Log;

import com.tripkorea.on.ontripkorea.R;
import com.tripkorea.on.ontripkorea.util.MyApplication;

import java.util.Locale;

*/
/**
 * Created by Edward Won on 2018-06-12.
 *//*


public class VoiceGuideGenerator {

    public VoiceGuide voiceGuideGenerator(Locale locale){
        VoiceGuide VoiceGuide = new VoiceGuide();
        String usinglanguage = locale.getDisplayLanguage();
        if(usinglanguage.equals("한국어") ){
            VoiceGuide.map_img_url = "";
        }else{
            VoiceGuide.map_img_url = "";
        }
        VoiceGuide = generatorLocList(VoiceGuide);

        return VoiceGuide;
    }

    private VoiceGuide generatorLocList(VoiceGuide VoiceGuide){
        VoiceGuideLocation changMain = new VoiceGuideLocation();
        changMain.voiceguideid = "changMain";
        changMain = generatorLoc(changMain);
        VoiceGuideLocation donhwa = new VoiceGuideLocation();
        donhwa.voiceguideid = "donhwa";
        donhwa = generatorLoc(donhwa);
        VoiceGuideLocation geumcheon = new VoiceGuideLocation();
        geumcheon.voiceguideid = "geumcheon";
        geumcheon = generatorLoc(geumcheon);
        VoiceGuideLocation jinseon = new VoiceGuideLocation();
        jinseon.voiceguideid = "jinseon";
        jinseon = generatorLoc(jinseon);
        VoiceGuideLocation inmunyard = new VoiceGuideLocation();
        inmunyard.voiceguideid = "inmunyard";
        inmunyard = generatorLoc(inmunyard);
        VoiceGuideLocation inmun = new VoiceGuideLocation();
        inmun.voiceguideid = "inmun";
        inmun = generatorLoc(inmun);
        VoiceGuideLocation inyard = new VoiceGuideLocation();
        inyard.voiceguideid = "inyard";
        inyard = generatorLoc(inyard);
        VoiceGuideLocation injeong = new VoiceGuideLocation();
        injeong.voiceguideid = "injeong";
        injeong = generatorLoc(injeong);
        VoiceGuideLocation seonjeong = new VoiceGuideLocation();
        seonjeong.voiceguideid = "seonjeong";
        seonjeong = generatorLoc(seonjeong);
        VoiceGuideLocation heejeong = new VoiceGuideLocation();
        heejeong.voiceguideid = "heejeong";
        heejeong = generatorLoc(heejeong);
        VoiceGuideLocation heejeongback = new VoiceGuideLocation();
        heejeongback.voiceguideid = "heejeongback";
        heejeongback = generatorLoc(heejeongback);
        VoiceGuideLocation daejo = new VoiceGuideLocation();
        daejo.voiceguideid = "daejo";
        daejo = generatorLoc(daejo);
        VoiceGuideLocation soora = new VoiceGuideLocation();
        soora.voiceguideid = "soora";
        soora = generatorLoc(soora);
        VoiceGuideLocation gyunghoon = new VoiceGuideLocation();
        gyunghoon.voiceguideid = "gyunghoon";
        gyunghoon = generatorLoc(gyunghoon);
        VoiceGuideLocation seongjeong = new VoiceGuideLocation();
        seongjeong.voiceguideid = "seongjeong";
        seongjeong = generatorLoc(seongjeong);
        VoiceGuideLocation nakseon = new VoiceGuideLocation();
        nakseon.voiceguideid = "nakseon";
        nakseon = generatorLoc(nakseon);
        VoiceGuideLocation seokbok = new VoiceGuideLocation();
        seokbok.voiceguideid = "seokbok";
        seokbok = generatorLoc(seokbok);
        VoiceGuideLocation okdang = new VoiceGuideLocation();
        okdang.voiceguideid = "okdang";
        okdang = generatorLoc(okdang);
        VoiceGuideLocation gyujang = new VoiceGuideLocation();
        gyujang.voiceguideid = "gyujang";
        gyujang = generatorLoc(gyujang);

        VoiceGuide.locList.add(changMain);
        VoiceGuide.locList.add(donhwa);
        VoiceGuide.locList.add(geumcheon);
        VoiceGuide.locList.add(jinseon);
        VoiceGuide.locList.add(inmunyard);
        VoiceGuide.locList.add(inmun);
        VoiceGuide.locList.add(inyard);
        VoiceGuide.locList.add(injeong);
        VoiceGuide.locList.add(seonjeong);
        VoiceGuide.locList.add(heejeong);
        VoiceGuide.locList.add(heejeongback);
        VoiceGuide.locList.add(daejo);
        VoiceGuide.locList.add(soora);
        VoiceGuide.locList.add(gyunghoon);
        VoiceGuide.locList.add(seongjeong);
        VoiceGuide.locList.add(nakseon);
        VoiceGuide.locList.add(seokbok);
        VoiceGuide.locList.add(okdang);
        VoiceGuide.locList.add(gyujang);

        return VoiceGuide;
    }

    private VoiceGuideLocation generatorLoc(VoiceGuideLocation VoiceGuideLocation){
        switch(VoiceGuideLocation.voiceguideid){
            case "donhwa":
                VoiceGuideLocation = makeDon(VoiceGuideLocation);
                break;
            case "geumcheon":
                VoiceGuideLocation = makeGeum(VoiceGuideLocation);
                break;
            case "jinseon":
                VoiceGuideLocation = makeJin(VoiceGuideLocation);
                break;
            case "inmunyard":
                VoiceGuideLocation = makeInmunyard(VoiceGuideLocation);
                break;
            case "inmun":
                VoiceGuideLocation = makeInmun(VoiceGuideLocation);
                break;
            case "inyard":
                VoiceGuideLocation = makeInyard(VoiceGuideLocation);
                break;
            case "injeong":
                VoiceGuideLocation = makeInJeong(VoiceGuideLocation);
                break;
            case "seonjeong":
                VoiceGuideLocation = makeSeonjeong(VoiceGuideLocation);
                break;
            case "heejeong":
                VoiceGuideLocation = makeHeejeong(VoiceGuideLocation);
                break;
            case "heejeongback":
                VoiceGuideLocation = makeHeejeongBack(VoiceGuideLocation);
                break;
            case "daejo":
                VoiceGuideLocation = makeDaejo(VoiceGuideLocation);
                break;
            case "soora":
                VoiceGuideLocation = makeSoora(VoiceGuideLocation);
                break;
            case "gyunghoon":
                VoiceGuideLocation = makeGyeonghoon(VoiceGuideLocation);
                break;
            case "seongjeong":
                VoiceGuideLocation = makeSeongjeong(VoiceGuideLocation);
                break;
            case "nakseon":
                VoiceGuideLocation = makeNakseon(VoiceGuideLocation);
                break;
            case "seokbok":
                VoiceGuideLocation = makeSeokbok(VoiceGuideLocation);
                break;
            case "okdang":
                VoiceGuideLocation = makeOkdang(VoiceGuideLocation);
                break;
            case "gyujang":
                VoiceGuideLocation = makeGyujang(VoiceGuideLocation);
                break;
        }

        Log.e("GUIDE_GENERATOR", "location_title : " + VoiceGuideLocation.location_title);
        Log.e("GUIDE_GENERATOR", "title : " + VoiceGuideLocation.voice_title);
        Log.e("GUIDE_GENERATOR", "lat : " + ((Double.parseDouble(VoiceGuideLocation.voice_loc_south) + Double.parseDouble(VoiceGuideLocation.voice_loc_north)) / 2));
        Log.e("GUIDE_GENERATOR", "lon : " + ((Double.parseDouble(VoiceGuideLocation.voice_loc_west) + Double.parseDouble(VoiceGuideLocation.voice_loc_east)) / 2));
        Log.e("GUIDE_GENERATOR", "voice address : " + VoiceGuideLocation.voice_addr);
        for(int i=0;i<VoiceGuideLocation.location_img_size;i++){
            Log.e("GUIDE_GENERATOR", "img "+i + " : " + VoiceGuideLocation.voiceGuideImg.get(i).voiceimgtext);
            Log.e("GUIDE_GENERATOR", "img "+i + " : " + VoiceGuideLocation.voiceGuideImg.get(i).imgAddr);
        }
        return VoiceGuideLocation;
    }

    private VoiceGuideLocation makeDon(VoiceGuideLocation VoiceGuideLocation){
        VoiceGuideLocation.location_title = MyApplication.getContext().getString(R.string.dontitle);
        VoiceGuideLocation.voice_addr = MyApplication.getContext().getString(R.string.donaudioaddress);
        VoiceGuideLocation.voice_title = MyApplication.getContext().getString(R.string.donsubtitle);
        VoiceGuideLocation.voice_loc_east ="126.990218";
        VoiceGuideLocation.voice_loc_north="37.577921";
        VoiceGuideLocation.voice_loc_south="37.577458";
        VoiceGuideLocation.voice_loc_west="126.989633";
        VoiceGuideLocation.location_img_size = 16;
        VoiceGuideImage donimg1 = new VoiceGuideImage();
        donimg1.imgAddr = MyApplication.getContext().getString(R.string.donUrlG1);
        donimg1.voiceimgtext = MyApplication.getContext().getString(R.string.donUrlG1_);
        VoiceGuideLocation.voiceGuideImg.add(donimg1);
        VoiceGuideImage donimg2 = new VoiceGuideImage();
        donimg2.imgAddr = MyApplication.getContext().getString(R.string.donUrlG2);
        donimg2.voiceimgtext = MyApplication.getContext().getString(R.string.donUrlG2_);
        VoiceGuideLocation.voiceGuideImg.add(donimg2);
        VoiceGuideImage donimg3 = new VoiceGuideImage();
        donimg3.imgAddr = MyApplication.getContext().getString(R.string.donUrlG3);
        donimg3.voiceimgtext = MyApplication.getContext().getString(R.string.donUrlG3_);
        VoiceGuideLocation.voiceGuideImg.add(donimg3);
        VoiceGuideImage donimg4 = new VoiceGuideImage();
        donimg4.imgAddr = MyApplication.getContext().getString(R.string.donUrlG4);
        donimg4.voiceimgtext = MyApplication.getContext().getString(R.string.donUrlG4_);
        VoiceGuideLocation.voiceGuideImg.add(donimg4);
        VoiceGuideImage donimg5 = new VoiceGuideImage();
        donimg5.imgAddr = MyApplication.getContext().getString(R.string.donUrlG5);
        donimg5.voiceimgtext = MyApplication.getContext().getString(R.string.donUrlG5_);
        VoiceGuideLocation.voiceGuideImg.add(donimg5);
        VoiceGuideImage donimg6 = new VoiceGuideImage();
        donimg6.imgAddr = MyApplication.getContext().getString(R.string.donUrlG6);
        donimg6.voiceimgtext = MyApplication.getContext().getString(R.string.donUrlG6_);
        VoiceGuideLocation.voiceGuideImg.add(donimg6);
        VoiceGuideImage donimg7 = new VoiceGuideImage();
        donimg7.imgAddr = MyApplication.getContext().getString(R.string.donUrlG7);
        donimg7.voiceimgtext = MyApplication.getContext().getString(R.string.donUrlG7_);
        VoiceGuideLocation.voiceGuideImg.add(donimg7);
        VoiceGuideImage donimg8 = new VoiceGuideImage();
        donimg8.imgAddr = MyApplication.getContext().getString(R.string.donUrlG8);
        donimg8.voiceimgtext = MyApplication.getContext().getString(R.string.donUrlG8_);
        VoiceGuideLocation.voiceGuideImg.add(donimg8);
        VoiceGuideImage donimg9 = new VoiceGuideImage();
        donimg9.imgAddr = MyApplication.getContext().getString(R.string.donUrlG9);
        donimg9.voiceimgtext = MyApplication.getContext().getString(R.string.donUrlG9_);
        VoiceGuideLocation.voiceGuideImg.add(donimg9);
        VoiceGuideImage donimg10 = new VoiceGuideImage();
        donimg10.imgAddr = MyApplication.getContext().getString(R.string.donUrlG10);
        donimg10.voiceimgtext = MyApplication.getContext().getString(R.string.donUrlG10_);
        VoiceGuideLocation.voiceGuideImg.add(donimg10);
        VoiceGuideImage donimg11 = new VoiceGuideImage();
        donimg11.imgAddr = MyApplication.getContext().getString(R.string.donUrlG11);
        donimg11.voiceimgtext = MyApplication.getContext().getString(R.string.donUrlG11_);
        VoiceGuideLocation.voiceGuideImg.add(donimg11);
        VoiceGuideImage donimg12 = new VoiceGuideImage();
        donimg12.imgAddr = MyApplication.getContext().getString(R.string.donUrlG12);
        donimg12.voiceimgtext = MyApplication.getContext().getString(R.string.donUrlG12_);
        VoiceGuideLocation.voiceGuideImg.add(donimg12);
        VoiceGuideImage donimg13 = new VoiceGuideImage();
        donimg13.imgAddr = MyApplication.getContext().getString(R.string.donUrlG13);
        donimg13.voiceimgtext = MyApplication.getContext().getString(R.string.donUrlG13_);
        VoiceGuideLocation.voiceGuideImg.add(donimg13);
        VoiceGuideImage donimg14 = new VoiceGuideImage();
        donimg14.imgAddr = MyApplication.getContext().getString(R.string.donUrlG14);
        donimg14.voiceimgtext = MyApplication.getContext().getString(R.string.donUrlG14_);
        VoiceGuideLocation.voiceGuideImg.add(donimg14);
        VoiceGuideImage donimg15 = new VoiceGuideImage();
        donimg15.imgAddr = MyApplication.getContext().getString(R.string.donUrlG15);
        donimg15.voiceimgtext =MyApplication.getContext().getString(R.string.donUrlG15_);
        VoiceGuideLocation.voiceGuideImg.add(donimg15);
        VoiceGuideImage donimg16 = new VoiceGuideImage();
        donimg16.imgAddr = MyApplication.getContext().getString(R.string.donUrlG16);
        donimg16.voiceimgtext = MyApplication.getContext().getString(R.string.donUrlG16_);
        VoiceGuideLocation.voiceGuideImg.add(donimg16);
        return VoiceGuideLocation;
    }

    private VoiceGuideLocation makeGeum(VoiceGuideLocation VoiceGuideLocation){

        VoiceGuideLocation.location_title = MyApplication.getContext().getString(R.string.geumtitle);
        VoiceGuideLocation.voice_addr = MyApplication.getContext().getString(R.string.geumaudioaddress);
        VoiceGuideLocation.voice_title = MyApplication.getContext().getString(R.string.geumsubtitle);
        VoiceGuideLocation.voice_loc_east ="126.990136";
        VoiceGuideLocation.voice_loc_north="37.578535";
        VoiceGuideLocation.voice_loc_south="37.578336";
        VoiceGuideLocation.voice_loc_west="126.989828";
        VoiceGuideLocation.location_img_size = 23;
        VoiceGuideImage geumimg1 = new VoiceGuideImage();
        geumimg1.imgAddr = MyApplication.getContext().getString(R.string.geumUrlG1);
        geumimg1.voiceimgtext = MyApplication.getContext().getString(R.string.geumUrlG1_);
        VoiceGuideLocation.voiceGuideImg.add(geumimg1);
        VoiceGuideImage geumimg2 = new VoiceGuideImage();
        geumimg2.imgAddr = MyApplication.getContext().getString(R.string.geumUrlG2);
        geumimg2.voiceimgtext = MyApplication.getContext().getString(R.string.geumUrlG2_);
        VoiceGuideLocation.voiceGuideImg.add(geumimg2);
        VoiceGuideImage geumimg3 = new VoiceGuideImage();
        geumimg3.imgAddr = MyApplication.getContext().getString(R.string.geumUrlG3);
        geumimg3.voiceimgtext = MyApplication.getContext().getString(R.string.geumUrlG3_);
        VoiceGuideLocation.voiceGuideImg.add(geumimg3);
        VoiceGuideImage geumimg4 = new VoiceGuideImage();
        geumimg4.imgAddr = MyApplication.getContext().getString(R.string.geumUrlG4);
        geumimg4.voiceimgtext = MyApplication.getContext().getString(R.string.geumUrlG4_);
        VoiceGuideLocation.voiceGuideImg.add(geumimg4);
        VoiceGuideImage geumimg5 = new VoiceGuideImage();
        geumimg5.imgAddr = MyApplication.getContext().getString(R.string.geumUrlG5);
        geumimg5.voiceimgtext = MyApplication.getContext().getString(R.string.geumUrlG5_);
        VoiceGuideLocation.voiceGuideImg.add(geumimg5);
        VoiceGuideImage geumimg6 = new VoiceGuideImage();
        geumimg6.imgAddr = MyApplication.getContext().getString(R.string.geumUrlG6);
        geumimg6.voiceimgtext = MyApplication.getContext().getString(R.string.geumUrlG6_);
        VoiceGuideLocation.voiceGuideImg.add(geumimg6);
        VoiceGuideImage geumimg7 = new VoiceGuideImage();
        geumimg7.imgAddr = MyApplication.getContext().getString(R.string.geumUrlG7);
        geumimg7.voiceimgtext = MyApplication.getContext().getString(R.string.geumUrlG7_);
        VoiceGuideLocation.voiceGuideImg.add(geumimg7);
        VoiceGuideImage geumimg8 = new VoiceGuideImage();
        geumimg8.imgAddr = MyApplication.getContext().getString(R.string.geumUrlG8);
        geumimg8.voiceimgtext = MyApplication.getContext().getString(R.string.geumUrlG8_);
        VoiceGuideLocation.voiceGuideImg.add(geumimg8);
        VoiceGuideImage geumimg9 = new VoiceGuideImage();
        geumimg9.imgAddr = MyApplication.getContext().getString(R.string.geumUrlG9);
        geumimg9.voiceimgtext = MyApplication.getContext().getString(R.string.geumUrlG9_);
        VoiceGuideLocation.voiceGuideImg.add(geumimg9);
        VoiceGuideImage geumimg10 = new VoiceGuideImage();
        geumimg10.imgAddr = MyApplication.getContext().getString(R.string.geumUrlG10);
        geumimg10.voiceimgtext = MyApplication.getContext().getString(R.string.geumUrlG10_);
        VoiceGuideLocation.voiceGuideImg.add(geumimg10);
        VoiceGuideImage geumimg11 = new VoiceGuideImage();
        geumimg11.imgAddr = MyApplication.getContext().getString(R.string.geumUrlG11);
        geumimg11.voiceimgtext = MyApplication.getContext().getString(R.string.geumUrlG11_);
        VoiceGuideLocation.voiceGuideImg.add(geumimg11);
        VoiceGuideImage geumimg12 = new VoiceGuideImage();
        geumimg12.imgAddr = MyApplication.getContext().getString(R.string.geumUrlG12);
        geumimg12.voiceimgtext = MyApplication.getContext().getString(R.string.geumUrlG12_);
        VoiceGuideLocation.voiceGuideImg.add(geumimg12);
        VoiceGuideImage geumimg13 = new VoiceGuideImage();
        geumimg13.imgAddr = MyApplication.getContext().getString(R.string.geumUrlG13);
        geumimg13.voiceimgtext = MyApplication.getContext().getString(R.string.geumUrlG13_);
        VoiceGuideLocation.voiceGuideImg.add(geumimg13);
        VoiceGuideImage geumimg14 = new VoiceGuideImage();
        geumimg14.imgAddr = MyApplication.getContext().getString(R.string.geumUrlG14);
        geumimg14.voiceimgtext = MyApplication.getContext().getString(R.string.geumUrlG14_);
        VoiceGuideLocation.voiceGuideImg.add(geumimg14);
        VoiceGuideImage geumimg15 = new VoiceGuideImage();
        geumimg15.imgAddr = MyApplication.getContext().getString(R.string.geumUrlG15);
        geumimg15.voiceimgtext = MyApplication.getContext().getString(R.string.geumUrlG15_);
        VoiceGuideLocation.voiceGuideImg.add(geumimg15);
        VoiceGuideImage geumimg16 = new VoiceGuideImage();
        geumimg16.imgAddr = MyApplication.getContext().getString(R.string.geumUrlG16);
        geumimg16.voiceimgtext = MyApplication.getContext().getString(R.string.geumUrlG16_);
        VoiceGuideLocation.voiceGuideImg.add(geumimg16);
        VoiceGuideImage geumimg17 = new VoiceGuideImage();
        geumimg17.imgAddr = MyApplication.getContext().getString(R.string.geumUrlG17);
        geumimg17.voiceimgtext = MyApplication.getContext().getString(R.string.geumUrlG17_);
        VoiceGuideLocation.voiceGuideImg.add(geumimg17);
        VoiceGuideImage geumimg18 = new VoiceGuideImage();
        geumimg18.imgAddr = MyApplication.getContext().getString(R.string.geumUrlG18);
        geumimg18.voiceimgtext = MyApplication.getContext().getString(R.string.geumUrlG18_);
        VoiceGuideLocation.voiceGuideImg.add(geumimg18);
        VoiceGuideImage geumimg19 = new VoiceGuideImage();
        geumimg19.imgAddr = MyApplication.getContext().getString(R.string.geumUrlG19);
        geumimg19.voiceimgtext = MyApplication.getContext().getString(R.string.geumUrlG19_);
        VoiceGuideLocation.voiceGuideImg.add(geumimg19);
        VoiceGuideImage geumimg20 = new VoiceGuideImage();
        geumimg20.imgAddr = MyApplication.getContext().getString(R.string.geumUrlG20);
        geumimg20.voiceimgtext = MyApplication.getContext().getString(R.string.geumUrlG20_);
        VoiceGuideLocation.voiceGuideImg.add(geumimg20);
        VoiceGuideImage geumimg21 = new VoiceGuideImage();
        geumimg21.imgAddr = MyApplication.getContext().getString(R.string.geumUrlG21);
        geumimg21.voiceimgtext = MyApplication.getContext().getString(R.string.geumUrlG21_);
        VoiceGuideLocation.voiceGuideImg.add(geumimg21);
        VoiceGuideImage geumimg22 = new VoiceGuideImage();
        geumimg22.imgAddr = MyApplication.getContext().getString(R.string.geumUrlG22);
        geumimg22.voiceimgtext = MyApplication.getContext().getString(R.string.geumUrlG22_);
        VoiceGuideLocation.voiceGuideImg.add(geumimg22);
        VoiceGuideImage geumimg23 = new VoiceGuideImage();
        geumimg23.imgAddr = MyApplication.getContext().getString(R.string.geumUrlG23);
        geumimg23.voiceimgtext = MyApplication.getContext().getString(R.string.geumUrlG23_);
        VoiceGuideLocation.voiceGuideImg.add(geumimg23);


        return VoiceGuideLocation;
    }

    private VoiceGuideLocation makeJin(VoiceGuideLocation VoiceGuideLocation){
        VoiceGuideLocation.location_title = MyApplication.getContext().getString(R.string.jintitle);
        VoiceGuideLocation.voice_addr = MyApplication.getContext().getString(R.string.jinaudioaddress);
        VoiceGuideLocation.voice_title = MyApplication.getContext().getString(R.string.jinsubtitle);
        VoiceGuideLocation.voice_loc_east ="126.990484";
        VoiceGuideLocation.voice_loc_north="37.578572";
        VoiceGuideLocation.voice_loc_south="37.578348";
        VoiceGuideLocation.voice_loc_west="126.990235";
        VoiceGuideLocation.location_img_size = 7;
        VoiceGuideImage jinimg1 = new VoiceGuideImage();
        jinimg1.imgAddr = MyApplication.getContext().getString(R.string.jinUrlG1);
        jinimg1.voiceimgtext = MyApplication.getContext().getString(R.string.jinUrlG1_);
        VoiceGuideLocation.voiceGuideImg.add(jinimg1);
        VoiceGuideImage jinimg2 = new VoiceGuideImage();
        jinimg2.imgAddr = MyApplication.getContext().getString(R.string.jinUrlG2);
        jinimg2.voiceimgtext = MyApplication.getContext().getString(R.string.jinUrlG2_);
        VoiceGuideLocation.voiceGuideImg.add(jinimg2);
        VoiceGuideImage jinimg3 = new VoiceGuideImage();
        jinimg3.imgAddr = MyApplication.getContext().getString(R.string.jinUrlG3);
        jinimg3.voiceimgtext = MyApplication.getContext().getString(R.string.jinUrlG3_);
        VoiceGuideLocation.voiceGuideImg.add(jinimg3);
        VoiceGuideImage jinimg4 = new VoiceGuideImage();
        jinimg4.imgAddr = MyApplication.getContext().getString(R.string.jinUrlG4);
        jinimg4.voiceimgtext = MyApplication.getContext().getString(R.string.jinUrlG4_);
        VoiceGuideLocation.voiceGuideImg.add(jinimg4);
        VoiceGuideImage jinimg5 = new VoiceGuideImage();
        jinimg5.imgAddr = MyApplication.getContext().getString(R.string.jinUrlG5);
        jinimg5.voiceimgtext = MyApplication.getContext().getString(R.string.jinUrlG5_);
        VoiceGuideLocation.voiceGuideImg.add(jinimg5);
        VoiceGuideImage jinimg6 = new VoiceGuideImage();
        jinimg6.imgAddr = MyApplication.getContext().getString(R.string.jinUrlG6);
        jinimg6.voiceimgtext = MyApplication.getContext().getString(R.string.jinUrlG6_);
        VoiceGuideLocation.voiceGuideImg.add(jinimg6);
        VoiceGuideImage jinimg7 = new VoiceGuideImage();
        jinimg7.imgAddr = MyApplication.getContext().getString(R.string.jinUrlG7);
        jinimg7.voiceimgtext = MyApplication.getContext().getString(R.string.jinUrlG7_);
        VoiceGuideLocation.voiceGuideImg.add(jinimg7);
        VoiceGuideImage jinimg8 = new VoiceGuideImage();
        jinimg8.imgAddr = MyApplication.getContext().getString(R.string.jinUrlG8);
        jinimg8.voiceimgtext = MyApplication.getContext().getString(R.string.jinUrlG8_);
        VoiceGuideLocation.voiceGuideImg.add(jinimg8);
        VoiceGuideImage jinimg9 = new VoiceGuideImage();
        jinimg9.imgAddr = MyApplication.getContext().getString(R.string.jinUrlG9);
        jinimg9.voiceimgtext = MyApplication.getContext().getString(R.string.jinUrlG9_);
        VoiceGuideLocation.voiceGuideImg.add(jinimg9);
        VoiceGuideImage jinimg10 = new VoiceGuideImage();
        jinimg10.imgAddr = MyApplication.getContext().getString(R.string.jinUrlG10);
        jinimg10.voiceimgtext = MyApplication.getContext().getString(R.string.jinUrlG10_);
        VoiceGuideLocation.voiceGuideImg.add(jinimg10);

        return VoiceGuideLocation;
    }

    private VoiceGuideLocation makeInmunyard(VoiceGuideLocation VoiceGuideLocation){

        VoiceGuideLocation.location_title = MyApplication.getContext().getString(R.string.inmunyardtitle);
        VoiceGuideLocation.voice_addr = MyApplication.getContext().getString(R.string.inmunaudioaddress);
        VoiceGuideLocation.voice_title = MyApplication.getContext().getString(R.string.inmunyardsubtitle);
        VoiceGuideLocation.voice_loc_east ="126.991493";
        VoiceGuideLocation.voice_loc_north="37.578572";
        VoiceGuideLocation.voice_loc_south="37.578348";
        VoiceGuideLocation.voice_loc_west="126.990737";
        VoiceGuideLocation.location_img_size = 7;
        VoiceGuideImage inmunyardimg1 = new VoiceGuideImage();
        inmunyardimg1.imgAddr = MyApplication.getContext().getString(R.string.inmunyardUrlG1);
        inmunyardimg1.voiceimgtext = MyApplication.getContext().getString(R.string.inmunyardUrlG1_);
        VoiceGuideLocation.voiceGuideImg.add(inmunyardimg1);
        VoiceGuideImage inmunyardimg2 = new VoiceGuideImage();
        inmunyardimg2.imgAddr = MyApplication.getContext().getString(R.string.inmunyardUrlG2);
        inmunyardimg2.voiceimgtext = MyApplication.getContext().getString(R.string.inmunyardUrlG2_);
        VoiceGuideLocation.voiceGuideImg.add(inmunyardimg2);
        VoiceGuideImage inmunyardimg3 = new VoiceGuideImage();
        inmunyardimg3.imgAddr = MyApplication.getContext().getString(R.string.inmunyardUrlG3);
        inmunyardimg3.voiceimgtext = MyApplication.getContext().getString(R.string.inmunyardUrlG3_);
        VoiceGuideLocation.voiceGuideImg.add(inmunyardimg3);
        VoiceGuideImage inmunyardimg4 = new VoiceGuideImage();
        inmunyardimg4.imgAddr = MyApplication.getContext().getString(R.string.inmunyardUrlG4);
        inmunyardimg4.voiceimgtext = MyApplication.getContext().getString(R.string.inmunyardUrlG4_);
        VoiceGuideLocation.voiceGuideImg.add(inmunyardimg4);
        VoiceGuideImage inmunyardimg5 = new VoiceGuideImage();
        inmunyardimg5.imgAddr = MyApplication.getContext().getString(R.string.inmunyardUrlG5);
        inmunyardimg5.voiceimgtext = MyApplication.getContext().getString(R.string.inmunyardUrlG5_);
        VoiceGuideLocation.voiceGuideImg.add(inmunyardimg5);
        VoiceGuideImage inmunyardimg6 = new VoiceGuideImage();
        inmunyardimg6.imgAddr = MyApplication.getContext().getString(R.string.inmunyardUrlG6);
        inmunyardimg6.voiceimgtext = MyApplication.getContext().getString(R.string.inmunyardUrlG6_);
        VoiceGuideLocation.voiceGuideImg.add(inmunyardimg6);
        VoiceGuideImage inmunyardimg7 = new VoiceGuideImage();
        inmunyardimg7.imgAddr = MyApplication.getContext().getString(R.string.inmunyardUrlG7);
        inmunyardimg7.voiceimgtext = MyApplication.getContext().getString(R.string.inmunyardUrlG7_);
        VoiceGuideLocation.voiceGuideImg.add(inmunyardimg7);


        return VoiceGuideLocation;
    }

    private VoiceGuideLocation makeInmun(VoiceGuideLocation VoiceGuideLocation){
        VoiceGuideLocation.location_title = MyApplication.getContext().getString(R.string.inmuntitle);
        VoiceGuideLocation.voice_addr = MyApplication.getContext().getString(R.string.inmunaudioaddress);
        VoiceGuideLocation.voice_title = MyApplication.getContext().getString(R.string.inmunsubtitle);
        VoiceGuideLocation.voice_loc_east ="126.991435";
        VoiceGuideLocation.voice_loc_north="37.57882";
        VoiceGuideLocation.voice_loc_south="37.57856";
        VoiceGuideLocation.voice_loc_west="126.990864";
        VoiceGuideLocation.location_img_size = 5;
        VoiceGuideImage inmunimg1 = new VoiceGuideImage();
        inmunimg1.imgAddr = MyApplication.getContext().getString(R.string.inmunUrlG1);
        inmunimg1.voiceimgtext = MyApplication.getContext().getString(R.string.inmunUrlG1_);
        VoiceGuideLocation.voiceGuideImg.add(inmunimg1);
        VoiceGuideImage inmunimg2 = new VoiceGuideImage();
        inmunimg2.imgAddr = MyApplication.getContext().getString(R.string.inmunUrlG2);
        inmunimg2.voiceimgtext = MyApplication.getContext().getString(R.string.inmunUrlG2_);
        VoiceGuideLocation.voiceGuideImg.add(inmunimg2);
        VoiceGuideImage inmunimg3 = new VoiceGuideImage();
        inmunimg3.imgAddr = MyApplication.getContext().getString(R.string.inmunUrlG3);
        inmunimg3.voiceimgtext = MyApplication.getContext().getString(R.string.inmunUrlG3_);
        VoiceGuideLocation.voiceGuideImg.add(inmunimg3);
        VoiceGuideImage inmunimg4 = new VoiceGuideImage();
        inmunimg4.imgAddr = MyApplication.getContext().getString(R.string.inmunUrlG4);
        inmunimg4.voiceimgtext = MyApplication.getContext().getString(R.string.inmunUrlG4_);
        VoiceGuideLocation.voiceGuideImg.add(inmunimg4);
        VoiceGuideImage inmunimg5 = new VoiceGuideImage();
        inmunimg5.imgAddr = MyApplication.getContext().getString(R.string.inmunUrlG5);
        inmunimg5.voiceimgtext = MyApplication.getContext().getString(R.string.inmunUrlG5_);
        VoiceGuideLocation.voiceGuideImg.add(inmunimg5);

        return VoiceGuideLocation;
    }


    private VoiceGuideLocation makeInyard(VoiceGuideLocation VoiceGuideLocation){
        VoiceGuideLocation.location_title = MyApplication.getContext().getString(R.string.inyardtitle);
        VoiceGuideLocation.voice_addr = MyApplication.getContext().getString(R.string.inyardaudioaddress);
        VoiceGuideLocation.voice_title = MyApplication.getContext().getString(R.string.inyardsubtitle);
        VoiceGuideLocation.voice_loc_east ="126.991354";
        VoiceGuideLocation.voice_loc_north="37.579303";
        VoiceGuideLocation.voice_loc_south="37.578747";
        VoiceGuideLocation.voice_loc_west="126.990823";
        VoiceGuideLocation.location_img_size = 12;
        VoiceGuideImage inyardimg1 = new VoiceGuideImage();
        inyardimg1.imgAddr = MyApplication.getContext().getString(R.string.inyardUrlG1);
        inyardimg1.voiceimgtext = MyApplication.getContext().getString(R.string.inyardUrlG1_);
        VoiceGuideLocation.voiceGuideImg.add(inyardimg1);
        VoiceGuideImage inyardimg2 = new VoiceGuideImage();
        inyardimg2.imgAddr = MyApplication.getContext().getString(R.string.inyardUrlG2);
        inyardimg2.voiceimgtext = MyApplication.getContext().getString(R.string.inyardUrlG2_);
        VoiceGuideLocation.voiceGuideImg.add(inyardimg2);
        VoiceGuideImage inyardimg3 = new VoiceGuideImage();
        inyardimg3.imgAddr = MyApplication.getContext().getString(R.string.inyardUrlG3);
        inyardimg3.voiceimgtext = MyApplication.getContext().getString(R.string.inyardUrlG3_);
        VoiceGuideLocation.voiceGuideImg.add(inyardimg3);
        VoiceGuideImage inyardimg4 = new VoiceGuideImage();
        inyardimg4.imgAddr = MyApplication.getContext().getString(R.string.inyardUrlG4);
        inyardimg4.voiceimgtext = MyApplication.getContext().getString(R.string.inyardUrlG4_);
        VoiceGuideLocation.voiceGuideImg.add(inyardimg4);
        VoiceGuideImage inyardimg5 = new VoiceGuideImage();
        inyardimg5.imgAddr = MyApplication.getContext().getString(R.string.inyardUrlG5);
        inyardimg5.voiceimgtext = MyApplication.getContext().getString(R.string.inyardUrlG5_);
        VoiceGuideLocation.voiceGuideImg.add(inyardimg5);
        VoiceGuideImage inyardimg6 = new VoiceGuideImage();
        inyardimg6.imgAddr = MyApplication.getContext().getString(R.string.inyardUrlG6);
        inyardimg6.voiceimgtext = MyApplication.getContext().getString(R.string.inyardUrlG6_);
        VoiceGuideLocation.voiceGuideImg.add(inyardimg6);
        VoiceGuideImage inyardimg7 = new VoiceGuideImage();
        inyardimg7.imgAddr = MyApplication.getContext().getString(R.string.inyardUrlG7);
        inyardimg7.voiceimgtext = MyApplication.getContext().getString(R.string.inyardUrlG7_);
        VoiceGuideLocation.voiceGuideImg.add(inyardimg7);
        VoiceGuideImage inyardimg8 = new VoiceGuideImage();
        inyardimg8.imgAddr = MyApplication.getContext().getString(R.string.inyardUrlG8);
        inyardimg8.voiceimgtext = MyApplication.getContext().getString(R.string.inyardUrlG8_);
        VoiceGuideLocation.voiceGuideImg.add(inyardimg8);
        VoiceGuideImage inyardimg9 = new VoiceGuideImage();
        inyardimg9.imgAddr = MyApplication.getContext().getString(R.string.inyardUrlG9);
        inyardimg9.voiceimgtext = MyApplication.getContext().getString(R.string.inyardUrlG9_);
        VoiceGuideLocation.voiceGuideImg.add(inyardimg9);
        VoiceGuideImage inyardimg10 = new VoiceGuideImage();
        inyardimg10.imgAddr = MyApplication.getContext().getString(R.string.inyardUrlG10);
        inyardimg10.voiceimgtext = MyApplication.getContext().getString(R.string.inyardUrlG10_);
        VoiceGuideLocation.voiceGuideImg.add(inyardimg10);
        VoiceGuideImage inyardimg11 = new VoiceGuideImage();
        inyardimg11.imgAddr = MyApplication.getContext().getString(R.string.inyardUrlG11);
        inyardimg11.voiceimgtext = MyApplication.getContext().getString(R.string.inyardUrlG11_);
        VoiceGuideLocation.voiceGuideImg.add(inyardimg11);
        VoiceGuideImage inyardimg12 = new VoiceGuideImage();
        inyardimg12.imgAddr = MyApplication.getContext().getString(R.string.inyardUrlG12);
        inyardimg12.voiceimgtext = MyApplication.getContext().getString(R.string.inyardUrlG12_);
        VoiceGuideLocation.voiceGuideImg.add(inyardimg12);

        return VoiceGuideLocation;
    }


    private VoiceGuideLocation makeInJeong(VoiceGuideLocation VoiceGuideLocation){
        VoiceGuideLocation.location_title = MyApplication.getContext().getString(R.string.intitle);
        VoiceGuideLocation.voice_addr = MyApplication.getContext().getString(R.string.inaudioaddress);
        VoiceGuideLocation.voice_title = MyApplication.getContext().getString(R.string.insubtitle);
        VoiceGuideLocation.voice_loc_east ="126.991266";
        VoiceGuideLocation.voice_loc_north="37.579356";
        VoiceGuideLocation.voice_loc_south="37.579269";
        VoiceGuideLocation.voice_loc_west="126.990888";
        VoiceGuideLocation.location_img_size = 9;
        VoiceGuideImage injeongimg1 = new VoiceGuideImage();
        injeongimg1.imgAddr = MyApplication.getContext().getString(R.string.inUrlG1);
        injeongimg1.voiceimgtext = MyApplication.getContext().getString(R.string.inUrlG1_);
        VoiceGuideLocation.voiceGuideImg.add(injeongimg1);
        VoiceGuideImage injeongimg2 = new VoiceGuideImage();
        injeongimg2.imgAddr = MyApplication.getContext().getString(R.string.inUrlG2);
        injeongimg2.voiceimgtext = MyApplication.getContext().getString(R.string.inUrlG2_);
        VoiceGuideLocation.voiceGuideImg.add(injeongimg2);
        VoiceGuideImage injeongimg3 = new VoiceGuideImage();
        injeongimg3.imgAddr = MyApplication.getContext().getString(R.string.inUrlG3);
        injeongimg3.voiceimgtext = MyApplication.getContext().getString(R.string.inUrlG3_);
        VoiceGuideLocation.voiceGuideImg.add(injeongimg3);
        VoiceGuideImage injeongimg4 = new VoiceGuideImage();
        injeongimg4.imgAddr = MyApplication.getContext().getString(R.string.inUrlG4);
        injeongimg4.voiceimgtext = MyApplication.getContext().getString(R.string.inUrlG4_);
        VoiceGuideLocation.voiceGuideImg.add(injeongimg4);
        VoiceGuideImage injeongimg5 = new VoiceGuideImage();
        injeongimg5.imgAddr = MyApplication.getContext().getString(R.string.inUrlG5);
        injeongimg5.voiceimgtext = MyApplication.getContext().getString(R.string.inUrlG5_);
        VoiceGuideLocation.voiceGuideImg.add(injeongimg5);
        VoiceGuideImage injeongimg6 = new VoiceGuideImage();
        injeongimg6.imgAddr = MyApplication.getContext().getString(R.string.inUrlG6);
        injeongimg6.voiceimgtext = MyApplication.getContext().getString(R.string.inUrlG6_);
        VoiceGuideLocation.voiceGuideImg.add(injeongimg6);
        VoiceGuideImage injeongimg7 = new VoiceGuideImage();
        injeongimg7.imgAddr = MyApplication.getContext().getString(R.string.inUrlG7);
        injeongimg7.voiceimgtext = MyApplication.getContext().getString(R.string.inUrlG7_);
        VoiceGuideLocation.voiceGuideImg.add(injeongimg7);
        VoiceGuideImage injeongimg8 = new VoiceGuideImage();
        injeongimg8.imgAddr = MyApplication.getContext().getString(R.string.inUrlG8);
        injeongimg8.voiceimgtext = MyApplication.getContext().getString(R.string.inUrlG8_);
        VoiceGuideLocation.voiceGuideImg.add(injeongimg8);
        VoiceGuideImage injeongimg9 = new VoiceGuideImage();
        injeongimg9.imgAddr = MyApplication.getContext().getString(R.string.inUrlG9);
        injeongimg9.voiceimgtext = MyApplication.getContext().getString(R.string.inUrlG9_);
        VoiceGuideLocation.voiceGuideImg.add(injeongimg9);

        return VoiceGuideLocation;
    }

    private VoiceGuideLocation makeSeonjeong(VoiceGuideLocation VoiceGuideLocation){
        VoiceGuideLocation.location_title = MyApplication.getContext().getString(R.string.seontitle);
        VoiceGuideLocation.voice_addr = MyApplication.getContext().getString(R.string.seonaudioaddress);
        VoiceGuideLocation.voice_title = MyApplication.getContext().getString(R.string.seonsubtitle);
        VoiceGuideLocation.voice_loc_east ="126.991831";
        VoiceGuideLocation.voice_loc_north="37.579725";
        VoiceGuideLocation.voice_loc_south="37.579289";
        VoiceGuideLocation.voice_loc_west="126.991555";
        VoiceGuideLocation.location_img_size = 13;
        VoiceGuideImage seonjeongimg1 = new VoiceGuideImage();
        seonjeongimg1.imgAddr = MyApplication.getContext().getString(R.string.seonUrlG1);
        seonjeongimg1.voiceimgtext = MyApplication.getContext().getString(R.string.seonUrlG1_);
        VoiceGuideLocation.voiceGuideImg.add(seonjeongimg1);
        VoiceGuideImage seonjeongimg2 = new VoiceGuideImage();
        seonjeongimg2.imgAddr = MyApplication.getContext().getString(R.string.seonUrlG2);
        seonjeongimg2.voiceimgtext = MyApplication.getContext().getString(R.string.seonUrlG2_);
        VoiceGuideLocation.voiceGuideImg.add(seonjeongimg2);
        VoiceGuideImage seonjeongimg3 = new VoiceGuideImage();
        seonjeongimg3.imgAddr = MyApplication.getContext().getString(R.string.seonUrlG3);
        seonjeongimg3.voiceimgtext = MyApplication.getContext().getString(R.string.seonUrlG3_);
        VoiceGuideLocation.voiceGuideImg.add(seonjeongimg3);
        VoiceGuideImage seonjeongimg4 = new VoiceGuideImage();
        seonjeongimg4.imgAddr = MyApplication.getContext().getString(R.string.seonUrlG4);
        seonjeongimg4.voiceimgtext = MyApplication.getContext().getString(R.string.seonUrlG4_);
        VoiceGuideLocation.voiceGuideImg.add(seonjeongimg4);
        VoiceGuideImage seonjeongimg5 = new VoiceGuideImage();
        seonjeongimg5.imgAddr = MyApplication.getContext().getString(R.string.seonUrlG5);
        seonjeongimg5.voiceimgtext = MyApplication.getContext().getString(R.string.seonUrlG5_);
        VoiceGuideLocation.voiceGuideImg.add(seonjeongimg5);
        VoiceGuideImage seonjeongimg6 = new VoiceGuideImage();
        seonjeongimg6.imgAddr = MyApplication.getContext().getString(R.string.seonUrlG6);
        seonjeongimg6.voiceimgtext = MyApplication.getContext().getString(R.string.seonUrlG6_);
        VoiceGuideLocation.voiceGuideImg.add(seonjeongimg6);
        VoiceGuideImage seonjeongimg7 = new VoiceGuideImage();
        seonjeongimg7.imgAddr = MyApplication.getContext().getString(R.string.seonUrlG7);
        seonjeongimg7.voiceimgtext = MyApplication.getContext().getString(R.string.seonUrlG7_);
        VoiceGuideLocation.voiceGuideImg.add(seonjeongimg7);
        VoiceGuideImage seonjeongimg8 = new VoiceGuideImage();
        seonjeongimg8.imgAddr = MyApplication.getContext().getString(R.string.seonUrlG8);
        seonjeongimg8.voiceimgtext = MyApplication.getContext().getString(R.string.seonUrlG8_);
        VoiceGuideLocation.voiceGuideImg.add(seonjeongimg8);
        VoiceGuideImage seonjeongimg9 = new VoiceGuideImage();
        seonjeongimg9.imgAddr = MyApplication.getContext().getString(R.string.seonUrlG9);
        seonjeongimg9.voiceimgtext = MyApplication.getContext().getString(R.string.seonUrlG9_);
        VoiceGuideLocation.voiceGuideImg.add(seonjeongimg9);
        VoiceGuideImage seonjeongimg10 = new VoiceGuideImage();
        seonjeongimg10.imgAddr = MyApplication.getContext().getString(R.string.seonUrlG10);
        seonjeongimg10.voiceimgtext = MyApplication.getContext().getString(R.string.seonUrlG10_);
        VoiceGuideLocation.voiceGuideImg.add(seonjeongimg10);
        VoiceGuideImage seonjeongimg11 = new VoiceGuideImage();
        seonjeongimg11.imgAddr = MyApplication.getContext().getString(R.string.seonUrlG11);
        seonjeongimg11.voiceimgtext = MyApplication.getContext().getString(R.string.seonUrlG11_);
        VoiceGuideLocation.voiceGuideImg.add(seonjeongimg11);
        VoiceGuideImage seonjeongimg12 = new VoiceGuideImage();
        seonjeongimg12.imgAddr = MyApplication.getContext().getString(R.string.seonUrlG12);
        seonjeongimg12.voiceimgtext = MyApplication.getContext().getString(R.string.seonUrlG12_);
        VoiceGuideLocation.voiceGuideImg.add(seonjeongimg12);
        VoiceGuideImage seonjeongimg13 = new VoiceGuideImage();
        seonjeongimg13.imgAddr = MyApplication.getContext().getString(R.string.seonUrlG13);
        seonjeongimg13.voiceimgtext = MyApplication.getContext().getString(R.string.seonUrlG13_);
        VoiceGuideLocation.voiceGuideImg.add(seonjeongimg13);

        return VoiceGuideLocation;
    }


    private VoiceGuideLocation makeHeejeong(VoiceGuideLocation VoiceGuideLocation){

        VoiceGuideLocation.location_title = MyApplication.getContext().getString(R.string.heetitle);
        VoiceGuideLocation.voice_addr = MyApplication.getContext().getString(R.string.heeaudioaddress);
        VoiceGuideLocation.voice_title = MyApplication.getContext().getString(R.string.heesubtitle);
        VoiceGuideLocation.voice_loc_east ="126.992448";
        VoiceGuideLocation.voice_loc_north="37.579831";
        VoiceGuideLocation.voice_loc_south="37.579266";
        VoiceGuideLocation.voice_loc_west="126.991839";
        VoiceGuideLocation.location_img_size = 15;
        VoiceGuideImage heejeongimg1 = new VoiceGuideImage();
        heejeongimg1.imgAddr = MyApplication.getContext().getString(R.string.heeUrlG1);
        heejeongimg1.voiceimgtext = MyApplication.getContext().getString(R.string.heeUrlG1_);
        VoiceGuideLocation.voiceGuideImg.add(heejeongimg1);
        VoiceGuideImage heejeongimg2 = new VoiceGuideImage();
        heejeongimg2.imgAddr = MyApplication.getContext().getString(R.string.heeUrlG2);
        heejeongimg2.voiceimgtext = MyApplication.getContext().getString(R.string.heeUrlG2_);
        VoiceGuideLocation.voiceGuideImg.add(heejeongimg2);
        VoiceGuideImage heejeongimg3 = new VoiceGuideImage();
        heejeongimg3.imgAddr = MyApplication.getContext().getString(R.string.heeUrlG3);
        heejeongimg3.voiceimgtext = MyApplication.getContext().getString(R.string.heeUrlG3_);
        VoiceGuideLocation.voiceGuideImg.add(heejeongimg3);
        VoiceGuideImage heejeongimg4 = new VoiceGuideImage();
        heejeongimg4.imgAddr = MyApplication.getContext().getString(R.string.heeUrlG4);
        heejeongimg4.voiceimgtext = MyApplication.getContext().getString(R.string.heeUrlG4_);
        VoiceGuideLocation.voiceGuideImg.add(heejeongimg4);
        VoiceGuideImage heejeongimg5 = new VoiceGuideImage();
        heejeongimg5.imgAddr = MyApplication.getContext().getString(R.string.heeUrlG5);
        heejeongimg5.voiceimgtext = MyApplication.getContext().getString(R.string.heeUrlG5_);
        VoiceGuideLocation.voiceGuideImg.add(heejeongimg5);
        VoiceGuideImage heejeongimg6 = new VoiceGuideImage();
        heejeongimg6.imgAddr = MyApplication.getContext().getString(R.string.heeUrlG6);
        heejeongimg6.voiceimgtext = MyApplication.getContext().getString(R.string.heeUrlG6_);
        VoiceGuideLocation.voiceGuideImg.add(heejeongimg6);
        VoiceGuideImage heejeongimg7 = new VoiceGuideImage();
        heejeongimg7.imgAddr = MyApplication.getContext().getString(R.string.heeUrlG7);
        heejeongimg7.voiceimgtext = MyApplication.getContext().getString(R.string.heeUrlG7_);
        VoiceGuideLocation.voiceGuideImg.add(heejeongimg7);
        VoiceGuideImage heejeongimg8 = new VoiceGuideImage();
        heejeongimg8.imgAddr = MyApplication.getContext().getString(R.string.heeUrlG8);
        heejeongimg8.voiceimgtext = MyApplication.getContext().getString(R.string.heeUrlG8_);
        VoiceGuideLocation.voiceGuideImg.add(heejeongimg8);
        VoiceGuideImage heejeongimg9 = new VoiceGuideImage();
        heejeongimg9.imgAddr = MyApplication.getContext().getString(R.string.heeUrlG9);
        heejeongimg9.voiceimgtext = MyApplication.getContext().getString(R.string.heeUrlG9_);
        VoiceGuideLocation.voiceGuideImg.add(heejeongimg9);
        VoiceGuideImage heejeongimg10 = new VoiceGuideImage();
        heejeongimg10.imgAddr = MyApplication.getContext().getString(R.string.heeUrlG10);
        heejeongimg10.voiceimgtext = MyApplication.getContext().getString(R.string.heeUrlG10_);
        VoiceGuideLocation.voiceGuideImg.add(heejeongimg10);
        VoiceGuideImage heejeongimg11 = new VoiceGuideImage();
        heejeongimg11.imgAddr = MyApplication.getContext().getString(R.string.heeUrlG11);
        heejeongimg11.voiceimgtext = MyApplication.getContext().getString(R.string.heeUrlG11_);
        VoiceGuideLocation.voiceGuideImg.add(heejeongimg11);
        VoiceGuideImage heejeongimg12 = new VoiceGuideImage();
        heejeongimg12.imgAddr = MyApplication.getContext().getString(R.string.heeUrlG12);
        heejeongimg12.voiceimgtext = MyApplication.getContext().getString(R.string.heeUrlG12_);
        VoiceGuideLocation.voiceGuideImg.add(heejeongimg12);
        VoiceGuideImage heejeongimg13 = new VoiceGuideImage();
        heejeongimg13.imgAddr = MyApplication.getContext().getString(R.string.heeUrlG13);
        heejeongimg13.voiceimgtext = MyApplication.getContext().getString(R.string.heeUrlG13_);
        VoiceGuideLocation.voiceGuideImg.add(heejeongimg13);
        VoiceGuideImage heejeongimg14 = new VoiceGuideImage();
        heejeongimg14.imgAddr = MyApplication.getContext().getString(R.string.heeUrlG14);
        heejeongimg14.voiceimgtext = MyApplication.getContext().getString(R.string.heeUrlG14_);
        VoiceGuideLocation.voiceGuideImg.add(heejeongimg14);
        VoiceGuideImage heejeongimg15 = new VoiceGuideImage();
        heejeongimg15.imgAddr = MyApplication.getContext().getString(R.string.heeUrlG15);
        heejeongimg15.voiceimgtext = MyApplication.getContext().getString(R.string.heeUrlG15_);
        VoiceGuideLocation.voiceGuideImg.add(heejeongimg15);

        return VoiceGuideLocation;
    }

    private VoiceGuideLocation makeHeejeongBack(VoiceGuideLocation VoiceGuideLocation){
        VoiceGuideLocation.location_title = MyApplication.getContext().getString(R.string.heebacktitle);
        VoiceGuideLocation.voice_addr = MyApplication.getContext().getString(R.string.heebackaudioaddress);
        VoiceGuideLocation.voice_title = MyApplication.getContext().getString(R.string.heebacktitle);
        VoiceGuideLocation.voice_loc_east ="126.992523";
        VoiceGuideLocation.voice_loc_north="37.579893";
        VoiceGuideLocation.voice_loc_south="37.579757";
        VoiceGuideLocation.voice_loc_west="126.992136";
        VoiceGuideLocation.location_img_size = 7;
        VoiceGuideImage heejeongbackimg1 = new VoiceGuideImage();
        heejeongbackimg1.imgAddr = MyApplication.getContext().getString(R.string.heebackUrlG1);
        heejeongbackimg1.voiceimgtext = MyApplication.getContext().getString(R.string.heebackUrlG1_);
        VoiceGuideLocation.voiceGuideImg.add(heejeongbackimg1);
        VoiceGuideImage heejeongbackimg2 = new VoiceGuideImage();
        heejeongbackimg2.imgAddr = MyApplication.getContext().getString(R.string.heebackUrlG2);
        heejeongbackimg2.voiceimgtext = MyApplication.getContext().getString(R.string.heebackUrlG2_);
        VoiceGuideLocation.voiceGuideImg.add(heejeongbackimg2);
        VoiceGuideImage heejeongbackimg3 = new VoiceGuideImage();
        heejeongbackimg3.imgAddr = MyApplication.getContext().getString(R.string.heebackUrlG3);
        heejeongbackimg3.voiceimgtext = MyApplication.getContext().getString(R.string.heebackUrlG3_);
        VoiceGuideLocation.voiceGuideImg.add(heejeongbackimg3);
        VoiceGuideImage heejeongbackimg4 = new VoiceGuideImage();
        heejeongbackimg4.imgAddr = MyApplication.getContext().getString(R.string.heebackUrlG4);
        heejeongbackimg4.voiceimgtext = MyApplication.getContext().getString(R.string.heebackUrlG4_);
        VoiceGuideLocation.voiceGuideImg.add(heejeongbackimg4);
        VoiceGuideImage heejeongbackimg5 = new VoiceGuideImage();
        heejeongbackimg5.imgAddr = MyApplication.getContext().getString(R.string.heebackUrlG5);
        heejeongbackimg5.voiceimgtext = MyApplication.getContext().getString(R.string.heebackUrlG5_);
        VoiceGuideLocation.voiceGuideImg.add(heejeongbackimg5);
        VoiceGuideImage heejeongbackimg6 = new VoiceGuideImage();
        heejeongbackimg6.imgAddr = MyApplication.getContext().getString(R.string.heebackUrlG6);
        heejeongbackimg6.voiceimgtext = MyApplication.getContext().getString(R.string.heebackUrlG6_);
        VoiceGuideLocation.voiceGuideImg.add(heejeongbackimg6);
        VoiceGuideImage heejeongbackimg7 = new VoiceGuideImage();
        heejeongbackimg7.imgAddr = MyApplication.getContext().getString(R.string.heebackUrlG7);
        heejeongbackimg7.voiceimgtext = MyApplication.getContext().getString(R.string.heebackUrlG7_);
        VoiceGuideLocation.voiceGuideImg.add(heejeongbackimg7);
        return VoiceGuideLocation;
    }


    private VoiceGuideLocation makeDaejo(VoiceGuideLocation VoiceGuideLocation){
        VoiceGuideLocation.location_title = MyApplication.getContext().getString(R.string.daetitle);
        VoiceGuideLocation.voice_addr = MyApplication.getContext().getString(R.string.daeaudioaddress);
        VoiceGuideLocation.voice_title = MyApplication.getContext().getString(R.string.daesubtitle);
        VoiceGuideLocation.voice_loc_east ="126.992537";
        VoiceGuideLocation.voice_loc_north="37.580101";
        VoiceGuideLocation.voice_loc_south="37.57989";
        VoiceGuideLocation.voice_loc_west="126.992204";
        VoiceGuideLocation.location_img_size = 11;
        VoiceGuideImage daejoimg1 = new VoiceGuideImage();
        daejoimg1.imgAddr = MyApplication.getContext().getString(R.string.daeUrlG1);
        daejoimg1.voiceimgtext = MyApplication.getContext().getString(R.string.daeUrlG1_);
        VoiceGuideLocation.voiceGuideImg.add(daejoimg1);
        VoiceGuideImage daejoimg2 = new VoiceGuideImage();
        daejoimg2.imgAddr = MyApplication.getContext().getString(R.string.daeUrlG2);
        daejoimg2.voiceimgtext = MyApplication.getContext().getString(R.string.daeUrlG2_);
        VoiceGuideLocation.voiceGuideImg.add(daejoimg2);
        VoiceGuideImage daejoimg3 = new VoiceGuideImage();
        daejoimg3.imgAddr = MyApplication.getContext().getString(R.string.daeUrlG3);
        daejoimg3.voiceimgtext = MyApplication.getContext().getString(R.string.daeUrlG3_);
        VoiceGuideLocation.voiceGuideImg.add(daejoimg3);
        VoiceGuideImage daejoimg4 = new VoiceGuideImage();
        daejoimg4.imgAddr = MyApplication.getContext().getString(R.string.daeUrlG4);
        daejoimg4.voiceimgtext = MyApplication.getContext().getString(R.string.daeUrlG4_);
        VoiceGuideLocation.voiceGuideImg.add(daejoimg4);
        VoiceGuideImage daejoimg5 = new VoiceGuideImage();
        daejoimg5.imgAddr = MyApplication.getContext().getString(R.string.daeUrlG5);
        daejoimg5.voiceimgtext = MyApplication.getContext().getString(R.string.daeUrlG5_);
        VoiceGuideLocation.voiceGuideImg.add(daejoimg5);
        VoiceGuideImage daejoimg6 = new VoiceGuideImage();
        daejoimg6.imgAddr = MyApplication.getContext().getString(R.string.daeUrlG6);
        daejoimg6.voiceimgtext = MyApplication.getContext().getString(R.string.daeUrlG6_);
        VoiceGuideLocation.voiceGuideImg.add(daejoimg6);
        VoiceGuideImage daejoimg7 = new VoiceGuideImage();
        daejoimg7.imgAddr = MyApplication.getContext().getString(R.string.daeUrlG7);
        daejoimg7.voiceimgtext = MyApplication.getContext().getString(R.string.daeUrlG7_);
        VoiceGuideLocation.voiceGuideImg.add(daejoimg7);
        VoiceGuideImage daejoimg8 = new VoiceGuideImage();
        daejoimg8.imgAddr = MyApplication.getContext().getString(R.string.daeUrlG8);
        daejoimg8.voiceimgtext = MyApplication.getContext().getString(R.string.daeUrlG8_);
        VoiceGuideLocation.voiceGuideImg.add(daejoimg8);
        VoiceGuideImage daejoimg9 = new VoiceGuideImage();
        daejoimg9.imgAddr = MyApplication.getContext().getString(R.string.daeUrlG9);
        daejoimg9.voiceimgtext = MyApplication.getContext().getString(R.string.daeUrlG9_);
        VoiceGuideLocation.voiceGuideImg.add(daejoimg9);
        VoiceGuideImage daejoimg10 = new VoiceGuideImage();
        daejoimg10.imgAddr = MyApplication.getContext().getString(R.string.daeUrlG10);
        daejoimg10.voiceimgtext = MyApplication.getContext().getString(R.string.daeUrlG10_);
        VoiceGuideLocation.voiceGuideImg.add(daejoimg10);
        VoiceGuideImage daejoimg11 = new VoiceGuideImage();
        daejoimg11.imgAddr = MyApplication.getContext().getString(R.string.daeUrlG11);
        daejoimg11.voiceimgtext = MyApplication.getContext().getString(R.string.daeUrlG11_);
        VoiceGuideLocation.voiceGuideImg.add(daejoimg11);
        return VoiceGuideLocation;
    }


    private VoiceGuideLocation makeSoora(VoiceGuideLocation VoiceGuideLocation){
        VoiceGuideLocation.location_title = MyApplication.getContext().getString(R.string.sootitle);
        VoiceGuideLocation.voice_addr = MyApplication.getContext().getString(R.string.sooaudioaddress);
        VoiceGuideLocation.voice_title = MyApplication.getContext().getString(R.string.soosubtitle);
        VoiceGuideLocation.voice_loc_east ="126.99203";
        VoiceGuideLocation.voice_loc_north="37.580149";
        VoiceGuideLocation.voice_loc_south="37.58";
        VoiceGuideLocation.voice_loc_west="126.991954";
        VoiceGuideLocation.location_img_size = 7;
        VoiceGuideImage sooraimg1 = new VoiceGuideImage();
        sooraimg1.imgAddr = MyApplication.getContext().getString(R.string.sooUrlG1);
        sooraimg1.voiceimgtext = MyApplication.getContext().getString(R.string.sooUrlG1_);
        VoiceGuideLocation.voiceGuideImg.add(sooraimg1);
        VoiceGuideImage sooraimg2 = new VoiceGuideImage();
        sooraimg2.imgAddr = MyApplication.getContext().getString(R.string.sooUrlG2);
        sooraimg2.voiceimgtext = MyApplication.getContext().getString(R.string.sooUrlG2_);
        VoiceGuideLocation.voiceGuideImg.add(sooraimg2);
        VoiceGuideImage sooraimg3 = new VoiceGuideImage();
        sooraimg3.imgAddr = MyApplication.getContext().getString(R.string.sooUrlG3);
        sooraimg3.voiceimgtext = MyApplication.getContext().getString(R.string.sooUrlG3_);
        VoiceGuideLocation.voiceGuideImg.add(sooraimg3);
        VoiceGuideImage sooraimg4 = new VoiceGuideImage();
        sooraimg4.imgAddr = MyApplication.getContext().getString(R.string.sooUrlG4);
        sooraimg4.voiceimgtext = MyApplication.getContext().getString(R.string.sooUrlG4_);
        VoiceGuideLocation.voiceGuideImg.add(sooraimg4);
        VoiceGuideImage sooraimg5 = new VoiceGuideImage();
        sooraimg5.imgAddr = MyApplication.getContext().getString(R.string.sooUrlG5);
        sooraimg5.voiceimgtext = MyApplication.getContext().getString(R.string.sooUrlG5_);
        VoiceGuideLocation.voiceGuideImg.add(sooraimg5);
        VoiceGuideImage sooraimg6 = new VoiceGuideImage();
        sooraimg6.imgAddr = MyApplication.getContext().getString(R.string.sooUrlG6);
        sooraimg6.voiceimgtext = MyApplication.getContext().getString(R.string.sooUrlG6_);
        VoiceGuideLocation.voiceGuideImg.add(sooraimg6);
        VoiceGuideImage sooraimg7 = new VoiceGuideImage();
        sooraimg7.imgAddr = MyApplication.getContext().getString(R.string.sooUrlG7);
        sooraimg7.voiceimgtext = MyApplication.getContext().getString(R.string.sooUrlG7_);
        VoiceGuideLocation.voiceGuideImg.add(sooraimg7);
        return VoiceGuideLocation;
    }

    private VoiceGuideLocation makeGyeonghoon(VoiceGuideLocation VoiceGuideLocation){
        VoiceGuideLocation.location_title = MyApplication.getContext().getString(R.string.gyeongtitle);
        VoiceGuideLocation.voice_addr = MyApplication.getContext().getString(R.string.gyeongaudioaddress);
        VoiceGuideLocation.voice_title = MyApplication.getContext().getString(R.string.gyeongsubtitle);
        VoiceGuideLocation.voice_loc_east ="126.992139";
        VoiceGuideLocation.voice_loc_north="37.580493";
        VoiceGuideLocation.voice_loc_south="37.580278";
        VoiceGuideLocation.voice_loc_west="126.99202";
        VoiceGuideLocation.location_img_size = 10;
        VoiceGuideImage gyunghoonimg1 = new VoiceGuideImage();
        gyunghoonimg1.imgAddr = MyApplication.getContext().getString(R.string.gyeongUrlG1);
        gyunghoonimg1.voiceimgtext = MyApplication.getContext().getString(R.string.gyeongUrlG1_);
        VoiceGuideLocation.voiceGuideImg.add(gyunghoonimg1);
        VoiceGuideImage gyunghoonimg2 = new VoiceGuideImage();
        gyunghoonimg2.imgAddr = MyApplication.getContext().getString(R.string.gyeongUrlG2);
        gyunghoonimg2.voiceimgtext = MyApplication.getContext().getString(R.string.gyeongUrlG2_);
        VoiceGuideLocation.voiceGuideImg.add(gyunghoonimg2);
        VoiceGuideImage gyunghoonimg3 = new VoiceGuideImage();
        gyunghoonimg3.imgAddr = MyApplication.getContext().getString(R.string.gyeongUrlG3);
        gyunghoonimg3.voiceimgtext = MyApplication.getContext().getString(R.string.gyeongUrlG3_);
        VoiceGuideLocation.voiceGuideImg.add(gyunghoonimg3);
        VoiceGuideImage gyunghoonimg4 = new VoiceGuideImage();
        gyunghoonimg4.imgAddr = MyApplication.getContext().getString(R.string.gyeongUrlG4);
        gyunghoonimg4.voiceimgtext = MyApplication.getContext().getString(R.string.gyeongUrlG4_);
        VoiceGuideLocation.voiceGuideImg.add(gyunghoonimg4);
        VoiceGuideImage gyunghoonimg5 = new VoiceGuideImage();
        gyunghoonimg5.imgAddr = MyApplication.getContext().getString(R.string.gyeongUrlG5);
        gyunghoonimg5.voiceimgtext = MyApplication.getContext().getString(R.string.gyeongUrlG5_);
        VoiceGuideLocation.voiceGuideImg.add(gyunghoonimg5);
        VoiceGuideImage gyunghoonimg6 = new VoiceGuideImage();
        gyunghoonimg6.imgAddr = MyApplication.getContext().getString(R.string.gyeongUrlG6);
        gyunghoonimg6.voiceimgtext = MyApplication.getContext().getString(R.string.gyeongUrlG6_);
        VoiceGuideLocation.voiceGuideImg.add(gyunghoonimg6);
        VoiceGuideImage gyunghoonimg7 = new VoiceGuideImage();
        gyunghoonimg7.imgAddr = MyApplication.getContext().getString(R.string.gyeongUrlG7);
        gyunghoonimg7.voiceimgtext = MyApplication.getContext().getString(R.string.gyeongUrlG7_);
        VoiceGuideLocation.voiceGuideImg.add(gyunghoonimg7);
        VoiceGuideImage gyunghoonimg8 = new VoiceGuideImage();
        gyunghoonimg8.imgAddr = MyApplication.getContext().getString(R.string.gyeongUrlG8);
        gyunghoonimg8.voiceimgtext = MyApplication.getContext().getString(R.string.gyeongUrlG8_);
        VoiceGuideLocation.voiceGuideImg.add(gyunghoonimg8);
        VoiceGuideImage gyunghoonimg9 = new VoiceGuideImage();
        gyunghoonimg9.imgAddr = MyApplication.getContext().getString(R.string.gyeongUrlG9);
        gyunghoonimg9.voiceimgtext = MyApplication.getContext().getString(R.string.gyeongUrlG9_);
        VoiceGuideLocation.voiceGuideImg.add(gyunghoonimg9);
        VoiceGuideImage gyunghoonimg10 = new VoiceGuideImage();
        gyunghoonimg10.imgAddr = MyApplication.getContext().getString(R.string.gyeongUrlG10);
        gyunghoonimg10.voiceimgtext = MyApplication.getContext().getString(R.string.gyeongUrlG10_);
        VoiceGuideLocation.voiceGuideImg.add(gyunghoonimg10);
        return VoiceGuideLocation;
    }


    private VoiceGuideLocation makeSeongjeong(VoiceGuideLocation VoiceGuideLocation){
        VoiceGuideLocation.location_title = MyApplication.getContext().getString(R.string.seongtitle);
        VoiceGuideLocation.voice_addr = MyApplication.getContext().getString(R.string.seongaudioaddress);
        VoiceGuideLocation.voice_title = MyApplication.getContext().getString(R.string.seongsubtitle);
        VoiceGuideLocation.voice_loc_east ="126.992795";
        VoiceGuideLocation.voice_loc_north="37.579389";
        VoiceGuideLocation.voice_loc_south="37.57917";
        VoiceGuideLocation.voice_loc_west="126.992467";
        VoiceGuideLocation.location_img_size = 14;
        VoiceGuideImage seongjeongimg1 = new VoiceGuideImage();
        seongjeongimg1.imgAddr = MyApplication.getContext().getString(R.string.seongUrlG1);
        seongjeongimg1.voiceimgtext = MyApplication.getContext().getString(R.string.seongUrlG1_);
        VoiceGuideLocation.voiceGuideImg.add(seongjeongimg1);
        VoiceGuideImage seongjeongimg2 = new VoiceGuideImage();
        seongjeongimg2.imgAddr = MyApplication.getContext().getString(R.string.seongUrlG2);
        seongjeongimg2.voiceimgtext = MyApplication.getContext().getString(R.string.seongUrlG2_);
        VoiceGuideLocation.voiceGuideImg.add(seongjeongimg2);
        VoiceGuideImage seongjeongimg3 = new VoiceGuideImage();
        seongjeongimg3.imgAddr = MyApplication.getContext().getString(R.string.seongUrlG3);
        seongjeongimg3.voiceimgtext = MyApplication.getContext().getString(R.string.seongUrlG3_);
        VoiceGuideLocation.voiceGuideImg.add(seongjeongimg3);
        VoiceGuideImage seongjeongimg4 = new VoiceGuideImage();
        seongjeongimg4.imgAddr = MyApplication.getContext().getString(R.string.seongUrlG4);
        seongjeongimg4.voiceimgtext = MyApplication.getContext().getString(R.string.seongUrlG4_);
        VoiceGuideLocation.voiceGuideImg.add(seongjeongimg4);
        VoiceGuideImage seongjeongimg5 = new VoiceGuideImage();
        seongjeongimg5.imgAddr = MyApplication.getContext().getString(R.string.seongUrlG5);
        seongjeongimg5.voiceimgtext = MyApplication.getContext().getString(R.string.seongUrlG5_);
        VoiceGuideLocation.voiceGuideImg.add(seongjeongimg5);
        VoiceGuideImage seongjeongimg6 = new VoiceGuideImage();
        seongjeongimg6.imgAddr = MyApplication.getContext().getString(R.string.seongUrlG6);
        seongjeongimg6.voiceimgtext = MyApplication.getContext().getString(R.string.seongUrlG6_);
        VoiceGuideLocation.voiceGuideImg.add(seongjeongimg6);
        VoiceGuideImage seongjeongimg7 = new VoiceGuideImage();
        seongjeongimg7.imgAddr = MyApplication.getContext().getString(R.string.seongUrlG7);
        seongjeongimg7.voiceimgtext = MyApplication.getContext().getString(R.string.seongUrlG7_);
        VoiceGuideLocation.voiceGuideImg.add(seongjeongimg7);
        VoiceGuideImage seongjeongimg8 = new VoiceGuideImage();
        seongjeongimg8.imgAddr = MyApplication.getContext().getString(R.string.seongUrlG8);
        seongjeongimg8.voiceimgtext = MyApplication.getContext().getString(R.string.seongUrlG8_);
        VoiceGuideLocation.voiceGuideImg.add(seongjeongimg8);
        VoiceGuideImage seongjeongimg9 = new VoiceGuideImage();
        seongjeongimg9.imgAddr = MyApplication.getContext().getString(R.string.seongUrlG9);
        seongjeongimg9.voiceimgtext = MyApplication.getContext().getString(R.string.seongUrlG9_);
        VoiceGuideLocation.voiceGuideImg.add(seongjeongimg9);
        VoiceGuideImage seongjeongimg10 = new VoiceGuideImage();
        seongjeongimg10.imgAddr = MyApplication.getContext().getString(R.string.seongUrlG10);
        seongjeongimg10.voiceimgtext = MyApplication.getContext().getString(R.string.seongUrlG10_);
        VoiceGuideLocation.voiceGuideImg.add(seongjeongimg10);
        VoiceGuideImage seongjeongimg11 = new VoiceGuideImage();
        seongjeongimg11.imgAddr = MyApplication.getContext().getString(R.string.seongUrlG11);
        seongjeongimg11.voiceimgtext = MyApplication.getContext().getString(R.string.seongUrlG11_);
        VoiceGuideLocation.voiceGuideImg.add(seongjeongimg11);
        VoiceGuideImage seongjeongimg12 = new VoiceGuideImage();
        seongjeongimg12.imgAddr = MyApplication.getContext().getString(R.string.seongUrlG12);
        seongjeongimg12.voiceimgtext = MyApplication.getContext().getString(R.string.seongUrlG12_);
        VoiceGuideLocation.voiceGuideImg.add(seongjeongimg12);
        VoiceGuideImage seongjeongimg13 = new VoiceGuideImage();
        seongjeongimg13.imgAddr = MyApplication.getContext().getString(R.string.seongUrlG13);
        seongjeongimg13.voiceimgtext = MyApplication.getContext().getString(R.string.seongUrlG13_);
        VoiceGuideLocation.voiceGuideImg.add(seongjeongimg13);
        VoiceGuideImage seongjeongimg14 = new VoiceGuideImage();
        seongjeongimg14.imgAddr = MyApplication.getContext().getString(R.string.seongUrlG14);
        seongjeongimg14.voiceimgtext = MyApplication.getContext().getString(R.string.seongUrlG14_);
        VoiceGuideLocation.voiceGuideImg.add(seongjeongimg14);
        return VoiceGuideLocation;
    }

    private VoiceGuideLocation makeNakseon(VoiceGuideLocation VoiceGuideLocation){
        VoiceGuideLocation.location_title = MyApplication.getContext().getString(R.string.naktitle);
        VoiceGuideLocation.voice_addr = MyApplication.getContext().getString(R.string.nakaudioaddress);
        VoiceGuideLocation.voice_title = MyApplication.getContext().getString(R.string.naksubtitle);
        VoiceGuideLocation.voice_loc_east ="126.993542";
        VoiceGuideLocation.voice_loc_north="37.578757";
        VoiceGuideLocation.voice_loc_south="37.578455";
        VoiceGuideLocation.voice_loc_west="126.993126";
        VoiceGuideLocation.location_img_size = 21;
        VoiceGuideImage nakseonimg1 = new VoiceGuideImage();
        nakseonimg1.imgAddr = MyApplication.getContext().getString(R.string.nakUrlG1);
        nakseonimg1.voiceimgtext = MyApplication.getContext().getString(R.string.nakUrlG1_);
        VoiceGuideLocation.voiceGuideImg.add(nakseonimg1);
        VoiceGuideImage nakseonimg2 = new VoiceGuideImage();
        nakseonimg2.imgAddr = MyApplication.getContext().getString(R.string.nakUrlG2);
        nakseonimg2.voiceimgtext = MyApplication.getContext().getString(R.string.nakUrlG2_);
        VoiceGuideLocation.voiceGuideImg.add(nakseonimg2);
        VoiceGuideImage nakseonimg3 = new VoiceGuideImage();
        nakseonimg3.imgAddr = MyApplication.getContext().getString(R.string.nakUrlG3);
        nakseonimg3.voiceimgtext = MyApplication.getContext().getString(R.string.nakUrlG3_);
        VoiceGuideLocation.voiceGuideImg.add(nakseonimg3);
        VoiceGuideImage nakseonimg4 = new VoiceGuideImage();
        nakseonimg4.imgAddr = MyApplication.getContext().getString(R.string.nakUrlG4);
        nakseonimg4.voiceimgtext = MyApplication.getContext().getString(R.string.nakUrlG4_);
        VoiceGuideLocation.voiceGuideImg.add(nakseonimg4);
        VoiceGuideImage nakseonimg5 = new VoiceGuideImage();
        nakseonimg5.imgAddr = MyApplication.getContext().getString(R.string.nakUrlG5);
        nakseonimg5.voiceimgtext = MyApplication.getContext().getString(R.string.nakUrlG5_);
        VoiceGuideLocation.voiceGuideImg.add(nakseonimg5);
        VoiceGuideImage nakseonimg6 = new VoiceGuideImage();
        nakseonimg6.imgAddr = MyApplication.getContext().getString(R.string.nakUrlG6);
        nakseonimg6.voiceimgtext = MyApplication.getContext().getString(R.string.nakUrlG6_);
        VoiceGuideLocation.voiceGuideImg.add(nakseonimg6);
        VoiceGuideImage nakseonimg7 = new VoiceGuideImage();
        nakseonimg7.imgAddr = MyApplication.getContext().getString(R.string.nakUrlG7);
        nakseonimg7.voiceimgtext = MyApplication.getContext().getString(R.string.nakUrlG7_);
        VoiceGuideLocation.voiceGuideImg.add(nakseonimg7);
        VoiceGuideImage nakseonimg8 = new VoiceGuideImage();
        nakseonimg8.imgAddr = MyApplication.getContext().getString(R.string.nakUrlG8);
        nakseonimg8.voiceimgtext = MyApplication.getContext().getString(R.string.nakUrlG8_);
        VoiceGuideLocation.voiceGuideImg.add(nakseonimg8);
        VoiceGuideImage nakseonimg9 = new VoiceGuideImage();
        nakseonimg9.imgAddr = MyApplication.getContext().getString(R.string.nakUrlG9);
        nakseonimg9.voiceimgtext = MyApplication.getContext().getString(R.string.nakUrlG9_);
        VoiceGuideLocation.voiceGuideImg.add(nakseonimg9);
        VoiceGuideImage nakseonimg10 = new VoiceGuideImage();
        nakseonimg10.imgAddr = MyApplication.getContext().getString(R.string.nakUrlG10);
        nakseonimg10.voiceimgtext = MyApplication.getContext().getString(R.string.nakUrlG10_);
        VoiceGuideLocation.voiceGuideImg.add(nakseonimg10);
        VoiceGuideImage nakseonimg11 = new VoiceGuideImage();
        nakseonimg11.imgAddr = MyApplication.getContext().getString(R.string.nakUrlG11);
        nakseonimg11.voiceimgtext = MyApplication.getContext().getString(R.string.nakUrlG11_);
        VoiceGuideLocation.voiceGuideImg.add(nakseonimg11);
        VoiceGuideImage nakseonimg12 = new VoiceGuideImage();
        nakseonimg12.imgAddr = MyApplication.getContext().getString(R.string.nakUrlG12);
        nakseonimg12.voiceimgtext = MyApplication.getContext().getString(R.string.nakUrlG12_);
        VoiceGuideLocation.voiceGuideImg.add(nakseonimg12);
        VoiceGuideImage nakseonimg13 = new VoiceGuideImage();
        nakseonimg13.imgAddr = MyApplication.getContext().getString(R.string.nakUrlG13);
        nakseonimg13.voiceimgtext = MyApplication.getContext().getString(R.string.nakUrlG13_);
        VoiceGuideLocation.voiceGuideImg.add(nakseonimg13);
        VoiceGuideImage nakseonimg14 = new VoiceGuideImage();
        nakseonimg14.imgAddr = MyApplication.getContext().getString(R.string.nakUrlG14);
        nakseonimg14.voiceimgtext = MyApplication.getContext().getString(R.string.nakUrlG14_);
        VoiceGuideLocation.voiceGuideImg.add(nakseonimg14);
        VoiceGuideImage nakseonimg15 = new VoiceGuideImage();
        nakseonimg15.imgAddr = MyApplication.getContext().getString(R.string.nakUrlG15);
        nakseonimg15.voiceimgtext = MyApplication.getContext().getString(R.string.nakUrlG15_);
        VoiceGuideLocation.voiceGuideImg.add(nakseonimg15);
        VoiceGuideImage nakseonimg16 = new VoiceGuideImage();
        nakseonimg16.imgAddr = MyApplication.getContext().getString(R.string.nakUrlG16);
        nakseonimg16.voiceimgtext = MyApplication.getContext().getString(R.string.nakUrlG16_);
        VoiceGuideLocation.voiceGuideImg.add(nakseonimg16);
        VoiceGuideImage nakseonimg17 = new VoiceGuideImage();
        nakseonimg17.imgAddr = MyApplication.getContext().getString(R.string.nakUrlG17);
        nakseonimg17.voiceimgtext = MyApplication.getContext().getString(R.string.nakUrlG17_);
        VoiceGuideLocation.voiceGuideImg.add(nakseonimg17);
        VoiceGuideImage nakseonimg18 = new VoiceGuideImage();
        nakseonimg18.imgAddr = MyApplication.getContext().getString(R.string.nakUrlG18);
        nakseonimg18.voiceimgtext = MyApplication.getContext().getString(R.string.nakUrlG18_);
        VoiceGuideLocation.voiceGuideImg.add(nakseonimg18);
        VoiceGuideImage nakseonimg19 = new VoiceGuideImage();
        nakseonimg19.imgAddr = MyApplication.getContext().getString(R.string.nakUrlG19);
        nakseonimg19.voiceimgtext = MyApplication.getContext().getString(R.string.nakUrlG19_);
        VoiceGuideLocation.voiceGuideImg.add(nakseonimg19);
        VoiceGuideImage nakseonimg20 = new VoiceGuideImage();
        nakseonimg20.imgAddr = MyApplication.getContext().getString(R.string.nakUrlG20);
        nakseonimg20.voiceimgtext = MyApplication.getContext().getString(R.string.nakUrlG20_);
        VoiceGuideLocation.voiceGuideImg.add(nakseonimg20);
        VoiceGuideImage nakseonimg21 = new VoiceGuideImage();
        nakseonimg21.imgAddr = MyApplication.getContext().getString(R.string.nakUrlG21);
        nakseonimg21.voiceimgtext = MyApplication.getContext().getString(R.string.nakUrlG21_);
        VoiceGuideLocation.voiceGuideImg.add(nakseonimg21);
        return VoiceGuideLocation;
    }

    private VoiceGuideLocation makeSeokbok(VoiceGuideLocation VoiceGuideLocation){
        VoiceGuideLocation.location_title = MyApplication.getContext().getString(R.string.seoktitle);
        VoiceGuideLocation.voice_addr = MyApplication.getContext().getString(R.string.seokaudioaddress);
        VoiceGuideLocation.voice_title = MyApplication.getContext().getString(R.string.seoksubtitle);
        VoiceGuideLocation.voice_loc_east ="126.993683";
        VoiceGuideLocation.voice_loc_north="37.578599";
        VoiceGuideLocation.voice_loc_south="37.578504";
        VoiceGuideLocation.voice_loc_west="126.993566";
        VoiceGuideLocation.location_img_size = 7;
        VoiceGuideImage seokbokimg1 = new VoiceGuideImage();
        seokbokimg1.imgAddr = MyApplication.getContext().getString(R.string.seokUrlG1);
        seokbokimg1.voiceimgtext = MyApplication.getContext().getString(R.string.seokUrlG1_);
        VoiceGuideLocation.voiceGuideImg.add(seokbokimg1);
        VoiceGuideImage seokbokimg2 = new VoiceGuideImage();
        seokbokimg2.imgAddr = MyApplication.getContext().getString(R.string.seokUrlG2);
        seokbokimg2.voiceimgtext = MyApplication.getContext().getString(R.string.seokUrlG2_);
        VoiceGuideLocation.voiceGuideImg.add(seokbokimg2);
        VoiceGuideImage seokbokimg3 = new VoiceGuideImage();
        seokbokimg3.imgAddr = MyApplication.getContext().getString(R.string.seokUrlG3);
        seokbokimg3.voiceimgtext = MyApplication.getContext().getString(R.string.seokUrlG3_);
        VoiceGuideLocation.voiceGuideImg.add(seokbokimg3);
        VoiceGuideImage seokbokimg4 = new VoiceGuideImage();
        seokbokimg4.imgAddr = MyApplication.getContext().getString(R.string.seokUrlG4);
        seokbokimg4.voiceimgtext = MyApplication.getContext().getString(R.string.seokUrlG4_);
        VoiceGuideLocation.voiceGuideImg.add(seokbokimg4);
        VoiceGuideImage seokbokimg5 = new VoiceGuideImage();
        seokbokimg5.imgAddr = MyApplication.getContext().getString(R.string.seokUrlG5);
        seokbokimg5.voiceimgtext = MyApplication.getContext().getString(R.string.seokUrlG5_);
        VoiceGuideLocation.voiceGuideImg.add(seokbokimg5);
        VoiceGuideImage seokbokimg6 = new VoiceGuideImage();
        seokbokimg6.imgAddr = MyApplication.getContext().getString(R.string.seokUrlG6);
        seokbokimg6.voiceimgtext = MyApplication.getContext().getString(R.string.seokUrlG6_);
        VoiceGuideLocation.voiceGuideImg.add(seokbokimg6);
        VoiceGuideImage seokbokimg7 = new VoiceGuideImage();
        seokbokimg7.imgAddr = MyApplication.getContext().getString(R.string.seokUrlG7);
        seokbokimg7.voiceimgtext = MyApplication.getContext().getString(R.string.seokUrlG7_);
        VoiceGuideLocation.voiceGuideImg.add(seokbokimg7);
        return VoiceGuideLocation;
    }

    private VoiceGuideLocation makeOkdang(VoiceGuideLocation VoiceGuideLocation){
        VoiceGuideLocation.location_title = MyApplication.getContext().getString(R.string.oktitle);
        VoiceGuideLocation.voice_addr = MyApplication.getContext().getString(R.string.okaudioaddress);
        VoiceGuideLocation.voice_title = MyApplication.getContext().getString(R.string.oksubtitle);
        VoiceGuideLocation.voice_loc_east ="126.990161";
        VoiceGuideLocation.voice_loc_north="37.578867";
        VoiceGuideLocation.voice_loc_south="37.578771";
        VoiceGuideLocation.voice_loc_west="126.989986";
        VoiceGuideLocation.location_img_size = 5;
        VoiceGuideImage okdangimg1 = new VoiceGuideImage();
        okdangimg1.imgAddr = MyApplication.getContext().getString(R.string.okUrlG1);
        okdangimg1.voiceimgtext = MyApplication.getContext().getString(R.string.okUrlG1_);
        VoiceGuideLocation.voiceGuideImg.add(okdangimg1);
        VoiceGuideImage okdangimg2 = new VoiceGuideImage();
        okdangimg2.imgAddr = MyApplication.getContext().getString(R.string.okUrlG2);
        okdangimg2.voiceimgtext = MyApplication.getContext().getString(R.string.okUrlG2_);
        VoiceGuideLocation.voiceGuideImg.add(okdangimg2);
        VoiceGuideImage okdangimg3 = new VoiceGuideImage();
        okdangimg3.imgAddr = MyApplication.getContext().getString(R.string.okUrlG3);
        okdangimg3.voiceimgtext = MyApplication.getContext().getString(R.string.okUrlG3_);
        VoiceGuideLocation.voiceGuideImg.add(okdangimg3);
        VoiceGuideImage okdangimg4 = new VoiceGuideImage();
        okdangimg4.imgAddr = MyApplication.getContext().getString(R.string.okUrlG4);
        okdangimg4.voiceimgtext = MyApplication.getContext().getString(R.string.okUrlG4_);
        VoiceGuideLocation.voiceGuideImg.add(okdangimg4);
        VoiceGuideImage okdangimg5 = new VoiceGuideImage();
        okdangimg5.imgAddr = MyApplication.getContext().getString(R.string.okUrlG5);
        okdangimg5.voiceimgtext = MyApplication.getContext().getString(R.string.okUrlG5_);
        VoiceGuideLocation.voiceGuideImg.add(okdangimg5);
        return VoiceGuideLocation;
    }

    private VoiceGuideLocation makeGyujang(VoiceGuideLocation VoiceGuideLocation){
        VoiceGuideLocation.location_title = MyApplication.getContext().getString(R.string.gyutitle);
        VoiceGuideLocation.voice_addr = MyApplication.getContext().getString(R.string.gyuaudioaddress);
        VoiceGuideLocation.voice_title = MyApplication.getContext().getString(R.string.gyusubtitle);
        VoiceGuideLocation.voice_loc_east ="126.989615";
        VoiceGuideLocation.voice_loc_north="37.579095";
        VoiceGuideLocation.voice_loc_south="37.578869";
        VoiceGuideLocation.voice_loc_west="126.989425";
        VoiceGuideLocation.location_img_size = 7;
        VoiceGuideImage gyujangimg1 = new VoiceGuideImage();
        gyujangimg1.imgAddr = MyApplication.getContext().getString(R.string.gyuUrlG1);
        gyujangimg1.voiceimgtext = MyApplication.getContext().getString(R.string.gyuUrlG1_);
        VoiceGuideLocation.voiceGuideImg.add(gyujangimg1);
        VoiceGuideImage gyujangimg2 = new VoiceGuideImage();
        gyujangimg2.imgAddr = MyApplication.getContext().getString(R.string.gyuUrlG2);
        gyujangimg2.voiceimgtext = MyApplication.getContext().getString(R.string.gyuUrlG2_);
        VoiceGuideLocation.voiceGuideImg.add(gyujangimg2);
        VoiceGuideImage gyujangimg3 = new VoiceGuideImage();
        gyujangimg3.imgAddr = MyApplication.getContext().getString(R.string.gyuUrlG3);
        gyujangimg3.voiceimgtext = MyApplication.getContext().getString(R.string.gyuUrlG3_);
        VoiceGuideLocation.voiceGuideImg.add(gyujangimg3);
        VoiceGuideImage gyujangimg4 = new VoiceGuideImage();
        gyujangimg4.imgAddr = MyApplication.getContext().getString(R.string.gyuUrlG4);
        gyujangimg4.voiceimgtext = MyApplication.getContext().getString(R.string.gyuUrlG4_);
        VoiceGuideLocation.voiceGuideImg.add(gyujangimg4);
        VoiceGuideImage gyujangimg5 = new VoiceGuideImage();
        gyujangimg5.imgAddr = MyApplication.getContext().getString(R.string.gyuUrlG5);
        gyujangimg5.voiceimgtext = MyApplication.getContext().getString(R.string.gyuUrlG5_);
        VoiceGuideLocation.voiceGuideImg.add(gyujangimg5);
        VoiceGuideImage gyujangimg6 = new VoiceGuideImage();
        gyujangimg6.imgAddr = MyApplication.getContext().getString(R.string.gyuUrlG6);
        gyujangimg6.voiceimgtext = MyApplication.getContext().getString(R.string.gyuUrlG6_);
        VoiceGuideLocation.voiceGuideImg.add(gyujangimg6);
        VoiceGuideImage gyujangimg7 = new VoiceGuideImage();
        gyujangimg7.imgAddr = MyApplication.getContext().getString(R.string.gyuUrlG7);
        gyujangimg7.voiceimgtext = MyApplication.getContext().getString(R.string.gyuUrlG7_);
        VoiceGuideLocation.voiceGuideImg.add(gyujangimg7);
        return VoiceGuideLocation;
    }
}
*/

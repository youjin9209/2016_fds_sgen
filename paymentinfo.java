package firstTest;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
/* 거래 내역(패킷으로 날라옴 )과 서울시 data api에서 받아오는 지역명 (json형)을 비교 
 전체 로직 - split[2] vs PANMAE_NM 비교 -> 같으면 -> PANMAE_NM에 해당하는 LOCPLC를 뽑아온다 -> 그걸 서버로 보내서 -> 지도 좌표 찍어
 */
public class Main {

   static String s;
   static String[] split;
   public static void main(String[] args) throws MalformedURLException, IOException, ParseException {
      // TODO Auto-generated method stub
      Scanner sc = new Scanner(new FileReader("input.txt"));
      /* input.txt내용 - 패킷으로 날라오는 거래내역 
         2016.04.29.19:17:15/씨유미아청마점/15000/0/339372
		 2016.04.29.19:18:15/GS25미아삼양점/15000/0/339372
       */
      
      /* api 불러와서 json형식으로 받아오는 데이터 내용 
        {"GB_SAFE_MEDIC_INFO":{"list_total_count":138,"RESULT":{"CODE":"INFO-000","MESSAGE":"정상 처리되었습니다"},
        "row":[{"SEQ":"1","PANMAE_NM":"GS25 수유이화점","LOCPLC":" 한천로 1055-1 (수유동)","TELNO":""},
        	   {"SEQ":"2","PANMAE_NM":"씨유 미아청마점","LOCPLC":" 솔매로 100, 1층 (미아동)","TELNO":"02-945-0715"},
        	   {"SEQ":"3","PANMAE_NM":"한국미니스톱 번동주공점","LOCPLC":" 한천로105길 33 (번동, 주공아파트1단지유치원상가)","TELNO":""},
        	   {"SEQ":"4","PANMAE_NM":"GS25 미아삼양점","LOCPLC":" 삼양로 248 (미아동)","TELNO":"02-988-8990"},
        	   {"SEQ":"5","PANMAE_NM":"(주)비지에프리테일 번동솔그린점","LOCPLC":" 오현로20길 62, 솔그린상가동 101, 102호 (번동, 솔그린상가동)","TELNO":"02-982-2224"}]}}
       */
      //split[2] vs PANMAE_NM 비교 -> 같으면 -> PANMAE_NM에 해당하는 LOCPLC를 뽑아온다 -> 그걸 서버로 보내서 -> 지도 좌표 찍어 
      while(sc.hasNextLine()){
    	  s = sc.nextLine();
    	  
    	  split = s.split("/");
    	  
	      URL url = new URL("http://openapi.seoul.go.kr:8088/4d43447779796f753336554e74444a/json/GB_SAFE_MEDIC_INFO/1/5");
	      InputStreamReader isr = new InputStreamReader(url.openConnection().getInputStream(), "UTF-8");
	      
	      JSONObject object = (JSONObject)JSONValue.parseWithException(isr);
	      JSONObject channel = (JSONObject)(object.get("GB_SAFE_MEDIC_INFO"));
	      JSONArray items = (JSONArray)channel.get("row");
	      
	      String result = null;
	      for(int i = 0; i < items.size(); i++) {
	         JSONObject obj1 = (JSONObject)items.get(i);        
	         if (obj1.get("PANMAE_NM").toString().replace(" ", "").equals(split[2])) {
	        	 result = obj1.get("LOCPLC").toString();
	        	 //삼양로 248 (미아동) -> 미아동 뽑아옴 
	        	 //System.out.println(result.substring(result.indexOf("(")+1,result.indexOf(")")));
	      
	        	 //그냥 주소 다 뽑아오고 싶을때 
	             System.out.println(result);     
	         }
	      }
      }
   }
}
      

package zerobase.Weather.service;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import zerobase.Weather.domain.DateWeather;
import zerobase.Weather.domain.Diary;
import zerobase.Weather.repository.DateWeatherRepository;
import zerobase.Weather.repository.DiaryRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class DiaryService {

    @Value("${openweathermap.key}")
    private String apiKey;

    private final DiaryRepository diaryRepository;
    private final DateWeatherRepository dateWeatherRepository;
    public DiaryService (DiaryRepository diaryRepository, DateWeatherRepository dateWeatherRepository){
        this.diaryRepository = diaryRepository;
        this.dateWeatherRepository = dateWeatherRepository;
    }

    // ------------------------------------------------------------------- //

    // ### CRUD 의 Create

    // # 쓴 일기 저장
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void createDiary(LocalDate date, String text){

        // 1. DB에서 날씨데이터 가져오기
        DateWeather dateWeather = getDateWeather(date);

        // 2. 일기 저장하기
        Diary nowDiary = new Diary();
        nowDiary.setDateWeather(dateWeather);
        nowDiary.setText(text);
        diaryRepository.save(nowDiary);
    }

    private DateWeather getDateWeather(LocalDate date){
        List<DateWeather> dateWeatherListFromDB = dateWeatherRepository.findAllByDate(date);
        // 가져오고자하는 날짜의 날씨가 존재하지 않을때
        // api로부터 날씨 데이터를 새로 받아옴
        if(dateWeatherListFromDB.size() == 0){
            // 과거의 날씨를 api로 가져오는것은 유료이기때문에
            // 정책상 현재의 날짜의 날씨를 가져옴
            return getWeatherFromApi();
        }
        // 가져오고자하는 날짜의 날씨가 존재하면
        // DB로부터 날씨 데이터를 가져옴
        return dateWeatherListFromDB.get(0);
    }



    // ------------------------------------------------------------------- //

    // ### CRUD의 Read

    // # 하루치 일기 조회
    @Transactional(readOnly = true)
    public List<Diary> readDiary(LocalDate date)    {
        return diaryRepository.findAllByDate(date);
    }

    // # 기간 범위 내 일기들 조회
    public List<Diary> readDiaries(LocalDate startDate, LocalDate endDate) {
        return diaryRepository.findAllByDateBetween(startDate, endDate);
    }

    // ------------------------------------------------------------------- //

    // ### CRUD의 Update

    // # 해당 날짜의 맨 첫번째 일기를 수정하기
    public void updateDiary(LocalDate date, String text) {
        Diary nowDiary = diaryRepository.getFirstByDate(date);
        nowDiary.setText(text);
        // 새로운 생성자를 만들지 않고 기존 id의 데이터에 set함수를 쓰면
        // 그대로 덮어씌워진다.
        diaryRepository.save(nowDiary);
    }

    // ------------------------------------------------------------------- //

    // ### CRUD의 Delete

    // # 해당 날짜의 일기 전부 삭제하기
    public void deleteDiary(LocalDate date) {
        diaryRepository.deleteAllByDate(date);
    }

    // ------------------------------------------------------------------- //

    // ### 날씨 데이터 가져오기

    // # 매일 새벽 1시마다 그날의 날씨 데이터 가져오기
    @Transactional
    @Scheduled(cron = "0 0 1 * * *")
    public void saveWeatherDate(){
        dateWeatherRepository.save(getWeatherFromApi());
    }

    // # API를 통해 날씨 데이터 가져오기
    private DateWeather getWeatherFromApi(){
        // 1. open weather map에서 API를 통해 날씨 데이터 가져오기
        String weatherData = getWheatherString();
        // 2. 받아온 날씨 데이터(json)를 파싱하기
        Map<String, Object> parsedWeather = parseWeather(weatherData);
        // 3. 파싱된 데이터 + 일기 내용을 DB에 저장
        DateWeather dateWeather = new DateWeather();
        dateWeather.setDate(LocalDate.now());
        dateWeather.setWeather(parsedWeather.get("weather").toString());
        dateWeather.setIcon(parsedWeather.get("icon").toString());
        dateWeather.setTemperature((Double) parsedWeather.get("temp"));
        return dateWeather;
    }

    // # API에 요청 보내고 응답 받기
    private String getWheatherString(){
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=seoul&appid=" + apiKey;
        try{
            // url을 http 형태로 연결시킴
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode(); // 응답코드
            BufferedReader br;
            if(responseCode == 200){
                // 응답한 내용(서버로부터 가져온 데이터)을 객체 br에 저장
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                // 오류 메시지를 br에 저장
                br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }
            // 내용을 한 줄 씩 response 객체에 담고 리턴
            String inputLine;
            StringBuilder response = new StringBuilder();
            while((inputLine = br.readLine()) != null){
                response.append(inputLine);
            }
            br.close();
            return response.toString();

        } catch (Exception e){
            return "failed to get response";
        }
    }

    // # 받아온 String객체를 json객체로 파싱하기
    private Map<String, Object> parseWeather(String jsonString){
        JSONParser jsonParser = new JSONParser(); // json.simple패키지로 가져오기
        JSONObject jsonObject;

        // String 객체를 JSONObject타입 객체로 변환
        try{
            jsonObject = (JSONObject) jsonParser.parse(jsonString);
        } catch (ParseException e){
            // 예외를 처리하지 않고 그냥 컴파일 에러 처리
            throw new RuntimeException(e);
        }

        // 해쉬테이블에 객체 jsonObject로부터 필요한 정보만 담기
        Map<String, Object> resultMap = new HashMap<>();
        JSONObject mainData = (JSONObject) jsonObject.get("main");
        resultMap.put("temp", mainData.get("temp"));
        JSONArray jsonArray = (JSONArray) jsonObject.get("weather");
        JSONObject weatherData = (JSONObject) jsonArray.get(0);
        resultMap.put("weather", weatherData.get("main"));
        resultMap.put("icon", weatherData.get("icon"));
        return resultMap;
    }
}
























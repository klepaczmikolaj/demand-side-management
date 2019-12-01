package pl.wut.wsd.dsm.agent.network_advisor.infrastructure.json

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import pl.wut.wsd.dsm.agent.network_advisor.weather.model.WeatherForecast
import spock.lang.Specification

class JsonParserTest extends Specification {

    private Gson gson = new GsonBuilder().create()

    def 'Should properly parse forecastJson'() {
        when:
        def result = gson.fromJson(forecastJson, WeatherForecast.class)

        then:
        noExceptionThrown()
        result.code == '200'

    }


    private final String forecastJson = "{\"cod\": \"200\",\n" +
            "\t\"message\": 0,\n" +
            "\t\"cnt\": 40,\n" +
            "\t\"list\": [{\n" +
            "\t\t\t\"dt\": 1575158400,\n" +
            "\t\t\t\"main\": {\n" +
            "\t\t\t\t\"temp\": 2.71,\n" +
            "\t\t\t\t\"temp_min\": 2.71,\n" +
            "\t\t\t\t\"temp_max\": 5.21,\n" +
            "\t\t\t\t\"pressure\": 1021,\n" +
            "\t\t\t\t\"sea_level\": 1021,\n" +
            "\t\t\t\t\"grnd_level\": 1017,\n" +
            "\t\t\t\t\"humidity\": 72,\n" +
            "\t\t\t\t\"temp_kf\": -2.5\n" +
            "\t\t\t},\n" +
            "\t\t\t\"weather\": [{\n" +
            "\t\t\t\t\t\"id\": 803,\n" +
            "\t\t\t\t\t\"main\": \"Clouds\",\n" +
            "\t\t\t\t\t\"description\": \"broken clouds\",\n" +
            "\t\t\t\t\t\"icon\": \"04n\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"clouds\": {\n" +
            "\t\t\t\t\"all\": 79\n" +
            "\t\t\t},\n" +
            "\t\t\t\"wind\": {\n" +
            "\t\t\t\t\"speed\": 2.64,\n" +
            "\t\t\t\t\"deg\": 65\n" +
            "\t\t\t},\n" +
            "\t\t\t\"sys\": {\n" +
            "\t\t\t\t\"pod\": \"n\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"dt_txt\": \"2019-12-01 00:00:00\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"dt\": 1575169200,\n" +
            "\t\t\t\"main\": {\n" +
            "\t\t\t\t\"temp\": 3.05,\n" +
            "\t\t\t\t\"temp_min\": 3.05,\n" +
            "\t\t\t\t\"temp_max\": 4.93,\n" +
            "\t\t\t\t\"pressure\": 1020,\n" +
            "\t\t\t\t\"sea_level\": 1020,\n" +
            "\t\t\t\t\"grnd_level\": 1016,\n" +
            "\t\t\t\t\"humidity\": 76,\n" +
            "\t\t\t\t\"temp_kf\": -1.88\n" +
            "\t\t\t},\n" +
            "\t\t\t\"weather\": [{\n" +
            "\t\t\t\t\t\"id\": 804,\n" +
            "\t\t\t\t\t\"main\": \"Clouds\",\n" +
            "\t\t\t\t\t\"description\": \"overcast clouds\",\n" +
            "\t\t\t\t\t\"icon\": \"04n\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"clouds\": {\n" +
            "\t\t\t\t\"all\": 98\n" +
            "\t\t\t},\n" +
            "\t\t\t\"wind\": {\n" +
            "\t\t\t\t\"speed\": 3.09,\n" +
            "\t\t\t\t\"deg\": 46\n" +
            "\t\t\t},\n" +
            "\t\t\t\"sys\": {\n" +
            "\t\t\t\t\"pod\": \"n\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"dt_txt\": \"2019-12-01 03:00:00\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"dt\": 1575180000,\n" +
            "\t\t\t\"main\": {\n" +
            "\t\t\t\t\"temp\": 3.37,\n" +
            "\t\t\t\t\"temp_min\": 3.37,\n" +
            "\t\t\t\t\"temp_max\": 4.62,\n" +
            "\t\t\t\t\"pressure\": 1021,\n" +
            "\t\t\t\t\"sea_level\": 1021,\n" +
            "\t\t\t\t\"grnd_level\": 1017,\n" +
            "\t\t\t\t\"humidity\": 76,\n" +
            "\t\t\t\t\"temp_kf\": -1.25\n" +
            "\t\t\t},\n" +
            "\t\t\t\"weather\": [{\n" +
            "\t\t\t\t\t\"id\": 803,\n" +
            "\t\t\t\t\t\"main\": \"Clouds\",\n" +
            "\t\t\t\t\t\"description\": \"broken clouds\",\n" +
            "\t\t\t\t\t\"icon\": \"04n\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"clouds\": {\n" +
            "\t\t\t\t\"all\": 51\n" +
            "\t\t\t},\n" +
            "\t\t\t\"wind\": {\n" +
            "\t\t\t\t\"speed\": 3.52,\n" +
            "\t\t\t\t\"deg\": 41\n" +
            "\t\t\t},\n" +
            "\t\t\t\"sys\": {\n" +
            "\t\t\t\t\"pod\": \"n\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"dt_txt\": \"2019-12-01 06:00:00\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"dt\": 1575190800,\n" +
            "\t\t\t\"main\": {\n" +
            "\t\t\t\t\"temp\": 5.28,\n" +
            "\t\t\t\t\"temp_min\": 5.28,\n" +
            "\t\t\t\t\"temp_max\": 5.91,\n" +
            "\t\t\t\t\"pressure\": 1023,\n" +
            "\t\t\t\t\"sea_level\": 1023,\n" +
            "\t\t\t\t\"grnd_level\": 1018,\n" +
            "\t\t\t\t\"humidity\": 69,\n" +
            "\t\t\t\t\"temp_kf\": -0.63\n" +
            "\t\t\t},\n" +
            "\t\t\t\"weather\": [{\n" +
            "\t\t\t\t\t\"id\": 803,\n" +
            "\t\t\t\t\t\"main\": \"Clouds\",\n" +
            "\t\t\t\t\t\"description\": \"broken clouds\",\n" +
            "\t\t\t\t\t\"icon\": \"04d\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"clouds\": {\n" +
            "\t\t\t\t\"all\": 69\n" +
            "\t\t\t},\n" +
            "\t\t\t\"wind\": {\n" +
            "\t\t\t\t\"speed\": 4.32,\n" +
            "\t\t\t\t\"deg\": 34\n" +
            "\t\t\t},\n" +
            "\t\t\t\"sys\": {\n" +
            "\t\t\t\t\"pod\": \"d\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"dt_txt\": \"2019-12-01 09:00:00\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"dt\": 1575201600,\n" +
            "\t\t\t\"main\": {\n" +
            "\t\t\t\t\"temp\": 7.54,\n" +
            "\t\t\t\t\"temp_min\": 7.54,\n" +
            "\t\t\t\t\"temp_max\": 7.54,\n" +
            "\t\t\t\t\"pressure\": 1024,\n" +
            "\t\t\t\t\"sea_level\": 1024,\n" +
            "\t\t\t\t\"grnd_level\": 1019,\n" +
            "\t\t\t\t\"humidity\": 52,\n" +
            "\t\t\t\t\"temp_kf\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"weather\": [{\n" +
            "\t\t\t\t\t\"id\": 803,\n" +
            "\t\t\t\t\t\"main\": \"Clouds\",\n" +
            "\t\t\t\t\t\"description\": \"broken clouds\",\n" +
            "\t\t\t\t\t\"icon\": \"04d\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"clouds\": {\n" +
            "\t\t\t\t\"all\": 57\n" +
            "\t\t\t},\n" +
            "\t\t\t\"wind\": {\n" +
            "\t\t\t\t\"speed\": 5.21,\n" +
            "\t\t\t\t\"deg\": 32\n" +
            "\t\t\t},\n" +
            "\t\t\t\"sys\": {\n" +
            "\t\t\t\t\"pod\": \"d\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"dt_txt\": \"2019-12-01 12:00:00\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"dt\": 1575212400,\n" +
            "\t\t\t\"main\": {\n" +
            "\t\t\t\t\"temp\": 6.86,\n" +
            "\t\t\t\t\"temp_min\": 6.86,\n" +
            "\t\t\t\t\"temp_max\": 6.86,\n" +
            "\t\t\t\t\"pressure\": 1024,\n" +
            "\t\t\t\t\"sea_level\": 1024,\n" +
            "\t\t\t\t\"grnd_level\": 1019,\n" +
            "\t\t\t\t\"humidity\": 65,\n" +
            "\t\t\t\t\"temp_kf\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"weather\": [{\n" +
            "\t\t\t\t\t\"id\": 800,\n" +
            "\t\t\t\t\t\"main\": \"Clear\",\n" +
            "\t\t\t\t\t\"description\": \"clear sky\",\n" +
            "\t\t\t\t\t\"icon\": \"01d\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"clouds\": {\n" +
            "\t\t\t\t\"all\": 5\n" +
            "\t\t\t},\n" +
            "\t\t\t\"wind\": {\n" +
            "\t\t\t\t\"speed\": 4.7,\n" +
            "\t\t\t\t\"deg\": 17\n" +
            "\t\t\t},\n" +
            "\t\t\t\"sys\": {\n" +
            "\t\t\t\t\"pod\": \"d\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"dt_txt\": \"2019-12-01 15:00:00\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"dt\": 1575223200,\n" +
            "\t\t\t\"main\": {\n" +
            "\t\t\t\t\"temp\": 6.1,\n" +
            "\t\t\t\t\"temp_min\": 6.1,\n" +
            "\t\t\t\t\"temp_max\": 6.1,\n" +
            "\t\t\t\t\"pressure\": 1026,\n" +
            "\t\t\t\t\"sea_level\": 1026,\n" +
            "\t\t\t\t\"grnd_level\": 1022,\n" +
            "\t\t\t\t\"humidity\": 71,\n" +
            "\t\t\t\t\"temp_kf\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"weather\": [{\n" +
            "\t\t\t\t\t\"id\": 803,\n" +
            "\t\t\t\t\t\"main\": \"Clouds\",\n" +
            "\t\t\t\t\t\"description\": \"broken clouds\",\n" +
            "\t\t\t\t\t\"icon\": \"04n\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"clouds\": {\n" +
            "\t\t\t\t\"all\": 53\n" +
            "\t\t\t},\n" +
            "\t\t\t\"wind\": {\n" +
            "\t\t\t\t\"speed\": 5.51,\n" +
            "\t\t\t\t\"deg\": 18\n" +
            "\t\t\t},\n" +
            "\t\t\t\"sys\": {\n" +
            "\t\t\t\t\"pod\": \"n\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"dt_txt\": \"2019-12-01 18:00:00\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"dt\": 1575234000,\n" +
            "\t\t\t\"main\": {\n" +
            "\t\t\t\t\"temp\": 4.25,\n" +
            "\t\t\t\t\"temp_min\": 4.25,\n" +
            "\t\t\t\t\"temp_max\": 4.25,\n" +
            "\t\t\t\t\"pressure\": 1028,\n" +
            "\t\t\t\t\"sea_level\": 1028,\n" +
            "\t\t\t\t\"grnd_level\": 1023,\n" +
            "\t\t\t\t\"humidity\": 82,\n" +
            "\t\t\t\t\"temp_kf\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"weather\": [{\n" +
            "\t\t\t\t\t\"id\": 801,\n" +
            "\t\t\t\t\t\"main\": \"Clouds\",\n" +
            "\t\t\t\t\t\"description\": \"few clouds\",\n" +
            "\t\t\t\t\t\"icon\": \"02n\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"clouds\": {\n" +
            "\t\t\t\t\"all\": 21\n" +
            "\t\t\t},\n" +
            "\t\t\t\"wind\": {\n" +
            "\t\t\t\t\"speed\": 4.12,\n" +
            "\t\t\t\t\"deg\": 5\n" +
            "\t\t\t},\n" +
            "\t\t\t\"sys\": {\n" +
            "\t\t\t\t\"pod\": \"n\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"dt_txt\": \"2019-12-01 21:00:00\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"dt\": 1575244800,\n" +
            "\t\t\t\"main\": {\n" +
            "\t\t\t\t\"temp\": 3.61,\n" +
            "\t\t\t\t\"temp_min\": 3.61,\n" +
            "\t\t\t\t\"temp_max\": 3.61,\n" +
            "\t\t\t\t\"pressure\": 1029,\n" +
            "\t\t\t\t\"sea_level\": 1029,\n" +
            "\t\t\t\t\"grnd_level\": 1025,\n" +
            "\t\t\t\t\"humidity\": 75,\n" +
            "\t\t\t\t\"temp_kf\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"weather\": [{\n" +
            "\t\t\t\t\t\"id\": 800,\n" +
            "\t\t\t\t\t\"main\": \"Clear\",\n" +
            "\t\t\t\t\t\"description\": \"clear sky\",\n" +
            "\t\t\t\t\t\"icon\": \"01n\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"clouds\": {\n" +
            "\t\t\t\t\"all\": 10\n" +
            "\t\t\t},\n" +
            "\t\t\t\"wind\": {\n" +
            "\t\t\t\t\"speed\": 3.72,\n" +
            "\t\t\t\t\"deg\": 353\n" +
            "\t\t\t},\n" +
            "\t\t\t\"sys\": {\n" +
            "\t\t\t\t\"pod\": \"n\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"dt_txt\": \"2019-12-02 00:00:00\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"dt\": 1575255600,\n" +
            "\t\t\t\"main\": {\n" +
            "\t\t\t\t\"temp\": 2.39,\n" +
            "\t\t\t\t\"temp_min\": 2.39,\n" +
            "\t\t\t\t\"temp_max\": 2.39,\n" +
            "\t\t\t\t\"pressure\": 1029,\n" +
            "\t\t\t\t\"sea_level\": 1029,\n" +
            "\t\t\t\t\"grnd_level\": 1025,\n" +
            "\t\t\t\t\"humidity\": 81,\n" +
            "\t\t\t\t\"temp_kf\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"weather\": [{\n" +
            "\t\t\t\t\t\"id\": 800,\n" +
            "\t\t\t\t\t\"main\": \"Clear\",\n" +
            "\t\t\t\t\t\"description\": \"clear sky\",\n" +
            "\t\t\t\t\t\"icon\": \"01n\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"clouds\": {\n" +
            "\t\t\t\t\"all\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"wind\": {\n" +
            "\t\t\t\t\"speed\": 3.16,\n" +
            "\t\t\t\t\"deg\": 346\n" +
            "\t\t\t},\n" +
            "\t\t\t\"sys\": {\n" +
            "\t\t\t\t\"pod\": \"n\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"dt_txt\": \"2019-12-02 03:00:00\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"dt\": 1575266400,\n" +
            "\t\t\t\"main\": {\n" +
            "\t\t\t\t\"temp\": 2.38,\n" +
            "\t\t\t\t\"temp_min\": 2.38,\n" +
            "\t\t\t\t\"temp_max\": 2.38,\n" +
            "\t\t\t\t\"pressure\": 1031,\n" +
            "\t\t\t\t\"sea_level\": 1031,\n" +
            "\t\t\t\t\"grnd_level\": 1027,\n" +
            "\t\t\t\t\"humidity\": 82,\n" +
            "\t\t\t\t\"temp_kf\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"weather\": [{\n" +
            "\t\t\t\t\t\"id\": 800,\n" +
            "\t\t\t\t\t\"main\": \"Clear\",\n" +
            "\t\t\t\t\t\"description\": \"clear sky\",\n" +
            "\t\t\t\t\t\"icon\": \"01n\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"clouds\": {\n" +
            "\t\t\t\t\"all\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"wind\": {\n" +
            "\t\t\t\t\"speed\": 2.59,\n" +
            "\t\t\t\t\"deg\": 341\n" +
            "\t\t\t},\n" +
            "\t\t\t\"sys\": {\n" +
            "\t\t\t\t\"pod\": \"n\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"dt_txt\": \"2019-12-02 06:00:00\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"dt\": 1575277200,\n" +
            "\t\t\t\"main\": {\n" +
            "\t\t\t\t\"temp\": 2.72,\n" +
            "\t\t\t\t\"temp_min\": 2.72,\n" +
            "\t\t\t\t\"temp_max\": 2.72,\n" +
            "\t\t\t\t\"pressure\": 1032,\n" +
            "\t\t\t\t\"sea_level\": 1032,\n" +
            "\t\t\t\t\"grnd_level\": 1028,\n" +
            "\t\t\t\t\"humidity\": 72,\n" +
            "\t\t\t\t\"temp_kf\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"weather\": [{\n" +
            "\t\t\t\t\t\"id\": 800,\n" +
            "\t\t\t\t\t\"main\": \"Clear\",\n" +
            "\t\t\t\t\t\"description\": \"clear sky\",\n" +
            "\t\t\t\t\t\"icon\": \"01d\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"clouds\": {\n" +
            "\t\t\t\t\"all\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"wind\": {\n" +
            "\t\t\t\t\"speed\": 1.98,\n" +
            "\t\t\t\t\"deg\": 324\n" +
            "\t\t\t},\n" +
            "\t\t\t\"sys\": {\n" +
            "\t\t\t\t\"pod\": \"d\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"dt_txt\": \"2019-12-02 09:00:00\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"dt\": 1575288000,\n" +
            "\t\t\t\"main\": {\n" +
            "\t\t\t\t\"temp\": 5.28,\n" +
            "\t\t\t\t\"temp_min\": 5.28,\n" +
            "\t\t\t\t\"temp_max\": 5.28,\n" +
            "\t\t\t\t\"pressure\": 1032,\n" +
            "\t\t\t\t\"sea_level\": 1032,\n" +
            "\t\t\t\t\"grnd_level\": 1028,\n" +
            "\t\t\t\t\"humidity\": 58,\n" +
            "\t\t\t\t\"temp_kf\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"weather\": [{\n" +
            "\t\t\t\t\t\"id\": 800,\n" +
            "\t\t\t\t\t\"main\": \"Clear\",\n" +
            "\t\t\t\t\t\"description\": \"clear sky\",\n" +
            "\t\t\t\t\t\"icon\": \"01d\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"clouds\": {\n" +
            "\t\t\t\t\"all\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"wind\": {\n" +
            "\t\t\t\t\"speed\": 1.96,\n" +
            "\t\t\t\t\"deg\": 294\n" +
            "\t\t\t},\n" +
            "\t\t\t\"sys\": {\n" +
            "\t\t\t\t\"pod\": \"d\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"dt_txt\": \"2019-12-02 12:00:00\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"dt\": 1575298800,\n" +
            "\t\t\t\"main\": {\n" +
            "\t\t\t\t\"temp\": 5.59,\n" +
            "\t\t\t\t\"temp_min\": 5.59,\n" +
            "\t\t\t\t\"temp_max\": 5.59,\n" +
            "\t\t\t\t\"pressure\": 1031,\n" +
            "\t\t\t\t\"sea_level\": 1031,\n" +
            "\t\t\t\t\"grnd_level\": 1027,\n" +
            "\t\t\t\t\"humidity\": 63,\n" +
            "\t\t\t\t\"temp_kf\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"weather\": [{\n" +
            "\t\t\t\t\t\"id\": 800,\n" +
            "\t\t\t\t\t\"main\": \"Clear\",\n" +
            "\t\t\t\t\t\"description\": \"clear sky\",\n" +
            "\t\t\t\t\t\"icon\": \"01d\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"clouds\": {\n" +
            "\t\t\t\t\"all\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"wind\": {\n" +
            "\t\t\t\t\"speed\": 1.82,\n" +
            "\t\t\t\t\"deg\": 289\n" +
            "\t\t\t},\n" +
            "\t\t\t\"sys\": {\n" +
            "\t\t\t\t\"pod\": \"d\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"dt_txt\": \"2019-12-02 15:00:00\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"dt\": 1575309600,\n" +
            "\t\t\t\"main\": {\n" +
            "\t\t\t\t\"temp\": 4,\n" +
            "\t\t\t\t\"temp_min\": 4,\n" +
            "\t\t\t\t\"temp_max\": 4,\n" +
            "\t\t\t\t\"pressure\": 1032,\n" +
            "\t\t\t\t\"sea_level\": 1032,\n" +
            "\t\t\t\t\"grnd_level\": 1027,\n" +
            "\t\t\t\t\"humidity\": 79,\n" +
            "\t\t\t\t\"temp_kf\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"weather\": [{\n" +
            "\t\t\t\t\t\"id\": 800,\n" +
            "\t\t\t\t\t\"main\": \"Clear\",\n" +
            "\t\t\t\t\t\"description\": \"clear sky\",\n" +
            "\t\t\t\t\t\"icon\": \"01n\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"clouds\": {\n" +
            "\t\t\t\t\"all\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"wind\": {\n" +
            "\t\t\t\t\"speed\": 1.68,\n" +
            "\t\t\t\t\"deg\": 302\n" +
            "\t\t\t},\n" +
            "\t\t\t\"sys\": {\n" +
            "\t\t\t\t\"pod\": \"n\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"dt_txt\": \"2019-12-02 18:00:00\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"dt\": 1575320400,\n" +
            "\t\t\t\"main\": {\n" +
            "\t\t\t\t\"temp\": 3.28,\n" +
            "\t\t\t\t\"temp_min\": 3.28,\n" +
            "\t\t\t\t\"temp_max\": 3.28,\n" +
            "\t\t\t\t\"pressure\": 1032,\n" +
            "\t\t\t\t\"sea_level\": 1032,\n" +
            "\t\t\t\t\"grnd_level\": 1028,\n" +
            "\t\t\t\t\"humidity\": 83,\n" +
            "\t\t\t\t\"temp_kf\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"weather\": [{\n" +
            "\t\t\t\t\t\"id\": 800,\n" +
            "\t\t\t\t\t\"main\": \"Clear\",\n" +
            "\t\t\t\t\t\"description\": \"clear sky\",\n" +
            "\t\t\t\t\t\"icon\": \"01n\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"clouds\": {\n" +
            "\t\t\t\t\"all\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"wind\": {\n" +
            "\t\t\t\t\"speed\": 1.31,\n" +
            "\t\t\t\t\"deg\": 301\n" +
            "\t\t\t},\n" +
            "\t\t\t\"sys\": {\n" +
            "\t\t\t\t\"pod\": \"n\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"dt_txt\": \"2019-12-02 21:00:00\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"dt\": 1575331200,\n" +
            "\t\t\t\"main\": {\n" +
            "\t\t\t\t\"temp\": 2.94,\n" +
            "\t\t\t\t\"temp_min\": 2.94,\n" +
            "\t\t\t\t\"temp_max\": 2.94,\n" +
            "\t\t\t\t\"pressure\": 1032,\n" +
            "\t\t\t\t\"sea_level\": 1032,\n" +
            "\t\t\t\t\"grnd_level\": 1028,\n" +
            "\t\t\t\t\"humidity\": 86,\n" +
            "\t\t\t\t\"temp_kf\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"weather\": [{\n" +
            "\t\t\t\t\t\"id\": 800,\n" +
            "\t\t\t\t\t\"main\": \"Clear\",\n" +
            "\t\t\t\t\t\"description\": \"clear sky\",\n" +
            "\t\t\t\t\t\"icon\": \"01n\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"clouds\": {\n" +
            "\t\t\t\t\"all\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"wind\": {\n" +
            "\t\t\t\t\"speed\": 1.12,\n" +
            "\t\t\t\t\"deg\": 302\n" +
            "\t\t\t},\n" +
            "\t\t\t\"sys\": {\n" +
            "\t\t\t\t\"pod\": \"n\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"dt_txt\": \"2019-12-03 00:00:00\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"dt\": 1575342000,\n" +
            "\t\t\t\"main\": {\n" +
            "\t\t\t\t\"temp\": 2.67,\n" +
            "\t\t\t\t\"temp_min\": 2.67,\n" +
            "\t\t\t\t\"temp_max\": 2.67,\n" +
            "\t\t\t\t\"pressure\": 1031,\n" +
            "\t\t\t\t\"sea_level\": 1031,\n" +
            "\t\t\t\t\"grnd_level\": 1027,\n" +
            "\t\t\t\t\"humidity\": 87,\n" +
            "\t\t\t\t\"temp_kf\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"weather\": [{\n" +
            "\t\t\t\t\t\"id\": 800,\n" +
            "\t\t\t\t\t\"main\": \"Clear\",\n" +
            "\t\t\t\t\t\"description\": \"clear sky\",\n" +
            "\t\t\t\t\t\"icon\": \"01n\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"clouds\": {\n" +
            "\t\t\t\t\"all\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"wind\": {\n" +
            "\t\t\t\t\"speed\": 0.77,\n" +
            "\t\t\t\t\"deg\": 275\n" +
            "\t\t\t},\n" +
            "\t\t\t\"sys\": {\n" +
            "\t\t\t\t\"pod\": \"n\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"dt_txt\": \"2019-12-03 03:00:00\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"dt\": 1575352800,\n" +
            "\t\t\t\"main\": {\n" +
            "\t\t\t\t\"temp\": 2.45,\n" +
            "\t\t\t\t\"temp_min\": 2.45,\n" +
            "\t\t\t\t\"temp_max\": 2.45,\n" +
            "\t\t\t\t\"pressure\": 1030,\n" +
            "\t\t\t\t\"sea_level\": 1030,\n" +
            "\t\t\t\t\"grnd_level\": 1026,\n" +
            "\t\t\t\t\"humidity\": 88,\n" +
            "\t\t\t\t\"temp_kf\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"weather\": [{\n" +
            "\t\t\t\t\t\"id\": 800,\n" +
            "\t\t\t\t\t\"main\": \"Clear\",\n" +
            "\t\t\t\t\t\"description\": \"clear sky\",\n" +
            "\t\t\t\t\t\"icon\": \"01n\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"clouds\": {\n" +
            "\t\t\t\t\"all\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"wind\": {\n" +
            "\t\t\t\t\"speed\": 0.89,\n" +
            "\t\t\t\t\"deg\": 235\n" +
            "\t\t\t},\n" +
            "\t\t\t\"sys\": {\n" +
            "\t\t\t\t\"pod\": \"n\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"dt_txt\": \"2019-12-03 06:00:00\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"dt\": 1575363600,\n" +
            "\t\t\t\"main\": {\n" +
            "\t\t\t\t\"temp\": 2.98,\n" +
            "\t\t\t\t\"temp_min\": 2.98,\n" +
            "\t\t\t\t\"temp_max\": 2.98,\n" +
            "\t\t\t\t\"pressure\": 1030,\n" +
            "\t\t\t\t\"sea_level\": 1030,\n" +
            "\t\t\t\t\"grnd_level\": 1026,\n" +
            "\t\t\t\t\"humidity\": 83,\n" +
            "\t\t\t\t\"temp_kf\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"weather\": [{\n" +
            "\t\t\t\t\t\"id\": 800,\n" +
            "\t\t\t\t\t\"main\": \"Clear\",\n" +
            "\t\t\t\t\t\"description\": \"clear sky\",\n" +
            "\t\t\t\t\t\"icon\": \"01d\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"clouds\": {\n" +
            "\t\t\t\t\"all\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"wind\": {\n" +
            "\t\t\t\t\"speed\": 0.85,\n" +
            "\t\t\t\t\"deg\": 237\n" +
            "\t\t\t},\n" +
            "\t\t\t\"sys\": {\n" +
            "\t\t\t\t\"pod\": \"d\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"dt_txt\": \"2019-12-03 09:00:00\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"dt\": 1575374400,\n" +
            "\t\t\t\"main\": {\n" +
            "\t\t\t\t\"temp\": 5.78,\n" +
            "\t\t\t\t\"temp_min\": 5.78,\n" +
            "\t\t\t\t\"temp_max\": 5.78,\n" +
            "\t\t\t\t\"pressure\": 1029,\n" +
            "\t\t\t\t\"sea_level\": 1029,\n" +
            "\t\t\t\t\"grnd_level\": 1025,\n" +
            "\t\t\t\t\"humidity\": 62,\n" +
            "\t\t\t\t\"temp_kf\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"weather\": [{\n" +
            "\t\t\t\t\t\"id\": 800,\n" +
            "\t\t\t\t\t\"main\": \"Clear\",\n" +
            "\t\t\t\t\t\"description\": \"clear sky\",\n" +
            "\t\t\t\t\t\"icon\": \"01d\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"clouds\": {\n" +
            "\t\t\t\t\"all\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"wind\": {\n" +
            "\t\t\t\t\"speed\": 1.44,\n" +
            "\t\t\t\t\"deg\": 210\n" +
            "\t\t\t},\n" +
            "\t\t\t\"sys\": {\n" +
            "\t\t\t\t\"pod\": \"d\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"dt_txt\": \"2019-12-03 12:00:00\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"dt\": 1575385200,\n" +
            "\t\t\t\"main\": {\n" +
            "\t\t\t\t\"temp\": 5.77,\n" +
            "\t\t\t\t\"temp_min\": 5.77,\n" +
            "\t\t\t\t\"temp_max\": 5.77,\n" +
            "\t\t\t\t\"pressure\": 1028,\n" +
            "\t\t\t\t\"sea_level\": 1028,\n" +
            "\t\t\t\t\"grnd_level\": 1023,\n" +
            "\t\t\t\t\"humidity\": 62,\n" +
            "\t\t\t\t\"temp_kf\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"weather\": [{\n" +
            "\t\t\t\t\t\"id\": 800,\n" +
            "\t\t\t\t\t\"main\": \"Clear\",\n" +
            "\t\t\t\t\t\"description\": \"clear sky\",\n" +
            "\t\t\t\t\t\"icon\": \"01d\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"clouds\": {\n" +
            "\t\t\t\t\"all\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"wind\": {\n" +
            "\t\t\t\t\"speed\": 1.23,\n" +
            "\t\t\t\t\"deg\": 203\n" +
            "\t\t\t},\n" +
            "\t\t\t\"sys\": {\n" +
            "\t\t\t\t\"pod\": \"d\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"dt_txt\": \"2019-12-03 15:00:00\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"dt\": 1575396000,\n" +
            "\t\t\t\"main\": {\n" +
            "\t\t\t\t\"temp\": 4.37,\n" +
            "\t\t\t\t\"temp_min\": 4.37,\n" +
            "\t\t\t\t\"temp_max\": 4.37,\n" +
            "\t\t\t\t\"pressure\": 1027,\n" +
            "\t\t\t\t\"sea_level\": 1027,\n" +
            "\t\t\t\t\"grnd_level\": 1023,\n" +
            "\t\t\t\t\"humidity\": 77,\n" +
            "\t\t\t\t\"temp_kf\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"weather\": [{\n" +
            "\t\t\t\t\t\"id\": 800,\n" +
            "\t\t\t\t\t\"main\": \"Clear\",\n" +
            "\t\t\t\t\t\"description\": \"clear sky\",\n" +
            "\t\t\t\t\t\"icon\": \"01n\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"clouds\": {\n" +
            "\t\t\t\t\"all\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"wind\": {\n" +
            "\t\t\t\t\"speed\": 1.43,\n" +
            "\t\t\t\t\"deg\": 180\n" +
            "\t\t\t},\n" +
            "\t\t\t\"sys\": {\n" +
            "\t\t\t\t\"pod\": \"n\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"dt_txt\": \"2019-12-03 18:00:00\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"dt\": 1575406800,\n" +
            "\t\t\t\"main\": {\n" +
            "\t\t\t\t\"temp\": 3.91,\n" +
            "\t\t\t\t\"temp_min\": 3.91,\n" +
            "\t\t\t\t\"temp_max\": 3.91,\n" +
            "\t\t\t\t\"pressure\": 1026,\n" +
            "\t\t\t\t\"sea_level\": 1026,\n" +
            "\t\t\t\t\"grnd_level\": 1022,\n" +
            "\t\t\t\t\"humidity\": 79,\n" +
            "\t\t\t\t\"temp_kf\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"weather\": [{\n" +
            "\t\t\t\t\t\"id\": 800,\n" +
            "\t\t\t\t\t\"main\": \"Clear\",\n" +
            "\t\t\t\t\t\"description\": \"clear sky\",\n" +
            "\t\t\t\t\t\"icon\": \"01n\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"clouds\": {\n" +
            "\t\t\t\t\"all\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"wind\": {\n" +
            "\t\t\t\t\"speed\": 1.44,\n" +
            "\t\t\t\t\"deg\": 176\n" +
            "\t\t\t},\n" +
            "\t\t\t\"sys\": {\n" +
            "\t\t\t\t\"pod\": \"n\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"dt_txt\": \"2019-12-03 21:00:00\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"dt\": 1575417600,\n" +
            "\t\t\t\"main\": {\n" +
            "\t\t\t\t\"temp\": 3.58,\n" +
            "\t\t\t\t\"temp_min\": 3.58,\n" +
            "\t\t\t\t\"temp_max\": 3.58,\n" +
            "\t\t\t\t\"pressure\": 1025,\n" +
            "\t\t\t\t\"sea_level\": 1025,\n" +
            "\t\t\t\t\"grnd_level\": 1021,\n" +
            "\t\t\t\t\"humidity\": 85,\n" +
            "\t\t\t\t\"temp_kf\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"weather\": [{\n" +
            "\t\t\t\t\t\"id\": 800,\n" +
            "\t\t\t\t\t\"main\": \"Clear\",\n" +
            "\t\t\t\t\t\"description\": \"clear sky\",\n" +
            "\t\t\t\t\t\"icon\": \"01n\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"clouds\": {\n" +
            "\t\t\t\t\"all\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"wind\": {\n" +
            "\t\t\t\t\"speed\": 1.27,\n" +
            "\t\t\t\t\"deg\": 187\n" +
            "\t\t\t},\n" +
            "\t\t\t\"sys\": {\n" +
            "\t\t\t\t\"pod\": \"n\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"dt_txt\": \"2019-12-04 00:00:00\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"dt\": 1575428400,\n" +
            "\t\t\t\"main\": {\n" +
            "\t\t\t\t\"temp\": 3.36,\n" +
            "\t\t\t\t\"temp_min\": 3.36,\n" +
            "\t\t\t\t\"temp_max\": 3.36,\n" +
            "\t\t\t\t\"pressure\": 1024,\n" +
            "\t\t\t\t\"sea_level\": 1024,\n" +
            "\t\t\t\t\"grnd_level\": 1020,\n" +
            "\t\t\t\t\"humidity\": 86,\n" +
            "\t\t\t\t\"temp_kf\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"weather\": [{\n" +
            "\t\t\t\t\t\"id\": 800,\n" +
            "\t\t\t\t\t\"main\": \"Clear\",\n" +
            "\t\t\t\t\t\"description\": \"clear sky\",\n" +
            "\t\t\t\t\t\"icon\": \"01n\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"clouds\": {\n" +
            "\t\t\t\t\"all\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"wind\": {\n" +
            "\t\t\t\t\"speed\": 1.12,\n" +
            "\t\t\t\t\"deg\": 164\n" +
            "\t\t\t},\n" +
            "\t\t\t\"sys\": {\n" +
            "\t\t\t\t\"pod\": \"n\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"dt_txt\": \"2019-12-04 03:00:00\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"dt\": 1575439200,\n" +
            "\t\t\t\"main\": {\n" +
            "\t\t\t\t\"temp\": 3.15,\n" +
            "\t\t\t\t\"temp_min\": 3.15,\n" +
            "\t\t\t\t\"temp_max\": 3.15,\n" +
            "\t\t\t\t\"pressure\": 1023,\n" +
            "\t\t\t\t\"sea_level\": 1023,\n" +
            "\t\t\t\t\"grnd_level\": 1018,\n" +
            "\t\t\t\t\"humidity\": 91,\n" +
            "\t\t\t\t\"temp_kf\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"weather\": [{\n" +
            "\t\t\t\t\t\"id\": 800,\n" +
            "\t\t\t\t\t\"main\": \"Clear\",\n" +
            "\t\t\t\t\t\"description\": \"clear sky\",\n" +
            "\t\t\t\t\t\"icon\": \"01n\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"clouds\": {\n" +
            "\t\t\t\t\"all\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"wind\": {\n" +
            "\t\t\t\t\"speed\": 1.25,\n" +
            "\t\t\t\t\"deg\": 157\n" +
            "\t\t\t},\n" +
            "\t\t\t\"sys\": {\n" +
            "\t\t\t\t\"pod\": \"n\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"dt_txt\": \"2019-12-04 06:00:00\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"dt\": 1575450000,\n" +
            "\t\t\t\"main\": {\n" +
            "\t\t\t\t\"temp\": 3.59,\n" +
            "\t\t\t\t\"temp_min\": 3.59,\n" +
            "\t\t\t\t\"temp_max\": 3.59,\n" +
            "\t\t\t\t\"pressure\": 1023,\n" +
            "\t\t\t\t\"sea_level\": 1023,\n" +
            "\t\t\t\t\"grnd_level\": 1019,\n" +
            "\t\t\t\t\"humidity\": 90,\n" +
            "\t\t\t\t\"temp_kf\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"weather\": [{\n" +
            "\t\t\t\t\t\"id\": 800,\n" +
            "\t\t\t\t\t\"main\": \"Clear\",\n" +
            "\t\t\t\t\t\"description\": \"clear sky\",\n" +
            "\t\t\t\t\t\"icon\": \"01d\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"clouds\": {\n" +
            "\t\t\t\t\"all\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"wind\": {\n" +
            "\t\t\t\t\"speed\": 1.21,\n" +
            "\t\t\t\t\"deg\": 139\n" +
            "\t\t\t},\n" +
            "\t\t\t\"sys\": {\n" +
            "\t\t\t\t\"pod\": \"d\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"dt_txt\": \"2019-12-04 09:00:00\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"dt\": 1575460800,\n" +
            "\t\t\t\"main\": {\n" +
            "\t\t\t\t\"temp\": 6.42,\n" +
            "\t\t\t\t\"temp_min\": 6.42,\n" +
            "\t\t\t\t\"temp_max\": 6.42,\n" +
            "\t\t\t\t\"pressure\": 1022,\n" +
            "\t\t\t\t\"sea_level\": 1022,\n" +
            "\t\t\t\t\"grnd_level\": 1018,\n" +
            "\t\t\t\t\"humidity\": 72,\n" +
            "\t\t\t\t\"temp_kf\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"weather\": [{\n" +
            "\t\t\t\t\t\"id\": 800,\n" +
            "\t\t\t\t\t\"main\": \"Clear\",\n" +
            "\t\t\t\t\t\"description\": \"clear sky\",\n" +
            "\t\t\t\t\t\"icon\": \"01d\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"clouds\": {\n" +
            "\t\t\t\t\"all\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"wind\": {\n" +
            "\t\t\t\t\"speed\": 0.55,\n" +
            "\t\t\t\t\"deg\": 141\n" +
            "\t\t\t},\n" +
            "\t\t\t\"sys\": {\n" +
            "\t\t\t\t\"pod\": \"d\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"dt_txt\": \"2019-12-04 12:00:00\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"dt\": 1575471600,\n" +
            "\t\t\t\"main\": {\n" +
            "\t\t\t\t\"temp\": 6.07,\n" +
            "\t\t\t\t\"temp_min\": 6.07,\n" +
            "\t\t\t\t\"temp_max\": 6.07,\n" +
            "\t\t\t\t\"pressure\": 1022,\n" +
            "\t\t\t\t\"sea_level\": 1022,\n" +
            "\t\t\t\t\"grnd_level\": 1018,\n" +
            "\t\t\t\t\"humidity\": 73,\n" +
            "\t\t\t\t\"temp_kf\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"weather\": [{\n" +
            "\t\t\t\t\t\"id\": 800,\n" +
            "\t\t\t\t\t\"main\": \"Clear\",\n" +
            "\t\t\t\t\t\"description\": \"clear sky\",\n" +
            "\t\t\t\t\t\"icon\": \"01d\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"clouds\": {\n" +
            "\t\t\t\t\"all\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"wind\": {\n" +
            "\t\t\t\t\"speed\": 0.06,\n" +
            "\t\t\t\t\"deg\": 116\n" +
            "\t\t\t},\n" +
            "\t\t\t\"sys\": {\n" +
            "\t\t\t\t\"pod\": \"d\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"dt_txt\": \"2019-12-04 15:00:00\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"dt\": 1575482400,\n" +
            "\t\t\t\"main\": {\n" +
            "\t\t\t\t\"temp\": 4.53,\n" +
            "\t\t\t\t\"temp_min\": 4.53,\n" +
            "\t\t\t\t\"temp_max\": 4.53,\n" +
            "\t\t\t\t\"pressure\": 1022,\n" +
            "\t\t\t\t\"sea_level\": 1022,\n" +
            "\t\t\t\t\"grnd_level\": 1018,\n" +
            "\t\t\t\t\"humidity\": 81,\n" +
            "\t\t\t\t\"temp_kf\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"weather\": [{\n" +
            "\t\t\t\t\t\"id\": 800,\n" +
            "\t\t\t\t\t\"main\": \"Clear\",\n" +
            "\t\t\t\t\t\"description\": \"clear sky\",\n" +
            "\t\t\t\t\t\"icon\": \"01n\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"clouds\": {\n" +
            "\t\t\t\t\"all\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"wind\": {\n" +
            "\t\t\t\t\"speed\": 0.39,\n" +
            "\t\t\t\t\"deg\": 263\n" +
            "\t\t\t},\n" +
            "\t\t\t\"sys\": {\n" +
            "\t\t\t\t\"pod\": \"n\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"dt_txt\": \"2019-12-04 18:00:00\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"dt\": 1575493200,\n" +
            "\t\t\t\"main\": {\n" +
            "\t\t\t\t\"temp\": 3.82,\n" +
            "\t\t\t\t\"temp_min\": 3.82,\n" +
            "\t\t\t\t\"temp_max\": 3.82,\n" +
            "\t\t\t\t\"pressure\": 1023,\n" +
            "\t\t\t\t\"sea_level\": 1023,\n" +
            "\t\t\t\t\"grnd_level\": 1019,\n" +
            "\t\t\t\t\"humidity\": 84,\n" +
            "\t\t\t\t\"temp_kf\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"weather\": [{\n" +
            "\t\t\t\t\t\"id\": 800,\n" +
            "\t\t\t\t\t\"main\": \"Clear\",\n" +
            "\t\t\t\t\t\"description\": \"clear sky\",\n" +
            "\t\t\t\t\t\"icon\": \"01n\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"clouds\": {\n" +
            "\t\t\t\t\"all\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"wind\": {\n" +
            "\t\t\t\t\"speed\": 0.64,\n" +
            "\t\t\t\t\"deg\": 224\n" +
            "\t\t\t},\n" +
            "\t\t\t\"sys\": {\n" +
            "\t\t\t\t\"pod\": \"n\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"dt_txt\": \"2019-12-04 21:00:00\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"dt\": 1575504000,\n" +
            "\t\t\t\"main\": {\n" +
            "\t\t\t\t\"temp\": 3.22,\n" +
            "\t\t\t\t\"temp_min\": 3.22,\n" +
            "\t\t\t\t\"temp_max\": 3.22,\n" +
            "\t\t\t\t\"pressure\": 1023,\n" +
            "\t\t\t\t\"sea_level\": 1023,\n" +
            "\t\t\t\t\"grnd_level\": 1019,\n" +
            "\t\t\t\t\"humidity\": 86,\n" +
            "\t\t\t\t\"temp_kf\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"weather\": [{\n" +
            "\t\t\t\t\t\"id\": 800,\n" +
            "\t\t\t\t\t\"main\": \"Clear\",\n" +
            "\t\t\t\t\t\"description\": \"clear sky\",\n" +
            "\t\t\t\t\t\"icon\": \"01n\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"clouds\": {\n" +
            "\t\t\t\t\"all\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"wind\": {\n" +
            "\t\t\t\t\"speed\": 1.45,\n" +
            "\t\t\t\t\"deg\": 229\n" +
            "\t\t\t},\n" +
            "\t\t\t\"sys\": {\n" +
            "\t\t\t\t\"pod\": \"n\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"dt_txt\": \"2019-12-05 00:00:00\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"dt\": 1575514800,\n" +
            "\t\t\t\"main\": {\n" +
            "\t\t\t\t\"temp\": 2.63,\n" +
            "\t\t\t\t\"temp_min\": 2.63,\n" +
            "\t\t\t\t\"temp_max\": 2.63,\n" +
            "\t\t\t\t\"pressure\": 1023,\n" +
            "\t\t\t\t\"sea_level\": 1023,\n" +
            "\t\t\t\t\"grnd_level\": 1020,\n" +
            "\t\t\t\t\"humidity\": 88,\n" +
            "\t\t\t\t\"temp_kf\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"weather\": [{\n" +
            "\t\t\t\t\t\"id\": 802,\n" +
            "\t\t\t\t\t\"main\": \"Clouds\",\n" +
            "\t\t\t\t\t\"description\": \"scattered clouds\",\n" +
            "\t\t\t\t\t\"icon\": \"03n\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"clouds\": {\n" +
            "\t\t\t\t\"all\": 34\n" +
            "\t\t\t},\n" +
            "\t\t\t\"wind\": {\n" +
            "\t\t\t\t\"speed\": 1.8,\n" +
            "\t\t\t\t\"deg\": 247\n" +
            "\t\t\t},\n" +
            "\t\t\t\"sys\": {\n" +
            "\t\t\t\t\"pod\": \"n\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"dt_txt\": \"2019-12-05 03:00:00\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"dt\": 1575525600,\n" +
            "\t\t\t\"main\": {\n" +
            "\t\t\t\t\"temp\": 2.37,\n" +
            "\t\t\t\t\"temp_min\": 2.37,\n" +
            "\t\t\t\t\"temp_max\": 2.37,\n" +
            "\t\t\t\t\"pressure\": 1024,\n" +
            "\t\t\t\t\"sea_level\": 1024,\n" +
            "\t\t\t\t\"grnd_level\": 1020,\n" +
            "\t\t\t\t\"humidity\": 86,\n" +
            "\t\t\t\t\"temp_kf\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"weather\": [{\n" +
            "\t\t\t\t\t\"id\": 803,\n" +
            "\t\t\t\t\t\"main\": \"Clouds\",\n" +
            "\t\t\t\t\t\"description\": \"broken clouds\",\n" +
            "\t\t\t\t\t\"icon\": \"04n\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"clouds\": {\n" +
            "\t\t\t\t\"all\": 57\n" +
            "\t\t\t},\n" +
            "\t\t\t\"wind\": {\n" +
            "\t\t\t\t\"speed\": 1.83,\n" +
            "\t\t\t\t\"deg\": 240\n" +
            "\t\t\t},\n" +
            "\t\t\t\"sys\": {\n" +
            "\t\t\t\t\"pod\": \"n\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"dt_txt\": \"2019-12-05 06:00:00\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"dt\": 1575536400,\n" +
            "\t\t\t\"main\": {\n" +
            "\t\t\t\t\"temp\": 3.04,\n" +
            "\t\t\t\t\"temp_min\": 3.04,\n" +
            "\t\t\t\t\"temp_max\": 3.04,\n" +
            "\t\t\t\t\"pressure\": 1024,\n" +
            "\t\t\t\t\"sea_level\": 1024,\n" +
            "\t\t\t\t\"grnd_level\": 1020,\n" +
            "\t\t\t\t\"humidity\": 83,\n" +
            "\t\t\t\t\"temp_kf\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"weather\": [{\n" +
            "\t\t\t\t\t\"id\": 804,\n" +
            "\t\t\t\t\t\"main\": \"Clouds\",\n" +
            "\t\t\t\t\t\"description\": \"overcast clouds\",\n" +
            "\t\t\t\t\t\"icon\": \"04d\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"clouds\": {\n" +
            "\t\t\t\t\"all\": 100\n" +
            "\t\t\t},\n" +
            "\t\t\t\"wind\": {\n" +
            "\t\t\t\t\"speed\": 2.12,\n" +
            "\t\t\t\t\"deg\": 227\n" +
            "\t\t\t},\n" +
            "\t\t\t\"sys\": {\n" +
            "\t\t\t\t\"pod\": \"d\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"dt_txt\": \"2019-12-05 09:00:00\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"dt\": 1575547200,\n" +
            "\t\t\t\"main\": {\n" +
            "\t\t\t\t\"temp\": 5.92,\n" +
            "\t\t\t\t\"temp_min\": 5.92,\n" +
            "\t\t\t\t\"temp_max\": 5.92,\n" +
            "\t\t\t\t\"pressure\": 1023,\n" +
            "\t\t\t\t\"sea_level\": 1023,\n" +
            "\t\t\t\t\"grnd_level\": 1019,\n" +
            "\t\t\t\t\"humidity\": 72,\n" +
            "\t\t\t\t\"temp_kf\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"weather\": [{\n" +
            "\t\t\t\t\t\"id\": 803,\n" +
            "\t\t\t\t\t\"main\": \"Clouds\",\n" +
            "\t\t\t\t\t\"description\": \"broken clouds\",\n" +
            "\t\t\t\t\t\"icon\": \"04d\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"clouds\": {\n" +
            "\t\t\t\t\"all\": 67\n" +
            "\t\t\t},\n" +
            "\t\t\t\"wind\": {\n" +
            "\t\t\t\t\"speed\": 2.73,\n" +
            "\t\t\t\t\"deg\": 228\n" +
            "\t\t\t},\n" +
            "\t\t\t\"sys\": {\n" +
            "\t\t\t\t\"pod\": \"d\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"dt_txt\": \"2019-12-05 12:00:00\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"dt\": 1575558000,\n" +
            "\t\t\t\"main\": {\n" +
            "\t\t\t\t\"temp\": 6.34,\n" +
            "\t\t\t\t\"temp_min\": 6.34,\n" +
            "\t\t\t\t\"temp_max\": 6.34,\n" +
            "\t\t\t\t\"pressure\": 1021,\n" +
            "\t\t\t\t\"sea_level\": 1021,\n" +
            "\t\t\t\t\"grnd_level\": 1017,\n" +
            "\t\t\t\t\"humidity\": 80,\n" +
            "\t\t\t\t\"temp_kf\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"weather\": [{\n" +
            "\t\t\t\t\t\"id\": 802,\n" +
            "\t\t\t\t\t\"main\": \"Clouds\",\n" +
            "\t\t\t\t\t\"description\": \"scattered clouds\",\n" +
            "\t\t\t\t\t\"icon\": \"03d\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"clouds\": {\n" +
            "\t\t\t\t\"all\": 49\n" +
            "\t\t\t},\n" +
            "\t\t\t\"wind\": {\n" +
            "\t\t\t\t\"speed\": 3.7,\n" +
            "\t\t\t\t\"deg\": 227\n" +
            "\t\t\t},\n" +
            "\t\t\t\"sys\": {\n" +
            "\t\t\t\t\"pod\": \"d\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"dt_txt\": \"2019-12-05 15:00:00\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"dt\": 1575568800,\n" +
            "\t\t\t\"main\": {\n" +
            "\t\t\t\t\"temp\": 6.07,\n" +
            "\t\t\t\t\"temp_min\": 6.07,\n" +
            "\t\t\t\t\"temp_max\": 6.07,\n" +
            "\t\t\t\t\"pressure\": 1019,\n" +
            "\t\t\t\t\"sea_level\": 1019,\n" +
            "\t\t\t\t\"grnd_level\": 1015,\n" +
            "\t\t\t\t\"humidity\": 80,\n" +
            "\t\t\t\t\"temp_kf\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"weather\": [{\n" +
            "\t\t\t\t\t\"id\": 803,\n" +
            "\t\t\t\t\t\"main\": \"Clouds\",\n" +
            "\t\t\t\t\t\"description\": \"broken clouds\",\n" +
            "\t\t\t\t\t\"icon\": \"04n\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"clouds\": {\n" +
            "\t\t\t\t\"all\": 74\n" +
            "\t\t\t},\n" +
            "\t\t\t\"wind\": {\n" +
            "\t\t\t\t\"speed\": 5,\n" +
            "\t\t\t\t\"deg\": 235\n" +
            "\t\t\t},\n" +
            "\t\t\t\"sys\": {\n" +
            "\t\t\t\t\"pod\": \"n\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"dt_txt\": \"2019-12-05 18:00:00\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"dt\": 1575579600,\n" +
            "\t\t\t\"main\": {\n" +
            "\t\t\t\t\"temp\": 6.44,\n" +
            "\t\t\t\t\"temp_min\": 6.44,\n" +
            "\t\t\t\t\"temp_max\": 6.44,\n" +
            "\t\t\t\t\"pressure\": 1018,\n" +
            "\t\t\t\t\"sea_level\": 1018,\n" +
            "\t\t\t\t\"grnd_level\": 1014,\n" +
            "\t\t\t\t\"humidity\": 79,\n" +
            "\t\t\t\t\"temp_kf\": 0\n" +
            "\t\t\t},\n" +
            "\t\t\t\"weather\": [{\n" +
            "\t\t\t\t\t\"id\": 804,\n" +
            "\t\t\t\t\t\"main\": \"Clouds\",\n" +
            "\t\t\t\t\t\"description\": \"overcast clouds\",\n" +
            "\t\t\t\t\t\"icon\": \"04n\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"clouds\": {\n" +
            "\t\t\t\t\"all\": 100\n" +
            "\t\t\t},\n" +
            "\t\t\t\"wind\": {\n" +
            "\t\t\t\t\"speed\": 5.36,\n" +
            "\t\t\t\t\"deg\": 236\n" +
            "\t\t\t},\n" +
            "\t\t\t\"sys\": {\n" +
            "\t\t\t\t\"pod\": \"n\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"dt_txt\": \"2019-12-05 21:00:00\"\n" +
            "\t\t}\n" +
            "\t],\n" +
            "\t\"city\": {\n" +
            "\t\t\"id\": 2643743,\n" +
            "\t\t\"name\": \"London\",\n" +
            "\t\t\"coord\": {\n" +
            "\t\t\t\"lat\": 51.5085,\n" +
            "\t\t\t\"lon\": -0.1258\n" +
            "\t\t},\n" +
            "\t\t\"country\": \"GB\",\n" +
            "\t\t\"population\": 1000000,\n" +
            "\t\t\"timezone\": 0,\n" +
            "\t\t\"sunrise\": 1575099724,\n" +
            "\t\t\"sunset\": 1575129383\n" +
            "\t}\n" +
            "}"
}

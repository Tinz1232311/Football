package Method;

import org.apache.http.client.ClientProtocolException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;  // Import JsonObject
import java.io.IOException;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import Content.Iconstant;
import Controller.GoogleAccount;

public class Google {
    public static String getToken(String code) throws ClientProtocolException, IOException {
        // Gửi yêu cầu POST để lấy token
        String response = Request.Post(Iconstant.GOOGLE_LINK_GET_TOKEN)
                .bodyForm(
                        Form.form()
                                .add("client_id", Iconstant.GOOGLE_CLIENT_ID)
                                .add("client_secret", Iconstant.GOOGLE_CLIENT_SECRET)
                                .add("redirect_uri", Iconstant.GOOGLE_REDIRECT_URI)
                                .add("code", code)
                                .add("grant_type", Iconstant.GOOGLE_GRANT_TYPE)
                                .build()
                )
                .execute().returnContent().asString();

        // Phân tích chuỗi JSON nhận được từ API
        JsonObject jobj = new Gson().fromJson(response, JsonObject.class);  
        String accessToken = jobj.get("access_token").getAsString();  
        return accessToken;
    }
    public static GoogleAccount getUserInfo(final String accessToken) throws ClientProtocolException, IOException {
    	String link = Iconstant.GOOGLE_LINK_GET_USER_INFO + accessToken;
    	String response = Request.Get(link).execute().returnContent().asString();
    	GoogleAccount googlePojo = new Gson().fromJson(response, GoogleAccount.class);
    	return googlePojo;
    }
}

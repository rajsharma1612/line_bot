package line.bot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Bot implements Runnable {

	ArrayList<JSONObject> campaign_list = null;
	ArrayList<JSONObject> agDroups_list = null;
	ArrayList<JSONObject> ads_list = null;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Bot bot = new Bot();
		bot.getCampaigns(40151);

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	public void getCampaigns(int campaign_id) {
		APIRequest api_request = new APIRequest("/campaigns", "/get");
		JSONObject jsonObject = api_request.send_post();
		System.out.println("-----ALL Campaigns-----");
		System.out.println(jsonObject);
		System.out.println("-----ALL Campaigns-----");
		campaign_list = getPausedObjects(jsonObject, campaign_id);
		System.out.println(campaign_list);
	}

	public void setCampaign() {

	}

	public ArrayList<JSONObject> getPausedObjects(JSONObject jsonObject, int campaign_id) {
		ArrayList<JSONObject> list = new ArrayList<JSONObject>();
		try {
			JSONArray jsonArray1 = jsonObject.getJSONArray("operands");
			for (int i = 0; i < jsonArray1.length(); i++) {
				JSONObject jsonObject2 = (JSONObject) jsonArray1.get(i);
				if ((jsonObject2.get("userStatus").equals("PAUSED")) && (jsonObject2.get("id").equals(campaign_id))) {
					list.add(jsonObject2);
				}

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;

	}

}

class APIRequest {

	final String URL = "https://ads.line.me/api/v1.0";
	final String Access_Token = "1djCb/mXV+KtryMxr6i1bXwJimZGZQho12gbGDoi5Gh/R9mwaw7NuppaB4ONTCf7NpnogdGbG4fvWJwTTMGT+nnysMoFTTDFJWlERwc7OU3EjVg0mB3NEEITwjCLPw8AGb7dghhNEY27vya6vq4b3I9PbdgDzCFqoOLOYbqAITQ=";
	String object_type = "";
	String request_type = "";

	APIRequest(String object_type, String request_type) {
		this.object_type = object_type;
		this.request_type = request_type;
	}

	public JSONObject send_post() {
		HttpClient httpClient = HttpClientBuilder.create().build();
		JSONObject jsonObject = null;

		try {

			HttpPost request = new HttpPost(this.URL + this.object_type + this.request_type);
			StringEntity params = new StringEntity("{\"accountId\":\"5514\"} ");
			request.addHeader("content-type", "application/json");
			request.addHeader("Authorization", "Bearer " + this.Access_Token);
			request.setEntity(params);
			HttpResponse response = httpClient.execute(request);

			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			System.out.println(result.toString());
			jsonObject = new JSONObject(result.toString());

		} catch (Exception ex) {

			ex.printStackTrace();

		} finally {

		}
		return jsonObject;

	}

}

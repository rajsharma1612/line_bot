package line.bot;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

public class Bot implements Runnable {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Bot bot = new Bot();
		bot.getCampaigns();

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	public void getCampaigns() {
		APIRequest api_request = new APIRequest("/campaigns","/get");
		api_request.send_post();
	}

	public void setCampaign() {

	}

}

class APIRequest<NameValuePair> {

	final String URL = "https://ads.line.me/api/v1.0";
	final String Access_Token = "1djCb/mXV+KtryMxr6i1bXwJimZGZQho12gbGDoi5Gh/R9mwaw7NuppaB4ONTCf7NpnogdGbG4fvWJwTTMGT+nnysMoFTTDFJWlERwc7OU3EjVg0mB3NEEITwjCLPw8AGb7dghhNEY27vya6vq4b3I9PbdgDzCFqoOLOYbqAITQ=";
	String object_type = "";
	String request_type = "";

	 APIRequest(String object_type, String request_type ){
	 this.object_type = object_type;
	 this.request_type = request_type;
	 }

	public void send_post() {
		HttpClient httpClient = HttpClientBuilder.create().build(); // Use this instead

		try {

			HttpPost request = new HttpPost(this.URL + this.object_type+this.request_type);
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

		} catch (Exception ex) {

			ex.printStackTrace();

		} finally {
	
		}

	}

}

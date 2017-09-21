package line.bot;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

class APIRequest {

	final String URL = "https://ads.line.me/api/v1.0";
	final String Access_Token = "1djCb/mXV+KtryMxr6i1bXwJimZGZQho12gbGDoi5Gh/R9mwaw7NuppaB4ONTCf7NpnogdGbG4fvWJwTTMGT+nnysMoFTTDFJWlERwc7OU3EjVg0mB3NEEITwjCLPw8AGb7dghhNEY27vya6vq4b3I9PbdgDzCFqoOLOYbqAITQ=";
	String object_type = "";
	String request_type = "";
	String params[] = null;

	APIRequest(String object_type, String request_type) {
		this.object_type = object_type;
		this.request_type = request_type;
	}

	APIRequest(String object_type, String request_type, String params[]) {
		this.object_type = object_type;
		this.request_type = request_type;
		this.params = params;
	}

	public APIRequest(String string, String string2, String string3, String string4) {
		// TODO Auto-generated constructor stub
	}

	public JSONObject send_post() {
		HttpClient httpClient = HttpClientBuilder.create().build();
		JSONObject jsonObject = null;

		try {

			HttpPost request = new HttpPost(this.URL + this.object_type + this.request_type);
			StringEntity params = null;
			if (this.request_type.equals("/get")) {
				params = new StringEntity("{\"accountId\":\"5514\"}");
			} else if (this.request_type.equals("/set")) {
				// params = new
				// StringEntity("{\"accountId\":\"5514\",\"operands\":[{\"id\":40151,\"name\":\"ddddd\"}]}");
				String output = String.format("{\"accountId\":\"5514\",\"operands\":[{\"id\":%s,\"name\":\"%s\"}]}",
						this.params[0], this.params[1]);
				params = new StringEntity(output);
			}
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

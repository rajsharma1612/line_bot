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
	final String Access_Token = Constants.Access_Token;
	String object_type = "";
	String request_type = "";
	String params[] = null;
	String ad_Account_Id = Constants.ad_Account_Id;

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
				String output = String.format("{\"accountId\":\"%s\"}", ad_Account_Id);
				params = new StringEntity(output, "UTF-8");
			} else if (this.request_type.equals("/set")) {

				String output = String.format("{\"accountId\":\"%s\",\"operands\":[{\"id\":%s,\"name\":\"%s\"}]}",
						ad_Account_Id, this.params[0], this.params[1]);
				if (this.params[1].equals("OptimizationOff")) {
					output = String.format(
							"{\"accountId\":\"%s\",\"operands\":[{\"id\":%s,\"bidOptimizationType\":\"%s\"}]}",
							ad_Account_Id, this.params[0], "NONE");
				} else if (this.params[1].equals("OptimizationOn")) {
					output = String.format(
							"{\"accountId\":\"%s\",\"operands\":[{\"id\":%s,\"bidOptimizationType\":\"%s\",\"bidOptimizationGoal\":\"%s\"}]}",
							ad_Account_Id, this.params[0], this.params[2], this.params[3]);
				}
				if (object_type.equals("/ads")) {
					output = String.format("{\"accountId\":\"5514\",\"operands\":[{\"id\":%s,\"bidAmount\":\"%s\"}]}",
							this.params[0], this.params[1]);
				}
				params = new StringEntity(output, "UTF-8");
			}
			params.setContentType("application/json");
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

			System.out.println("API Resonse :" + result.toString());
			jsonObject = new JSONObject(result.toString());

		} catch (Exception ex) {

			ex.printStackTrace();

		} finally {

		}
		return jsonObject;

	}

}

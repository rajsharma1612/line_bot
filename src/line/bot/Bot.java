package line.bot;

import java.util.ArrayList;

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
		int campaign_id = 40151;
		bot.getCampaigns(campaign_id);
		bot.setCampaign();

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
		JSONObject jsonObject = campaign_list.get(0);
		try {
			String campaign_name = (String) jsonObject.get("name");
			System.out.println(campaign_name);
			// campaign_name = campaign_name + "★";
			String params[] = {"40151","raj-----"};
			APIRequest api_request = new APIRequest("/campaigns", "/set",params);
			api_request.send_post();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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


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

		new Thread(new Bot()).start();
	}

	@Override
	public void run() {
		try {
			int campaign_id = 40151;
			getObject("/campaign", campaign_id);
			Thread.sleep(1000);
			String original_name = changeName(String.valueOf(campaign_id), "_BOT", false);
			System.out.println(original_name);
			Thread.sleep(1000);
			changeName(String.valueOf(campaign_id), original_name, true);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void getObject(String objectType, int campaign_id) {
		if (objectType.equals("/campaign")) {
			APIRequest api_request = new APIRequest("/campaigns", "/get");
			JSONObject jsonObject = api_request.send_post();
			campaign_list = getPausedObjects(jsonObject, campaign_id);
			if (campaign_list.size() < 1) {
				System.out.println("Sorry,Campaign is not PAUSED. Please specify another Campaign ID");
				System.exit(-1);
			}
		}

	}

	public String changeName(String id, String name_to_change, boolean isRevert) {
		JSONObject jsonObject = campaign_list.get(0);
		String original_name = null;
		try {
			String old_name = (String) jsonObject.get("name");
			original_name = old_name;
			String new_name = old_name + name_to_change;
			if (isRevert == true) {
				new_name = name_to_change;
			}
			String params[] = { id, new_name };
			APIRequest api_request = new APIRequest("/campaigns", "/set", params);
			api_request.send_post();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return original_name;

	}

	public ArrayList<JSONObject> getPausedObjects(JSONObject jsonObject, int object_id) {
		ArrayList<JSONObject> list = new ArrayList<JSONObject>();
		try {
			JSONArray jsonArray1 = jsonObject.getJSONArray("operands");
			for (int i = 0; i < jsonArray1.length(); i++) {
				JSONObject jsonObject2 = (JSONObject) jsonArray1.get(i);
				if ((jsonObject2.get("userStatus").equals("PAUSED")) && (jsonObject2.get("id").equals(object_id))) {
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

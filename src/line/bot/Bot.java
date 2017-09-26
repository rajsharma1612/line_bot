package line.bot;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Bot implements Runnable {

	ArrayList<JSONObject> campaign_list = null;
	ArrayList<JSONObject> agDroups_list = null;
	ArrayList<JSONObject> ads_list = null;
	boolean optimizationEnabled = false;
	String optimizationType = null;
	int optimizationValue = 0;

	public static void main(String[] args) {

		new Thread(new Bot()).start();
	}

	@Override
	public void run() {
		try {
			int campaign_id = 40989;
			System.out.println("----------Processing Start----------");
			
			getObject("/campaign", campaign_id);
			Thread.sleep(1000);
			System.out.println("----------Campaign Start----------");
			String original_name = changeName(String.valueOf(campaign_id), "_BOT", false, "/campaign", 0);
			System.out.println(original_name);
			Thread.sleep(1000);
			changeName(String.valueOf(campaign_id), original_name, true, "/campaign", 0);
			System.out.println("----------Campaign End----------");

			Thread.sleep(1000);
			getObject("/adgroup", campaign_id);
			System.out.println("----------adgroup Start----------");
			for (int i = 0; i < agDroups_list.size(); i++) {
				JSONObject jsonObject = agDroups_list.get(i);
				String original_name1 = changeName(String.valueOf(jsonObject.get("id")), "_BOT", false, "/adgroup", i);
				System.out.println(original_name1);
				Thread.sleep(1000);
				changeName(String.valueOf(jsonObject.get("id")), original_name1, true, "/adgroup", i);
			}
			System.out.println("----------adgroup End----------");
			// check bidOptimization
			if (!campaign_list.get(0).getString("bidOptimizationType").equals("NONE")) {
				changeCampaignOptimization(String.valueOf(campaign_id), false);
			}
			getObject("/ads", campaign_id);
			System.out.println("----------ads Start----------");
			for (int i = 0; i < ads_list.size(); i++) {
				JSONObject jsonObject = ads_list.get(i);
				int new_bid = change_bid(jsonObject, jsonObject.getInt("bidAmount"), jsonObject.getInt("bidAmount"),
						false);
				Thread.sleep(1000);
				change_bid(jsonObject, jsonObject.getInt("bidAmount"), new_bid, true);
			}
			System.out.println("----------ads End----------");
			if (optimizationEnabled) {
				changeCampaignOptimization(String.valueOf(campaign_id), true);
			}
			System.out.println("----------Processing End----------");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public int change_bid(JSONObject jsonObject, int original_bid, int new_bid, boolean revert) {
		String params[] = new String[2];
		int new_bids = 0;
		try {
			int ad_id = jsonObject.getInt("id");
			params[0] = String.valueOf(ad_id);

			if (original_bid <= 24) {
				if (revert) {
					new_bids = 24;
				} else {
					new_bids = 25;
				}

			} else {
				new_bids = ++original_bid;
				if (revert) {
					new_bids = --new_bid;
				}
			}
			params[1] = String.valueOf(new_bids);

			APIRequest api_request = new APIRequest("/ads", "/set", params);
			api_request.send_post();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new_bids;

	}

	public void changeCampaignOptimization(String campaign_id, boolean revert) {
		try {
			if (optimizationEnabled && revert) {
				JSONObject jsonObject = campaign_list.get(0);
				optimizationType = jsonObject.getString("bidOptimizationType");
				optimizationValue = jsonObject.getInt("bidOptimizationGoal");
				String params[] = { campaign_id, "OptimizationOn", optimizationType,
						String.valueOf(optimizationValue) };
				APIRequest api_request = new APIRequest("/campaigns", "/set", params);
				api_request.send_post();
			} else {
				JSONObject jsonObject = campaign_list.get(0);
				optimizationEnabled = true;
				optimizationType = jsonObject.getString("bidOptimizationType");
				optimizationValue = jsonObject.getInt("bidOptimizationGoal");
				String params[] = { campaign_id, "OptimizationOff" };
				APIRequest api_request = new APIRequest("/campaigns", "/set", params);
				api_request.send_post();
			}
		} catch (Exception e) {

		}

	}

	public void getObject(String objectType, int campaign_id) {
		if (objectType.equals("/campaign")) {
			APIRequest api_request = new APIRequest("/campaigns", "/get");
			JSONObject jsonObject = api_request.send_post();
			campaign_list = getPausedObjects(jsonObject, objectType, campaign_id);
			if (campaign_list.size() < 1) {
				System.out.println("Sorry,Campaign is not PAUSED. Please specify another Campaign ID");
				System.exit(-1);
			}
		}
		if (objectType.equals("/adgroup")) {
			APIRequest api_request = new APIRequest("/adgroups", "/get");
			JSONObject jsonObject = api_request.send_post();
			agDroups_list = getPausedObjects(jsonObject, objectType, campaign_id);
			if (agDroups_list.size() < 1) {
				System.out.println("Sorry,No adGroup is PAUSED. Please specify another Campaign ID");
				System.exit(-1);
			}
		}
		if (objectType.equals("/ads")) {
			APIRequest api_request = new APIRequest("/ads", "/get");
			JSONObject jsonObject = api_request.send_post();
			ads_list = getPausedObjects(jsonObject, objectType, 0);
			for (int i = 0; i < ads_list.size(); i++) {
				JSONObject jsonObject2 = ads_list.get(i);
				try {
					int ad_id = jsonObject2.getInt("adGroupId");
					boolean found = false;
					for (int j = 0; j < agDroups_list.size(); j++) {
						JSONObject jsonObject3 = agDroups_list.get(j);
						int adgroup_id = jsonObject3.getInt("id");
						if (adgroup_id == ad_id) {
							found = true;
							break;
						}else {
							found = false;
						}
					}
					if (found == false) {
						ads_list.remove(i);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	public String changeName(String id, String name_to_change, boolean isRevert, String object_type, int adGroupIndex) {
		JSONObject jsonObject = null;
		if (object_type.equals("/campaign")) {
			jsonObject = campaign_list.get(0);
		} else {
			jsonObject = agDroups_list.get(adGroupIndex);
		}

		String original_name = null;
		try {
			String old_name = (String) jsonObject.get("name");
			original_name = old_name;
			String new_name = old_name + name_to_change;
			if (isRevert == true) {
				new_name = name_to_change;
			}
			String params[] = { id, new_name };
			if (object_type.equals("/campaign")) {
				APIRequest api_request = new APIRequest("/campaigns", "/set", params);
				api_request.send_post();
			} else {
				APIRequest api_request = new APIRequest("/adgroups", "/set", params);
				api_request.send_post();
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return original_name;

	}

	public ArrayList<JSONObject> getPausedObjects(JSONObject jsonObject, String object_type, int object_id) {
		ArrayList<JSONObject> list = new ArrayList<JSONObject>();
		try {
			JSONArray jsonArray1 = jsonObject.getJSONArray("operands");
			if (object_type.equals("/campaign")) {
				for (int i = 0; i < jsonArray1.length(); i++) {
					JSONObject jsonObject2 = (JSONObject) jsonArray1.get(i);
					if ((jsonObject2.get("userStatus").equals("PAUSED")) && (jsonObject2.get("id").equals(object_id))) {
						list.add(jsonObject2);
					}
				}
			} else if (object_type.equals("/adgroup")) {
				for (int i = 0; i < jsonArray1.length(); i++) {
					JSONObject jsonObject2 = (JSONObject) jsonArray1.get(i);
					if ((jsonObject2.get("userStatus").equals("PAUSED"))
							&& (jsonObject2.get("campaignId").equals(object_id))) {
						list.add(jsonObject2);
					}
				}

			} else if (object_type.equals("/ads")) {
				for (int i = 0; i < jsonArray1.length(); i++) {
					JSONObject jsonObject2 = (JSONObject) jsonArray1.get(i);
					if ((jsonObject2.get("userStatus").equals("PAUSED"))) {
						list.add(jsonObject2);
					}
				}

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;

	}

}

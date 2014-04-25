package cn.burgeon.core.ui.allot;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.burgeon.core.App;
import cn.burgeon.core.R;
import cn.burgeon.core.adapter.AllotReplenishmentLVAdapter;
import cn.burgeon.core.bean.AllotReplenishment;
import cn.burgeon.core.ui.BaseActivity;
import cn.burgeon.core.utils.PreferenceUtils;
import cn.burgeon.core.utils.ScreenUtils;

public class AllotReplenishmentQueryActivity extends BaseActivity {

    private ListView allotreplenishmentLV;
    private TextView recodeNumTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupFullscreen();
        setContentView(R.layout.activity_allot_replenishment_query);

        init();

        initLVData();
    }

    private void init() {
        // 初始化门店信息
        TextView storeTV = (TextView) findViewById(R.id.storeTV);
        storeTV.setText(App.getPreferenceUtils().getPreferenceStr(PreferenceUtils.store_key));

        TextView currTimeTV = (TextView) findViewById(R.id.currTimeTV);
        currTimeTV.setText(getCurrDate());

        HorizontalScrollView hsv = (HorizontalScrollView) findViewById(R.id.hsv);
        ViewGroup.LayoutParams params = hsv.getLayoutParams();
        params.height = (int) ScreenUtils.getAllotInLVHeight(this);

        allotreplenishmentLV = (ListView) findViewById(R.id.allotreplenishmentLV);
        recodeNumTV = (TextView) findViewById(R.id.recodeNumTV);
    }

    private void initLVData() {
        Map<String, String> params = new HashMap<String, String>();
        try {
            JSONArray array = new JSONArray();
            JSONObject transactions = new JSONObject();
            transactions.put("id", 112);
            transactions.put("command", "Query");

            JSONObject paramsInTransactions = new JSONObject();
            paramsInTransactions.put("table", "M_TRANSFER");
            paramsInTransactions.put("columns", new JSONArray().put("DOCNO").put("BILLDATE").put("C_DEST_ID").put("STATUSERID").put("DESCRIPTION"));
            transactions.put("params", paramsInTransactions);

            array.put(transactions);
            params.put("transactions", array.toString());
            sendRequest(params, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // 取消进度条
                    stopProgressDialog();

                    try {
                        ArrayList<AllotReplenishment> lists = resJAToList(response);
                        AllotReplenishmentLVAdapter mAdapter = new AllotReplenishmentLVAdapter(AllotReplenishmentQueryActivity.this, lists, R.layout.allot_replenishment);
                        allotreplenishmentLV.setAdapter(mAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private ArrayList<AllotReplenishment> resJAToList(String response) throws JSONException {
        ArrayList<AllotReplenishment> lists = new ArrayList<AllotReplenishment>();

        JSONArray resJA = new JSONArray(response);
        JSONObject resJO = resJA.getJSONObject(0);
        JSONArray rowsJA = resJO.getJSONArray("rows");
        int len = rowsJA.length();
        recodeNumTV.setText("共" + len + "个补货单");
        for (int i = 0; i < len; i++) {
            // ["TF0912140000005",20091214,3860,1692,null]
            String currRow = rowsJA.get(i).toString();
            String[] currRows = currRow.split(",");

            AllotReplenishment allotReplenishment = new AllotReplenishment();
            allotReplenishment.setDOCNO(currRows[0].substring(2, currRows[0].length() - 1));
            allotReplenishment.setBILLDATE(currRows[1]);
            allotReplenishment.setC_DEST_ID(currRows[2]);
            allotReplenishment.setSTATUSERID(currRows[3]);
            allotReplenishment.setDESCRIPTION(("null".equals(currRows[4].substring(0, currRows[4].length() - 1)) ? "" : (currRows[4].substring(0, currRows[4].length() - 1))));
            lists.add(allotReplenishment);
        }
        return lists;
    }
}

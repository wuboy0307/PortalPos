package cn.burgeon.core.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.burgeon.core.R;
import cn.burgeon.core.bean.Order;

public class SalesOrderSearchAdapter extends BaseAdapter {

	private List<Order> list;
	private Context context;

	public List<Order> getList() {
		return list;
	}

	public SalesOrderSearchAdapter(List<Order> list, Context context) {
		super();
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.sales_order_search_list_item, null);
			holder.textView1 = (TextView) convertView.findViewById(R.id.ordersearch_date);
			holder.textView2 = (TextView) convertView.findViewById(R.id.ordersearch_count);
			holder.textView3 = (TextView) convertView.findViewById(R.id.ordersearch_danhao);
			holder.textView4 = (TextView) convertView.findViewById(R.id.ordersearch_upstate);
			holder.textView5 = (TextView) convertView.findViewById(R.id.ordersearch_money);
			holder.textView6 = (TextView) convertView.findViewById(R.id.ordersearch_yingyeyuan);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		Order order = list.get(position);
		holder.textView1.setText(order.getOrderDate());
		holder.textView2.setText(order.getOrderCount());
		holder.textView3.setText(order.getOrderNo());
		holder.textView4.setText(order.getOrderState());
		holder.textView5.setText(order.getOrderMoney());
		holder.textView6.setText(order.getSaleAsistant());
		
		return convertView;
	}

	class ViewHolder{
		TextView textView1;
		TextView textView2;
		TextView textView3;
		TextView textView4;
		TextView textView5;
		TextView textView6;
	}
	
    public void setList(List<Order> data) {
        list = data;
        notifyDataSetChanged();
    }

}

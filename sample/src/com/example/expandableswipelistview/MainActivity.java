package com.example.expandableswipelistview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.ExpandableSwipeListView;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {
	
	private ExpandableSwipeListView listView;
	private ExpandableListAdapter adapter;
	private List<String> listDataHeader;
    private HashMap<String, ArrayList<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        listView = (ExpandableSwipeListView) findViewById(R.id.list);

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, ArrayList<String>>();
        createHeaders();
        createChilds();

        adapter = new ExpandableListAdapter(this, listDataHeader, listDataChild, R.layout.header_list, R.layout.list_child);
        adapter.enableExpandableSwipeListView(listView);
        adapter.setOnDeleteButton(new ExpandableListAdapter.DeleteListener() {
            @Override
            public boolean onDeleteItem(int position) {
                return true;
            }
        });
        listView.setAdapter(adapter);
        listView.setSwipeListViewListener(new BaseSwipeListViewListener() {
            @Override
            public void onDismiss(int[] reverseSortedPositions) {
            	listView.resetAll();
                //prepareLastScrollPos();
                super.onDismiss(reverseSortedPositions);
                for (int pos : reverseSortedPositions) {
                    //get group and child ids
                    int[] conversion = listView.getGroupAndChildPositions(pos);
                    String header = listDataHeader.get(conversion[0]);
                    listDataChild.get(header).remove(conversion[1]);
                    if (listDataChild.get(header).size() == 0) {
                        listDataChild.remove(header);
                        listDataHeader.remove(conversion[0]);
                    }
                    //listViewEventRequests.resetItem(pos);
                }
                adapter.notifyDataSetChanged();
            }
        });
        listView.setClickable(false);
    }

	private void createChilds() {
		ArrayList<String> childs1=new ArrayList<String>();
		childs1.add("Group 1 Item 1");
		childs1.add("Group 1 Item 2");
		childs1.add("Group 1 Item 3");
		listDataChild.put("First Group", childs1);
		ArrayList<String> childs2=new ArrayList<String>();
		childs2.add("Group 2 Item 1");
		childs2.add("Group 2 Item 2");
		listDataChild.put("Second Group", childs2);
		ArrayList<String> childs3=new ArrayList<String>();
		childs3.add("Group 3 Item 1");
		childs3.add("Group 3 Item 2");
		childs3.add("Group 3 Item 3");
		childs3.add("Group 3 Item 4");
		listDataChild.put("Third Group", childs3);
	}

	private void createHeaders() {
		listDataHeader.add("First Group");
		listDataHeader.add("Second Group");
		listDataHeader.add("Third Group");
	}
}

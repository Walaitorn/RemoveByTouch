package com.egco428.removebytouch;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends ListActivity {
    private CommentDataSource dataSource;
    public String name;
    EditText nameText ;
    EditText LastText ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataSource = new CommentDataSource(this);
        dataSource.open();
        List<Comment> values = dataSource.getAllComments();
        ArrayAdapter<Comment> adapter = new ArrayAdapter<Comment>(this,android.R.layout.simple_expandable_list_item_1,values);
        setListAdapter(adapter);

        nameText = (EditText)findViewById(R.id.nameTxt);
        LastText = (EditText)findViewById(R.id.lastTxt);


    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        ArrayAdapter<Comment> adapter = (ArrayAdapter<Comment>)getListAdapter();
        Comment comment = null;

        if(getListAdapter().getCount()>0){
            comment = (Comment)getListAdapter().getItem(position); // delete first item
            dataSource.deleteComment(comment); // delete in database
            adapter.remove(comment); // delete in listview
        }

    }

    public void onClick(View view){
        ArrayAdapter<Comment> adapter = (ArrayAdapter<Comment>)getListAdapter();
        Comment comment = null;


        switch (view.getId()){
            case R.id.addBtn:
                //String[] comments = new String[] {"Good","Cool","#whatever","Very nice"};
                //int nextInt = new Random().nextInt(4); // 4 = have 4 words;

                name = nameText.getText().toString()+" "+LastText.getText().toString();
                comment = dataSource.createComment(name);
                adapter.add(comment);
                break;
//            case R.id.deleteBtn:
//                if(getListAdapter().getCount()>0){
//                    comment = (Comment)getListAdapter().getItem(pos); // delete first item
//                    dataSource.deleteComment(comment); // delete in database
//                    adapter.remove(comment); // delete in listview
//                }
//                break;

        }
        adapter.notifyDataSetChanged(); // Automatic refresh adapter
    }

    @Override
    protected void onResume(){
        dataSource.open();
        super.onResume();
    }

    @Override
    protected void onPause(){
        dataSource.close();
        super.onPause();
    }
}

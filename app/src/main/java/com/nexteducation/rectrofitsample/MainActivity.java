package com.nexteducation.rectrofitsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.widget.ListView;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.List;

import okhttp3.Request;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ListView.OnItemClickListener
{
    //Root URL of our web service
    //public static final String ROOT_URL = "http://simplifiedcoding.16mb.com/";
    public static final String ROOT_URL = "http://www.androidbegin.com/tutorial/jsonparsetutorial.txt";

    //Strings to bind with intent will be used to send data to other activity
    public static final String KEY_BOOK_ID = "key_book_id";
    public static final String KEY_BOOK_NAME = "key_book_name";
    public static final String KEY_BOOK_PRICE = "key_book_price";
    public static final String KEY_BOOK_STOCK = "key_book_stock";

    //List view to show data
    private ListView listView;

    //List of type books this list will store type Book which is our data model
    private List<Book> books;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing the listview
        listView = (ListView) findViewById(R.id.listViewBooks);

        //Calling the method that will fetch data
        getBooks();

        //Setting onItemClickListener to listview
        listView.setOnItemClickListener(this);
    }

    private void getBooks()
    {
        //While the app fetched data we are displaying a progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Fetching Data","Please wait...",false,false);

        //Creating a rest adapter
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ROOT_URL)
                .build();

        //Creating an object of our api interface
        BooksAPI api = adapter.create(BooksAPI.class);

        retrofit2.Call<PopulationResponse> call = api.getPopulations();

        call.enqueue(new retrofit2.Callback<PopulationResponse>()
        {
            @Override
            public void onResponse(retrofit2.Call<PopulationResponse> call, retrofit2.Response<PopulationResponse> response)
            {
                List<Population> populations = response.body().getPopulations();
                Log.i("Main", "onResponse: "+populations.get(0).country);
                Log.i("Main", "onResponse: "+populations.get(0).flag);
                Log.i("Main", "onResponse: "+populations.get(0).rank);
            }

            @Override
            public void onFailure(retrofit2.Call<PopulationResponse> call, Throwable t) {

            }
        });

        //Defining the method
        api.getBooks(new Callback<List<Book>>() {

            @Override
            public void success(List<Book> list, Response response) {
                //Dismissing the loading progressbar
                loading.dismiss();

                //Storing the data in our list
                books = list;

                //Calling a method to show the list
                showList();
            }

            @Override
            public void failure(RetrofitError error) {
                //you can handle the errors here
                Log.i("MainError", "failure: "+error.toString());
            }
        });
    }

    //Our method to show list
    private void showList()
    {
        //String array to store all the book names
        String[] items = new String[books.size()];

        //Traversing through the whole list to get all the names
        for(int i=0; i<books.size(); i++)
        {
            //Storing names to string array
            items[i] = books.get(i).getName();
        }

        //Creating an array adapter for list view
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.simple_list,items);

        //Setting adapter to listview
        listView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        //Creating an intent
        Intent intent = new Intent(this, ShowBookDetails.class);

        //Getting the requested book from the list
        Book book = books.get(position);

        //Adding book details to intent
        intent.putExtra(KEY_BOOK_ID,book.getBookId());
        intent.putExtra(KEY_BOOK_NAME,book.getName());
        intent.putExtra(KEY_BOOK_PRICE,book.getPrice());
        intent.putExtra(KEY_BOOK_STOCK,book.getInStock());

        //Starting another activity to show book details
        startActivity(intent);
    }
}

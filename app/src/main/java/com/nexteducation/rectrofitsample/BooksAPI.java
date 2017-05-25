package com.nexteducation.rectrofitsample;

import java.util.List;

import retrofit.Callback;
import retrofit2.Call;
import retrofit2.http.GET;
//import retrofit.http.GET;

/**
 * Created by next on 27/1/17.
 */
public interface BooksAPI
{
    void getBooks(Callback<List<Book>> response);

    @GET
    Call<PopulationResponse> getPopulations();
}

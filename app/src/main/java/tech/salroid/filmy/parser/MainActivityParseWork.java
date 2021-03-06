package tech.salroid.filmy.parser;

import android.content.ContentValues;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

import tech.salroid.filmy.database.FilmContract;


public class MainActivityParseWork {

    private final String LOG_TAG = MainActivityParseWork.class.getSimpleName();
    // private final int type;
    private Context context;
    private String result;
    private String imdb_id;


    public MainActivityParseWork(Context context, String result) {
        this.context = context;
        this.result = result;
        // this.type=type;
    }

    public void parse() {

        //final List<MovieData> movieDataArrayList = new ArrayList<MovieData>();
        //MovieData movieData = null;


        try {

            JSONArray jsonArray = new JSONArray(result);
            Vector<ContentValues> cVVector = new Vector<ContentValues>(jsonArray.length());

            for (int i = 0; i < jsonArray.length(); i++) {

                //movieData = new MovieData();
                String title, poster, id;
                int year;

                title = (jsonArray.getJSONObject(i).getJSONObject("movie")).getString("title");
                year = (jsonArray.getJSONObject(i).getJSONObject("movie")).getInt("year");
                poster = (jsonArray.getJSONObject(i).getJSONObject("movie").getJSONObject("images").getJSONObject("poster").getString("thumb"));
                id = (jsonArray.getJSONObject(i).getJSONObject("movie")).getJSONObject("ids").getString("tmdb");


                //movieData.setMovie(title);
                //movieData.setYear(year);
                //movieData.setId(id);
                //movieData.setPoster(poster);

                //movieDataArrayList.add(movieData);

                // Insert the new weather information into the database

                ContentValues movieValues = new ContentValues();


                movieValues.put(FilmContract.MoviesEntry.MOVIE_ID, id);
                movieValues.put(FilmContract.MoviesEntry.MOVIE_TITLE, title);
                movieValues.put(FilmContract.MoviesEntry.MOVIE_YEAR, year);
                movieValues.put(FilmContract.MoviesEntry.MOVIE_POSTER_LINK, poster);

                cVVector.add(movieValues);

            }
            int inserted = 0;
            // add to database
            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);

                context.getContentResolver().delete(FilmContract.MoviesEntry.CONTENT_URI, null, null);
                inserted = context.getContentResolver().bulkInsert(FilmContract.MoviesEntry.CONTENT_URI, cvArray);

            }

            // Log.d(LOG_TAG, "Fetching Complete. " + inserted + " Inserted");


        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return;
    }

    public void parseupcoming() {

        try {

            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("results");

            Vector<ContentValues> cVVector = new Vector<ContentValues>(jsonArray.length());

            for (int i = 0; i < jsonArray.length(); i++) {


                String title, poster, id;

                title = (jsonArray.getJSONObject(i)).getString("title");
                poster = (jsonArray.getJSONObject(i).getString("poster_path"));
                id = (jsonArray.getJSONObject(i)).getString("id");

                String temp_year[] = (jsonArray.getJSONObject(i)).getString("release_date").split("-");
                String year = temp_year[0];


                String trimmedQuery = (title.toLowerCase()).trim();

                String finalQuery = trimmedQuery.replace(" ", "-");
                finalQuery = finalQuery.replace("'", "-");
                String slug = (finalQuery.replace(":", "")) + "-" + year;


                // Insert the new weather information into the database

                ContentValues movieValues = new ContentValues();

                if (!(poster.equals("null"))) {


                    movieValues.put(FilmContract.MoviesEntry.MOVIE_ID, id);
                    movieValues.put(FilmContract.MoviesEntry.MOVIE_TITLE, title);
                    movieValues.put(FilmContract.MoviesEntry.MOVIE_YEAR, year);
                    movieValues.put(FilmContract.MoviesEntry.MOVIE_POSTER_LINK, "http://image.tmdb.org/t/p/w185" + poster);


                    cVVector.add(movieValues);
                }
            }
            int inserted = 0;
            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);

                context.getContentResolver().delete(FilmContract.UpComingMoviesEntry.CONTENT_URI, null, null);
                inserted = context.getContentResolver().bulkInsert(FilmContract.UpComingMoviesEntry.CONTENT_URI, cvArray);

            }

            // Log.d("webi", "Fetching Complete. " + inserted + " Inserted");


        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return;
    }


    public void intheatres() {
        try {


            JSONObject jsonObject = new JSONObject(result);

            JSONArray jsonArray = jsonObject.getJSONArray("results");

            Vector<ContentValues> cVVector = new Vector<ContentValues>(jsonArray.length());

            for (int i = 0; i < jsonArray.length(); i++) {

                //movieData = new MovieData();
                String title, poster, id;
                title = (jsonArray.getJSONObject(i)).getString("title");
                poster = (jsonArray.getJSONObject(i).getString("poster_path"));
                id = (jsonArray.getJSONObject(i)).getString("id");


                String temp_year[] = (jsonArray.getJSONObject(i)).getString("release_date").split("-");
                String year = temp_year[0];

                String trimmedQuery = (title.toLowerCase()).trim();
                String finalQuery = trimmedQuery.replace(" ", "-");
                finalQuery = finalQuery.replace("'", "-");
                String slug = (finalQuery.replace(":", "")) + "-" + year;


                if (!(poster.equals("null"))) {

                    ContentValues movieValues = new ContentValues();

                    movieValues.put(FilmContract.MoviesEntry.MOVIE_ID, id);
                    movieValues.put(FilmContract.MoviesEntry.MOVIE_TITLE, title);
                    movieValues.put(FilmContract.MoviesEntry.MOVIE_YEAR, year);
                    movieValues.put(FilmContract.MoviesEntry.MOVIE_POSTER_LINK, "http://image.tmdb.org/t/p/w185" + poster);

                    cVVector.add(movieValues);
                }
            }
            int inserted = 0;
            // add to database
            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);

                context.getContentResolver().delete(FilmContract.InTheatersMoviesEntry.CONTENT_URI, null, null);
                inserted = context.getContentResolver().bulkInsert(FilmContract.InTheatersMoviesEntry.CONTENT_URI, cvArray);

            }

            // Log.d(LOG_TAG, "Fetching Complete. " + inserted + " Inserted");


        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return;

    }
}

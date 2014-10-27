package application.party;



    import android.os.AsyncTask;
    import android.os.Bundle;
    import android.support.v7.app.ActionBarActivity;
    import android.widget.TextView;
    import org.json.JSONArray;
    import org.json.JSONException;
    import org.json.JSONObject;

    import application.party.util.ApiConnector;


public class PruebaBBDD extends ActionBarActivity {

        private TextView responseTextView;

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_prueba_bbdd);

            this.responseTextView = (TextView) this.findViewById(R.id.responseTextView);

            new GetAllCustomerTask().execute(new ApiConnector());

        }


        public void setTextToTextView(JSONArray jsonArray){

            String s = "";
            for (int i=0; i<jsonArray.length();i++){

                JSONObject json = null;
                try{
                    json = jsonArray.getJSONObject(i);
                    s = s + "Name : "+json.getString("FirstName")+" "+json.getString("LastName")+"\n"+
                            "Age : "+json.getString("Age")+"\n";

                }catch (JSONException e){e.printStackTrace();}
            }

            this.responseTextView.setText(s);
        }
        private class GetAllCustomerTask extends AsyncTask<ApiConnector,Long,JSONArray> {

            @Override
            protected JSONArray doInBackground(ApiConnector... params) {

                return params[0].GetAllCustomers();
            }

            @Override
            protected void onPostExecute(JSONArray jsonArray) {

                setTextToTextView(jsonArray);
            }
        }

    }
package br.felipesilva.meuscontatos;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.*;
import android.support.v7.widget.*;

import android.view.*;
import android.widget.*;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import models.ContatoModel;



public class MainActivity extends AppCompatActivity {

    private TextView tvData;
    private ListView lvContatos;
    private List<ContatoModel> listaContatos;
    private List<ContatoModel> contatoModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create global configuration and initialize ImageLoader with this config
        // Create default options which will be used for every
        //  displayImage(...) call if no options will be passed to this method
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(defaultOptions).build();
        ImageLoader.getInstance().init(config); // Do it on Application start

        lvContatos = (ListView)findViewById(R.id.lvContatos);
        //new JSONTask().execute("http://private-61391-person9.apiary-mock.com/
        //
        //ContadoAdapter adapter = new ContadoAdapter(getApplicationContext(), R.layout.contato, listaContatos);
        //lvContatos.setAdapter(adapter);
        //


        lvContatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ContatoActivity.class);
                intent.putExtra("nome", contatoModelList.get(position).getNomeContato() );
                intent.putExtra("sobrenome", contatoModelList.get(position).getSobrenomeContato() );
                intent.putExtra("idade", contatoModelList.get(position).getIdadeContato() );
                intent.putExtra("telefone", contatoModelList.get(position).getPhoneContato() );
                startActivity(intent);
            }
        });

    }
    public class JSONTask extends AsyncTask<String,String, List<ContatoModel>>{

        @Override
        protected List<ContatoModel> doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";
                while((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String finalJson = buffer.toString();
                JSONArray parentArray = new JSONArray(finalJson);
                StringBuffer finaBufferDate = new StringBuffer();

                List<ContatoModel> contatoModelList = new ArrayList<>();

                for(int i=0; i<parentArray.length();i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    ContatoModel contatoModel = new ContatoModel();
                    contatoModel.setIdadeContato(finalObject.getString("id"));
                    contatoModel.setIdadeContato(finalObject.getString("age"));
                    contatoModel.setNomeContato(finalObject.getString("name") + " ");
                    contatoModel.setPhoneContato(finalObject.getString("phoneNumber"));
                    contatoModel.setSobrenomeContato(finalObject.getString("surname"));
                    contatoModel.setImageContado("https://placeholdit.imgix.net/~text?txtsize=15&txt="
                    + finalObject.getString("name").charAt(0)+"&w=50&h=50");
                    contatoModelList.add(contatoModel);
                }
                return contatoModelList;

                //return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(connection != null)
                    connection.disconnect();
                try {
                    if(reader != null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<ContatoModel> result) {
            super.onPostExecute(result);
            ContadoAdapter adapter = new ContadoAdapter(getApplicationContext(), R.layout.contato, result);
            lvContatos.setAdapter(adapter);
        }
    }
    public class ContadoAdapter extends ArrayAdapter{

        private  int resource;
        private LayoutInflater inflater;
        public ContadoAdapter(Context context, int resource, List<ContatoModel> objects) {
            super(context, resource, objects);
            contatoModelList = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = inflater.inflate(resource,null);
            }
            ImageView ivContatoIcon;
            TextView tvNome;
            TextView tvSobrenome;
            ivContatoIcon = (ImageView) convertView.findViewById(R.id.ivContatoIc);
            tvNome = (TextView) convertView.findViewById(R.id.tvNome);
            tvSobrenome = (TextView)convertView.findViewById(R.id.tvSobrenome);

            tvNome.setText(contatoModelList.get(position).getNomeContato());
            tvSobrenome.setText(contatoModelList.get(position).getSobrenomeContato());

            // Then later, when you want to display image
            ImageLoader.getInstance().displayImage(contatoModelList.get(position).getImageContado(), ivContatoIcon);
            // Default options will be used

            return convertView;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_atualizar) {
            new JSONTask().execute("http://private-61391-person9.apiary-mock.com/people");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
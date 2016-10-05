package es.ricardo.lector;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Collection;

public class TareaAsincrona extends AsyncTask<String, String, Boolean> {

	/** application context. */
	private final Context context;
    private Lector app = null;
	private final IntroActivity activity;
    private Collection files = null;

	
	//private GoogleAccountCredential credential;
	//private String URLServidor="https://googledrive.com/host/0B4O65dFE5SPsNGZTR0puWjREWTQ/";
		
	public TareaAsincrona(IntroActivity activity) {
		this.context = activity.getApplicationContext();
		this.activity = activity;
        this.app = (Lector) activity.getApplicationContext();
	}


    protected void onPreExecute() {

	}

	protected void onPostExecute(Boolean result) {
        if(app.getCurrentActivity() instanceof ListadoActivity) {
            ((ListadoActivity) app.getCurrentActivity()).updateResults(files);
        }
	}

	@Override
	protected Boolean doInBackground(String... args) {

        long tiempo = System.currentTimeMillis();

        File root = new File("//mnt");

        String[] extensions = { "mp3" };

        files = FileUtils.listFiles(root, extensions, true);

        Log.v("lector00","Tiempo empleado : "+ Long.toString(System.currentTimeMillis()- tiempo));
		Log.v("lector00","Archivos encontrados : "+ Long.toString(files.size()));

		 return true;
     }

/*	@Override
	protected void onProgressUpdate(String... values) {   }

	  	@Override
		protected void onCancelled(){
			return;
		}

*/
	}

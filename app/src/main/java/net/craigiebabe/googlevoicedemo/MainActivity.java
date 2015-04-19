package net.craigiebabe.googlevoicedemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources.NotFoundException;
import android.speech.RecognizerIntent;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private static final int REQUEST_CODE_COACH_COMMAND = 1;

    private Button speakButton;
    private EditText spokenWordsText;
    private TextView classificationText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spokenWordsText = (EditText) findViewById(R.id.spoken_words);
        classificationText = (TextView) findViewById(R.id.classification_text);
        speakButton = (Button) findViewById(R.id.speak_button);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_COACH_COMMAND)
            if (resultCode == RESULT_OK) {
                ArrayList<String> textMatchList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (!textMatchList.isEmpty()) {
                    StringBuffer inputText = new StringBuffer();
                    for(String word : textMatchList) {
                        inputText.append(word + " ");
                    }
                    spokenWordsText.setText(inputText);
                    if (CommandHelper.isCommand(textMatchList)) {

                        InputStream is = null;
                        try {
                            is = getResources().openRawResource(R.raw.trainer);
                            CommandHelper.EnumCommand command = CommandHelper.identifyCommand(is, inputText);
                            classificationText.setText(command.toString());
                            is.close();
                            showToastMessage(" command recognised as: " + command.toString());
                            CommandHelper.sendCommand(command);
                        } catch (NotFoundException e) {
                            showToastMessage("error identifying coach command: trainer.bin missing - " + e.getMessage());
                            e.printStackTrace();
                        } catch (IOException e) {
                            showToastMessage("error identifying coach command: " + e.getMessage());
                            e.printStackTrace();
                        } finally {
                            if(is != null)
                                try { is.close(); } catch (Exception e) { }
                        }
                    } else {
                        showToastMessage("No coach command recognised");
                    }
                }
            } else if(resultCode == RecognizerIntent.RESULT_AUDIO_ERROR){
                showToastMessage("Audio Error");
            } else if(resultCode == RecognizerIntent.RESULT_CLIENT_ERROR){
                showToastMessage("Client Error");
            } else if(resultCode == RecognizerIntent.RESULT_NETWORK_ERROR){
                showToastMessage("Network Error");
            } else if(resultCode == RecognizerIntent.RESULT_NO_MATCH){
                showToastMessage("No Match");
            } else if(resultCode == RecognizerIntent.RESULT_SERVER_ERROR){
                showToastMessage("Server Error");
            }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void checkVoiceRecognition() {
        // Check if voice recognition is present
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0) {
            speakButton.setEnabled(false);
            showToastMessage("Voice recognizer not present");
        }
    }

    /**
     * Triggered via btSpeak button onClick - see activity_voice_recognition.xml
     * @param view
     */
    public void launchSpeechRecogniserActivity(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "What do you want the robot to do?");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        startActivityForResult(intent, REQUEST_CODE_COACH_COMMAND);
    }

    void showToastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

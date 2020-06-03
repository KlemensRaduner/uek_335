package ch.noseryoung;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ch.noseryoung.persistence.AppDatabase;
import ch.noseryoung.persistence.Pendenz;

public class PendenzActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Pendenz pendenz = new Pendenz();
    private EditText dateInput;
    private EditText descriptionInput;
    private Spinner priorityInput;
    private EditText titleInput;

    private SimpleDateFormat SDFormat = new SimpleDateFormat("dd.mm.yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendenz);


        // set Listeners for all the inputfields

        dateInput = findViewById(R.id.date_input);

        priorityInput = findViewById(R.id.priority_input);
        priorityInput.setSelection(1);
        priorityInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pendenz.setPriority(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                pendenz.setPriority(0);
            }
        });

        titleInput = findViewById(R.id.title_input);
        titleInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                pendenz.setTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        descriptionInput = findViewById(R.id.description_input);
        // prevents linebreaks with enter
        descriptionInput.setImeOptions(EditorInfo.IME_ACTION_DONE);
        descriptionInput.setRawInputType(InputType.TYPE_CLASS_TEXT);
        descriptionInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                pendenz.setDescription(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        // get pendent from intent and store it
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            pendenz = (Pendenz) extras.get("PENDENZ");

            dateInput.setText(SDFormat.format(pendenz.getDate()));
            priorityInput.setSelection(pendenz.getPriority());
            titleInput.setText(pendenz.getTitle());
            descriptionInput.setText(pendenz.getDescription());

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbar, menu);

        // delete
        MenuItem deleteButton = menu.findItem(R.id.delete_button);
        deleteButton.setVisible(pendenz.getId() != 0);
        deleteButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                AppDatabase.getAppDb(getApplicationContext()).getPendenzDao().delete(pendenz);
                Toast.makeText(getApplicationContext(), getString(R.string.pendenz_deleted), Toast.LENGTH_LONG).show();
                finish();
                return true;
            }
        });

        //save
        MenuItem saveButton = menu.findItem(R.id.save_button);
        saveButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @SuppressLint("ShowToast")
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                // validation
                if (pendenz.getTitle() == null || pendenz.getTitle().equals("")) {
                    Toast.makeText(getApplicationContext(), getString(R.string.title_validation), Toast.LENGTH_SHORT).show();
                    titleInput.requestFocus();
                    return false;
                }

                if (pendenz.getDate() == 0) {
                    Toast.makeText(getApplicationContext(), getString(R.string.date_validation), Toast.LENGTH_SHORT).show();
                    dateInput.requestFocus();
                    return false;
                }

                if (pendenz.getDescription() == null || pendenz.getDescription().equals("")) {
                    Toast.makeText(getApplicationContext(), getString(R.string.description_validation), Toast.LENGTH_SHORT).show();
                    descriptionInput.requestFocus();
                    return false;
                }

                // if there was a pendenz in the intent, update it, else create a new one
                if (pendenz.getId() == 0) {
                    AppDatabase.getAppDb(getApplicationContext()).getPendenzDao().insert(pendenz);
                    Toast.makeText(getApplicationContext(), getString(R.string.pendenz_created), Toast.LENGTH_LONG).show();
                } else {
                    AppDatabase.getAppDb(getApplicationContext()).getPendenzDao().update(pendenz);
                    Toast.makeText(getApplicationContext(), getString(R.string.pendenz_updated), Toast.LENGTH_LONG).show();
                }
                finish();

                return true;
            }
        });

        // cancel (same as back button)
        MenuItem cancelButton = menu.findItem(R.id.cancel_button);
        cancelButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                finish();
                return true;
            }

        });
        return true;
    }

    //select the current date
    public void openDatePicker(View view) {
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "date picker");
    }

    //converts the selected date to long
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        String currentDateString = SDFormat.format(c.getTimeInMillis());
        dateInput.setText(currentDateString);
        pendenz.setDate(c.getTimeInMillis());
    }

    public static class DatePickerFragment extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), year, month, day);
        }
    }
}

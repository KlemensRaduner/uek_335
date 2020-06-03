package ch.noseryoung;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.Collections;
import java.util.List;

import ch.noseryoung.persistence.AppDatabase;
import ch.noseryoung.persistence.Pendenz;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnListItemClickListener {


    private List<Pendenz> pendenzen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //load pendenzen from db
        pendenzen = AppDatabase.getAppDb(getApplicationContext()).getPendenzDao().getAll();

        // sort by date
        Collections.sort(pendenzen);


        // initalize RecylcerView
        RecyclerView recyclerView = findViewById(R.id.pendenzen_list);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(getDrawable(R.drawable.divider));
        recyclerView.addItemDecoration(dividerItemDecoration);


        RecyclerViewAdapter adapter = new RecyclerViewAdapter(pendenzen, this);
        recyclerView.setAdapter(adapter);

    }

    // switch to PendenzActivity when the button is pressed
    public void createNewPendenz(View view) {
        Intent intent = new Intent(getBaseContext(), PendenzActivity.class);
        startActivity(intent);
    }

    // switch to PendenzActivity when a pendenz is clicked
    @Override
    public void onItemClick(int position) {
        Pendenz pendenz = pendenzen.get(position);
        Intent intent = new Intent(getBaseContext(), PendenzActivity.class);
        intent.putExtra("PENDENZ", pendenz);
        startActivity(intent);
    }
}

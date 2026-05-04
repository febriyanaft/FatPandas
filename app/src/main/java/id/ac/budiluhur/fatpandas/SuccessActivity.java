package id.ac.budiluhur.fatpandas;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class SuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_success);

        String name  = getIntent().getStringExtra("name");
        int qty      = getIntent().getIntExtra("qty", 1);
        int total    = getIntent().getIntExtra("total", 0);
        String meja  = getIntent().getStringExtra("meja");
        String waktu = getIntent().getStringExtra("waktu");
        String type  = getIntent().getStringExtra("type");
        String pay   = getIntent().getStringExtra("pay");

        ((TextView) findViewById(R.id.tvSuccessMenu)).setText(name);
        ((TextView) findViewById(R.id.tvSuccessQty)).setText(String.valueOf(qty));

        String formattedTotal = String.format(Locale.GERMANY, "%,d", total);
        ((TextView) findViewById(R.id.tvSuccessTotal)).setText(getString(R.string.cart_total_format, formattedTotal));

        ((TextView) findViewById(R.id.tvSuccessMeja)).setText(meja);
        ((TextView) findViewById(R.id.tvSuccessType)).setText(type);
        ((TextView) findViewById(R.id.tvSuccessPayment)).setText(pay);
        ((TextView) findViewById(R.id.tvSuccessWaktu)).setText(waktu);

        findViewById(R.id.btnBack).setOnClickListener(v -> {
            Intent i = new Intent(this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        });

        findViewById(R.id.btnKembali).setOnClickListener(v -> {
            Intent i = new Intent(this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        });

        findViewById(R.id.btnLihatPesanan).setOnClickListener(v -> {
            startActivity(new Intent(this, OrderListActivity.class));
            finish();
        });
    }
}

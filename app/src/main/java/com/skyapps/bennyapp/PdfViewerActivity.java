package com.skyapps.bennyapp;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

public class PdfViewerActivity extends AppCompatActivity {

    private PDFView pdfView ;
    private WebView webView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        pdfView = (PDFView)findViewById(R.id.pdf_viewer);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = getIntent().getStringExtra("D");
            Toast.makeText(this,value,Toast.LENGTH_LONG).show();
            Uri url = Uri.parse(value);
            if(url == null){
                Toast.makeText(this,"somthing not ok !",Toast.LENGTH_LONG).show();
            }
            //pdfView.fromUri(url).load();
            pdfView.fromAsset("d.pdf").load();

        }

        // TODO web view option
        /*String value = getIntent().getStringExtra("D");
        webView = (WebView)findViewById(R.id.w);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(value);*/

    }
}

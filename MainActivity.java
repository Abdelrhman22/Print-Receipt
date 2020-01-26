package com.example.reciptdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button btn_create_pdf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_create_pdf = findViewById(R.id.btn_create_pdf);
        Dexter.withActivity(this).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        .withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                btn_create_pdf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createPDFFile(Common.getAppPath(MainActivity.this)+"test_pdf.pdf");
                    }
                });
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

            }
        })
        .check();
    }

    private void createPDFFile(String path) {
        if(new File(path).exists())
        new File(path).delete();
        try{
            Document document = new Document();
            // save
            PdfWriter.getInstance(document,new FileOutputStream(path));
            // open to write
            document.open();

            // settings
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("Arafa");
            document.addCreator("Fawry Corp");

            // font setting
            BaseColor colorAccent = new BaseColor(0,153,204,255);
            float fontSize = 20.0f;
            float valueFontSize = 20.0f;
            // custom font
            BaseFont fontName = BaseFont.createFont("assets/fonts/brandon_meduim.otf","UTF-8",BaseFont.EMBEDDED);
            // create Title of Document
            Font titleFont = new Font(fontName,36.0f,Font.NORMAL,BaseColor.BLACK);
            addnewItem(document,"Order Details", Element.ALIGN_CENTER,titleFont);
            //------------------------------------------------------------------------//
            Font orderNumberFont = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addnewItem(document,"Order No:", Element.ALIGN_LEFT,orderNumberFont);

            Font orderNumberValueFont = new Font(fontName,valueFontSize,Font.NORMAL,BaseColor.BLACK);
            addnewItem(document,"#718183", Element.ALIGN_LEFT,orderNumberValueFont);

            addLineSeperator(document); //-----------------------

            addnewItem(document,"Order Date", Element.ALIGN_LEFT,orderNumberFont);
            addnewItem(document,"26/1/2020", Element.ALIGN_LEFT,orderNumberValueFont);

            addLineSeperator(document); //-----------------------
            addnewItem(document,"Account Name", Element.ALIGN_LEFT,orderNumberFont);
            addnewItem(document,"Abdo Arafa", Element.ALIGN_LEFT,orderNumberValueFont);
            addLineSeperator(document); //-----------------------

            // Add product Order Details
            addLineSpace(document);
            addnewItem(document,"Product Details", Element.ALIGN_CENTER,titleFont);
            addLineSeperator(document);

            // Item 1
            addNewItemWithLeftAndRight(document,"Pizza 25","(0.0%)",titleFont,orderNumberValueFont);
            addNewItemWithLeftAndRight(document,"12.0*1000","12000.0",titleFont,orderNumberValueFont);
            addLineSeperator(document); //-----------------------
            // Item 2
            addNewItemWithLeftAndRight(document,"Pizza 26","(0.0%)",titleFont,orderNumberValueFont);
            addNewItemWithLeftAndRight(document,"12.0*1000","12000.0",titleFont,orderNumberValueFont);
            addLineSeperator(document); //-----------------------
            document.close();
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
            printPDF();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    

    private void printPDF() {
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        try {
            PrintDocumentAdapter  printDocumentAdapter = new PdfDocumentAdapter(MainActivity.this,
                    Common.getAppPath(MainActivity.this)+"test_pdf.pdf");
            printManager.print("Document",printDocumentAdapter ,
                    new PrintAttributes.Builder().build());
        } catch (Exception e) {
            Log.e("errorMessage",e.getMessage());
            e.printStackTrace();
        }
    }

    private void addnewItem(Document document, String text, int align, Font font) throws DocumentException{
        Chunk chunk = new Chunk(text,font);
        Paragraph paragraph = new Paragraph(chunk);
        paragraph.setAlignment(align);
        document.add(paragraph);

    }

    private void addLineSeperator(Document document) throws DocumentException{
        LineSeparator lineSeparator =  new LineSeparator();
        lineSeparator.setLineColor(new BaseColor(0,0,0,68));
        addLineSpace(document);
        document.add(new Chunk(lineSeparator));
    }

    private void addLineSpace(Document document) throws DocumentException{
        document.add(new Paragraph(""));
    }
    private void addNewItemWithLeftAndRight(Document document, String textLeft, String textRight, Font textLeftFont , Font textRightFont) throws DocumentException {
        Chunk chunkTextLeft  = new Chunk(textLeft,textLeftFont);
        Chunk chunkTextRight = new Chunk(textRight,textRightFont);
        Paragraph paragraph = new Paragraph(chunkTextLeft);
        paragraph.add(new Chunk(new VerticalPositionMark()));
        paragraph.add(new Chunk(chunkTextRight));
        document.add(paragraph);

    }
}

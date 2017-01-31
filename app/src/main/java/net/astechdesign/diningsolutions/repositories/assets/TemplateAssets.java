package net.astechdesign.diningsolutions.repositories.assets;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TemplateAssets {

    private static String template;
    private static String newLine = System.getProperty("line.separator");


    public static String getTemplate(Context context, String templateName) {
        if (template != null) return template;
        AssetManager assets = context.getAssets();
        InputStream is;
        try {
            is = assets.open("templates/" + templateName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        template =  "";
        String line;
        try {
            while ((line = br.readLine()) != null) {
                template += line + newLine;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return template;
    }
}

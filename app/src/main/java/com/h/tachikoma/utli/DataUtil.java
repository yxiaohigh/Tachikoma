package com.h.tachikoma.utli;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.h.tachikoma.entity.FuliData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.List;

/**
 * 往手机内存写入数据 读取数据
 * Created by tony on 2016/4/21.
 */
public class DataUtil {


    /**
     *写入手机内存
     * @param dataFile
     * @param string
     */
    public static void writeData(File dataFile, String string) {

        try {
            if (!dataFile.exists()) {
                try {
                    dataFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Writer writer = new FileWriter(dataFile);
            writer.write(string);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 从手机内存读取数据
     * @return
     * @param dataFile
     * @param gson
     */
    public static List<FuliData> ReadrFuliDatas(File dataFile, Gson gson) {
         Reader reader = null;
        try {
            reader = new FileReader(dataFile);
            return gson.fromJson(reader, new TypeToken<List<FuliData>>() {
            }.getType());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}

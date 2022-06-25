package com.teamcom.smartwardrobe;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by d on 2018-01-17.
 */

public class UploadFile {

    public static String getImageNameToUri(Context context,Uri data, int type){
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        String imgPath = cursor.getString(column_index);
        String imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);

        if(type == 1){
            return imgName;//이미지 제목을 가져옴
        }
        else if(type==2){
            return imgPath;//주소를 가져옴
        }
        else{
            return "";
        }
    }

    public static int uploadFile(final String sourceFileUri, String urlimage) {//업로드 파일 여기다가 넣어도 됨
        HttpURLConnection conn=null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;//바이너리 파일로 바꿔주는 것
        File sourceFile = new File(sourceFileUri);
        if (!sourceFile.isFile()) {
//            dialog.dismiss();
//            Log.e("uploadFile", "Source File not exist :" + uploadFileName);
//            runOnUiThread(new Runnable() {
//                public void run() {
//                    messageText.setText("Source File not exist :"
//                            + uploadFileName);
//                }
//            });
            return 0;
        } else  {
            int serverResponseCode = 0;
            try {
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                //URL url = new URL("http://bsb.joinsns.co.kr/android/picture.php");//윤수형이 준 php 따라하기
                URL url = new URL(urlimage);
                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", sourceFileUri);//제목 이든 주소

                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + sourceFileUri + "\"" + lineEnd);//솔직히 클라이언트쪽 세팅만 잘해줬으면 PHP 쪽은 할일이 거의 없다.
                dos.writeBytes(lineEnd);                                                                                                 //만약 올리는 쪽에서 input의 name이 "uploadFile"이라고 했다면, PHP쪽에서는 그 파라미터로 동일하게 가져오면 된다.

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);

                if (serverResponseCode == 200) {

                            InputStream is   = null;
                            ByteArrayOutputStream baos = null;
                            try {
                                is = conn.getInputStream();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            baos = new ByteArrayOutputStream();
                            byte[] byteBuffer = new byte[1024];
                            byte[] byteData = null;
                            int nLength = 0;
                            try {
                                while((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                                    baos.write(byteBuffer, 0, nLength);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            byteData = baos.toByteArray();

                            String response = new String(byteData);
                            Log.i("response", response);
                            ///////////////////////////////
                          // Toast.makeText(MainActivity, "File Upload Complete.", Toast.LENGTH_SHORT).show();

                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {
                // dialog.dismiss();
                ex.printStackTrace();

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (IOException e) {
                // dialog.dismiss();
                e.printStackTrace();
                //  Log.e("Upload file to server Exception", "Exception : " + e.getMessage(), e);
            }

            // dialog.dismiss();
            return serverResponseCode;
        } // End else block

    }
}

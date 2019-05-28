package sastroman.suplierkomoditas;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import app.AppConfig;
import app.AppController;
import helper.SessionManager;

public class SignUp extends Activity {
    private static final String TAG = SignUp.class.getSimpleName();
    private Button btnRegister;
    private EditText inputFullName;
    private EditText inputUsername;
    private EditText inputPassword;
    private EditText inputCompany;
    private EditText inputAddress;
    private EditText inputPhone;
    private ProgressDialog pDialog;
    private ImageView iv;
    private ImageButton ib;


    //Image request code
    private int PICK_IMAGE_REQUEST = 1;

    //Bitmap to get image from gallery
    private Bitmap b = null;
    private Uri filePath = null;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        inputFullName = (EditText) findViewById(R.id.tfNama);
        inputUsername = (EditText) findViewById(R.id.tfUsername);
        inputPassword = (EditText) findViewById(R.id.tfPassword);
        inputCompany = (EditText) findViewById(R.id.tfNamper);
        inputAddress = (EditText) findViewById(R.id.tfAlamper);
        inputPhone = (EditText) findViewById(R.id.tfTelp);
        btnRegister = (Button) findViewById(R.id.btSignup);
        iv = (ImageView) findViewById(R.id.iv);
        ib = (ImageButton) findViewById(R.id.ib);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String nama = inputFullName.getText().toString().trim();
                String username = inputUsername.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String alamat_perusahaan = inputAddress.getText().toString().trim();
                String no_telp = inputPhone.getText().toString().trim();
                String nama_perusahaan = inputCompany.getText().toString().trim();

                if (!nama.isEmpty() && !username.isEmpty() && !password.isEmpty() && !nama_perusahaan.isEmpty()
                        && !alamat_perusahaan.isEmpty() && !no_telp.isEmpty() && username.length()>= 6 && password.length()>= 6
                        && !username.contains(" ") && nama_perusahaan.contains("@") && filePath !=null
                        && username.matches("[a-zA-Z0-9]*")) {
                    String image = compressImage(filePath.toString());
                    sendData(nama, username, password, nama_perusahaan, alamat_perusahaan, no_telp, image);

                } else {
                    String toast = "Tolong isi data dengan benar.";
                    if (!username.matches("[a-zA-Z0-9]*") || username.length() < 6 || username.contains(" ")) {
                        toast = "Username minimal 6 abjad dan tidak boleh ada spasi dan karakter spesial.";
                    }
                    if (password.length() < 6) {
                        toast = "Password minimal 6 abjad.";
                    }
                    if (!nama_perusahaan.contains("@")){
                        toast = "Alamat email tidak benar.";
                    }
                    if(username.equals("") || nama.equals("") || password.equals("") || nama_perusahaan.equals("") ||
                            alamat_perusahaan.equals("") || no_telp.equals("")){
                        toast = "Masih ada form yang belum diisi.";
                    }
                    if (filePath ==null){
                        toast = "Tolong pilih foto profile anda.";
                    }
                    Toast.makeText(getApplicationContext(),toast, Toast.LENGTH_LONG).show();
                }
            }
        });

        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

    }

    @Override
    public void onBackPressed() {
        // finish() is called in super: we only override this method to be able to override the transition
        Intent intent = new Intent(SignUp.this, Login.class);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
        finish();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void sendData(final String nama, final String username, final String password, final String nama_perusahaan,
                          final String alamat_perusahaan, final String no_telp, final String image){

        String tag_string_req = "req_register";
        pDialog.setMessage("Mendaftarkan ...");
        showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        Log.d(TAG, "Register Response: " + response);

                        try {
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean("error");
                            if (!error) {
                                Toast.makeText(getApplicationContext(), "User berhasil di daftarkan. Coba login sekarang!",
                                        Toast.LENGTH_LONG).show();

                                // Launch login activity
                                Intent intent = new Intent(
                                        SignUp.this,
                                        Login.class);
                                startActivity(intent);
                                finish();
                            } else {

                                // Error occurred in registration. Get the error
                                // message
                                String errorMsg = jObj.getString("error_msg");
                                Toast.makeText(getApplicationContext(),
                                        errorMsg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Log.e(TAG, "error : " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error terjadi saat pendaftaran. Check Koneksi Internet Anda.",
                        Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put("nama", nama);
                params.put("username", username);
                params.put("password", password);
                params.put("alamat_email", nama_perusahaan);
                params.put("alamat_pengiriman", alamat_perusahaan);
                params.put("no_telp", no_telp);
                params.put("image", image);

                //returning parameters
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }



    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == this.RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                b = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                iv.setImageBitmap(b);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);

        BitmapFactory.Options options = new BitmapFactory.Options();

        Bitmap scaledBitmap = null;

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {imgRatio = maxHeight / actualHeight; actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;} else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = this.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;      }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }
        return inSampleSize;
    }
}
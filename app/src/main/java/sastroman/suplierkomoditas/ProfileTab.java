package sastroman.suplierkomoditas;

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
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import app.AppConfig;
import app.AppController;
import helper.SQLiteHandler;
import helper.SessionManager;

/**
 * Created by A10 on 03/02/2017.
 */
public class ProfileTab extends Fragment {

    // Log tag
    private static final String TAG = ProfileTab.class.getSimpleName();

    //  json url
    private SQLiteHandler db;
    private TextView tvNama;
    private TextView tvUsername;
    private TextView tvPerusahaan;
    private TextView tvAlamat;
    private TextView tvNo;
    private ProgressDialog pDialog;
    private LinearLayout llcp;
    private LinearLayout lld;
    private Button btcp;
    private Button btgp;
    private Button btca;
    private SessionManager session;
    private NetworkImageView iv;
    private ImageButton ib;

    //Image request code
    private int PICK_IMAGE_REQUEST = 1;

    //Bitmap to get image from gallery

    private Bitmap scaledBitmap = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_profile, container, false);
        setHasOptionsMenu(true);

        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        lld = (LinearLayout)rootView.findViewById(R.id.LLD);
        llcp = (LinearLayout)rootView.findViewById(R.id.LLCd);
        btcp = (Button)rootView.findViewById(R.id.btCp);
        btca = (Button)rootView.findViewById(R.id.btca);
        btgp = (Button)rootView.findViewById(R.id.btgp);


        final EditText etae = (EditText)rootView. findViewById(R.id.tfNamper);
        final EditText etap = (EditText)rootView. findViewById(R.id.tfAlamper);
        final EditText etnt = (EditText)rootView. findViewById(R.id.tfTelp);
        final EditText etpl = (EditText)rootView. findViewById(R.id.tfPasswordlama);
        final EditText etpb = (EditText)rootView. findViewById(R.id.tfPasswordbaru);
        final EditText etplg = (EditText)rootView. findViewById(R.id.tfPasswordlagi);

        tvUsername = (TextView)rootView. findViewById(R.id.tvUsername);
        tvNama = (TextView)rootView. findViewById(R.id.tvNama);
        tvAlamat = (TextView)rootView. findViewById(R.id.tvAlamat);
        tvPerusahaan = (TextView)rootView. findViewById(R.id.tvPerusahaan);
        tvNo = (TextView)rootView. findViewById(R.id.tvNo);
        iv = (NetworkImageView)rootView.findViewById(R.id.iv);
        ib = (ImageButton)rootView.findViewById(R.id.ib);
        db = new SQLiteHandler(getActivity());
        session = new SessionManager(getContext());
        // Fetching user details from sqlite
        final HashMap<String, String> user = db.getUserDetails();

        final String username = user.get("username");
        final String nama = user.get("nama");
        final String perusahaan = user.get("alamat_email");
        final String alamat = user.get("alamat_pengiriman");
        final String no = user.get("no_telp");

        String url = AppConfig.URL_PIC + username + ".jpg";

        // Displaying the user details on the screen
        tvUsername.setText(username);
        tvNama.setText(nama);
        tvPerusahaan.setText(perusahaan);
        tvAlamat.setText(alamat);
        tvNo.setText(no);
        iv.setImageUrl(url, imageLoader);

        etae.setText(perusahaan);
        etap.setText(alamat);
        etnt.setText(no);

        btca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lld.setVisibility(View.VISIBLE);
                llcp.setVisibility(View.GONE);
            }
        });


        btcp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lld.setVisibility(View.GONE);
                llcp.setVisibility(View.VISIBLE);
            }
        });

        btgp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String pl = etpl.getText().toString().trim();
                String pb = etpb.getText().toString().trim();
                String plg = etplg.getText().toString().trim();
                String alamat_email = etae.getText().toString().trim();
                String alamat_pengiriman = etap.getText().toString().trim();
                String no_telp = etnt.getText().toString().trim();


                if (!pl.isEmpty() && !pb.isEmpty() && !plg.isEmpty() && !alamat_email.isEmpty()
                        && !alamat_pengiriman.isEmpty() && !no_telp.isEmpty()) {
                    if (pb.equals(plg)) {
                        changePassword(username, pl, pb, alamat_email, alamat_pengiriman, no_telp);
                    }else{
                        Toast.makeText(getContext(), "Ketik password lagi tidak cocok dengan pasword baru anda.", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getContext(), "Tolong isi semua data yang diperlukan!", Toast.LENGTH_LONG).show();
                }
            }
        });


        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        return rootView;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, final MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_profile2, menu);

    }

    private void changePassword(final String username, final String pl, final String pb, final String alamat_email,
                                final String alamat_pengiriman, final String no_telp) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Ganti Data ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_CP, new Response.Listener<String>() {

            @Override
            public void onResponse(java.lang.String response) {
                Log.d(getTag(), "Register Response: " + response.toString());
                hideDialog();


                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Toast.makeText(getContext(), "Password anda berhasil dirubah. Coba login lagi!", Toast.LENGTH_LONG).show();
                        logoutUser();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(getTag(), "Change Password Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("pl", pl);
                params.put("pb", pb);
                params.put("alamat_email", alamat_email);
                params.put("alamat_pengiriman", alamat_pengiriman);
                params.put("no_telp", no_telp);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
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

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            String image = compressImage(filePath.toString());
            final HashMap<String, String> user = db.getUserDetails();
            final String username = user.get("username");
            uploadImage(username, image);

        }
    }

    private void uploadImage(final String username,final String image){
        //Showing the progress dialog
        String tag_string_req = "req_upload";
        final ProgressDialog loading = ProgressDialog.show(getActivity(),"Uploading...","Mohon Tunggu...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_UPLOAD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(java.lang.String response) {
                        Log.d(TAG, "Upload Response: " + response.toString());
                        loading.dismiss();

                        try {
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean("error");
                            if (!error) {
                                String url = AppConfig.URL_PIC + username + ".jpg";
                                AppController.getInstance().getRequestQueue().getCache().remove(url);
                                Toast.makeText(getActivity(), "Upload Berhasil!",
                                        Toast.LENGTH_SHORT).show();
                            } else {

                                // Error occurred in registration. Get the error
                                // message
                                String errorMsg = jObj.getString("error_msg");
                                Toast.makeText(getContext(),
                                        errorMsg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Adding Error: " + error.getMessage());
                Toast.makeText(getActivity(), "Upload Gagal!",
                        Toast.LENGTH_SHORT).show();
                loading.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put("username", username);
                params.put("image", image);

                //returning parameters
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }

    private void logoutUser() {
        session.setLogin(false);
        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(getActivity(), Login.class);
        startActivity(intent);
        getActivity().finish();
    }

    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);

        BitmapFactory.Options options = new BitmapFactory.Options();

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
            iv.setImageBitmap(scaledBitmap);
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
        Cursor cursor = getActivity().getContentResolver().query(contentUri, null, null, null, null);
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